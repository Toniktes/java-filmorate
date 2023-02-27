package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.dao.MpaDbStorage;

import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class MpaDbStorageTest {

    private final MpaDbStorage mpaDbStorage;

    @Test
    public void getMpaTest() {
        Mpa dbMpa = mpaDbStorage.getMpaById(2);
        assertThat(dbMpa).hasFieldOrPropertyWithValue("id", 2);
    }

    @Test
    void getAllMpaTest() {
        Collection<Mpa> allMpa = mpaDbStorage.getAllMpa();
        assertEquals(5, allMpa.size());
    }

}
