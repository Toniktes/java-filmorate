package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film addFilm(Film film);

    Film getFilm(int filmId);

    Collection<Film> getAllFilms();

    Film updateFilm(Film film);

    void deleteFilm(Film film);

    void addLike(int filmId, int userId);

    void deleteLike(int filmId, int userId);

    Collection<Film> getMostPopularFilms(int size);
}
