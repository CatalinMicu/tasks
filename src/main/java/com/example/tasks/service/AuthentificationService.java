package com.example.tasks.service;

import com.example.tasks.domain.User;
import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.dto.ResponseDTO;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthentificationService {

    private final UserRepository userRepository;

    public ResponseDTO authenticate(CredentialsDTO credentials) {
        User user = userRepository.findByEmail(credentials.getEmail())
                .orElse(null);

        if (user == null) {
            return ResponseDTO.builder()
                    .user(null)
                    .response("User not found")
                    .build();
        }

        if (!Objects.equals(user.getPassword(), credentials.getPassword())) {
            return ResponseDTO.builder()
                    .user(null)
                    .response("Wrong password")
                    .build();
        }

        UserDTO userDTO = UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .build();

        return ResponseDTO.builder()
                .user(userDTO)
                .response("Login successful")
                .build();
    }
}