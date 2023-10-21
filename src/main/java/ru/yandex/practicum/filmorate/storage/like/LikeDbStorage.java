package ru.yandex.practicum.filmorate.storage.like;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LikeDbStorage implements LikeStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Integer> addLike(int filmId, int userId) {

        String query = "INSERT INTO PUBLIC.FILM_LIKES (FILM_ID, USER_ID )" + "VALUES (?,?);";
        jdbcTemplate.update(query, filmId, userId);
        return getFilmLikes(filmId);
    }

    @Override
    public Set<Integer> deleteLike(int filmId, int userId) {

        String query = "DELETE from FILM_LIKES where FILM_ID = ? AND USER_ID = ?";
        jdbcTemplate.update(query, filmId, userId);
        return getFilmLikes(filmId);
    }

    @Override
    public Set<Integer> getFilmLikes(int filmId) {
        String query = "Select USER_ID from PUBLIC.FILM_LIKES WHERE FILM_ID = ?; ";
        return new HashSet<>(jdbcTemplate.query(query, (rs, row) -> rs.getInt("USER_ID"), filmId));
    }
}
