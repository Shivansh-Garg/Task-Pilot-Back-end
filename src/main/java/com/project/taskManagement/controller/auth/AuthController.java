package com.project.taskManagement.controller.auth;

import com.project.taskManagement.dto.AuthenticationRequest;
import com.project.taskManagement.dto.AuthenticationResponse;
import com.project.taskManagement.dto.SignupRequest;
import com.project.taskManagement.dto.UserDto;
import com.project.taskManagement.entities.Employees;
import com.project.taskManagement.repositories.UserRepository;
import com.project.taskManagement.services.auth.AuthService;
import com.project.taskManagement.services.jwt.UserService;
import com.project.taskManagement.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/signup")
    public ResponseEntity<?> signupUser(@RequestBody SignupRequest signupRequest){

        if(authService.HaveUserWithEmail(signupRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User already exist with the given email");
        }
        UserDto createdUserDto = authService.SignupUser(signupRequest);
        if(createdUserDto == null){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body("User not created");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUserDto);
    }

    @PostMapping("/login")
    public AuthenticationResponse login(@RequestBody AuthenticationRequest authenticationRequest){
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),authenticationRequest.getPassword()));
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Incorrect Username or Password");
        }
        final UserDetails userDetails = userService.userDetailService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<Employees> optionalTask = userRepository.findFirstByEmail(authenticationRequest.getEmail());
        final String jwtToken = jwtUtils.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();

        if(optionalTask.isPresent()){
            authenticationResponse.setJwt(jwtToken);
            authenticationResponse.setUserId(optionalTask.get().getId());
            authenticationResponse.setUserRole(optionalTask.get().getUserRole());
        }

        return authenticationResponse;
    }
}
