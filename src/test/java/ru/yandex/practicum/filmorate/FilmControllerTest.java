package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.Duration;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {
    ru.yandex.practicum.filmorate.controllers.FilmController filmController;
    Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new ru.yandex.practicum.filmorate.controllers.FilmController();
        film = new Film(1, "Фильм1", "Содержание", Instant.now(), Duration.ofHours(2));
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
        film.setReleaseDate(Instant.parse("1985-11-28T00:00:00Z"));
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }

    @Test
    void filmHaveNegativeOrNullDuration() {
        film.setDuration(Duration.ofSeconds(0));
        assertThrows(ValidationException.class, () -> filmController.create(film));
        film.setDuration(Duration.ofSeconds(-1));
        assertThrows(ValidationException.class, () -> filmController.create(film));
    }
}
