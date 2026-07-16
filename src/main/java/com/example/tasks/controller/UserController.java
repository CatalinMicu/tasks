package com.example.tasks.controller;

import com.example.tasks.dto.CredentialsDTO;
import com.example.tasks.dto.ResponseDTO;
import com.example.tasks.dto.UserDTO;
import com.example.tasks.service.AuthentificationService;
import com.example.tasks.service.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final AuthentificationService authentificationService;

    public UserController(UserService userService, AuthentificationService authentificationService) {
        this.userService = userService;
        this.authentificationService = authentificationService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
        return userService.createUser(userDTO);
    }

    @PostMapping("/login")
    public ResponseDTO login(@RequestBody CredentialsDTO credentials) {
        return authentificationService.authenticate(credentials);
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
        return userService.updateUser(id, userDTO);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/search")
    public List<UserDTO> searchByUsername(@RequestParam String username) {
        return userService.searchByUsername(username);
    }


}
