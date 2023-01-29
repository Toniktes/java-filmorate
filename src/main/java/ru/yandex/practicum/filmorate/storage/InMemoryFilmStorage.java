package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Integer, Film> films = new HashMap<>();
    private static final String CONTROL_DATE = "1895-12-28";
    private int generatorId = 0;

    public Film create(Film film) {
        validation(film);
        generateId(film);
        films.put(film.getId(), film);
        log.debug("");
        log.info("Фильм добавлен: " + film);
        return film;
    }

    public Film update(Film film) {
        validation(film);
        if (films.get(film.getId()) == null) {
            throw new ValidationException("Попытка обновить данные не существующего фильма");
        }
        generateId(film);
        films.put(film.getId(), film);
        log.debug("");
        log.info("Фильм добавлен: " + film);
        return film;
    }

    public Collection<Film> findAll() {
        return films.values();
    }

    private void generateId(Film film) {
        if (film.getId() == 0) {
            film.setId(++generatorId);
        }
    }

    private void validation(Film film) throws ValidationException {
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
    }
}

