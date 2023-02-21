package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getAllGenres();

    List<Genre> getGenresByFilmId(int filmId);

    Genre getGenreById(int genreId);

    void deleteFilmGenres(int filmId);

    void addFilmGenres(int filmId, List<Genre> genres);
}
