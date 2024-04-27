package com.example.userscrud.dao;

import com.example.userscrud.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findAllByBirthDateBetween(LocalDate from, LocalDate to, Pageable pageable);

    Optional<User> findByEmail(String email);
}
