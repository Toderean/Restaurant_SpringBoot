package com.example.tema1.repositories;

import com.example.tema1.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findUserById(@PathVariable Integer Id);
    Optional<User> findUserByUsername(String username);
}
