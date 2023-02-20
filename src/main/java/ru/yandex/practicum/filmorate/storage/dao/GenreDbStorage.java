package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storage.GenreStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Component
public class GenreDbStorage implements GenreStorage {

    private final JdbcTemplate jdbcTemplate;

    public GenreDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Genre getGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return new Genre(resultSet.getInt("GenreID"), resultSet.getString("Name"));
    }

    @Override
    public Collection<Genre> getAllGenres() {
        String sqlGenre = "select GENREID, NAME from GENRE ORDER BY GENREID";
        return jdbcTemplate.query(sqlGenre, this::getGenre);
    }

    @Override
    public List<Genre> getGenresByFilmId(int filmId) {
        String sqlGenre = "select GENRE.GENREID, NAME from GENRE " +
                "INNER JOIN GENRELINE GL on GENRE.GENREID = GL.GENREID " +
                "where FILMID = ?";
        return jdbcTemplate.query(sqlGenre, this::getGenre, filmId);
    }

    @Override
    public Genre getGenreById(int genreId) {
        String sqlGenre = "select * from GENRE where GENREID = ?";
        Genre genre;
        try {
            genre = jdbcTemplate.queryForObject(sqlGenre, this::getGenre, genreId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Жанр с идентификатором " +
                    genreId + " не зарегистрирован!");
        }
        return genre;
    }

    @Override
    public boolean deleteFilmGenres(int filmId) {
        String deleteGenres = "delete from GENRELINE where FILMID = ?";
        jdbcTemplate.update(deleteGenres, filmId);
        return true;
    }

    @Override
    public boolean addFilmGenres(int filmId, Collection<Genre> genres) {
        Set<Genre> setGenre = new LinkedHashSet<>(genres);
        for (Genre genre : setGenre) {
            String addGenres = "insert into GenreLine (FilmID, GenreID) values (?,?)";
            jdbcTemplate.update(addGenres, filmId, genre.getId());
        }
        return true;
    }
}
