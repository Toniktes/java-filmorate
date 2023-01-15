package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.Instant;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class UserControllerTest {
    UserController userController;
    User user;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
        user = new User(1, "Anton", "teslyaaanton@yandex.ru", "Horizont", LocalDate.parse("1993-04-09"));
    }

    @AfterEach
    void afterEach() {
        userController = null;
        user = null;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void ifUserNameIsEmpty() throws ValidationException {
        user.setName("");
        userController.create(user);
        assertEquals(user.getEmail(), user.getName());
    }

    @Test
    void emailIsBlankOrHaveNotCharDog() {
        user.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        user.setEmail("hotdogYandex.ru");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void loginIsEmptyOrHaveSpaceTest() {
        user.setLogin("");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
        user.setLogin("log in");
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }

    @Test
    void birthdayInFutureTest() {
        user.setBirthday(LocalDate.parse("3000-04-09"));
        Assertions.assertThrows(ValidationException.class, () -> userController.create(user));
    }
}
