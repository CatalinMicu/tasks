package com.example.tasks.service;

import com.example.tasks.domain.User;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.mapper.UserMapper;
import com.example.tasks.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;


    public List<UserDTO> getAllUsers() {
        List<UserDTO> users = new ArrayList<>();
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            users.add(userMapper.toDto(user));
        }

        return users;
    }

    public UserDTO getUserById(Long id) {
        User user = userRepository.findById(id).orElse(null);
        if (user == null) {
            log.warn("User not found with id {}", id);
            return null;
        }
        return userMapper.toDto(user);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = userMapper.toEntity(userDTO);
        applyDefaults(user);
        User savedUser = userRepository.save(user);
        log.info("User created with id {}", savedUser.getUserId());
        return userMapper.toDto(savedUser);
    }

    @Transactional
    public void deleteUser(Long id) {
        if(!userRepository.existsById(id)) {
            log.warn("User not found with id {}", id);
            return;
        }
        userRepository.deleteById(id);
        log.info("User deleted with id {}", id);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        User existingUser = userRepository.findById(id).orElse(null);
        if (existingUser == null) {
            log.warn("User not found with id {}", id);
            return null;
        }
        existingUser.setUsername(userDTO.getUsername());
        existingUser.setBirthDate(userDTO.getBirthDate());
        existingUser.setIsInternal(userDTO.getIsInternal());
        existingUser.setCreationDate(userDTO.getCreationDate());
        existingUser.setCreatedBy(userDTO.getCreatedBy());
        existingUser.setLastUpdateDate(userDTO.getLastUpdateDate());

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toDto(updatedUser);
    }

    private void applyDefaults(User user) {
        if (user.getIsInternal() == null) {
            user.setIsInternal(1);
        }
        if (user.getCreationDate() == null) {
            user.setCreationDate(LocalDateTime.now());
        }
        if (user.getCreatedBy() == null || user.getCreatedBy().isBlank()) {
            user.setCreatedBy("system");
        }
        if (user.getLastUpdateDate() == null) {
            user.setLastUpdateDate(LocalDateTime.now());
        }
        if (user.getLastUpdatedBy() == null || user.getLastUpdatedBy().isBlank()) {
            user.setLastUpdatedBy("system");
        }
    }

}
