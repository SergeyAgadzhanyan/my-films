package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import ru.yandex.practicum.filmorate.annotation.RealiseDate;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class Film {
    private int id;
    @NotBlank
    private final String name;
    @NotBlank
    @Size(max = 200)
    private final String description;
    @NotNull
    @RealiseDate
    private final LocalDate releaseDate;
    @NotNull
    @Positive
    private final Double duration;
    private final LinkedHashSet<Genre> genres = new LinkedHashSet<>();
    @NotNull
    @Valid
    private Mpa mpa;


    public Set<Genre> addGenre(Genre genre) {
        genres.add(genre);
        return genres;
    }

}
