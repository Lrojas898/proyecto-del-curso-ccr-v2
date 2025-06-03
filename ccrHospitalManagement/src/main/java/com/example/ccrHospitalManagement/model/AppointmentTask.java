package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "appointment_task")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private boolean required;

    @Column(nullable = false)
    private boolean completed = false;

    @Column
    private LocalDateTime completedAt;

    @Column
    private String completedBy;

    @Column(length = 1000)
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appointment_tasks_id", nullable = false)
    private AppointmentTasks appointmentTasks;
}

