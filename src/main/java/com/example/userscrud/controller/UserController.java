package com.example.userscrud.controller;

import com.example.userscrud.exceptions.DateViolationException;
import com.example.userscrud.model.User;
import com.example.userscrud.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final ObjectMapper objectMapper;

    private final Validator validator;

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody User user) {
        user = userService.createUser(user);
        return ResponseEntity.created(
                ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri()
        ).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Long id) {
        return ResponseEntity.of(userService.getUserById(id));
    }

    @GetMapping
    public ResponseEntity<List<User>> getAllBetween(@RequestParam(defaultValue = "0000-01-01") LocalDate from,
                                                    @RequestParam(defaultValue = "2100-01-01") LocalDate to,
                                                    Pageable pageable) {
        validateDates(from, to);
        return ResponseEntity.ok(userService.getAllByBirthDateBetween(from, to, pageable));
    }

    @PatchMapping(path = "/{id}", consumes = "application/json-patch+json")
    public ResponseEntity<User> partialUpdateUser(@RequestBody JsonPatch patch, @PathVariable Long id) throws JsonPatchException, JsonProcessingException {
        User user = userService.getUserById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        User userPatched = applyPatchToUserDto(patch, user);
        validateUser(userPatched);
        return ResponseEntity.ok(userService.updateUser(userPatched));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    private User applyPatchToUserDto(JsonPatch patch, User user) throws JsonPatchException, JsonProcessingException {
        JsonNode patched = patch.apply(objectMapper.convertValue(user, JsonNode.class));
        return objectMapper.treeToValue(patched, User.class);
    }

    private void validateUser(User userPatched) {
        Set<ConstraintViolation<User>> constraintViolations = validator.validate(userPatched);
        if (!constraintViolations.isEmpty())
            throw new ConstraintViolationException(constraintViolations);
    }

    private void validateDates(LocalDate from, LocalDate to) {
        if (from.isAfter(to))
            throw new DateViolationException("'from' date must be before 'to'");
    }
}
