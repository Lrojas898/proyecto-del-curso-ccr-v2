package com.example.ccrHospitalManagement.config;

import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EPSRepository epsRepository;
    private final PrepaidMedicineRepository prepaidRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            EPS eps1 = new EPS("EPS001", "Sanitas");
            EPS eps2 = new EPS("EPS002", "Sura");
            epsRepository.saveAll(List.of(eps1, eps2));

            PrepaidMedicine pm1 = new PrepaidMedicine("PM001", "Coomeva");
            PrepaidMedicine pm2 = new PrepaidMedicine("PM002", "Colsanitas");
            prepaidRepository.saveAll(List.of(pm1, pm2));

            List<String> roleNames = List.of(
                "admin", "doctor", "paciente",
                "técnico de laboratorio", "asistente de doctor", "med-asis"
            );
            for (String roleName : roleNames) {
                if (roleRepository.findByName(roleName).isEmpty()) {
                    roleRepository.save(new Role(null, roleName)); // ID autogenerado
                }
            }

            createUserIfNotExists(
                "123456", "admin", "123456", "admin@hospital.com",
                "Admin", "Principal", "Hombre", eps1, pm1,
                "Administración", "admin"
            );

            createUserIfNotExists("doctor1-id", "doctor1", "123456", "doc1@hospital.com",
                    "Carlos", "Médico", "Hombre", eps1, pm2, "Medicina Interna", "doctor");
            createUserIfNotExists("doctor2-id", "doctor2", "123456", "doc2@hospital.com",
                    "Luisa", "Médica", "Mujer", eps2, pm1, "Pediatría", "doctor");
            createUserIfNotExists("doctor3-id", "doctor3", "123456", "doc3@hospital.com",
                    "Marco", "Salud", "Hombre", eps1, pm1, "Cardiología", "doctor");

            createUserIfNotExists("paciente1-id", "paciente1", "123456", "paciente1@gmail.com",
                    "Laura", "Pérez", "Mujer", eps2, pm1, null, "paciente");
            createUserIfNotExists("paciente2-id", "paciente2", "123456", "paciente2@gmail.com",
                    "Pedro", "Ramírez", "Hombre", eps1, pm2, null, "paciente");
            createUserIfNotExists("paciente3-id", "paciente3", "123456", "paciente3@gmail.com",
                    "Lucía", "Fernández", "Mujer", eps2, pm2, null, "paciente");

            createUserIfNotExists("tecnico1-id", "tecnico1", "123456", "tecnico1@gmail.com",
                    "Ana", "López", "Mujer", eps1, pm2, "Laboratorio", "técnico de laboratorio");
            createUserIfNotExists("tecnico2-id", "tecnico2", "123456", "tecnico2@gmail.com",
                    "Miguel", "Torres", "Hombre", eps2, pm1, "Radiología", "técnico de laboratorio");
            createUserIfNotExists("tecnico3-id", "tecnico3", "123456", "tecnico3@gmail.com",
                    "Valentina", "Ríos", "Mujer", eps1, pm2, "Análisis Clínico", "técnico de laboratorio");

            // ✅ Nuevo usuario con rol "med-asis"
            createUserIfNotExists("asistente1-id", "asistente1", "123456", "asistente1@hospital.com",
                    "María", "Asistente", "Mujer", eps1, pm1, "Asistencia Médica", "med-asis");


            createUserIfNotExists("asistente1-id", "asistente1", "123456", "asistente1@hospital.com",
                    "María", "Asistente", "Mujer", eps1, pm1, "Asistencia Médica", "med-asis");
        };
    }

    private void createUserIfNotExists(String id, String username, String rawPassword, String email,
                                       String firstName, String lastName, String sex,
                                       EPS eps, PrepaidMedicine prepaid, String speciality, String roleName) {

        if (userRepository.existsByUsername(username)) return;

        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado: " + roleName));

        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAddress("Calle 123");
        user.setPhone("3000000000");
        user.setSex(sex);
        user.setDateOfBirth(LocalDate.of(1990, 1, 1));
        user.setSpeciality(speciality);
        user.setEps(eps);
        user.setPrepaidMedicine(prepaid);
        user.setRoles(Set.of(role));

        userRepository.save(user);
    }
}
