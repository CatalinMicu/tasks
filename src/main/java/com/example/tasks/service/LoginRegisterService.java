package com.example.tasks.service;

import com.example.tasks.domain.User;
import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.dto.RegisterDTO;
import com.example.tasks.repository.UserRepository;
import org.eclipse.jetty.util.security.Credential;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Base64;

@Service
public class LoginRegisterService {
    private final UserRepository userRepository;

    @Value("${jwt.secret}")
    String jwtSecret;
    @Value(("${jwt.expiration.ms}"))
    String jwtExpiration;

    public LoginRegisterService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public ResponseEntity<String> login(CredentialsDTO credentialsDTO) throws JoseException {
        credentialsDTO.setEmail(new String(Base64.getDecoder().decode(credentialsDTO.getEmail())));
        credentialsDTO.setPassword(new String(Base64.getDecoder().decode(credentialsDTO.getPassword())));

        String plainPassword = credentialsDTO.getPassword().replaceFirst("MD5:", "");
        String hashPassword = Credential.MD5.digest(plainPassword);
        User dbPassword = userRepository.findByEmail(credentialsDTO.getEmail()).orElse(null);

        System.out.println("=== LOGIN DEBUG ===");
        System.out.println("Email decoded: " + credentialsDTO.getEmail());
        System.out.println("Password decoded: " + credentialsDTO.getPassword());
        System.out.println("After replaceFirst: " + plainPassword);
        System.out.println("Computed hash: " + hashPassword);
        System.out.println("DB hash: " + (dbPassword != null ? dbPassword.getPassword() : "USER NOT FOUND"));
        System.out.println("Match: " + (dbPassword != null && hashPassword.equals(dbPassword.getPassword())));

        if (dbPassword != null && hashPassword.equals(dbPassword.getPassword())) {
            try {
                return ResponseEntity.ok(createJWToken(userRepository.findByEmail(credentialsDTO.getEmail()).get().getUserId(), userRepository.findByEmail(credentialsDTO.getEmail()).get().getUsername(), userRepository.findByEmail(credentialsDTO.getEmail()).get().getEmail()));
            } catch (Exception e) {
                System.out.println("JWT ERROR: " + e.getMessage());
                e.printStackTrace();
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error");
            }
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Invalid credentials");
        }

    }

    public ResponseEntity<String> register(RegisterDTO registerDTO) throws JoseException {
        registerDTO.setEmail(new String(Base64.getDecoder().decode(registerDTO.getEmail())));
        registerDTO.setPassword(new String(Base64.getDecoder().decode(registerDTO.getPassword())));

        if (userRepository.findByEmail(registerDTO.getEmail()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("User already exists");
        }

        String plainPassword = registerDTO.getPassword().replaceFirst("MD5:", "");
        String hashPassword = Credential.MD5.digest(plainPassword);

        User user = User.builder()
                .email(registerDTO.getEmail())
                .username(registerDTO.getUsername())
                .password(hashPassword)
                .birthDate(registerDTO.getBirthDate())
                .creationDate(LocalDateTime.now())
                .createdBy("system")
                .lastUpdateDate(LocalDateTime.now())
                .lastUpdatedBy("system")
                .isInternal(0)
                .build();

        userRepository.save(user);
        return  ResponseEntity.ok(createJWToken(user.getUserId(), user.getUsername(), user.getEmail()));

    }

    private String createJWToken(Long userId, String username, String email) throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture((float) Long.parseLong(jwtExpiration) / 1000 / 60);
        JsonWebSignature jws = new JsonWebSignature();
        claims.setClaim("userId", userId);
        claims.setClaim("username", username);
        claims.setClaim("email", email);

        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(new AesKey(jwtSecret.getBytes(StandardCharsets.UTF_8)));
        return jws.getCompactSerialization();
    }

}