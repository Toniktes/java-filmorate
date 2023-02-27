package ru.yandex.practicum.filmorate.storage.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.*;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component("filmDbBean")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;
    private final GenreService genreService;

    public FilmDbStorage(JdbcTemplate jdbcTemplate, GenreService genreService) {
        this.jdbcTemplate = jdbcTemplate;
        this.genreService = genreService;
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "insert into FILM " +
                "(NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE, Rating_id) " +
                "values (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, film.getName());
            preparedStatement.setString(2, film.getDescription());
            preparedStatement.setDate(3, Date.valueOf(film.getReleaseDate()));
            preparedStatement.setLong(4, film.getDuration());
            preparedStatement.setInt(5, film.getRate());
            preparedStatement.setInt(6, Math.toIntExact(film.getMpa().getId()));
            return preparedStatement;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }
        if (!film.getLikes().isEmpty()) {
            film.getLikes().forEach(num -> addLike(film.getId(), num));
        }
        return getFilm(id);
    }

    @Override
    public Film getFilm(int filmId) {
        String sqlFilm = "select * from FILM " +
                "INNER JOIN RATINGMPA R on FILM.Rating_id = R.Rating_id " +
                "where Film_id = ?";
        Film film;
        try {
            film = jdbcTemplate.queryForObject(sqlFilm, (rs, rowNum) -> getFilmDb(rs), filmId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Фильм с идентификатором " +
                    filmId + " не зарегистрирован!");
        }
        log.info("Найден фильм: {} {}", filmId, film.getName());
        return film;
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "select * from FILM " +
                "INNER JOIN RATINGMPA R on FILM.Rating_id = R.Rating_id ";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> getFilmDb(resultSet));
    }

    @Override
    public Collection<Film> getMostPopularFilms(int size) {
        String sqlMostPopular = "select count(L.Like_id) as likeRate" +
                ",FILM.Film_id" +
                ",FILM.NAME ,FILM.DESCRIPTION ,RELEASEDATE ,DURATION ,RATE ,R.Rating_id, R.NAME, R.DESCRIPTION from FILM " +
                "left join LIKES L on L.Film_id = FILM.Film_id " +
                "inner join RATINGMPA R on R.Rating_id = FILM.Rating_id " +
                "group by FILM.Film_id " +
                "ORDER BY likeRate desc " +
                "limit ?";
        return jdbcTemplate.query(sqlMostPopular, (rs, rowNum) -> getFilmDb(rs), size);
    }

    private Film getFilmDb(ResultSet resultSet) throws SQLException {
        int filmId = resultSet.getInt("Film_id");
        return new Film(
                filmId,
                resultSet.getString("Name"),
                resultSet.getString("Description"),
                Objects.requireNonNull(resultSet.getDate("ReleaseDate")).toLocalDate(),
                resultSet.getLong("Duration"),
                resultSet.getInt("Rate"),
                new Mpa(resultSet.getInt("RatingMPA.Rating_id"),
                        resultSet.getString("RatingMPA.Name"),
                        resultSet.getString("RatingMPA.Description")),
                getFilmLikes(filmId),
                genreService.getFilmGenres(filmId));
    }

    private List<Integer> getFilmLikes(int filmId) {
        String sqlGetLikes = "select User_id from LIKES where Film_id = ?";
        return jdbcTemplate.queryForList(sqlGetLikes, Integer.class, filmId);
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "update FILM " +
                "set NAME = ?, DESCRIPTION = ?, RELEASEDATE = ?, DURATION = ?, RATE = ? ,Rating_id = ? " +
                "where Film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getMpa().getId(),
                film.getId());

        genreService.deleteFilmGenres(film.getId());
        if (!film.getGenres().isEmpty()) {
            genreService.addFilmGenres(film.getId(), film.getGenres());
        }

        if (film.getLikes() != null) {
            film.getLikes().forEach(num -> addLike(film.getId(), num));
        }
        return getFilm(film.getId());
    }

    @Override
    public void deleteFilm(Film film) {
        String sqlQuery = "delete from FILM where Film_id = ?";
        jdbcTemplate.update(sqlQuery, film.getId());
    }

    @Override
    public void addLike(int filmId, int userId) {
        String sql = "select * from LIKES where User_id = ? and Film_id = ?";
        SqlRowSet existLike = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        if (!existLike.next()) {
            String setLike = "insert into LIKES (User_id, Film_id) values (?, ?) ";
            jdbcTemplate.update(setLike, userId, filmId);
        }
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sql, userId, filmId);
        log.info(String.valueOf(sqlRowSet.next()));
        sqlRowSet.next();
    }

    @Override
    public void deleteLike(int filmId, int userId) {
        String deleteLike = "delete from LIKES where Film_id = ? and User_id = ?";
        jdbcTemplate.update(deleteLike, filmId, userId);
    }
}
