package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.dto.UserDTO;
import com.example.ccrHospitalManagement.dto.UserRegistrationDto;
import com.example.ccrHospitalManagement.model.*;
import com.example.ccrHospitalManagement.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.*;
import com.example.ccrHospitalManagement.service.UserServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
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
        role = new Role("roleId1", "Patient"); // Constructor válido según tu clase

        user = new User();
        user.setId("userId1");
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setPassword("password");
        user.setRoles(new HashSet<>(Collections.singletonList(role)));
    }


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
        when(userRepository.existsById("U001")).thenReturn(false);
        when(userRepository.existsByUsername("jdoe")).thenReturn(false);
        when(epsRepository.findById("EPS1")).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.saveUser(dto));
        assertTrue(e.getMessage().contains("EPS no encontrada"));
    }

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
        when(roleRepository.findById("invalid")).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.createUser(user, List.of("invalid")));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void testCreateUser_Valid() {
        when(roleRepository.findById("roleId1")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User created = userService.createUser(user, List.of("roleId1"));
        assertEquals("testuser", created.getUsername());
    }

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
        when(roleRepository.findById("bad")).thenReturn(Optional.empty());
        Exception e = assertThrows(IllegalArgumentException.class, () -> userService.updateUser(user, List.of("bad")));
        assertTrue(e.getMessage().contains("no existe"));
    }

    @Test
    void testUpdateUser_Valid() {
        when(roleRepository.findById("roleId1")).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);
        User updated = userService.updateUser(user, List.of("roleId1"));
        assertNotNull(updated);
    }

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
}
