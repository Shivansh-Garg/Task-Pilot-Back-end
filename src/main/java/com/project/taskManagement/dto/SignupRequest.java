package com.project.taskManagement.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data

public class SignupRequest {
    private String name;
    private String email;
    private String password;

}
