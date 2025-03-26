package com.example.ccrHospitalManagement.services;

import com.example.ccrHospitalManagement.model.Role;
import com.example.ccrHospitalManagement.repository.RoleRepository;
import com.example.ccrHospitalManagement.service.RoleService;
import com.example.ccrHospitalManagement.service.RoleServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceTest {
    //Cargamos una simulación de la capa repository
    @Mock
    private RoleRepository roleRepository;
    //Inyectamos el mock a RoleService teniendo en cuenta la dependencia que tiene
    @InjectMocks
    private RoleServiceImpl roleService;

    @Test
    void createRole_WhenCalled_ReturnsRole(){
        // Arrange
        // Creamos la información que simularemos que nos devolverá la capa de Repository
        Role roleToCreate = new Role();
        roleToCreate.setId("ADMIN");
        roleToCreate.setName("admin");

        List<String> permissionIds = Arrays.asList("READ", "WRITE");

        when(roleRepository.save(roleToCreate)).thenReturn(roleToCreate);

        //act
        Role result = roleService.createRole(roleToCreate, permissionIds);

        //assert
        assertNotNull(result);
        assertEquals("ADMIN", result.getId());
        assertEquals("admin", result.getName());
    }
    @Test
    void createRole_WhenPermissionIdsIsNull_ThrowsIllegalArgumentException() {
        // Arrange
        Role roleToCreate = new Role();
        roleToCreate.setId("ROLE_ADMIN");
        roleToCreate.setName("Admin");

        //act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(roleToCreate,
                        null)
        );
        assertEquals("El rol debe tener al menos un permiso asignado.", e.getMessage());
        verify(roleRepository, never()).save(roleToCreate);
    }
    @Test
    void createRole_WhenPermissionIdsIsEmpty_ThrowsIllegalArgumentException() {
        // Arrange
        Role roleToCreate = new Role();
        roleToCreate.setId("ROLE_ADMIN");
        roleToCreate.setName("Admin");
        List<String> emptyPermissionIds = Arrays.asList();

        //act
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class,
                () -> roleService.createRole(roleToCreate, emptyPermissionIds)
        );
        assertEquals("El rol debe tener al menos un permiso asignado.", e.getMessage());
        verify(roleRepository, never()).save(roleToCreate);
    }
}
