package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.AssistanceActDTO;
import com.example.ccrHospitalManagement.dto.AttentionEpisodeDTO;
import com.example.ccrHospitalManagement.dto.ClinicalHistoryDTO;
import com.example.ccrHospitalManagement.dto.UserDTO;
import com.example.ccrHospitalManagement.mapper.*;
import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.AssistanceActTypeRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.service.AppointmentService;
import com.example.ccrHospitalManagement.service.AssistanceActService;
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
    private final AssistanceActMapper assistanceActMapper;
    private final AssistanceActTypeRepository assistanceActTypeRepository;
    private final AssistanceActService assistanceActService;




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

    @PostMapping("/episodes/{episodeId}/acts")
    public AssistanceActDTO addAssistanceAct(
            @PathVariable Long episodeId,
            @RequestBody AssistanceActDTO dto
    ) {
        AttentionEpisode episode = clinicalHistoryService.getEpisodeById(episodeId)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado con ID " + episodeId));

        if (dto.getTypeId() == null) {
            throw new IllegalArgumentException("El ID del tipo de acto no puede ser null.");
        }

        AssistanceActType type = assistanceActTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de acto asistencial no válido."));

        AssistanceAct act = new AssistanceAct();
        act.setIssueDate(dto.getDate());
        act.setDescription(dto.getDescription());
        act.setAttentionEpisode(episode);
        act.setType(type);

        AssistanceAct saved = assistanceActService.createAssistanceAct(act);
        return assistanceActMapper.toDto(saved);
    }



}
