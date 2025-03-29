package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "EXAM_RESULT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExamResult {

    @Id
    @Column(length = 50, nullable = false)
    private String id;

    @Column(nullable = false)
    private java.sql.Date resultDate; // result_date -> resultDate

    @Column(nullable = false)
    private String description;

    // si vamos a usar archivos/imagenes, hay que usar BLOB
    private String attached;

    // Relación con ExamType
    @ManyToOne
    @JoinColumn(name = "EXAM_TYPE_id", nullable = false)
    private ExamType examType;

    @ManyToOne
    @JoinColumn(name = "myuser_id", nullable = false)
    private User user; // Usuario al que pertenece el examen (rol paciente)

    @ManyToOne
    @JoinColumn(name = "registered_by_id", nullable = false)
    private User registeredBy; // Profesional que subió o autorizó el examen
}
