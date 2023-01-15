package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.Instant;

@AllArgsConstructor
@Data
public class Film {
    private int id;

    private String name;

    private String description;
    private Instant releaseDate;


    private Duration duration;
}
