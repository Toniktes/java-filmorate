package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {

    private final UserDbStorage userDbStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        user1 = new User(1,
                "name1",
                "email1",
                "login1",
                LocalDate.of(2020, 2, 15),
                new ArrayList<>());
        user2 = new User(2,
                "name2",
                "email2",
                "login2",
                LocalDate.of(2020, 2, 16),
                new ArrayList<>());
        user3 = new User(3,
                "name3",
                "email3",
                "login3",
                LocalDate.of(2020, 2, 17),
                new ArrayList<>());
    }
    @AfterEach
    public void afterEach() {
        user1 = null;
        user2 = null;
        user3 = null;
    }


    @Test
    public void getUserTest() {
        User dbUser = userDbStorage.addUser(user1);
        assertThat(dbUser).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllUsersTest() {
        userDbStorage.addUser(user2);
        userDbStorage.addUser(user3);
        Collection<User> dbUsers = userDbStorage.getAllUsers();
        assertEquals(2, dbUsers.size());
    }

    @Test
    void deleteUser() {
        Collection<User> countUsers = userDbStorage.getAllUsers();
        userDbStorage.deleteUser(user1);
        Collection<User> countUsersBefore = userDbStorage.getAllUsers();
        assertEquals(countUsers.size() - 1, countUsersBefore.size());
    }
}
