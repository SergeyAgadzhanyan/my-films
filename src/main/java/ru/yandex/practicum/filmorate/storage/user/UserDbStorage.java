package ru.yandex.practicum.filmorate.storage.user;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String WRONG_ID_MESSAGE = "Wrong id";


    @Override
    public User addUser(User user) {

        String query = "INSERT INTO PUBLIC.USERS (NAME, LOGIN, EMAIL, BIRTHDAY)" + "VALUES (?, ?," +
                " ?, ?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, user.getName());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getEmail());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        user.setId(keyHolder.getKey().intValue());
        return user;
    }

    @Override
    public User updateUser(User user) {
        String query = "UPDATE PUBLIC.USERS" +
                " SET NAME = ?, LOGIN = ?, EMAIL = ?, BIRTHDAY= ?  WHERE ID = ?;";
        int i = jdbcTemplate.update(query, user.getName(), user.getLogin(),
                user.getEmail(), user.getBirthday(), user.getId());
        if (i == 0) throw new EntityNotFoundException(WRONG_ID_MESSAGE);
        return user;

    }


    @Override
    public List<User> getUsers() {
        String query = " SELECT *  FROM PUBLIC.USERS";
        return jdbcTemplate.query(query, (rs, row) -> makeUser(rs));
    }

    @Override
    public User getUserById(int id) {
        String userQuery = " SELECT *  FROM PUBLIC.USERS WHERE ID = ?";
        List<User> users = jdbcTemplate.query(userQuery, (rs, row) -> makeUser(rs), id);
        if (users.isEmpty()) throw new EntityNotFoundException(WRONG_ID_MESSAGE);
        return users.get(0);
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");

        return new User(id, rs.getString("EMAIL"), rs.getString("LOGIN"), rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());

    }

}
