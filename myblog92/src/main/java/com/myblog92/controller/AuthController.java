package com.myblog92.controller;

import com.myblog92.entity.Role;
import com.myblog92.entity.User;
import com.myblog92.payload.JWTAuthResponse;
import com.myblog92.payload.LoginDto;
import com.myblog92.payload.SignUpDto;
import com.myblog92.repository.RoleRepository;
import com.myblog92.repository.UserRepository;
import com.myblog92.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;              //Spring Security Library

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        if (userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email already exist" +signUpDto.getEmail(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (userRepository.existsByUsername(signUpDto.getUsername())){
            return new ResponseEntity<>("Username already exist" +signUpDto.getUsername(), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        User user = new User();
        user.setName(signUpDto.getName());
        user.setEmail(signUpDto.getEmail());
        user.setUsername(signUpDto.getUsername());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));

        //By default for all signup it will create admin role
        //Role roles=roleRepository.findByName("ROLE_ADMIN").get();
        //Set<Role> role = new HashSet<>();
       // role.add(roles);
       // user.setRoles(role);

        // Modify the role assignment during user registration
        Role roles = roleRepository.findByName("ROLE_ADMIN").get();
        Set<Role> role = new HashSet<>();
        role.add(roles);
        user.setRoles(role);


        User saveUsers = userRepository.save(user);

        return new ResponseEntity<>("User is now Registered"+saveUsers,HttpStatus.CREATED);
    }


    @PostMapping("/signin")
    public ResponseEntity<JWTAuthResponse> authenticateUser(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // get token form tokenProvider
        String token = tokenProvider.generateToken(authentication);

        return ResponseEntity.ok(new JWTAuthResponse(token));
    }

}