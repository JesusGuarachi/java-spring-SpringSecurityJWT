package com.reynald.controllers;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestRolesController {
    @GetMapping("/accessAdmin")
    @PreAuthorize("hasRole('ADMIN')")
    public String accessAddmin(){
        return  "Hola, accediste con el perfil de Administrador ";
    }

    @GetMapping("/accessUser")
    @PreAuthorize("hasRole('USER')")
    public String accessUser(){
        return  "Hola, accediste con el perfil de Usuario ";
    }

    @GetMapping("/accessInvitado")
    @PreAuthorize("hasRole('INVITED')")
    public String accessInvited(){
        return  "Hola, accediste con el perfil de Inividado ";
    }
}
