package com.example.ccrHospitalManagement.service;

import com.example.ccrHospitalManagement.model.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
    User createUser(User user, List<String> roleIds);
    List<User> getAllUsers();
    Optional<User> getUserById(String id);
    User updateUser(User user, List<String> roleIds);
    void deleteUser(String id);
}
