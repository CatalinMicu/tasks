package com.example.tasks.controller;

import com.example.tasks.dto.UserDTO;
import com.example.tasks.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
@PreAuthorize("@permissionChecker.isAdmin()")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/page")
    public Page<UserDTO> getUserPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "8") int size,
            @RequestParam(defaultValue = "username") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return userService.getUserPage(page, size, sortBy, direction);
    }

    @PatchMapping("/{id}/role")
    public UserDTO updateRole(@PathVariable Long id, @RequestParam String roleName) {
        return userService.updateRole(id, roleName);
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // @GetMapping("/{id}")
    // public UserDTO getUserById(@PathVariable Long id) {
    //     return userService.getUserById(id);
    // }

    // @PostMapping
    // public UserDTO createUser(@Valid @RequestBody UserDTO userDTO) {
    //     return userService.createUser(userDTO);
    // }

    // @PutMapping("/{id}")
    // public UserDTO updateUser(@PathVariable Long id, @Valid @RequestBody UserDTO userDTO) {
    //     return userService.updateUser(id, userDTO);
    // }

    // @GetMapping("/search")
    // public List<UserDTO> searchByUsername(@RequestParam String username) {
    //     return userService.searchByUsername(username);
    // }


}
