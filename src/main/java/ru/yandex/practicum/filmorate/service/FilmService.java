package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;
import ru.yandex.practicum.filmorate.storage.genre.GenreStorage;
import ru.yandex.practicum.filmorate.storage.like.LikeStorage;
import ru.yandex.practicum.filmorate.storage.rating.RatingStorage;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreStorage genreStorage;
    private final RatingStorage ratingStorage;
    private final LikeStorage likeStorage;
    private final UserStorage userStorage;


    public Set<Integer> addLike(int filmId, int userId) {
        filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        return likeStorage.addLike(filmId, userId);
    }

    public Set<Integer> deleteLike(int filmId, int userId) {
        filmStorage.getFilmById(filmId);
        userStorage.getUserById(userId);
        return likeStorage.deleteLike(filmId, userId);
    }

    public List<Film> popularFilms(Integer count) {
        List<Film> films = filmStorage.getPopularFilms(count);
        genreStorage.addGenresToFilms(films);
        return films;
    }

    public Film addFilm(Film film) {
        filmStorage.addFilm(film);
        genreStorage.insertGenres(film);
        return film;
    }

    public Film update(Film film) {
        filmStorage.update(film);
        genreStorage.updateGenres(film);
        return film;
    }

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        genreStorage.addGenresToFilms(films);
        return films;
    }

    public Film getFilmById(int id) {
        List<Film> films = List.of(filmStorage.getFilmById(id));
        genreStorage.addGenresToFilms(films);
        return films.get(0);
    }

    public List<Genre> getGenres() {
        return genreStorage.getGenres();
    }

    public Genre getGenreById(int id) {
        return genreStorage.getGenreById(id);
    }

    public List<Mpa> getAgeRatings() {
        return ratingStorage.getAgeRatings();
    }

    public Mpa getAgeRatingById(int id) {
        return ratingStorage.getAgeRatingById(id);
    }

}
