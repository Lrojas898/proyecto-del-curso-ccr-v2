package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.dto.RoleDTO;
import com.example.ccrHospitalManagement.dto.UserRegistrationDto;
import com.example.ccrHospitalManagement.dto.UserRoleDTO;
import com.example.ccrHospitalManagement.model.EPS;
import com.example.ccrHospitalManagement.model.PrepaidMedicine;
import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.model.User;
import com.example.ccrHospitalManagement.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements  UserService{

    private final UserRepository userRepository;
    private final EPSRepository epsRepository;
    private final PrepaidMedicineRepository prepaidRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

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
        user.setDateOfBirth(java.sql.Date.valueOf(dto.getDateOfBirth()));
        user.setEps(eps);
        user.setPrepaidMedicine(prepaid);

        userRepository.save(user);



    }

    @Transactional
    public User createUser(User user, List<String> roleIds) {
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


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }


    public Optional<User> getUserById(String id) {
        return userRepository.findById(id);
    }


    @Transactional
    public User updateUser(User user, List<String> roleIds) {
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
        Optional<User> user = userRepository.findById(id); // Buscar al usuario primero
        if (user.isPresent()) {
            userRepository.deleteById(id); // Si existe, lo eliminamos
        } else {
            // Si no existe, lanzar una excepción o simplemente no hacer nada
            throw new IllegalArgumentException("Usuario no encontrado");
        }
    }

    public List<UserRoleDTO> getAllUsersWithRoles() {
        List<User> users = userRepository.findAll();
        List<Role> allRoles = roleRepository.findAll();

        Set<RoleDTO> availableRoles = allRoles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        return users.stream()
                .map(user -> {
                    // Usamos una consulta separada para evitar inicializar la colección directamente
                    Set<String> assignedRoleIds = userRepository.findRoleIdsByUserId(user.getId());
                    return new UserRoleDTO(user.getId(), user.getUsername(), assignedRoleIds, availableRoles);
                })
                .collect(Collectors.toList());
    }

    public UserRoleDTO getUserWithRoles(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        List<Role> allRoles = roleRepository.findAll();

        // Usamos una consulta separada para evitar inicializar la colección directamente
        Set<String> assignedRoleIds = userRepository.findRoleIdsByUserId(userId);
        Set<RoleDTO> availableRoles = allRoles.stream()
                .map(role -> new RoleDTO(role.getId(), role.getName()))
                .collect(Collectors.toSet());

        return new UserRoleDTO(user.getId(), user.getUsername(), assignedRoleIds, availableRoles);
    }

    @Transactional
    public void updateUserRoles(String userId, Set<String> roleIds) {
        // Cargar el usuario sin inicializar la colección de roles
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Cargar los roles seleccionados
        Set<Role> rolesToAssign = new HashSet<>(roleRepository.findAllById(roleIds));

        // Sincronizar la colección existente
        Set<Role> currentRoles = user.getRoles();
        currentRoles.clear();         // Elimina los roles actuales
        currentRoles.addAll(rolesToAssign); // Añade los nuevos roles

        // Guardar los cambios
        userRepository.save(user);
    }


}
