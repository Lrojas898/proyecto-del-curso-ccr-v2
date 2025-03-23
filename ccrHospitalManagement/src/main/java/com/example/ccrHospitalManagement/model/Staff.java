package com.example.ccrHospitalManagement.model;



import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "STAFF",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"USER_id"})
        })
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Staff {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(length = 30, nullable = false)
    private String professionId; // profession_id -> professionId

    @Column
    private String phoneExtension; // sin especificar length, puedes agregarlo

    // Relación 1:1 con User
    @OneToOne
    @JoinColumn(name = "USER_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String undergraduate; // define length si deseas

    private String speciality; // define length si deseas
}
