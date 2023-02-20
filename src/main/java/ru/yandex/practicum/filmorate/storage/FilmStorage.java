package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.Map;

public interface FilmStorage {
    Film addFilm(Film film);

    Film getFilm(int filmId);

    Collection<Film> getAllFilms();

    Film updateFilm(Film film);

    boolean deleteFilm(Film film);

    boolean addLike(int filmId, int userId);

    boolean deleteLike(int filmId, int userId);

    Collection<Film> getMostPopularFilms(int size);
}
