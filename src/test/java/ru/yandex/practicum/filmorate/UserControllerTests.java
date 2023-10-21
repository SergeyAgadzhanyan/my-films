package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserControllerTests {

    private final UserController userController;

    @Test
    public void checkValidity() {
        try (ValidatorFactory validatorFactory = Validation.buildDefaultValidatorFactory()) {
            Validator validator = validatorFactory.getValidator();
            User user1 = new User(1, "test@mail", "login1", "name",
                    LocalDate.of(2023, 4, 12));
            User user2 = new User(2, "mail", "login1", "name",
                    LocalDate.of(2023, 4, 12));
            User user3 = new User(3, "test@mail", "login1", "name",
                    LocalDate.now().plusDays(1));

            assertTrue(validator.validate(user1).isEmpty());
            assertFalse(validator.validate(user2).isEmpty());
            assertFalse(validator.validate(user3).isEmpty());
        }
    }

    @Test
    public void checkValidLogin() {
        User user1 = new User(1, "test@mail", "login1", "name",
                LocalDate.of(2023, 4, 12));
        User user2 = new User(2, "test2@mail", "lo gin2", "name",
                LocalDate.of(2023, 4, 12));
        assertEquals(user1, userController.addUser(user1));
        assertThrows(ValidationException.class, () -> userController.addUser(user2));
    }

    @Test
    public void generateName() {
        User user1 = userController.addUser(new User(3, "test@mail", "login1", null,
                LocalDate.of(2023, 4, 12)));

        assertEquals("login1", user1.getName());
    }
}
