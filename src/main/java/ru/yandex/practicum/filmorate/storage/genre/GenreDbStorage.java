package ru.yandex.practicum.filmorate.storage.genre;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.function.Function.identity;

@Component
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        String genreQuery = "SELECT * FROM PUBLIC.GENRE";
        return jdbcTemplate.query(genreQuery, (rs, row) -> makeGenre(rs));
    }

    @Override
    public Genre getGenreById(int id) {
        String genreQuery = "SELECT * FROM PUBLIC.GENRE WHERE GENRE_ID = ?";
        List<Genre> genres = jdbcTemplate.query(genreQuery, (rs, row) -> makeGenre(rs), id);
        if (genres.isEmpty()) throw new EntityNotFoundException("GENRE DOES NOT EXIST");
        return genres.get(0);
    }

    @Override
    public void addGenresToFilms(List<Film> films) {
        final Map<Integer, Film> filmsMap = films.stream().collect(Collectors.toMap(Film::getId, identity()));
        String inSql = String.join(",", Collections.nCopies(films.size(), "?"));
        final String sqlQuery =
                "select * from GENRE g, FILM_GENRE fg where fg.GENRE_ID = g.GENRE_ID AND fg.FILM_ID in (" + inSql + ")";
        jdbcTemplate.query(sqlQuery, (rs) -> {
//Получили из ResultSet'a идентификатор фильма и извлекли по нему из мапы значение)
            final Film film = filmsMap.get(rs.getInt("FILM_ID"));
//Добавили в коллекцию внутри объекта класса FIlm новый жанр)
            film.addGenre(makeGenre(rs));
            //Преобразуем коллекцию типа Film к Integer и в массив, так как передавать требуется именно его
        }, films.stream().map(Film::getId).toArray());

    }

    @Override
    public void updateGenres(Film film) {
        String queryDeleteGenres = "DELETE from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(queryDeleteGenres, film.getId());
        insertGenres(film);
    }

    private Genre makeGenre(ResultSet rs) throws SQLException {
        return new Genre(rs.getInt("GENRE_ID"), rs.getString(
                "TITLE"));
    }

    @Override
    public void insertGenres(Film film) {
        String query = "INSERT INTO PUBLIC.FILM_GENRE (FILM_ID, GENRE_ID)" + "VALUES (?,?);";
        List<Genre> genres = new ArrayList<>(film.getGenres());
        jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, film.getId());
                ps.setInt(2, genres.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genres.size();
            }
        });

    }
}
