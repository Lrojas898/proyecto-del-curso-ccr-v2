package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.dto.UserDTO;
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
@RequestMapping("/api/patients")
@RequiredArgsConstructor
public class DoctorController {

    private final AppointmentService appointmentService;
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ClinicalHistoryService clinicalHistoryService;
    private final ClinicalHistoryMapper clinicalHistoryMapper;
    private final ExamResultService examResultService;


    @GetMapping("/doctor")
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

    @GetMapping("/{patientId}/medical-history")
    public ClinicalHistoryDTO getMedicalHistory(@PathVariable String patientId) {
        ClinicalHistory history = clinicalHistoryService.getClinicalHistoryByUserId(patientId)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada para el paciente " + patientId));

        return clinicalHistoryMapper.toDto(history);  // ✅ ¡Este es el cambio clave!
    }


    @GetMapping("/{patientId}/lab-results")
    public List<ExamResult> getExamResultsForPatient(@PathVariable String patientId) {
        return examResultService.getExamResultsByUsername(patientId);
    }

    @PostMapping("/{patientId}/episodes")
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
        return clinicalHistoryMapper.toDto(saved);
    }


}
