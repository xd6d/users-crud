package com.example.userscrud.service;

import com.example.userscrud.model.User;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UserService {

    User createUser(User user);

    User updateUser(User user);

    void deleteUser(Long id);

    Optional<User> getUserById(Long id);

    List<User> getAllByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);

    boolean isPresent(Long id);
}
