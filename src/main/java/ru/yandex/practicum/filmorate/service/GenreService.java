package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.util.Collection;
import java.util.List;

@Service
public class GenreService {

    private final GenreStorage genreStorage;

    @Autowired
    public GenreService(GenreStorage genreStorage) {
        this.genreStorage = genreStorage;
    }

    public Collection<Genre> getAllGenres() {
        return genreStorage.getAllGenres();
    }

    public Collection<Genre> getFilmGenres(int filmId) {
        return genreStorage.getGenresByFilmId(filmId);
    }

    public Genre getGenre(int genreId) {
        return genreStorage.getGenreById(genreId);
    }

    public boolean deleteFilmGenres(int filmId) {
        return genreStorage.deleteFilmGenres(filmId);
    }

    public boolean addFilmGenres(int filmId, Collection<Genre> genres) {
        return genreStorage.addFilmGenres(filmId, genres);
    }
}