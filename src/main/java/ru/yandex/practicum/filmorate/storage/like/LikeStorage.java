package ru.yandex.practicum.filmorate.storage.like;

import java.util.Set;

public interface LikeStorage {
    Set<Integer> addLike(int filmId, int userId);

    Set<Integer> deleteLike(int filmId, int userId);

    Set<Integer> getFilmLikes(int filmId);
}
