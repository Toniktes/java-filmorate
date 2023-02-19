package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    private List<Integer> friends = new ArrayList<>();

    public void addFriend(Integer id) {
        friends.add(id);
    }

    public void deleteFriend(final Integer id) {
        friends.remove(id);
    }

}
