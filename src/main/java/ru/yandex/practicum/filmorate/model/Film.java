package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class Film {
    private int id;
    private String name;
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Positive
    private long duration;
}
