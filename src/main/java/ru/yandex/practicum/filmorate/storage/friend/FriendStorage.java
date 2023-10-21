package ru.yandex.practicum.filmorate.storage.friend;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Set;

public interface FriendStorage {
    Set<Integer> addFriend(int userId, int friendId);

    Set<User> getFriends(int userId);

    Set<Integer> deleteFriend(int userId, int friendId);

    Set<User> getGeneralFriends(int id, int otherId);
}
