package ru.yandex.practicum.filmorate.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@Slf4j
@RequestMapping("/films")
public class FilmController {

    private final FilmService filmService;

    public FilmController(@Autowired FilmService filmService) {
        this.filmService = filmService;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Получен запрос на добавление фильма");
        Film addFilm = filmService.create(film);
        log.info("Фильм с идентификатором {} добавлен!", addFilm.getId());
        return addFilm;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Получен запрос на обновление данных фильма");
        Film updateFilm = filmService.update(film);
        log.info("Данные о фильме с идентификатором {} обновлены!", updateFilm.getId());
        return updateFilm;
    }

    @GetMapping("/{id}")
    public Film getFilm(@PathVariable int id) {
        log.info("Запрос на на получение фильма id: {}", id);
        return filmService.getFilm(id);
    }

    @GetMapping
    public Collection<Film> findAll() {
        log.info("Запрос на получение списка фильмов");
        return filmService.findAll();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос на добавление Like для фильма id: {} от пользователя id: {}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void deleteLike(@PathVariable int id, @PathVariable int userId) {
        log.info("Получен запрос на удаление Like для фильма id: {} от пользователя id: {}", id, userId);
        filmService.deleteLike(id, userId);
    }

    @GetMapping({"/popular?count={count}", "/popular"})
    public Collection<Film> findMostPopular(@RequestParam(defaultValue = "10") int count) {
        log.info("Запрос на получение списка самых популярных фильмов");
        return filmService.getMostPopularFilms(count);
    }

}

