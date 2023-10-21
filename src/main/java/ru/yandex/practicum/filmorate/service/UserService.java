package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.friend.FriendStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String WRONG_LOGIN_MESSAGE = "Login can't contain blank space";
    private final UserStorage userStorage;
    private final FriendStorage friendStorage;

    public User addUser(User user) {
        checkValidLogin(user);
        generateName(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        checkValidLogin(user);
        generateName(user);
        return userStorage.updateUser(user);
    }

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public Set<Integer> addFriend(int userId, int friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        return friendStorage.addFriend(userId, friendId);
    }

    public void deleteFriend(int userId, int friendId) {
        userStorage.getUserById(userId);
        userStorage.getUserById(friendId);
        friendStorage.deleteFriend(userId, friendId);
    }

    public Set<User> getFriends(int userId) {
        userStorage.getUserById(userId);
        return friendStorage.getFriends(userId);
    }


    public Set<User> getGeneralFriends(int id, int otherId) {
        userStorage.getUserById(id);
        userStorage.getUserById(otherId);
        return friendStorage.getGeneralFriends(id, otherId);
    }

    private void checkValidLogin(User user) {
        if (user.getLogin().contains(" ")) {
            throw new ValidationException(WRONG_LOGIN_MESSAGE);
        }

    }

    private void generateName(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }

}
