package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmoRateApplicationTests {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    private final User user = new User(1, "email", "l", "n", LocalDate.of(2000, 1, 1));
    Film film = new Film(1, "f1", "d1",
            LocalDate.of(1895, 12, 28), 120.0, new Mpa(1, "G"));

    @Test
    public void testFindUserById() {
        userStorage.addUser(user);

        Optional<User> userOptional = Optional.ofNullable(userStorage.getUserById(1));

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1)
                );
    }

    @Test
    public void testGetAllUsers() {
        assertEquals(0, userStorage.getUsers().size());
        userStorage.addUser(user);
        assertEquals(1, userStorage.getUsers().size());
    }

    @Test
    public void testAddUser() {
        int sizeBeforeAdd = userStorage.getUsers().size();
        userStorage.addUser(user);
        int sizeAfterAdd = userStorage.getUsers().size();
        assertTrue(sizeAfterAdd > sizeBeforeAdd);
    }

    @Test
    public void testUpdateUser() {
        userStorage.addUser(user);
        User user = userStorage.getUserById(1);
        user.setName("newName");
        userStorage.updateUser(user);
        assertEquals(userStorage.getUserById(1).getName(), user.getName());
    }

    @Test
    public void testAddFilm() {
        int sizeBeforeAdd = filmStorage.getFilms().size();
        filmStorage.addFilm(film);
        int sizeAfterAdd = filmStorage.getFilms().size();
        assertTrue(sizeAfterAdd > sizeBeforeAdd);
    }

    @Test
    public void testGetAllFilms() {
        List<Film> films = filmStorage.getFilms();
        assertEquals(0, filmStorage.getFilms().size());
        filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilms().size());
    }

    @Test
    public void testUpdateFilm() {
        filmStorage.addFilm(film);
        Film film2 = new Film(1, "f2", "d1",
                LocalDate.of(1895, 12, 28), 120.0, new Mpa(1, "G"));
        filmStorage.update(film2);

        assertEquals("f2", filmStorage.getFilmById(1).getName());
    }

    @Test
    public void testGetFilmById() {
        filmStorage.addFilm(film);
        assertEquals(1, filmStorage.getFilmById(1).getId());
    }

    @Test
    public void testGetAllGenres() {
        assertTrue(genreStorage.getGenres().size() > 0);
    }

    @Test
    public void testGetGenreById() {
        assertEquals(1, genreStorage.getGenreById(1).getId());
    }

    @Test
    public void testGetAllAgeRating() {
        assertTrue(ratingStorage.getAgeRatings().size() > 0);
    }

    @Test
    public void testGetAgeRatingById() {
        assertEquals(ratingStorage.getAgeRatingById(1).getId(), 1);

    }
}
