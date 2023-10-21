package ru.yandex.practicum.filmorate.storage.friend;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FriendDbStorage implements FriendStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Integer> addFriend(int userId, int friendId) {
        getFriends(friendId);
        String query = "INSERT INTO PUBLIC.USER_FRIENDS (USER_ID, FRIEND_ID, STATUS_ID) VALUES (?, ?,1);";
        jdbcTemplate.update(query, userId, friendId);
        return getFriends(userId).stream().map(User::getId).collect(Collectors.toSet());
    }

    public Set<User> getFriends(int userId) {
        String query = "Select u.ID, u.NAME, u.LOGIN, u.EMAIL, u.BIRTHDAY From USER_FRIENDS as uf " +
                " left join USERS as u on u.Id = uf.FRIEND_ID Where uf.USER_ID = ?;";
        return new LinkedHashSet<>(jdbcTemplate.query(query, (rs, row) -> makeUser(rs), userId));
    }

    @Override
    public Set<Integer> deleteFriend(int userId, int friendId) {
        getFriends(friendId);
        String query = "DELETE from PUBLIC.USER_FRIENDS where USER_ID = ? AND FRIEND_ID = ?";
        jdbcTemplate.update(query, userId, friendId);
        return getFriends(userId).stream().map(User::getId).collect(Collectors.toSet());
    }

    @Override
    public Set<User> getGeneralFriends(int id, int otherId) {
        String query = "select *\n" +
                "from USERS as u\n" +
                "where u.ID in (select Friend_id\n" +
                "               from USER_FRIENDS\n" +
                "               where USER_ID = ?\n" +
                "                 and FRIEND_ID in\n" +
                "                     (select FRIEND_ID from USER_FRIENDS where USER_ID = ?))";
        return new HashSet<>(jdbcTemplate.query(query, (rs, row) -> makeUser(rs), id, otherId));
    }

    private User makeUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("ID");

        return new User(id, rs.getString("EMAIL"), rs.getString("LOGIN"), rs.getString("NAME"),
                rs.getDate("BIRTHDAY").toLocalDate());

    }
}
