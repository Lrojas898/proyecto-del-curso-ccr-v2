package com.example.ccrHospitalManagement.config;

import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EPSRepository epsRepository;
    private final PrepaidMedicineRepository prepaidRepository;
    private final LocationRepository locationRepository;
    private final AppointmentRepository appointmentRepository;
    private final PasswordEncoder passwordEncoder;
    private final ExamTypeRepository examTypeRepository;

  @Bean
public CommandLineRunner initData() {
    return args -> {

        // === EPS ===
        EPS eps1 = new EPS("EPS001", "Sanitas");
        EPS eps2 = new EPS("EPS002", "Sura");
        EPS eps3 = new EPS("EPS003", "Nueva EPS");
        epsRepository.saveAll(List.of(eps1, eps2, eps3));

        // === Medicina Prepagada ===
        PrepaidMedicine pm1 = new PrepaidMedicine("PM001", "Coomeva");
        PrepaidMedicine pm2 = new PrepaidMedicine("PM002", "Colsanitas");
        PrepaidMedicine pm3 = new PrepaidMedicine("PM003", "Aliansalud");
        prepaidRepository.saveAll(List.of(pm1, pm2, pm3));

        // === Roles ===
        List<String> roleNames = List.of("ADMIN", "DOCTOR", "PACIENTE", "ASISTENTE", "LABTECH");
        for (String roleName : roleNames) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(null, roleName)));
        }

        // === Ubicaciones ===
        Location loc1 = createLocationIfNotExists("Consultorio 101", "Ala Norte", "Medicina General");
        Location loc2 = createLocationIfNotExists("Consultorio 202", "Ala Sur", "Pediatría");
        Location loc3 = createLocationIfNotExists("Laboratorio Central", "Sótano", "Toma de muestras");
        Location loc4 = createLocationIfNotExists("Unidad de Imagenología", "Piso 2", "Exámenes especiales");

        // === Tipos de Examen ===
        if (examTypeRepository.count() == 0) {
            examTypeRepository.saveAll(List.of(
                    new ExamType(null, "Hemograma"),
                    new ExamType(null, "Química sanguínea"),
                    new ExamType(null, "Perfil lipídico"),
                    new ExamType(null, "Orina general"),
                    new ExamType(null, "PCR COVID"),
                    new ExamType(null, "Ecografía"),
                    new ExamType(null, "Rayos X"),
                    new ExamType(null, "Resonancia magnética")
            ));
        }

        // === Usuarios ===
        // DOCTORES
        createUserIfNotExists("doctor1", "123456", "doc1@hospital.com", "Carlos", "Gómez", "Hombre", eps1, pm1, "Medicina Interna", "DOCTOR");
        createUserIfNotExists("doctor2", "123456", "doc2@hospital.com", "Ana", "Ruiz", "Mujer", eps2, pm2, "Pediatría", "DOCTOR");
        createUserIfNotExists("doctor3", "123456", "doc3@hospital.com", "Luis", "Martínez", "Hombre", eps3, pm3, "Dermatología", "DOCTOR");

        // PACIENTES
        createUserIfNotExists("paciente1", "123456", "pac1@gmail.com", "Laura", "Pérez", "Mujer", eps1, pm2, null, "PACIENTE");
        createUserIfNotExists("paciente2", "123456", "pac2@gmail.com", "Miguel", "Torres", "Hombre", eps2, pm1, null, "PACIENTE");
        createUserIfNotExists("paciente3", "123456", "pac3@gmail.com", "Sofía", "Rodríguez", "Mujer", eps3, pm3, null, "PACIENTE");

        // ASISTENTES
        createUserIfNotExists("asistente1", "123456", "asis1@hospital.com", "María", "Ramírez", "Mujer", eps1, pm1, "Asistencia Médica", "ASISTENTE");
        createUserIfNotExists("asistente2", "123456", "asis2@hospital.com", "Pedro", "Sánchez", "Hombre", eps2, pm2, "Asistencia Clínica", "ASISTENTE");

        // TÉCNICOS
        createUserIfNotExists("labtech1", "123456", "lab1@hospital.com", "Camila", "Morales", "Mujer", eps1, pm1, "Laboratorio", "LABTECH");
        createUserIfNotExists("labtech2", "123456", "lab2@hospital.com", "Andrés", "Fernández", "Hombre", eps3, pm3, "Bioquímica", "LABTECH");

        // ADMIN
        createUserIfNotExists("admin", "123456", "admin@hospital.com", "Admin", "Principal", "Otro", eps1, pm1, "Administración", "ADMIN");

        // === Crear citas de prueba para cada paciente con doctor1 ===
        for (int i = 1; i <= 3; i++) {
            String pacienteUsername = "paciente" + i;
            createAppointmentIfNotExists(pacienteUsername, "doctor1", loc1, "Consulta general del paciente " + i);
        }

    };
}


    private void createUserIfNotExists(String username, String rawPassword, String email,
                                       String firstName, String lastName, String sex,
                                       EPS eps, PrepaidMedicine prepaid, String speciality, String roleName) {

        if (userRepository.existsByUsername(username)) return;

        Optional<Role> roleOpt = roleRepository.findByName(roleName);
        if (roleOpt.isEmpty()) return;

        Role role = roleOpt.get();

        User user = new User();
        user.setId(username);
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

    private Location createLocationIfNotExists(String name, String address, String description) {
        return locationRepository.findAll().stream()
                .filter(loc -> loc.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Location location = new Location();
                    location.setName(name);
                    location.setAddress(address);
                    location.setDescription(description);
                    return locationRepository.save(location);
                });
    }

    private void createAppointmentIfNotExists(String patientUsername, String doctorUsername, Location location, String description) {
        Optional<User> patientOpt = Optional.ofNullable(userRepository.findByUsername(patientUsername));
        Optional<User> doctorOpt = Optional.ofNullable(userRepository.findByUsername(doctorUsername));

        if (patientOpt.isEmpty() || doctorOpt.isEmpty()) return;

        User patient = patientOpt.get();
        User doctor = doctorOpt.get();

        boolean exists = appointmentRepository.findAll().stream()
                .anyMatch(app -> app.getPatient().getId().equals(patient.getId())
                        && app.getDoctor().getId().equals(doctor.getId())
                        && app.getDate().equals(LocalDate.now().plusDays(2)));

        if (exists) return;

        Appointment appointment = new Appointment();
        appointment.setDate(LocalDate.now().plusDays(2));
        appointment.setStartTime(LocalTime.of(10, 0));
        appointment.setDescription(description);
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        appointment.setLocation(location);
        appointment.setStatus(AppointmentStatus.PENDING);

        appointmentRepository.save(appointment);
    }
}