package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;
import java.time.Instant;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private static final String CONTROL_DATE = "1895-12-28";
    private int generatorId = 0;

    @PostMapping(value = "/films")
    public Film create(@RequestBody Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse(CONTROL_DATE))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительной");
        }
        if (film.getId() == 0) {
            film.setId(++generatorId);
        }
        films.put(film.getId(), film);
        log.debug("");
        log.info("Фильм добавлен: " + film);
        return film;
    }

    @PutMapping(value = "/films")
    public Film update(@RequestBody Film film) throws ValidationException {
        if (film.getName().isBlank()) {
            throw new ValidationException("Название не может быть пустым");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.parse(CONTROL_DATE))) {
            throw new ValidationException("Дата релиза — не раньше 28 декабря 1895 года");
        }
        if (film.getDuration() <= 0) {
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
