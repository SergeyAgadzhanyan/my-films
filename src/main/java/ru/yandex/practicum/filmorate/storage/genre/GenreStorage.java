package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;

public interface GenreStorage {
    List<Genre> getGenres();

    Genre getGenreById(int id);

    void addGenresToFilms(List<Film> films);

    void updateGenres(Film film);

    void insertGenres(Film film);
}
