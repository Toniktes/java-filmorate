package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.Positive;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Film {
    private int id;
    private String name;
    private String description;
    @NonNull
    private LocalDate releaseDate;
    @Positive
    private long duration;
    private int rate;

    private Set<Integer> likes = new HashSet<>();

    public void addLike(Integer userId) {
        likes.add(userId);
    }

    public void deleteLike(Integer userId) {
        likes.remove(userId);
    }
}
