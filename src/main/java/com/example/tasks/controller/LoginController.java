package com.example.tasks.controller;

import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.service.LoginRegisterService;
import lombok.RequiredArgsConstructor;
import org.jose4j.lang.JoseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
@RequiredArgsConstructor
@CrossOrigin
public class LoginController {

    private final LoginRegisterService loginRegisterService;

    @PostMapping
    public ResponseEntity<String> login(@RequestBody CredentialsDTO credentialsDTO) throws JoseException {
        return loginRegisterService.login(credentialsDTO);
    }
}
