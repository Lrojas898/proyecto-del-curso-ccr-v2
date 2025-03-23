package com.example.ccrHospitalManagement.model;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleId implements Serializable {

    @Column(name = "USER_id", length = 30)
    private String userId;

    @Column(name = "ROLE_id", length = 40)
    private String roleId;
}
