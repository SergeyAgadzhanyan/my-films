package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@Slf4j
@RequiredArgsConstructor
public class AgeRatingController {
    private final FilmService filmService;

    @GetMapping
    public List<Mpa> getAgeRatings() {
        return filmService.getAgeRatings();
    }

    @GetMapping("/{id}")
    public Mpa getAgeRating(@PathVariable Integer id) {
        return filmService.getAgeRatingById(id);
    }
}
