package ru.yandex.practicum.filmorate.storage.film;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.EntityNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;


    @Override
    public Film addFilm(Film film) {
        String query = "INSERT INTO PUBLIC.FILMS (NAME, DESCRIPTION, RELEASE_DATE, DURATION,RATING_ID)"
                + "VALUES (?,?,?,?,?);";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(query, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setDouble(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);

        film.setId(keyHolder.getKey().intValue());
        return film;
    }

    @Override
    public Film update(Film film) {
        getFilmById(film.getId());

        String filmQuery = "UPDATE PUBLIC.FILMS" + " SET NAME = ?, DESCRIPTION = ?, RELEASE_DATE = ?, DURATION = ?, " +
                "RATING_ID = ?  WHERE ID = ?;";

        jdbcTemplate.update(filmQuery, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());

        return film;
    }


    @Override
    public List<Film> getFilms() {
        String genreQuery = "SELECT fg.FILM_ID, g.GENRE_ID, g.TITLE FROM PUBLIC.FILM_GENRE AS fg" +
                " LEFT JOIN GENRE as g on fg.GENRE_ID = g.GENRE_ID";
        SqlRowSet genreSet = jdbcTemplate.queryForRowSet(genreQuery);

        String query = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE" + ",f.DURATION, r.RATING_ID, r.TITLE  " +
                "FROM PUBLIC.FILMS as f" + " left join PUBLIC.RATING as r on f.RATING_ID = r.RATING_ID";

        return jdbcTemplate.query(query, (rs, row) -> makeFilm(rs));
    }


    @Override
    public Film getFilmById(int id) {

        String filmQuery = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE," + "f.DURATION, r.RATING_ID, r.TITLE" +
                "  FROM PUBLIC.FILMS as f " + "left join PUBLIC.RATING as r on f.RATING_ID = r.RATING_ID " + "WHERE f.ID = ?;";
        List<Film> films = jdbcTemplate.query(filmQuery, (rs, row) -> makeFilm(rs), id);

        if (films.isEmpty()) throw new EntityNotFoundException("FILM DOES NOT EXIST");
        return films.get(0);
    }

    @Override
    public List<Film> getPopularFilms(int size) {
        String query = " SELECT f.ID, f.NAME, f.DESCRIPTION, f.RELEASE_DATE,f.DURATION, r.RATING_ID, r.TITLE\n" +
                "                 FROM PUBLIC.FILMS as f\n" +
                "                     left join PUBLIC.RATING as r on f.RATING_ID = r.RATING_ID\n" +
                "                left join FILM_LIKES as fl on f.ID = fl.FILM_ID\n" +
                "                group by f.ID\n" +
                "                order by count(fl.USER_ID) desc\n" +
                "                limit ?;";
        return jdbcTemplate.query(query, (rs, row) -> makeFilm(rs), size);

    }


    private Film makeFilm(ResultSet rs) throws SQLException {
        int filmId = rs.getInt("ID");

        return new Film(filmId, rs.getString("NAME"), rs.getString("DESCRIPTION"),
                rs.getDate("RELEASE_DATE").toLocalDate(), rs.getDouble("DURATION"), new Mpa(rs.getInt("RATING_ID"),
                rs.getString("TITLE")));
    }

}
