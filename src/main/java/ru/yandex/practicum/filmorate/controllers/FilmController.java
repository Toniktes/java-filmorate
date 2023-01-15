package ru.yandex.practicum.filmorate.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class FilmController {//()

    private final Map<Integer, Film> films = new HashMap<>();
    private static final String CONTROL_DATE = "1985-12-28T00:00:00Z";
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @PostMapping(value = "/film")
    public Film create(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(Instant.parse(CONTROL_DATE))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().getSeconds() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        films.put(film.getId(), film);
        log.debug("");
        log.info("Фильм добавлен: " + film);
        return film;
    }

    @PutMapping(value = "/film")
    public Film update(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(Instant.parse(CONTROL_DATE))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration().getSeconds() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        films.put(film.getId(), film);
        log.debug("");
        log.info("Фильм добавлен: " + film);
        return film;
    }

    @GetMapping("/films")
    public Map<Integer, Film> findAll() {
        return films;
    }
}
