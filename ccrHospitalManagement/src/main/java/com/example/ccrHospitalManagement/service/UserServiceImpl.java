package com.example.ccrHospitalManagement.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.ccrHospitalManagement.dto.RoleDTO;
import com.example.ccrHospitalManagement.dto.UserDTO;
import com.example.ccrHospitalManagement.dto.UserRegistrationDto;
import com.example.ccrHospitalManagement.dto.UserRoleDTO;
import com.example.ccrHospitalManagement.model.Appointment;
import com.example.ccrHospitalManagement.model.ClinicalHistory;
import com.example.ccrHospitalManagement.model.EPS;
import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.AppointmentRepository;
import com.example.ccrHospitalManagement.repository.EPSRepository;
import com.example.ccrHospitalManagement.repository.PrepaidMedicineRepository;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import com.example.ccrHospitalManagement.repository.UserRepository;
import com.example.ccrHospitalManagement.repository.UserRoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{

    private final UserRepository userRepository;
    private final EPSRepository epsRepository;
    private final PrepaidMedicineRepository prepaidRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final AppointmentRepository appointmentRepository;
    private final ClinicalHistoryService clinicalHistoryService;


@Override
@Transactional
public void saveUser(UserRegistrationDto dto) {
    if (userRepository.existsById(dto.getId())) {
        throw new IllegalArgumentException("El ID ya existe.");
    }
    if (userRepository.existsByUsername(dto.getUsername())) {
        throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
    }

    EPS eps = epsRepository.findById(dto.getEpsNit())
            .orElseThrow(() -> new IllegalArgumentException("EPS no encontrada"));
    PrepaidMedicine prepaid = prepaidRepository.findById(dto.getPrepaidMedicineNit())
            .orElseThrow(() -> new IllegalArgumentException("Medicina prepagada no encontrada"));

    Role defaultRole = roleRepository.findByName("PACIENTE")
            .orElseThrow(() -> new IllegalArgumentException("Rol por defecto 'PACIENTE' no encontrado"));

    User user = new User();
    user.setId(dto.getId());
    user.setUsername(dto.getUsername());
    user.setPassword(passwordEncoder.encode(dto.getPassword()));
    user.setEmail(dto.getEmail());
    user.setFirstName(dto.getFirstName());
    user.setLastName(dto.getLastName());
    user.setAddress(dto.getAddress());
    user.setPhone(dto.getPhone());
    user.setSex(dto.getSex());
    user.setDateOfBirth(dto.getDateOfBirth());
    user.setEps(eps);
    user.setPrepaidMedicine(prepaid);
    user.setRoles(Set.of(defaultRole));

    userRepository.save(user);

    if (defaultRole.getName().equals("PACIENTE")) {
        ClinicalHistory history = new ClinicalHistory();
        history.setDate(LocalDate.now());
        history.setHour(LocalTime.now());
        history.setGeneralObservations("Historia clínica creada automáticamente al registrar al paciente.");
        history.setUser(user);
        clinicalHistoryService.createClinicalHistory(history);
    }
}


    @Transactional
    public User createUser(User user, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }

        List<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("El rol con id " + roleId + " no existe.")))
                .collect(Collectors.toList());

        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }

    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }

    @Transactional
    public User updateUser(User user, List<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            throw new IllegalArgumentException("El usuario debe tener al menos un rol asignado.");
        }

        List<Role> roles = roleIds.stream()
                .map(roleId -> roleRepository.findById(roleId)
                        .orElseThrow(() -> new IllegalArgumentException("El rol con id " + roleId + " no existe.")))
                .collect(Collectors.toList());

        user.setRoles(new HashSet<>(roles));
        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(String id) {
        Optional<User> userOpt = userRepository.findById(id);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }

        User user = userOpt.get();

        // Eliminar citas donde sea paciente
        List<Appointment> asPatient = appointmentRepository.findByPatientId(user.getId());
        appointmentRepository.deleteAll(asPatient);

        // Eliminar citas donde sea doctor (si aplica)
        List<Appointment> asDoctor = appointmentRepository.findByDoctorId(user.getId());
        appointmentRepository.deleteAll(asDoctor);

        userRepository.deleteById(id);
    }


    public List<UserRoleDTO> getAllUsersWithRoles() {
        List<User> users = userRepository.findAll();
        List<Role> allRoles = roleRepository.findAll();

        Set<RoleDTO> availableRoles = allRoles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        return users.stream()
                .map(user -> {
                    Set<String> assignedRoleIds = userRepository.findRoleIdsByUserId(user.getId());
                    return new UserRoleDTO(user.getId(), user.getUsername(), assignedRoleIds, availableRoles);
                })
                .collect(Collectors.toList());
    }

    public UserRoleDTO getUserWithRoles(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        List<Role> allRoles = roleRepository.findAll();

        Set<String> assignedRoleIds = userRepository.findRoleIdsByUserId(userId);
        Set<RoleDTO> availableRoles = allRoles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        return new UserRoleDTO(user.getId(), user.getUsername(), assignedRoleIds, availableRoles);
    }

    @Transactional
    public void updateUserRoles(String userId, Set<Long> roleIds) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Set<Role> rolesToAssign = new HashSet<>(roleRepository.findAllById(roleIds));
        Hibernate.initialize(user.getRoles());
        Set<Role> currentRoles = user.getRoles();
        currentRoles.clear();
        currentRoles.addAll(rolesToAssign);

        userRepository.save(user);
    }

    private UserDTO convertToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        // Convertir roles a lista de Strings (nombres)
        dto.setRoles(
            user.getRoles()
                .stream()
                .map(role -> role.getName())  // o getRoleName() según tu entidad
                .collect(Collectors.toList())
        );
        return dto;
    }

}
