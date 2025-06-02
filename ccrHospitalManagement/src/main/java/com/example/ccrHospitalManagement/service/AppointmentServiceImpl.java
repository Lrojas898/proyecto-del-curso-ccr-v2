package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.RescheduleRequest;
import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.model.AppointmentStatus;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.AppointmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public Appointment createAppointment(Appointment appointment) {
        validateAppointment(appointment, true);
        appointment.setStatus(AppointmentStatus.PENDING);
        return appointmentRepository.save(appointment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Appointment> getAppointmentById(Long id) {
        return appointmentRepository.findById(id).map(appointment -> {
            // Fuerza la carga de relaciones si son LAZY
            appointment.getDoctor().getFirstName();
            appointment.getPatient().getFirstName();
            appointment.getLocation().getName();
            return appointment;
        });
    }



    @Override
    public Appointment UpdateAppointment(Appointment appointment) {
        if (!appointmentRepository.existsById(appointment.getId())) {
            throw new IllegalArgumentException("No se puede actualizar una cita que no existe.");
        }
        validateAppointment(appointment, false);
        return appointmentRepository.save(appointment);
    }

    @Override
    public void removeAppointmentById(Long id) {
        if (!appointmentRepository.existsById(id)) {
            throw new IllegalArgumentException("No se puede eliminar una cita que no existe.");
        }
        appointmentRepository.deleteById(id);
    }


    @Override
    public Appointment updateAppointmentStatus(Long id, AppointmentStatus newStatus, String requesterRole) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

        AppointmentStatus currentStatus = appointment.getStatus();

        // Estados finales que no deben modificarse excepto cancelación por paciente
        if (currentStatus == AppointmentStatus.FULLY_APPROVED &&
                !(newStatus == AppointmentStatus.CANCELLED_BY_PATIENT && requesterRole.equals("ROLE_PACIENTE"))) {
            throw new IllegalStateException("No se puede modificar una cita totalmente aprobada. Solo puede ser cancelada por el paciente.");
        }

        if (currentStatus == AppointmentStatus.DENIED || currentStatus == AppointmentStatus.CANCELLED_BY_PATIENT) {
            throw new IllegalStateException("No se puede modificar una cita cancelada o denegada.");
        }

        // Transiciones válidas por rol y estado
        switch (newStatus) {
            case APPROVED_BY_DOCTOR -> {
                if (!requesterRole.equals("ROLE_DOCTOR"))
                    throw new IllegalArgumentException("Solo el médico puede aprobar como doctor.");

                if (currentStatus != AppointmentStatus.PENDING &&
                        currentStatus != AppointmentStatus.PROPOSED_BY_PATIENT &&
                        currentStatus != AppointmentStatus.MODIFIED)
                    throw new IllegalStateException("No se puede aprobar esta cita en su estado actual.");

                appointment.setStatus(AppointmentStatus.APPROVED_BY_DOCTOR);
            }

            case APPROVED_BY_ASSISTANT -> {
                if (!requesterRole.equals("ROLE_ASISTENTE"))
                    throw new IllegalArgumentException("Solo el asistente puede aprobar como asistente.");

                if (currentStatus != AppointmentStatus.PENDING &&
                        currentStatus != AppointmentStatus.PROPOSED_BY_PATIENT &&
                        currentStatus != AppointmentStatus.MODIFIED)
                    throw new IllegalStateException("No se puede aprobar esta cita en su estado actual.");

                appointment.setStatus(AppointmentStatus.APPROVED_BY_ASSISTANT);
            }

            case FULLY_APPROVED -> {
                if (!requesterRole.equals("ROLE_DOCTOR"))
                    throw new IllegalArgumentException("Solo el médico puede aprobar completamente.");

                // Permitir sólo si previamente hubo una aprobación por asistente
                if (currentStatus != AppointmentStatus.APPROVED_BY_ASSISTANT &&
                        !appointmentPreviouslyApprovedByAssistant(appointment))
                    throw new IllegalStateException("La cita debe haber sido aprobada por el asistente antes de completarse.");

                appointment.setStatus(AppointmentStatus.FULLY_APPROVED);
            }

            case PROPOSED_BY_DOCTOR -> {
                if (!requesterRole.equals("ROLE_DOCTOR") && !requesterRole.equals("ROLE_ASISTENTE"))
                    throw new IllegalArgumentException("Solo el médico o asistente pueden proponer cambios.");

                appointment.setStatus(AppointmentStatus.PROPOSED_BY_DOCTOR);
            }

            case PROPOSED_BY_PATIENT -> {
                if (!requesterRole.equals("ROLE_PACIENTE"))
                    throw new IllegalArgumentException("Solo el paciente puede proponer cambios.");

                if (currentStatus == AppointmentStatus.APPROVED_BY_DOCTOR || currentStatus == AppointmentStatus.MODIFIED) {
                    appointment.setStatus(AppointmentStatus.PROPOSED_BY_PATIENT);
                } else {
                    throw new IllegalStateException("No se puede proponer cambios desde este estado.");
                }
            }

            case CONFIRMED_BY_PATIENT -> {
                if (!requesterRole.equals("ROLE_PACIENTE"))
                    throw new IllegalArgumentException("Solo el paciente puede confirmar cambios.");

                if (currentStatus != AppointmentStatus.PROPOSED_BY_DOCTOR)
                    throw new IllegalStateException("Solo se pueden confirmar cambios propuestos por el doctor.");

                appointment.setStatus(AppointmentStatus.CONFIRMED_BY_PATIENT);
            }

            case CANCELLED_BY_PATIENT -> {
                if (!requesterRole.equals("ROLE_PACIENTE"))
                    throw new IllegalArgumentException("Solo el paciente puede cancelar.");

                appointment.setStatus(AppointmentStatus.CANCELLED_BY_PATIENT);
            }

            case DENIED -> {
                if (!(requesterRole.equals("ROLE_DOCTOR") || requesterRole.equals("ROLE_ASISTENTE") || requesterRole.equals("ROLE_PACIENTE")))
                    throw new IllegalArgumentException("No autorizado para denegar la cita.");
                appointment.setStatus(AppointmentStatus.DENIED);
            }

            case MODIFIED -> {
                if (!requesterRole.equals("ROLE_DOCTOR") && !requesterRole.equals("ROLE_ASISTENTE"))
                    throw new IllegalArgumentException("Solo el médico o asistente pueden marcar como modificada.");

                appointment.setStatus(AppointmentStatus.MODIFIED);
            }

            default -> throw new IllegalArgumentException("Transición de estado no válida.");
        }

        return appointmentRepository.save(appointment);
    }







    private void updateFullyApprovedIfNeeded(Appointment appointment) {
        boolean doctorApproved = appointment.getStatus() == AppointmentStatus.APPROVED_BY_DOCTOR
                || appointmentPreviouslyApprovedByDoctor(appointment);
        boolean assistantApproved = appointment.getStatus() == AppointmentStatus.APPROVED_BY_ASSISTANT
                || appointmentPreviouslyApprovedByAssistant(appointment);

        if (doctorApproved && assistantApproved) {
            appointment.setStatus(AppointmentStatus.FULLY_APPROVED);
        }
    }





    private boolean appointmentPreviouslyApprovedByDoctor(Appointment appointment) {
        return appointmentRepository.findById(appointment.getId())
                .map(a -> a.getStatus() == AppointmentStatus.APPROVED_BY_DOCTOR || a.getStatus() == AppointmentStatus.FULLY_APPROVED)
                .orElse(false);
    }



    private boolean appointmentPreviouslyApprovedByAssistant(Appointment appointment) {
        return appointmentRepository.findById(appointment.getId())
                .map(a -> a.getStatus() == AppointmentStatus.APPROVED_BY_ASSISTANT || a.getStatus() == AppointmentStatus.FULLY_APPROVED)
                .orElse(false);
    }

    private void validateAppointment(Appointment appointment, boolean isCreate) {
    if (appointment.getDate() == null) {
        throw new IllegalArgumentException("La fecha de la cita es obligatoria.");
    }
    if (appointment.getDate().isBefore(LocalDate.now())) {
        throw new IllegalArgumentException("La cita no puede agendarse en una fecha pasada.");
    }

    if (appointment.getStartTime() == null) {
        throw new IllegalArgumentException("La hora de inicio es obligatoria.");
    }
    if (appointment.getStartTime().isBefore(LocalTime.of(8, 0)) ||
        appointment.getStartTime().isAfter(LocalTime.of(18, 0))) {
        throw new IllegalArgumentException("La hora debe estar entre las 08:00 y las 18:00.");
    }

    if (appointment.getDoctor() == null || appointment.getPatient() == null) {
        throw new IllegalArgumentException("Debe asignarse un paciente y un médico.");
    }

    if (appointment.getDoctor().getId().equals(appointment.getPatient().getId())) {
        throw new IllegalArgumentException("El médico y el paciente deben ser diferentes.");
    }

    if (appointment.getLocation() == null) {
        throw new IllegalArgumentException("Debe especificarse una ubicación.");
    }

    if (isCreate && appointmentRepository.existsByDoctorIdAndDateAndStartTime(
            appointment.getDoctor().getId(),
            appointment.getDate(),
            appointment.getStartTime())) {
        throw new IllegalArgumentException("El doctor ya tiene una cita programada en esa fecha y hora.");
    }
}


    @Override
    @Transactional
    public List<Appointment> getAppointmentsByPatientId(String patientId) {
        List<Appointment> appointments = appointmentRepository.findByPatientId(patientId);
        // Fuerza carga
        appointments.forEach(a -> {
            a.getDoctor().getFirstName();
            a.getPatient().getFirstName();
            a.getLocation().getName();
        });
        return appointments;
    }



    @Override
public long countAllAppointments() {
    return appointmentRepository.count();
}


@Override
@Transactional
public Appointment handleRescheduleRequest(Long appointmentId, RescheduleRequest request, String username) {
    Appointment appointment = appointmentRepository.findById(appointmentId)
            .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada"));

    if (!appointment.getPatient().getUsername().equals(username)) {
        throw new IllegalArgumentException("No autorizado para reprogramar esta cita.");
    }

    appointment.setDate(request.getNewDate());
    appointment.setStartTime(request.getNewTime());

    String originalDescription = appointment.getDescription() != null ? appointment.getDescription() : "";
    appointment.setDescription(originalDescription + " (Reprogramación solicitada: " + request.getReason() + ")");
    appointment.setStatus(AppointmentStatus.MODIFIED);

    return appointmentRepository.save(appointment);
}

    @Override
    @Transactional
    public List<Appointment> getAppointmentsByDoctorId(String doctorId) {
        return appointmentRepository.findByDoctorId(doctorId);
    }

    @Override
    @Transactional
    public Appointment rescheduleByDoctor(Long appointmentId, LocalDate newDate, LocalTime newTime, String reason) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new IllegalArgumentException("Cita no encontrada con ID: " + appointmentId));

        // Actualizar fecha y hora
        appointment.setDate(newDate);
        appointment.setStartTime(newTime);

        // Anexar nota de reprogramación si hay razón
        if (reason != null && !reason.trim().isEmpty()) {
            String updatedDescription = appointment.getDescription() != null
                    ? appointment.getDescription() + "\n(Reprogramada por el doctor: " + reason + ")"
                    : "(Reprogramada por el doctor: " + reason + ")";
            appointment.setDescription(updatedDescription);
        }

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<User> getPatientsByDoctorId(String doctorId) {
        return appointmentRepository.findDistinctPatientsByDoctorId(doctorId);
    }



}
