package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User addUser(User user);

    User getUser(int userId);

    Collection<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(User user);

    void addFriend(int userId, int friendId);

    void deleteFriend(int userId, int friendId);
}
