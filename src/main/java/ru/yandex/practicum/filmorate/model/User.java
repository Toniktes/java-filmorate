package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Data
public class User {
    private int id;
    private String name;
    @Email(message = "Введен некореектный адрес электронной почты.")
    private String email;
    @NotBlank
    private String login;
    @PastOrPresent(message = "Дата рождения не может быть в будущем.")
    private LocalDate birthday;

    private Set<Integer> friends;

    public boolean addFriend(final Integer id) {
        return friends.add(id);
    }

    public boolean deleteFriend(final Integer id) {
        return friends.remove(id);
    }

}
