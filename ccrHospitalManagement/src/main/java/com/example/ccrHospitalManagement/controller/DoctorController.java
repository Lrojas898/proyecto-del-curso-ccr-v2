package com.example.ccrHospitalManagement.controller;

import com.example.ccrHospitalManagement.dto.*;
import com.example.ccrHospitalManagement.mapper.*;
import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.AssistanceActTypeRepository;
import com.example.ccrHospitalManagement.repository.AttentionEpisodeRepository;
import com.example.ccrHospitalManagement.repository.DiagnosisRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.service.*;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
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
    private final AppointmentMapper appointmentMapper;
    private final AttentionEpisodeServiceImpl attentionEpisodeService;
    private final AttentionEpisodeRepository attentionEpisodeRepository;
    private final DiagnosisRepository diagnosisRepository;



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

        ClinicalHistory history = clinicalHistoryService.getClinicalHistoryByUserId(patientId)
                .orElseThrow(() -> new RuntimeException("Historia clínica no encontrada para el paciente " + patientId));

        // Setear el ID de la historia clínica en el DTO
        episodeDTO.setClinicalHistoryId(history.getId());

        // Crear el objeto AttentionEpisode base
        AttentionEpisode episode = new AttentionEpisode();
        episode.setDescription(episodeDTO.getDescription());
       
        episode.setCreationDate(LocalDate.now());
        episode.setDoctor(doctor);

        // Usar el DTO actualizado
        AttentionEpisode saved = attentionEpisodeService.createAttentionEpisodeWithAssociations(episode, episodeDTO);

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
            @RequestBody AssistanceActDTO dto,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        AttentionEpisode episode = clinicalHistoryService.getEpisodeById(episodeId)
                .orElseThrow(() -> new RuntimeException("Episodio no encontrado con ID " + episodeId));

        if (dto.getTypeId() == null) {
            throw new IllegalArgumentException("El ID del tipo de acto no puede ser null.");
        }

        AssistanceActType type = assistanceActTypeRepository.findById(dto.getTypeId())
                .orElseThrow(() -> new RuntimeException("Tipo de acto asistencial no válido."));

        User doctor = userRepository.findByUsername(userDetails.getUsername());
        if (doctor == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        AssistanceAct act = new AssistanceAct();
        act.setIssueDate(dto.getIssueDate());
        act.setDescription(dto.getDescription());
        act.setAttentionEpisode(episode);
        act.setType(type);
        act.setDoctor(doctor); // ✅ Aquí lo agregamos

        AssistanceAct saved = assistanceActService.createAssistanceAct(act);
        return assistanceActMapper.toDto(saved);
    }



    @GetMapping("/doctors/stats/total-patients")
    public int getTotalPatientsOfDoctor(@AuthenticationPrincipal UserDetails userDetails) {
        User doctor = userRepository.findByUsername(userDetails.getUsername());
        if (doctor == null) {
            throw new RuntimeException("Doctor no encontrado");
        }

        List<User> patients = appointmentService.getPatientsByDoctorId(doctor.getId());
        return patients.size();
    }

    @GetMapping("/appointments/patient/{patientId}/doctor-view")
    public List<AppointmentDTO> getAppointmentsByPatient(@PathVariable String patientId) {
        return appointmentService.getAppointmentsByPatientId(patientId)
                .stream()
                .map(appointmentMapper::toDto)
                .collect(Collectors.toList());
    }
    @GetMapping("/appointments/{appointmentId}/episode-exists")
    public boolean checkIfEpisodeExists(@PathVariable Long appointmentId) {
        return attentionEpisodeRepository.existsByAppointment_Id(appointmentId);
    }

    @PutMapping("/episodes/{episodeId}/diagnoses")
@PreAuthorize("hasRole('DOCTOR')")
public ResponseEntity<Void> associateDiagnosesToEpisode(
        @PathVariable Long episodeId,
        @RequestBody List<Long> diagnosisIds) {

    Optional<AttentionEpisode> episodeOpt = attentionEpisodeRepository.findById(episodeId);
    if (!episodeOpt.isPresent()) {
        return ResponseEntity.notFound().build();
    }

    AttentionEpisode episode = episodeOpt.get();
    List<Diagnosis> diagnoses = diagnosisRepository.findAllById(diagnosisIds);
    episode.setDiagnoses(diagnoses);
    attentionEpisodeRepository.save(episode);

    return ResponseEntity.ok().build();
}

@PutMapping("/episodes/{episodeId}")
public ResponseEntity<AttentionEpisodeDTO> updateEpisode(
        @PathVariable Long episodeId,
        @RequestBody AttentionEpisodeDTO dto,
        Authentication auth) {
    
    AttentionEpisodeDTO updated = attentionEpisodeService.updateEpisode(episodeId, dto, auth);
    return ResponseEntity.ok(updated);
}


}
