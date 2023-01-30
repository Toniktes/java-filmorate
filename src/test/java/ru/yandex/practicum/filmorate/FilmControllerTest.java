package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.controllers.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class FilmControllerTest {
    /*FilmController filmController;
    Film film;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
        film = new Film(1, "Фильм1", "Содержание", LocalDate.now(), 20);
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
}
