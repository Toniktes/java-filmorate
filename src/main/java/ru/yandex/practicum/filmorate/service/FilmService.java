package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.Optional;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;
    private static final String CONTROL_DATE = "1895-12-28";
    private int generatorId = 0;

    @Autowired
    public FilmService(@Qualifier("filmDbBean") FilmStorage filmStorage, @Qualifier("userDbBean") UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public Film create(Film film) {
        validation(film);
        return filmStorage.addFilm(film);
    }

    public Film update(Film film) {
        validation(film);
        return filmStorage.updateFilm(film);
    }

    public Collection<Film> findAll() {
        return filmStorage.getAllFilms();
    }

    private void generateId(Film film) {
        if (film.getId() == 0) {
            film.setId(++generatorId);
        }
    }

    public void addLike(int filmId, int userId) {
        validateLike(filmId, userId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        validateLike(filmId, userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public Film getFilm(int filmId) {
        return Optional.ofNullable(filmStorage.getFilm(filmId))
                .orElseThrow(() -> new NotFoundException("Фильм с идентификатором " + filmId + " не зарегистрирован!"));
    }

    public Collection<Film> getMostPopularFilms(int count) {
        return filmStorage.getMostPopularFilms(count);
    }

    private void validation(Film film) {
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
        generateId(film);
    }

    private void validateLike(int filmId, int userId) {
        Optional.ofNullable(filmStorage.getFilm(filmId))
                .orElseThrow(() -> new NotFoundException("Фильм с идентификатором " + filmId + " не зарегистрирован!"));
        Optional.ofNullable(userStorage.getUser(userId))
                .orElseThrow(() -> new NotFoundException("Пользователь с идентификатором " + userId + " не зарегистрирован!"));
    }
}
