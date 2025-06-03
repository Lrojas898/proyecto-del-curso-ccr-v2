package com.example.ccrHospitalManagement.repository;

import com.example.ccrHospitalManagement.model.ProtocolTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProtocolTaskRepository extends JpaRepository<ProtocolTask, Long> {

}
