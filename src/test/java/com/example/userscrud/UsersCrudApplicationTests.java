package com.example.userscrud;

import com.example.userscrud.config.TestContainerConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestContainerConfiguration.class)
class UsersCrudApplicationTests {

    @Test
    void contextLoads() {
    }

}
