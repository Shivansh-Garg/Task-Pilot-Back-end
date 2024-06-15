package com.project.taskManagement.services.auth;


import com.project.taskManagement.dto.SignupRequest;
import com.project.taskManagement.dto.UserDto;
import com.project.taskManagement.entities.Employees;
import com.project.taskManagement.enums.UserRole;
import com.project.taskManagement.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService{

    private final UserRepository userRepository;


    @PostConstruct
    public void CreateAdminAccount(){
        Optional<Employees> optionalTask = userRepository.findByUserRole(UserRole.ADMIN);
        if(optionalTask.isEmpty()){
            Employees employees = new Employees();
            employees.setEmail("admin@test.com");
            employees.setName("admin");
            employees.setPassword(new BCryptPasswordEncoder().encode("admin"));
            employees.setUserRole(UserRole.ADMIN);
            userRepository.save(employees);
            System.out.println("Account created successfully");

        }else{
            System.out.println("Admin Account already exist");
        }
    }

    @Override
    public UserDto SignupUser(SignupRequest signupRequest) {
        Employees employees = new Employees();
        employees.setEmail(signupRequest.getEmail());
        employees.setName(signupRequest.getName());
        employees.setPassword(new BCryptPasswordEncoder().encode(signupRequest.getPassword()));
        employees.setUserRole(UserRole.EMPLOYEE);
        Employees createdUser = userRepository.save(employees);
        return createdUser.getUserDto();
    }

    @Override
    public boolean HaveUserWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
