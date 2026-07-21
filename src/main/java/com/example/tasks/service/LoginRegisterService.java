package com.example.tasks.service;

import com.example.tasks.domain.User;
import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.repository.UserRepository;
import org.eclipse.jetty.util.security.Credential;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.nio.charset.StandardCharsets;
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

    public String login(CredentialsDTO credentialsDTO) throws JoseException {
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
                return createJWToken();
            } catch (Exception e) {
                System.out.println("JWT ERROR: " + e.getMessage());
                e.printStackTrace();
                return "500: JWT creation failed - " + e.getMessage();
            }
        } else {
            return "403: Unauthorized";
        }

    }

    private String createJWToken() throws JoseException {
        JwtClaims claims = new JwtClaims();
        claims.setIssuedAtToNow();
        claims.setExpirationTimeMinutesInTheFuture((float) Long.parseLong(jwtExpiration) / 1000 / 60);
        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.HMAC_SHA256);
        jws.setKey(new AesKey(jwtSecret.getBytes(StandardCharsets.UTF_8)));
        return jws.getCompactSerialization();
    }

}
