package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GenreStorage {
    Collection<Genre> getAllGenres();

    Collection<Genre> getGenresByFilmId(int filmId);

    Genre getGenreById(int genreId);

    boolean deleteFilmGenres(int filmId);

    boolean addFilmGenres(int filmId, Collection<Genre> genres);
}
