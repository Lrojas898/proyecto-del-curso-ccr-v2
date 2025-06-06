package com.example.ccrHospitalManagement.config;

import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

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
    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final AssistanceActTypeRepository assistanceActTypeRepository;
    private final DiagnosisRepository diagnosisRepository;
    private final AssistanceActRepository assistanceActRepository;
    private final MedicalProtocolRepository medicalProtocolRepository;
    private final AttentionEpisodeRepository attentionEpisodeRepository;
    private final ExamResultRepository examResultRepository;
    private final ExamResultDetailRepository examResultDetailRepository;

    @Bean
    public CommandLineRunner initData() {
        return args -> {
            // EPS
            EPS eps1 = new EPS("EPS001", "Sanitas");
            EPS eps2 = new EPS("EPS002", "Sura");
            EPS eps3 = new EPS("EPS003", "Nueva EPS");
            epsRepository.saveAll(List.of(eps1, eps2, eps3));

            // Medicina Prepagada
            PrepaidMedicine pm1 = new PrepaidMedicine("PM001", "Coomeva");
            PrepaidMedicine pm2 = new PrepaidMedicine("PM002", "Colsanitas");
            PrepaidMedicine pm3 = new PrepaidMedicine("PM003", "Aliansalud");
            prepaidRepository.saveAll(List.of(pm1, pm2, pm3));

            // Roles
            List<String> roleNames = List.of("ADMIN", "DOCTOR", "PACIENTE", "ASISTENTE", "LABTECH");
            for (String roleName : roleNames) {
                roleRepository.findByName(roleName).orElseGet(() -> roleRepository.save(new Role(null, roleName)));
            }

            // Ubicaciones
            Location loc1 = createLocationIfNotExists("Consultorio 101", "Ala Norte", "Medicina General");
            Location loc2 = createLocationIfNotExists("Laboratorio 1", "Sótano", "Laboratorio Clínico");

            // Tipos de examen
            if (examTypeRepository.count() == 0) {
                examTypeRepository.saveAll(List.of(
                        new ExamType(null, "Hemograma"),
                        new ExamType(null, "Orina general")
                ));
            }

            // Tipos de acto
            if (assistanceActTypeRepository.count() == 0) {
                assistanceActTypeRepository.saveAll(List.of(
                        new AssistanceActType(null, "CONSULTATION"),
                        new AssistanceActType(null, "TREATMENT")
                ));
            }

            // Usuarios
            createUserIfNotExists("doctor1", "123456", "doc1@hospital.com", "Carlos", "Gómez", "Hombre", eps1, pm1, "Medicina Interna", "DOCTOR");
            createUserIfNotExists("doctor2", "123456", "doc2@hospital.com", "Ana", "Ruiz", "Mujer", eps2, pm2, "Pediatría", "DOCTOR");
            createUserIfNotExists("doctor3", "123456", "doc3@hospital.com", "Luis", "Martínez", "Hombre", eps3, pm3, "Dermatología", "DOCTOR");

            createUserIfNotExists("paciente1", "123456", "pac1@gmail.com", "Laura", "Pérez", "Mujer", eps1, pm2, null, "PACIENTE");
            createUserIfNotExists("paciente2", "123456", "pac2@gmail.com", "Miguel", "Torres", "Hombre", eps2, pm1, null, "PACIENTE");
            createUserIfNotExists("paciente3", "123456", "pac3@gmail.com", "Sofía", "Rodríguez", "Mujer", eps3, pm3, null, "PACIENTE");

            createUserIfNotExists("asistente1", "123456", "asis1@hospital.com", "María", "Ramírez", "Mujer", eps1, pm1, "Asistencia Médica", "ASISTENTE");
            createUserIfNotExists("asistente2", "123456", "asis2@hospital.com", "Pedro", "Sánchez", "Hombre", eps2, pm2, "Asistencia Clínica", "ASISTENTE");

            createUserIfNotExists("labtech1", "123456", "lab1@hospital.com", "Camila", "Morales", "Mujer", eps1, pm1, "Laboratorio", "LABTECH");
            createUserIfNotExists("labtech2", "123456", "lab2@hospital.com", "Andrés", "Fernández", "Hombre", eps2, pm2, "Bioquímica", "LABTECH");
            createUserIfNotExists("labtech3", "123456", "lab3@hospital.com", "Luisa", "Salazar", "Mujer", eps3, pm3, "Microbiología", "LABTECH");

            createUserIfNotExists("admin", "123456", "admin@hospital.com", "Admin", "Principal", "Otro", eps1, pm1, "Administración", "ADMIN");

            // Citas
            createAppointmentIfNotExists("paciente1", "doctor1", loc1, "Consulta paciente 1", 1, 9);
            createAppointmentIfNotExists("paciente2", "doctor2", loc1, "Consulta paciente 2", 2, 10);
            createAppointmentIfNotExists("paciente3", "doctor3", loc1, "Consulta paciente 3", 3, 11);

            // Diagnósticos
            User doctor1 = userRepository.findByUsername("doctor1");
            List<Diagnosis> baseDiagnoses = List.of(
                    new Diagnosis(null, doctor1, "Gripe", "Infección viral común"),
                    new Diagnosis(null, doctor1, "Hipertensión", "Presión alta")
            );
            diagnosisRepository.saveAll(baseDiagnoses);

            // Protocolo
            if (medicalProtocolRepository.count() == 0) {
                MedicalProtocol protocolo = new MedicalProtocol(null, "Protocolo General", "Guía médica general", new ArrayList<>());
                ProtocolTask t1 = new ProtocolTask(null, "Signos vitales", "Medición básica", true, 1, protocolo);
                protocolo.getTasks().add(t1);
                medicalProtocolRepository.save(protocolo);
            }

            // Episodios, actos y resultados de laboratorio
            List<User> patients = userRepository.findAll().stream()
                    .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("PACIENTE"))).toList();
            List<Diagnosis> allDiagnoses = diagnosisRepository.findAll();
            MedicalProtocol protocol = medicalProtocolRepository.findAll().stream().findFirst().orElse(null);
            AssistanceActType consulta = (AssistanceActType) assistanceActTypeRepository.findByName("CONSULTATION").orElse(null);
            AssistanceActType tratamiento = (AssistanceActType) assistanceActTypeRepository.findByName("TREATMENT").orElse(null);
            List<User> labTechs = userRepository.findAll().stream()
                    .filter(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("LABTECH"))).toList();
            List<ExamType> examTypes = examTypeRepository.findAll();

            for (int i = 0; i < patients.size(); i++) {
                User patient = patients.get(i);
                User tech = labTechs.get(i % labTechs.size());
                ClinicalHistory history = clinicalHistoryRepository.findByUserId(patient.getId());

                AttentionEpisode episode = new AttentionEpisode();
                episode.setCreationDate(LocalDate.now());
                episode.setDescription("Episodio generado automáticamente");
                episode.setDoctor(doctor1);
                episode.setClinicalHistory(history);
                episode.setMedicalProtocol(protocol);
                episode.setDiagnoses(List.of(allDiagnoses.get(0)));
                AttentionEpisode savedEpisode = attentionEpisodeRepository.save(episode);

                assistanceActRepository.saveAll(List.of(
                        new AssistanceAct(null, Date.valueOf(LocalDate.now()), "Consulta médica", consulta, savedEpisode, doctor1),
                        new AssistanceAct(null, Date.valueOf(LocalDate.now()), "Tratamiento inicial", tratamiento, savedEpisode, doctor1)
                ));

                ExamType type = examTypes.get(i % examTypes.size());
                ExamResult exam = new ExamResult();
                exam.setResultDate(LocalDate.now());
                exam.setDescription("Resultado de examen generado automáticamente");
                exam.setExamType(type);
                exam.setPatient(patient);
                exam.setTechnician(tech);
                exam = examResultRepository.save(exam);

                examResultDetailRepository.saveAll(List.of(
                        new ExamResultDetail(null, "Hemoglobina", "13.5", "g/dL", exam),
                        new ExamResultDetail(null, "Leucocitos", "7000", "cel/mL", exam)
                ));
            }
        };
    }

    private void createUserIfNotExists(String username, String rawPassword, String email,
                                       String firstName, String lastName, String sex,
                                       EPS eps, PrepaidMedicine prepaid, String speciality, String roleName) {
        if (userRepository.existsByUsername(username)) return;
        Role role = roleRepository.findByName(roleName).orElseThrow();
        User user = new User(username, username, passwordEncoder.encode(rawPassword), email, firstName, lastName, "Calle 123", "3000000000", sex, LocalDate.of(1990, 1, 1), speciality, eps, prepaid, new HashSet<>(Set.of(role)));
        userRepository.save(user);
        if (role.getName().equals("PACIENTE")) {
            ClinicalHistory history = new ClinicalHistory();
            history.setDate(LocalDate.now());
            history.setHour(LocalTime.now());
            history.setGeneralObservations("Historia clínica inicial");
            history.setUser(user);
            clinicalHistoryRepository.save(history);
        }
    }

    private void createAppointmentIfNotExists(String patientUsername, String doctorUsername, Location location, String description, int daysAhead, int hour) {
        User patient = userRepository.findByUsername(patientUsername);
        User doctor = userRepository.findByUsername(doctorUsername);
        LocalDate date = LocalDate.now().plusDays(daysAhead);
        LocalTime time = LocalTime.of(hour, 0);

        boolean exists = appointmentRepository.findAll().stream()
                .anyMatch(app -> app.getPatient().getId().equals(patient.getId()) &&
                        app.getDoctor().getId().equals(doctor.getId()) &&
                        app.getDate().equals(date) &&
                        app.getStartTime().equals(time));
        if (exists) return;

        Appointment appointment = new Appointment(null, date, time, description, location, patient, doctor, AppointmentStatus.PENDING);
        appointmentRepository.save(appointment);
    }

    private Location createLocationIfNotExists(String name, String address, String description) {
        return locationRepository.findAll().stream()
                .filter(loc -> loc.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> {
                    Location location = new Location(null, name, address, description);
                    return locationRepository.save(location);
                });
    }
}
