package com.example.userscrud.model;

import com.example.userscrud.validation.Adult;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotEmpty
    @Column(unique = true, nullable = false)
    private String email;

    @NotEmpty
    @Column(nullable = false)
    private String firstName;

    @NotEmpty
    @Column(nullable = false)
    private String lastName;

    @Adult
    @NotNull
    @Column(nullable = false)
    private LocalDate birthDate;

    private String address;

    private String phoneNumber;
}
