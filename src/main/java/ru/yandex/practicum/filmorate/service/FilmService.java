package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;

@Service
public class FilmService {
    FilmStorage filmStorage;
    UserService userService;
    private static final String CONTROL_DATE = "1895-12-28";
    private int generatorId = 0;

    @Autowired
    public FilmService(FilmStorage filmStorage, @Autowired(required = false) UserService userService) {
        this.filmStorage = filmStorage;
        this.userService = userService;
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
        Film film = getFilm(filmId);
        User user = userService.getUser(userId);
        filmStorage.addLike(filmId, userId);
    }

    public void deleteLike(int filmId, int userId) {
        Film film = getFilm(filmId);
        User user = userService.getUser(userId);
        filmStorage.deleteLike(filmId, userId);
    }

    public Film getFilm(int filmId) {
        Film film = filmStorage.getFilm(filmId);
        if (film == null) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не найден!");
        }
        return film;
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
}
