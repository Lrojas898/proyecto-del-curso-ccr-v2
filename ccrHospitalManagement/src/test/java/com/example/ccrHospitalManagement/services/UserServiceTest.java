package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.dto.RoleDTO;
import com.example.ccrHospitalManagement.dto.UserDTO;
import com.example.ccrHospitalManagement.dto.UserRegistrationDto;
import com.example.ccrHospitalManagement.dto.UserRoleDTO;
import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.*;
import com.example.ccrHospitalManagement.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private EPSRepository epsRepository;
    @Mock private PrepaidMedicineRepository prepaidRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserServiceImpl userService;

    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        role = new Role(1L, "Patient");

        user = new User();
        user.setId("userId1");
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
    }

    // Save user
    @Test
    void testSaveUser_Valid() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setId("U001");
        dto.setUsername("jdoe");
        dto.setPassword("123");
        dto.setEmail("jdoe@email.com");
        dto.setFirstName("John");
        dto.setLastName("Doe");
        dto.setPhone("123456");
        dto.setSex("M");
        dto.setAddress("Street");
        dto.setDateOfBirth(LocalDate.of(1990, 1, 1));
        dto.setEpsNit("EPS1");
        dto.setPrepaidMedicineNit("PREP1");

        when(userRepository.existsById("U001")).thenReturn(false);
        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(epsRepository.findById("EPS1")).thenReturn(Optional.of(new EPS("EPS1", "EPS name")));
        when(prepaidRepository.findById("PREP1")).thenReturn(Optional.of(new PrepaidMedicine("PREP1", "Prep name")));
        when(passwordEncoder.encode("123")).thenReturn("encoded123");

        assertDoesNotThrow(() -> userService.saveUser(dto));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testSaveUser_DuplicateId_Throws() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setId("U001");
        when(userRepository.existsById("U001")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(dto));
        assertTrue(e.getMessage().contains("ID ya existe"));
    }

    @Test
    void testSaveUser_DuplicateUsername_Throws() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setId("U001");
        dto.setUsername("jdoe");
        when(userRepository.existsById("U001")).thenReturn(false);
        when(userRepository.existsByUsername("jdoe")).thenReturn(true);
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(dto));
        assertTrue(e.getMessage().contains("ya está en uso"));
    }

    @Test
    void testSaveUser_EpsNotFound_Throws() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setId("U001");
        dto.setUsername("jdoe");
        dto.setEpsNit("EPS1");
        dto.setPrepaidMedicineNit("PREP1");
        when(userRepository.existsById("U001")).thenReturn(false);
        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(epsRepository.findById("EPS1")).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(dto));
        assertTrue(e.getMessage().contains("EPS no encontrada"));
    }

    @Test
    void testSaveUser_PrepaidNotFound_Throws() {
        UserRegistrationDto dto = new UserRegistrationDto();
        dto.setId("U001");
        dto.setUsername("jdoe");
        dto.setEpsNit("EPS1");
        dto.setPrepaidMedicineNit("PREP1");
        when(userRepository.existsById("U001")).thenReturn(false);
        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(epsRepository.findById("EPS1")).thenReturn(Optional.of(new EPS("EPS1", "EPS name")));
        when(prepaidRepository.findById("PREP1")).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(dto));
        assertTrue(e.getMessage().contains("Medicina prepagada no encontrada"));
    }

    // Create user
    @Test
    void testCreateUser_RoleNull_Throws() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user, null));
        assertTrue(e.getMessage().contains("al menos un rol"));
    }

    @Test
    void testCreateUser_RoleEmpty_Throws() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user, new ArrayList<>()));
        assertTrue(e.getMessage().contains("al menos un rol"));
    }

    @Test
    void testCreateUser_RoleNotFound_Throws() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user, List.of(99L)));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void testCreateUser_Valid() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User created = userService.createUser(user, List.of(1L));
        assertEquals("testuser", created.getUsername());
    }

    // Update
    @Test
    void testUpdateUser_RoleNull_Throws() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user, null));
        assertTrue(e.getMessage().contains("al menos un rol"));
    }

    @Test
    void testUpdateUser_RoleEmpty_Throws() {
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user, List.of()));
        assertTrue(e.getMessage().contains("al menos un rol"));
    }

    @Test
    void testUpdateUser_RoleNotFound_Throws() {
        when(roleRepository.findById(99L)).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user, List.of(99L)));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void testUpdateUser_Valid() {
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updated = userService.updateUser(user, List.of(1L));
        assertNotNull(updated);
    }

    // Delete
    @Test
    void testDeleteUser_Valid() {
        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));
        userService.deleteUser("userId1");
        verify(userRepository).deleteById("userId1");
    }

    @Test
    void testDeleteUser_NotFound_Throws() {
        when(userRepository.findById("X")).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.deleteUser("X"));
        assertTrue(e.getMessage().contains("Usuario no encontrado"));
    }

    // get
    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<UserDTO> users = userService.getAllUsers();
        assertEquals(1, users.size());
        assertEquals("testuser", users.get(0).getUsername());
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));
        Optional<User> result = userService.getUserById("userId1");
        assertTrue(result.isPresent());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById("notExist")).thenReturn(Optional.empty());
        Optional<User> result = userService.getUserById("notExist");
        assertTrue(result.isEmpty());
    }

    // get users with roles
    @Test
    void testGetAllUsersWithRoles() {
        User user1 = new User();
        user1.setId("U1");
        user1.setUsername("user1");

        User user2 = new User();
        user2.setId("U2");
        user2.setUsername("user2");

        Role r1 = new Role(2L, "ADMIN");
        Role r2 = new Role(3L, "PATIENT");

        when(userRepository.findAll()).thenReturn(List.of(user1, user2));
        when(roleRepository.findAll()).thenReturn(List.of(r1, r2));
        when(userRepository.findRoleIdsByUserId("U1")).thenReturn(Set.of("R1"));
        when(userRepository.findRoleIdsByUserId("U2")).thenReturn(Set.of("R2"));

        List<UserRoleDTO> result = userService.getAllUsersWithRoles();
        assertEquals(2, result.size());
    }

    @Test
    void testGetUserWithRoles_Found() {
        Role role = new Role(2L, "ADMIN");
        Set<String> roleIds = Set.of("R1");

        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));
        when(roleRepository.findAll()).thenReturn(List.of(role));
        when(userRepository.findRoleIdsByUserId("userId1")).thenReturn(roleIds);

        UserRoleDTO result = userService.getUserWithRoles("userId1");
        assertEquals("userId1", result.getUserId());
        assertTrue(result.getAssignedRoleIds().contains("R1"));
    }

    @Test
    void testGetUserWithRoles_NotFound_Throws() {
        when(userRepository.findById("X")).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.getUserWithRoles("X"));
        assertTrue(e.getMessage().contains("Usuario no encontrado"));
    }

    @Test
    void testUpdateUserRoles_Valid() {
        Role role = new Role(2L, "ADMIN");
        when(userRepository.findById("userId1")).thenReturn(Optional.of(user));
        when(roleRepository.findAllById(Set.of(2L))).thenReturn(List.of(role));
        assertDoesNotThrow(() -> userService.updateUserRoles("userId1", Set.of(2L)));
        verify(userRepository).save(any(User.class));
    }

    @Test
    void testUpdateUserRoles_UserNotFound_Throws() {
        when(userRepository.findById("X")).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.updateUserRoles("X", Set.of(2L)));
        assertTrue(e.getMessage().contains("Usuario no encontrado"));
    }
}
