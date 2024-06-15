package com.project.taskManagement.services.auth;

import com.project.taskManagement.dto.SignupRequest;
import com.project.taskManagement.dto.UserDto;

public interface AuthService {

    UserDto SignupUser(SignupRequest signupRequest);

    boolean HaveUserWithEmail(String email);
}
