package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.dto.UserDTO;
import com.example.ccrHospitalManagement.mapper.AttentionEpisodeMapper;
import com.example.ccrHospitalManagement.mapper.ClinicalHistoryMapper;
import com.example.ccrHospitalManagement.mapper.UserMapper;
import com.example.ccrHospitalManagement.model.AttentionEpisode;
import com.example.ccrHospitalManagement.model.ClinicalHistory;
import com.example.ccrHospitalManagement.model.ExamResult;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.service.AppointmentService;
import com.example.ccrHospitalManagement.service.ClinicalHistoryService;
import com.example.ccrHospitalManagement.service.ExamResultService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DoctorController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ClinicalHistoryService clinicalHistoryService;
    private final ClinicalHistoryMapper clinicalHistoryMapper;
    private final ExamResultService examResultService;
    private final AttentionEpisodeMapper attentionEpisodeMapper;



    @GetMapping("/patients/doctor")
    public List<UserDTO> getPatientsOfDoctor(@AuthenticationPrincipal UserDetails userDetails) {
        // Obtener doctor autenticado
        User doctor = userRepository.findByUsername(userDetails.getUsername());
        if (doctor == null) {
            throw new RuntimeException("Doctor no encontrado");
        }

        // Obtener pacientes únicos con citas asignadas a ese doctor
        List<User> patients = appointmentService.getPatientsByDoctorId(doctor.getId());

        return patients.stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/patients/{patientId}/medical-history")
    public ClinicalHistoryDTO getMedicalHistory(@PathVariable String patientId) {
        ClinicalHistory history = clinicalHistoryService.getClinicalHistoryByUserId(patientId)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada para el paciente " + patientId));

        return clinicalHistoryMapper.toDto(history);  // ✅ ¡Este es el cambio clave!
    }


    @GetMapping("/patients/{patientId}/lab-results")
    public List<ExamResult> getExamResultsForPatient(@PathVariable String patientId) {
        return examResultService.getExamResultsByUsername(patientId);
    }

    @PostMapping("/patients/{patientId}/episodes")
    public AttentionEpisodeDTO addEpisodeToPatient(
            @PathVariable String patientId,
            @RequestBody AttentionEpisodeDTO episodeDTO,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User doctor = userRepository.findByUsername(userDetails.getUsername());
        if (doctor == null) {
            throw new RuntimeException("Doctor no encontrado");
        }

        // Obtener historia clínica del paciente
        ClinicalHistory history = clinicalHistoryService.getClinicalHistoryByUserId(patientId)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada para el paciente " + patientId));

        // Completar datos del episodio
        AttentionEpisode episode = new AttentionEpisode();
        episode.setDescription(episodeDTO.getDescription());
        episode.setDiagnosis(episodeDTO.getDiagnosis());
        episode.setCreationDate(LocalDate.now());
        episode.setClinicalHistory(history);
        episode.setDoctor(doctor);

        // Guardar episodio
        AttentionEpisode saved = clinicalHistoryService.saveEpisode(episode);
        return attentionEpisodeMapper.toDto(saved);

    }

    @PutMapping("/doctors/profile")
    public UserDTO updateDoctorProfile(@RequestBody UserDTO updatedDoctor, @AuthenticationPrincipal UserDetails userDetails) {
        User doctor = userRepository.findByUsername(userDetails.getUsername());
        if (doctor == null) {
            throw new RuntimeException("Doctor no encontrado");
        }

        // Actualizar solo los campos permitidos
        doctor.setFirstName(updatedDoctor.getFirstName());
        doctor.setLastName(updatedDoctor.getLastName());
        doctor.setEmail(updatedDoctor.getEmail());
        doctor.setPhone(updatedDoctor.getPhone());
        doctor.setAddress(updatedDoctor.getAddress());
        doctor.setSpeciality(updatedDoctor.getSpeciality());

        User saved = userRepository.save(doctor);
        return userMapper.toDto(saved);
    }

    @GetMapping("/doctors")
    public List<UserDTO> getAllDoctors() {
        return userRepository.findAll().stream()
                .filter(user -> user.getRoles().stream()
                        .anyMatch(role -> "DOCTOR".equalsIgnoreCase(role.getName())))
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

}
