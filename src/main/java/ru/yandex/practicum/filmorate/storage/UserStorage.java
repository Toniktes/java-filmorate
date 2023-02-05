package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.Map;

public interface UserStorage {
    User addUser(User user);

    User getUser(int userId);

    Map<Integer, User> userMap();

    Collection<User> getAllUsers();

    User updateUser(User user);

    boolean deleteUser(User user);

    boolean addFriend(int userId, int friendId);

    boolean deleteFriend(int userId, int friendId);
}
