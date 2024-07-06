package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }


    @RequestMapping
    public String hello(Model model, Principal principal) {
        model.addAttribute("adminName", userService.getUserByUsername(principal.getName()).getName());
        return "admin";
    }


    @GetMapping("/list-users")
    public String listUsers(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", userService.getRoles());
        return "list-users";
    }

    @PostMapping("/list-users/add")
    public String addUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin/list-users";
    }

    @GetMapping("/list-users/remove")
    public String removeUser(@RequestParam("id") Long id) {
        userService.deleteUser(userService.findById(id));
        return "redirect:/admin/list-users";
    }

    @GetMapping("/list-users/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("users", userService.getUsers());
        model.addAttribute("roles", userService.getRoles());
        return "list-users";
    }

}
