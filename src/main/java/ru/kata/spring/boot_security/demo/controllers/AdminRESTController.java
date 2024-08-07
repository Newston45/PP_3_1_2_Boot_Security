package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping("/api")
public class AdminRESTController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public AdminRESTController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping("/user")
    public ResponseEntity<User> showUserInfo(Principal principal) {
        return new ResponseEntity<>(userService.getUserByUsername(principal.getName()), HttpStatus.OK);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> showAllRoles() {
        return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
    }

    @GetMapping("/roles/{id}")
    public ResponseEntity<Role> showRole(@PathVariable Long id) {
        try {
            Role role = userService.findRoleById(id);
            return new ResponseEntity<>(role, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        return new ResponseEntity<>(userService.getUsers(), HttpStatus.OK);
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        try {
            User user = userService.findById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/users")
    public ResponseEntity<String> createUser(@RequestBody User user) {
        System.out.println("Received data: " + user);
        try {
            userService.saveUser(user);
            return new ResponseEntity<>("User created successfully", HttpStatus.CREATED);
        }catch (RuntimeException r) {
            return new ResponseEntity<>(r.toString(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/users")
    public ResponseEntity<String> updateUser(@RequestBody User user) {
        try {
            userService.saveUser(user);
            return new ResponseEntity<>("User update successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Error with update user", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<String> deleteUserById(@PathVariable Long id) {
        try {
            userService.deleteUser(userService.findById(id));
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("User not found", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
