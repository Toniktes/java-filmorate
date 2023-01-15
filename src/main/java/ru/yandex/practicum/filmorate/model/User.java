package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.Instant;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class User {
    private int id;
    private String name;
    private String email;
    private String login;
    private LocalDate birthday;
}
