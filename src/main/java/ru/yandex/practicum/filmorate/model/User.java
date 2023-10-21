package ru.yandex.practicum.filmorate.model;


import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.time.LocalDate;

@AllArgsConstructor
@Data
public class User {
    private int id;
    @NotBlank
    @Email
    private final String email;
    @NotBlank
    private final String login;
    private String name;
    @NotNull
    @PastOrPresent
    private final LocalDate birthday;


}
