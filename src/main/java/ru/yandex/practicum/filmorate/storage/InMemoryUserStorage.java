package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public User addUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User getUser(int userId) {
        return users.get(userId);
    }

    @Override
    public Collection<User> getAllUsers() {
        Collection<User> allUsers = users.values();
        if (allUsers.isEmpty()) {
            allUsers.addAll(users.values());
        }
        return allUsers;
    }

    @Override
    public User updateUser(User user) {
        if (users.get(user.getId()) == null) {
            throw new NotFoundException(String.format("Пользователь с идентификатором %d не найден", user.getId()));
        }
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public boolean deleteUser(User user) {
        users.remove(user.getId());
        return true;
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.addFriend(friendId);
        friend.addFriend(userId);
        updateUser(user);
        updateUser(friend);
        return true;
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        User user = users.get(userId);
        User friend = users.get(friendId);
        user.deleteFriend(friendId);
        friend.deleteFriend(userId);
        updateUser(user);
        updateUser(friend);
        return true;
    }
}

