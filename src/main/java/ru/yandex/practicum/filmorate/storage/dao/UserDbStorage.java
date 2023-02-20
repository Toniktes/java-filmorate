package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component("UserDbStorage")
public class UserDbStorage implements UserStorage {

    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private User getUserDb(ResultSet resultSet) throws SQLException {
        int userId = resultSet.getInt("UserID");
        return new User(
                userId,
                resultSet.getString("Name"),
                resultSet.getString("Email"),
                resultSet.getString("Login"),
                Objects.requireNonNull(resultSet.getDate("BirthDay")).toLocalDate(),
                getUserFriends(userId));
    }

    private List<Integer> getUserFriends(int userId) {
        String sqlGetFriends = "select FRIENDID from FRIENDLIST where USERID = ?";
        return jdbcTemplate.queryForList(sqlGetFriends, Integer.class, userId);
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "insert into USERS " +
                "(NAME, EMAIL, LOGIN, BIRTHDAY) " +
                "values (?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getName());
            preparedStatement.setString(2, user.getEmail());
            preparedStatement.setString(3, user.getLogin());
            preparedStatement.setDate(4, Date.valueOf(user.getBirthday()));
            return preparedStatement;
        }, keyHolder);

        int id = Objects.requireNonNull(keyHolder.getKey()).intValue();

        if (!user.getFriends().isEmpty()) {
            for (Integer friendId : user.getFriends()) {
                addFriend(user.getId(), friendId);
            }
        }
        return getUser(id);
    }

    @Override
    public User getUser(int userId) {
        String sqlUser = "select * from USERS where USERID = ?";
        User user;
        try {
            user = jdbcTemplate.queryForObject(sqlUser, (rs, rowNum) -> getUserDb(rs), userId);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundException("Пользователь с идентификатором " +
                    userId + " не зарегистрирован!");
        }
        return user;
    }

    @Override
    public Collection<User> getAllUsers() {
        String sql = "select * from USERS";
        return jdbcTemplate.query(sql, (resultSet, rowNum) -> getUserDb(resultSet));
    }

    @Override
    public User updateUser(User user) {
        String sqlUser = "update USERS set " +
                "EMAIL = ?, LOGIN = ?, NAME = ?, BIRTHDAY = ? " +
                "where USERID = ?";
        jdbcTemplate.update(sqlUser, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUser(user.getId());
    }

    @Override
    public boolean deleteUser(User user) {
        String sqlQuery = "delete from USERS where USERID = ?";
        jdbcTemplate.update(sqlQuery, user.getId());
        return true;
    }

    @Override
    public boolean addFriend(int userId, int friendId) {
        boolean friendAccepted;
        String sqlGetReversFriend = "select * from FRIENDLIST " +
                "where USERID = ? and FRIENDID = ?";
        SqlRowSet sqlRowSet = jdbcTemplate.queryForRowSet(sqlGetReversFriend, friendId, userId);
        friendAccepted = sqlRowSet.next();
        String sqlSetFriend = "insert into FRIENDLIST (USERID, FRIENDID, STATUS) " +
                "VALUES (?,?,?)";
        jdbcTemplate.update(sqlSetFriend, userId, friendId, friendAccepted);
        if (friendAccepted) {
            String sqlSetStatus = "update FRIENDLIST set STATUS = true " +
                    "where USERID = ? and FRIENDID = ?";
            jdbcTemplate.update(sqlSetStatus, friendId, userId);
        }
        return true;
    }

    @Override
    public boolean deleteFriend(int userId, int friendId) {
        String sqlDeleteFriend = "delete from FRIENDLIST where USERID = ? and FRIENDID = ?";
        jdbcTemplate.update(sqlDeleteFriend, userId, friendId);
        String sqlSetStatus = "update FRIENDLIST set STATUS = false " +
                "where USERID = ? and FRIENDID = ?";
        jdbcTemplate.update(sqlSetStatus, friendId, userId);
        return true;
    }
}
