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
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collections;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class FilmControllerTest {
        @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
        @MockBean
        private FilmController filmController;

        @Test
        void shouldReturn200whenGetFilms() throws Exception {
            Film film = Film.builder()
                    .id(1)
                    .name("Kyle Reese")
                    .description("terminatormail@")
                    .releaseDate(LocalDate.of(2000, 8,20))
                    .duration(100)
                    .rate(4)
                    .build();
            Mockito.when(filmController.findAll()).thenReturn(Collections.singletonList(film));
            mockMvc.perform(get("/films"))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(Collections.singletonList(film))));
        }

        @Test
        void shouldReturn200whenPostCorrectFilmData() throws Exception {
            Film film = Film.builder()
                    .id(1)
                    .name("Kyle Reese")
                    .description("terminatormail@")
                    .releaseDate(LocalDate.of(2000, 8,20))
                    .duration(100)
                    .rate(4)
                    .build();
            Mockito.when(filmController.create(Mockito.any())).thenReturn(film);
            mockMvc.perform(post("/films")
                            .content(objectMapper.writeValueAsString(film))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().json(objectMapper.writeValueAsString(film)));
        }
    }
    /*FilmController filmController;
    Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film(1, "Фильм1", "Содержание", LocalDate.now(), 20, );
    }

    @AfterEach
    void afterEach() {
        filmController = null;
        film = null;
    }

    @Test
    void contextLoads() {
    }

    @Test
    void filmNameIsEmpty() {
        film.setName("");
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void filmHaveDescription210Char() {
        String space210 = new String(new char[210]).replace('\0', ' ');
        film.setDescription(space210);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void filmHaveIncorrectReleaseDate() {
        film.setReleaseDate(LocalDate.parse("1895-11-28"));
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void filmHaveNegativeOrNullDuration() {
        film.setDuration(0);
        assertThrows(ValidationException.class, () -> filmController.create(film));
        film.setDuration(-1);
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }*/

