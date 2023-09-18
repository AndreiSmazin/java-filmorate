package ru.yandex.practicum.filmorate.service;


import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.service.impl.UserServiceInMemoryImpl;

@SpringBootTest
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserServiceInMemoryIntegrationTest extends UserServiceIntegrationTest {
    @Autowired
    public UserServiceInMemoryIntegrationTest(UserServiceInMemoryImpl userService) {
        super(userService);
    }

    @BeforeEach
    public void createTestData() {
        super.userService.createUser(testUser1);
        super.userService.createUser(testUser2);
        super.userService.createUser(testUser3);

        super.userService.addFriend(1, 2);
        super.userService.addFriend(2, 1);
        super.userService.addFriend(1, 3);
        super.userService.addFriend(3, 1);
    }
}
