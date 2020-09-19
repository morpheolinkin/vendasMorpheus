package com.morpheo.cursomc.resources.controllers;

import com.morpheo.cursomc.dto.EmailDTO;
import com.morpheo.cursomc.security.JWTUtil;
import com.morpheo.cursomc.security.UserSS;
import com.morpheo.cursomc.services.AuthService;
import com.morpheo.cursomc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Objects;

@RestController
@RequestMapping(value = "/auth")
public class AuthResources {
    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private AuthService service;

    @PostMapping(value = "refresh_token")
    public ResponseEntity<Void> refreshToken(HttpServletResponse response){
        UserSS userSS = UserService.authenticated();
        //Estava dando um Warning para NullPointException em .getUserName(). Qualquer coisa só apagar essa linha
        //Objects.requireNonNull(userSS).getUsername() e deixar userSS.getUsername()
        String token = jwtUtil.generationToken(Objects.requireNonNull(userSS).getUsername());
        response.addHeader("Authorization", "Bearer " + token);
        return ResponseEntity.noContent().build();
    }

    @PostMapping(value = "/forgot")
    public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDTO){
        service.sendNewPassword(objDTO.getEmail());
        return ResponseEntity.noContent().build();
    }
}
