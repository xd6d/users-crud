package com.example.userscrud.service;

import com.example.userscrud.dao.UserRepository;
import com.example.userscrud.model.User;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent())
            throw new EntityExistsException("User with email %s already exists".formatted(user.getEmail()));
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        Optional<User> foundByEmail = userRepository.findByEmail(user.getEmail());
        if (foundByEmail.isPresent() && !foundByEmail.get().equals(user)) {
            throw new EntityExistsException("User with email %s already exists".formatted(user.getEmail()));
        }
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public List<User> getAllByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable) {
        return userRepository.findAllByBirthDateBetween(from, to, pageable);
    }

    @Override
    @Transactional
    public boolean isPresent(Long id) {
        return userRepository.existsById(id);
    }
}
