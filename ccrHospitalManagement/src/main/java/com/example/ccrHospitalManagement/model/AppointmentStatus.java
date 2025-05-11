package com.example.ccrHospitalManagement.model;

public enum AppointmentStatus {
    PENDING,                 // Estado inicial tras solicitud
    APPROVED_BY_DOCTOR,      // El médico aprobó
    APPROVED_BY_ASSISTANT,   // El asistente aprobó
    FULLY_APPROVED,          // Aprobado por médico y asistente
    MODIFIED,                // Modificada por médico o asistente
    CONFIRMED_BY_PATIENT,    // Confirmada por paciente
    DENIED,                  // Rechazada por alguno
    CANCELLED_BY_PATIENT     // Cancelada por el paciente
}