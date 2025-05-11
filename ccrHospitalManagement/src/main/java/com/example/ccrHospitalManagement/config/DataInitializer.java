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

    @Bean
    public CommandLineRunner initData() {
        return args -> {

            // EPS
            EPS eps1 = new EPS("EPS001", "Sanitas");
            EPS eps2 = new EPS("EPS002", "Sura");
            epsRepository.saveAll(List.of(eps1, eps2));

            // Medicina Prepagada
            PrepaidMedicine pm1 = new PrepaidMedicine("PM001", "Coomeva");
            PrepaidMedicine pm2 = new PrepaidMedicine("PM002", "Colsanitas");
            prepaidRepository.saveAll(List.of(pm1, pm2));

            // Roles
            List<String> roleNames = List.of("admin", "doctor", "paciente", "técnico de laboratorio", "asistente de doctor", "med-asis");
            for (String roleName : roleNames) {
                roleRepository.findByName(roleName)
                    .orElseGet(() -> roleRepository.save(new Role(null, roleName)));
            }

            // Usuarios
            createUserIfNotExists("admin", "123456", "admin@hospital.com", "Admin", "Principal", "Hombre", eps1, pm1, "Administración", "admin");
            createUserIfNotExists("doctor1", "123456", "doc1@hospital.com", "Carlos", "Médico", "Hombre", eps1, pm2, "Medicina Interna", "doctor");
            createUserIfNotExists("paciente1", "123456", "paciente1@gmail.com", "Laura", "Pérez", "Mujer", eps2, pm1, null, "paciente");
            createUserIfNotExists("asistente1", "123456", "asistente1@hospital.com", "María", "Asistente", "Mujer", eps1, pm1, "Asistencia Médica", "med-asis");

            // Ubicación
            Location location = createLocationIfNotExists("Consultorio General 101", "Av. Siempre Viva 742", "Ubicación en el ala norte del hospital");

            // Cita de prueba
            createAppointmentIfNotExists("paciente1", "doctor1", location, "Control médico general");
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
