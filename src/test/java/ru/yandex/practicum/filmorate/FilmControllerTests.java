package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FilmControllerTests {


    @Test
    public void checkValidity() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            Film film1 = new Film(1, "name", "test1",
                    LocalDate.of(1895, 12, 28), 120.0, new Mpa(1,"G"));
            Film film2 = new Film(2, "", "test1",
                    LocalDate.of(1895, 12, 28), 120.0,  new Mpa(1,"G"));
            String someLongText = "Behind me, field and meadow sleeping,\n" +
                    "I leave in deep, prophetic night,\n" +
                    "Within whose dread and holy keeping\n" +
                    "The better soul awakes to light.\n" +
                    "The wild desires no longer win us,\n" +
                    "The deeds of passion cease to chain;\n" +
                    "The love of Man revives within us,\n" +
                    "The love of God revives again.\n" +
                    "Be still, thou poodle; make not such racket and riot!\n" +
                    "Why at the threshold wilt snuffing be?\n" +
                    "Behind the stove repose thee in quiet!\n" +
                    "My softest cushion I give to thee.\n" +
                    "As thou, up yonder, with running and leaping\n" +
                    "Amused us hast, on the mountain's crest,\n" +
                    "So now I take thee into my keeping,\n" +
                    "A welcome, but also a silent, guest.";

            Film film3 = new Film(3, "name3", someLongText,
                    LocalDate.of(1895, 12, 28), 120.0,new Mpa(1,"G"));
            Film film4 = new Film(4, "name3", "description",
                    null, 120.0, new Mpa(1,"G"));
            Film film5 = new Film(5, "name3", someLongText,
                    LocalDate.of(1895, 12, 28), -120.0, new Mpa(1,"G"));
            assertTrue(validator.validate(film1).isEmpty());
            assertFalse(validator.validate(film2).isEmpty());
            assertFalse(validator.validate(film3).isEmpty());
            assertFalse(validator.validate(film4).isEmpty());
            assertFalse(validator.validate(film5).isEmpty());

            Film filmAnnotationRealiseDate = new Film(0, "name", "test1",
                    LocalDate.of(1893, 12, 28), 120.0,new Mpa(1,"G"));
            assertFalse(validator.validate(filmAnnotationRealiseDate).isEmpty());
        }
    }
}
