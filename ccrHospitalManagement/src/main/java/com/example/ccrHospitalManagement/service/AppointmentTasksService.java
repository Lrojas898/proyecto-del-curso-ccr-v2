package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.AppointmentTasksRepository;
import com.example.ccrHospitalManagement.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AppointmentTasksService {

    private final AppointmentTasksRepository appointmentTasksRepository;
    private final AppointmentRepository appointmentRepository;
    private final MedicalProtocolService medicalProtocolService;

    @Autowired
    public AppointmentTasksService(
            AppointmentTasksRepository appointmentTasksRepository,
            AppointmentRepository appointmentRepository,
            MedicalProtocolService medicalProtocolService) {
        this.appointmentTasksRepository = appointmentTasksRepository;
        this.appointmentRepository = appointmentRepository;
        this.medicalProtocolService = medicalProtocolService;
    }

    public Optional<AppointmentTasks> getTasksByAppointmentId(Long appointmentId) {
        return appointmentTasksRepository.findByAppointmentId(appointmentId);
    }

    @Transactional
    public AppointmentTasks assignProtocolToAppointment(Long appointmentId, Long protocolId) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Appointment not found"));

        MedicalProtocol protocol = medicalProtocolService.getProtocolById(protocolId)
                .orElseThrow(() -> new RuntimeException("Protocol not found"));

        // Check if appointment already has tasks assigned
        Optional<AppointmentTasks> existingTasks = appointmentTasksRepository.findByAppointmentId(appointmentId);
        if (existingTasks.isPresent()) {
            throw new RuntimeException("Appointment already has a protocol assigned");
        }

        // Create new appointment tasks
        AppointmentTasks appointmentTasks = new AppointmentTasks();
        appointmentTasks.setAppointment(appointment);
        appointmentTasks.setProtocol(protocol);

        // Create task instances from protocol tasks
        List<AppointmentTask> tasks = protocol.getTasks().stream()
                .map(protocolTask -> {
                    AppointmentTask task = new AppointmentTask();
                    task.setName(protocolTask.getName());
                    task.setDescription(protocolTask.getDescription());
                    task.setRequired(protocolTask.isRequired());
                    task.setCompleted(false);
                    task.setAppointmentTasks(appointmentTasks);
                    return task;
                })
                .collect(Collectors.toList());

        appointmentTasks.setTasks(tasks);

        return appointmentTasksRepository.save(appointmentTasks);
    }

    @Transactional
    public AppointmentTask updateTaskStatus(Long appointmentId, Long taskId, boolean completed, String notes, String username) {
        AppointmentTasks appointmentTasks = appointmentTasksRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("No tasks found for this appointment"));

        AppointmentTask task = appointmentTasks.getTasks().stream()
                .filter(t -> t.getId().equals(taskId))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Task not found"));

        task.setCompleted(completed);
        task.setNotes(notes);

        if (completed) {
            task.setCompletedAt(LocalDateTime.now());
            task.setCompletedBy(username);
        } else {
            task.setCompletedAt(null);
            task.setCompletedBy(null);
        }

        appointmentTasksRepository.save(appointmentTasks);

        return task;
    }

    public boolean verifyProtocolCompliance(Long appointmentId) {
        AppointmentTasks appointmentTasks = appointmentTasksRepository.findByAppointmentId(appointmentId)
                .orElseThrow(() -> new RuntimeException("No tasks found for this appointment"));

        // Check if all required tasks are completed
        boolean allRequiredTasksCompleted = appointmentTasks.getTasks().stream()
                .filter(AppointmentTask::isRequired)
                .allMatch(AppointmentTask::isCompleted);

        return allRequiredTasksCompleted;
    }
}
