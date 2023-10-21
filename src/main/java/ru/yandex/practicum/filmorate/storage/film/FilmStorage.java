package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);

    Film update(Film film);

    List<Film> getFilms();

    Film getFilmById(int id);

    List<Film> getPopularFilms(int size);

}
