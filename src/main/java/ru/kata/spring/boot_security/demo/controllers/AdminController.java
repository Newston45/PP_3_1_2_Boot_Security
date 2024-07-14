package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.Collection;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String listUsers(Model model, Principal principal) {
        model.addAttribute("admin", userService.getUserByUsername(principal.getName()));
        model.addAttribute("user", new User());
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", userService.getRoles());
        return "admin";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/remove")
    public String removeUser(@RequestParam("id") Long id, Authentication authentication) {
        UserDetails currentUser = (UserDetails) authentication.getPrincipal();
        String currentUsername = currentUser.getUsername();

        User userToRemove = userService.findById(id);
        userService.deleteUser(userToRemove);

        if (userToRemove.getUsername().equals(currentUsername)) {
            SecurityContextHolder.clearContext();
            return "redirect:/login";
        }

        return "redirect:/admin";
    }

    @GetMapping("/edit")
    public String editUser(@RequestParam("id") Long id, Model model, Principal principal) {
        model.addAttribute("admin", userService.getUserByUsername(principal.getName()));
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", userService.getRoles());
        return "admin";
    }

}
