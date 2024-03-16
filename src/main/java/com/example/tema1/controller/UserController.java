package com.example.tema1.controller;

import com.example.tema1.model.User;
import com.example.tema1.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@Service
public class UserController {
    @Autowired
    private UserRepository userRepository;
//    private final BCryptPasswordEncoder passwordEncoder;

//    public UserController( BCryptPasswordEncoder passwordEncoder) {
//        this.passwordEncoder = passwordEncoder;
//    }

    @PostMapping("/create")
    private ResponseEntity<User> createUser(@RequestBody User user){
        HttpSession session = getSession();
        String username = (String) session.getAttribute("username");
        String password = (String) session.getAttribute("password");
        Optional<User> adminUser = userRepository.findUserByUsername(username);
        if(adminUser.isPresent() && adminUser.get().getUserType() == 1) {
            User newUser = new User(user.getId(),
                    user.getName(),
                    user.getUserType(),
                    user.getUsername(),
//                    passwordEncoder.encode(user.getPassword()));
                    user.getPassword());
            userRepository.save(newUser);
            return new ResponseEntity<>(newUser, HttpStatus.CREATED);
        }
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @GetMapping("/login")
    private ResponseEntity<User> logIn(@RequestParam String username, @RequestParam String password){
        HttpSession session = getSession();

        Optional<User> userOptional = userRepository.findUserByUsername(username);
            if (userOptional.isPresent() & password.equals(userOptional.get().getPassword())) {
                session.setAttribute("username", username);
                session.setAttribute("password", password);
                return new ResponseEntity<>(userOptional.get(), HttpStatus.ACCEPTED);
            }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }

    private HttpSession getSession() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest().getSession(true); // true for creating a new session if not exists
    }

    @GetMapping("/user/{id}")
    private Optional<User> findUser(@PathVariable Integer id){
        return userRepository.findUserById(id);
    }

    @PostMapping("/update/{id}")
    private ResponseEntity<User> updateUser(@PathVariable Integer id, @RequestBody User user){
        Optional<User> userOptional = userRepository.findUserById(id);
        if(userOptional.isPresent()){
            User newUser = userOptional.get();
            newUser.setName(user.getName());
            newUser.setUsername(user.getUsername());
            newUser.setPassword(user.getPassword());
            newUser.setUserType(user.getUserType());
            userRepository.save(newUser);
            return new ResponseEntity<>(newUser, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.CONFLICT);
    }

    @DeleteMapping("/delete/{id}")
    private ResponseEntity<String> deleteUser(@PathVariable Integer id){
        if(userRepository.findUserById(id).isPresent()) {
            userRepository.deleteById(id);
            return new ResponseEntity<>("User deleted", HttpStatus.OK);
        }
        return new ResponseEntity<>("User not found",HttpStatus.CONFLICT);
    }
}
