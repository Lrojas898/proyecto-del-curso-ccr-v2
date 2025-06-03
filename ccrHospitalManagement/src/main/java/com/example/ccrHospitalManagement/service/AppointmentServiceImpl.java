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

        if (currentStatus == AppointmentStatus.DENIED ||
                currentStatus == AppointmentStatus.CANCELLED_BY_PATIENT ||
                currentStatus == AppointmentStatus.CANCELLED_BY_STAFF) {
            throw new IllegalStateException("No se puede modificar una cita cancelada o denegada.");
        }

        // Añadir validación para rechazar aprobaciones múltiples (Caso 17)
        if (currentStatus == AppointmentStatus.FULLY_APPROVED && newStatus == AppointmentStatus.FULLY_APPROVED) {
            throw new IllegalStateException("La cita ya está completamente aprobada.");
        }

        switch (newStatus) {
            case FULLY_APPROVED -> {
                if (!(requesterRole.equals("ROLE_DOCTOR") || requesterRole.equals("ROLE_ASISTENTE") ||
                        (requesterRole.equals("ROLE_PACIENTE") && currentStatus == AppointmentStatus.PROPOSED_BY_STAFF)))
                    throw new IllegalArgumentException("Solo el médico, asistente o paciente (para propuestas del staff) pueden aprobar completamente.");

                if (currentStatus != AppointmentStatus.PENDING &&
                        currentStatus != AppointmentStatus.PROPOSED_BY_PATIENT &&
                        currentStatus != AppointmentStatus.MODIFIED &&
                        currentStatus != AppointmentStatus.CONFIRMED_BY_PATIENT &&
                        !(currentStatus == AppointmentStatus.PROPOSED_BY_STAFF && requesterRole.equals("ROLE_PACIENTE"))) {
                    throw new IllegalStateException("No se puede aprobar esta cita en su estado actual.");
                }

                appointment.setStatus(AppointmentStatus.FULLY_APPROVED);
            }

            case CANCELLED_BY_STAFF -> {
                if (!(requesterRole.equals("ROLE_DOCTOR") || requesterRole.equals("ROLE_ASISTENTE")))
                    throw new IllegalArgumentException("Solo el personal puede cancelar esta cita.");

                appointment.setStatus(AppointmentStatus.CANCELLED_BY_STAFF);
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

            case PROPOSED_BY_STAFF -> {
                if (!(requesterRole.equals("ROLE_DOCTOR") || requesterRole.equals("ROLE_ASISTENTE")))
                    throw new IllegalArgumentException("Solo el médico o asistente pueden proponer cambios.");
                appointment.setStatus(AppointmentStatus.PROPOSED_BY_STAFF);
            }

            case PROPOSED_BY_PATIENT -> {
                if (!requesterRole.equals("ROLE_PACIENTE"))
                    throw new IllegalArgumentException("Solo el paciente puede proponer cambios.");

                if (currentStatus == AppointmentStatus.MODIFIED) {
                    appointment.setStatus(AppointmentStatus.PROPOSED_BY_PATIENT);
                } else {
                    throw new IllegalStateException("No se puede proponer cambios desde este estado.");
                }
            }

            case CONFIRMED_BY_PATIENT -> {
                if (!requesterRole.equals("ROLE_PACIENTE"))
                    throw new IllegalArgumentException("Solo el paciente puede confirmar cambios.");

                if (currentStatus != AppointmentStatus.PROPOSED_BY_STAFF)
                    throw new IllegalStateException("Solo se pueden confirmar cambios propuestos por el doctor.");

                appointment.setStatus(AppointmentStatus.CONFIRMED_BY_PATIENT);
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

        if (appointment.getStatus() == AppointmentStatus.FULLY_APPROVED) {
            throw new IllegalStateException("Las citas aprobadas no pueden ser reprogramadas, solo canceladas.");
        }

        if (appointmentRepository.existsByDoctorIdAndDateAndStartTimeAndStatus(
                appointment.getDoctor().getId(), request.getNewDate(), request.getNewTime(), AppointmentStatus.FULLY_APPROVED)) {
            throw new IllegalArgumentException("Ya existe una cita aprobada con este doctor en ese horario.");
        }

        appointment.setDate(request.getNewDate());
        appointment.setStartTime(request.getNewTime());

        String originalDescription = appointment.getDescription() != null ? appointment.getDescription() : "";
        appointment.setDescription(originalDescription + " (Reprogramación solicitada: " + request.getReason() + ")");

        // Cambiar automáticamente el estado a PROPOSED_BY_PATIENT en lugar de MODIFIED
        appointment.setStatus(AppointmentStatus.PROPOSED_BY_PATIENT);

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

        if (appointment.getStatus() == AppointmentStatus.FULLY_APPROVED) {
            throw new IllegalStateException("Las citas aprobadas no pueden ser reprogramadas, solo canceladas.");
        }

        if (appointmentRepository.existsByDoctorIdAndDateAndStartTimeAndStatus(
                appointment.getDoctor().getId(), newDate, newTime, AppointmentStatus.FULLY_APPROVED)) {
            throw new IllegalArgumentException("Ya existe una cita aprobada con este doctor en ese horario.");
        }

        appointment.setDate(newDate);
        appointment.setStartTime(newTime);

        if (reason != null && !reason.trim().isEmpty()) {
            String updatedDescription = appointment.getDescription() != null
                    ? appointment.getDescription() + "\n(Reprogramada por el doctor: " + reason + ")"
                    : "(Reprogramada por el doctor: " + reason + ")";
            appointment.setDescription(updatedDescription);
        }

        // Cambiar automáticamente el estado a PROPOSED_BY_STAFF
        appointment.setStatus(AppointmentStatus.PROPOSED_BY_STAFF);

        return appointmentRepository.save(appointment);
    }

    @Override
    public List<User> getPatientsByDoctorId(String doctorId) {
        return appointmentRepository.findDistinctPatientsByDoctorId(doctorId);
    }
}
