package com.example.userscrud;

import com.example.userscrud.model.User;

import java.time.LocalDate;

public class Utils {

    private static final User USER = new User();

    static {
        USER.setId(1L);
        USER.setEmail("user@mail.com");
        USER.setFirstName("John");
        USER.setLastName("Black");
        USER.setBirthDate(LocalDate.of(2000, 1, 1));
        USER.setAddress("Kyiv, Khreshchatyk street, 18");
        USER.setPhoneNumber("+380123456789");
    }

    public static User getDefaultUser() {
        return USER;
    }
}
