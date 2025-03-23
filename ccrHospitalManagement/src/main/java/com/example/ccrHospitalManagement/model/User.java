package com.example.ccrHospitalManagement.model;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "\"USER\"") // user es palabra reservada en algunos motores,
// se escapa con comillas dobles
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @Column(length = 30, nullable = false)
    private String id;

    @Column(length = 100, nullable = false)
    private String username;

    @Column(length = 120, nullable = false)
    private String password;

    @Column(length = 200, nullable = false)
    private String email;

    @Column(length = 50, nullable = false)
    private String firstName;  // first_name -> firstName

    @Column(length = 50, nullable = false)
    private String lastName;

    @Column(length = 50, nullable = false)
    private String address;

    @Column(length = 30, nullable = false)
    private String phone;

    @Column(length = 30, nullable = false)
    private String sex;

    @Column(nullable = false)
    private java.sql.Date dateOfBirth;

    // Relación con EPS (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "EPS_nit", nullable = false)
    private EPS eps;

    // Relación con PrepaidMedicine (ManyToOne)
    @ManyToOne
    @JoinColumn(name = "PREPAID_MEDICINE_nit", nullable = false)
    private PrepaidMedicine prepaidMedicine;

    // Relación ManyToMany con Role a través de la tabla user_role
    // Opción 1: Usar una entidad intermedia UserRole (ver más abajo).
    // Opción 2: Usar @ManyToMany con @JoinTable. Depende de si guardas datos extra en "USER_ROLE".
}
