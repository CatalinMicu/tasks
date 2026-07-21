package com.example.tasks.controller;

import com.example.tasks.dto.RegisterDTO;
import com.example.tasks.service.LoginRegisterService;
import lombok.RequiredArgsConstructor;
import org.jose4j.lang.JoseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
@CrossOrigin
@RequiredArgsConstructor

public class RegisterController {

    private final LoginRegisterService loginRegisterService;

    @PostMapping
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) throws JoseException {
        return loginRegisterService.register(registerDTO);
    }
}
