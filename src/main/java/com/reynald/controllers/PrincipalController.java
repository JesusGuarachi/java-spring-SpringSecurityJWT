package com.reynald.controllers;

import com.reynald.controllers.request.CreateUserDTO;
import com.reynald.models.ERole;
import com.reynald.models.RoleEntity;
import com.reynald.models.UserEntity;
import com.reynald.repositories.RoleRepository;
import com.reynald.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class PrincipalController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    @GetMapping("/hello")
    public String hello(){
        return "Hello World Not Security";
    }

    @GetMapping("/helloSecured")
    public String helloSecured(){
        return "Hello World  Security";
    }

    @PostMapping("/users")
    public ResponseEntity<UserEntity> createUser(@RequestBody CreateUserDTO user){
         Set<RoleEntity> roles = roleRepository.findByNameIn(user.getRoles());
         if(roles.size() != user.getRoles().size()){
            roles.addAll(user.getRoles().stream().map(role-> RoleEntity.builder()
                     .name(ERole.valueOf(role))
                     .build()).collect(Collectors.toSet()));
         }

        UserEntity userEntity = UserEntity.builder()
                .email(user.getEmail())
                .username(user.getUsername())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(roles)
                .build();
        userRepository.save(userEntity);

        return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
    }


    @GetMapping("/users")
    public ResponseEntity<?> getUsers(){

        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<?> getUser( @PathVariable Long id){

        return ResponseEntity.ok(userRepository.findById(id));
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser( @PathVariable Long id){
        userRepository.deleteById(id);
        return ResponseEntity.ok("Usuario elminado");
    }
}
