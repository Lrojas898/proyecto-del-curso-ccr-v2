package com.example.ccrHospitalManagement.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "appointment_tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTasks {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "appointment_id", nullable = false)
    private Appointment appointment;

    @ManyToOne
    @JoinColumn(name = "protocol_id", nullable = false)
    private MedicalProtocol protocol;

    @OneToMany(mappedBy = "appointmentTasks", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AppointmentTask> tasks = new ArrayList<>();
}
