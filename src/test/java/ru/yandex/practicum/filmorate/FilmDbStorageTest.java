package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {

    private final FilmDbStorage filmDbStorage;

    @Test
    public void getFilmTest() {

        Film film1 = new Film(1,
                "name1",
                "des1",
                LocalDate.now().minusDays(1),
                90L,
                3,
                new Mpa(1, "name1", "des1"),
                new ArrayList<>(),
                new ArrayList<>());
        filmDbStorage.addFilm(film1);

        Film dbFilm = filmDbStorage.getFilm(1);
        assertThat(dbFilm).hasFieldOrPropertyWithValue("id", 1);
    }

    @Test
    void getAllFilms() {
        Film film1 = new Film(1,
                "name1",
                "des1",
                LocalDate.now().minusDays(1),
                90L,
                3,
                new Mpa(1, "name1", "des1"),
                new ArrayList<>(),
                new ArrayList<>());
        Film film2 = new Film(0,
                "name2",
                "des2",
                LocalDate.now().minusDays(1),
                100L,
                3,
                new Mpa(3, "name2", "des2"),
                new ArrayList<>(),
                new ArrayList<>());
        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);

        Collection<Film> dbFilms = filmDbStorage.getAllFilms();
        assertEquals(2, dbFilms.size());
    }

    @Test
    void updateFilm() {
        Film film1 = new Film(1,
                "name1",
                "des1",
                LocalDate.now().minusDays(1),
                90L,
                3,
                new Mpa(1, "name1", "des1"),
                new ArrayList<>(),
                new ArrayList<>());
        Film added = filmDbStorage.addFilm(film1);
        added.setName("update");
        filmDbStorage.updateFilm(added);
        Film dbFilm = filmDbStorage.getFilm(added.getId());
        assertThat(dbFilm).hasFieldOrPropertyWithValue("name", "update");
    }

    @Test
    void deleteFilm() {
        Film film1 = new Film(1,
                "name1",
                "des1",
                LocalDate.now().minusDays(1),
                90L,
                3,
                new Mpa(1, "name1", "des1"),
                new ArrayList<>(),
                new ArrayList<>());
        Film film2 = new Film(0,
                "name2",
                "des2",
                LocalDate.now().minusDays(1),
                100L,
                3,
                new Mpa(3, "name2", "des2"),
                new ArrayList<>(),
                new ArrayList<>());
        Film addedFirst = filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);

        Collection<Film> delete = filmDbStorage.getAllFilms();
        filmDbStorage.deleteFilm(addedFirst);
        Collection<Film> afterDelete = filmDbStorage.getAllFilms();
        assertEquals(delete.size() - 1, afterDelete.size());
    }
}
