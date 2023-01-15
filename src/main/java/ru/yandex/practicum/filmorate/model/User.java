package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.Instant;

@AllArgsConstructor
@Data
public class User {
    private int id;
    private String name;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String login;

    @Past
    private Instant birthday;
}