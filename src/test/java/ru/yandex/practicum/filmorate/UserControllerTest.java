package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.yandex.practicum.filmorate.controllers.UserController;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserController userController;

    @MockBean
    private UserService service;


    @Test
    void shouldReturn200whenGetUsers() throws Exception {
        User user = User.builder()
                .id(5)
                .name("Kyle Reese")
                .email("ff@yandex.ru")
                .login("KReese")
                .birthday(LocalDate.of(2000, 8, 20))
                .build();
        Mockito.when(userController.findAll()).thenReturn(Collections.singletonList(user));
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(user))));
    }

    @Test
    void shouldReturn200whenPostCorrectUserData() throws Exception {
        User user = User.builder()
                .id(6)
                .name("Kyle Reese")
                .email("ff@yandex.ru")
                .login("KReese")
                .birthday(LocalDate.of(2000, 8, 20))
                .build();
        Mockito.when(userController.create(Mockito.any())).thenReturn(user);
        mockMvc.perform(post("/users")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }
}

/*@SpringBootTest
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
        assertEquals(user.getLogin(), user.getName());
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
    }*/

