package ru.kata.spring.boot_security.demo.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public void saveUser(User user) {

        if (user.getId() != null) {

            if (userRepository.existsById(user.getId())) {
                User userFromDB = userRepository.findById(user.getId()).orElseThrow(() -> new RuntimeException("User not found"));
                user.setPassword(userFromDB.getPassword());
                userRepository.save(user);
            } else {
                throw new RuntimeException("User with id " + user.getId() + " does not exist.");
            }
        } else {
            if (userRepository.findUserByUsername(user.getUsername()).isPresent()) {
                throw new RuntimeException("Username " + user.getUsername() + " already exists.");
            }
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            userRepository.save(user);
        }
    }

    @Override
    @Transactional
    public void deleteUser(User user) {
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findById(Long id) throws NoSuchElementException {
        return userRepository.findById(id).orElseThrow(() -> new NoSuchElementException("User not found"));
    }


    @Override
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findUserByUsername(username).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findRoleById(Long id) {
        return roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found"));
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
