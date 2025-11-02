package Homework2test.service;

import dao.UserDao;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



@ExtendWith(MockitoExtension.class)
class UserServiceRegisterTest {

    private static final Logger log = LoggerFactory.getLogger(UserServiceRegisterTest.class);

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;

    @Captor
    private ArgumentCaptor<User> userCaptor;


    @Test
    @DisplayName("registerUser: сохраняет нового пользователя, если email валиден и не занят")
    void registerUser_shouldSaveUser_whenValidData() {

        String name = "Денис";
        String email = "denis@test.com";
        Integer age = 27;
        log.info("1. Тест: registerUser — создаём пользователя name='{}', email='{}', age={}", name, email, age);

        when(userDao.isEmailExists(email)).thenReturn(false);

        when(userDao.saveUser(any(User.class))).thenReturn(1L);

        Long userId = userService.registerUser(name, email, age);
        log.debug("2. Метод registerUser() вернул userId={}", userId);

        assertNotNull(userId, "ID не должен быть null");
        assertEquals(1L, userId);

        verify(userDao).isEmailExists(email);

        verify(userDao).saveUser(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        log.info("3. Сохранён пользователь: name='{}', email='{}', age={}",
                savedUser.getName(), savedUser.getEmail(), savedUser.getAge());

        assertEquals(name, savedUser.getName());
        assertEquals(email, savedUser.getEmail());
        assertEquals(age, savedUser.getAge());

        verifyNoMoreInteractions(userDao);
    }


    @Test
    @DisplayName("registerUser: должен выбросить исключение, если email уже существует")
    void registerUser_shouldThrow_whenEmailExists() {

        String name = "Артем";
        String email = "artem@example.com";
        Integer age = 20;

        when(userDao.isEmailExists(email)).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(name, email, age)
        );

        assertTrue(ex.getMessage().contains("уже существует"));

        verify(userDao).isEmailExists(email);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @DisplayName("registerUser: должен выбросить исключение, если email некорректный")
    void registerUser_shouldThrow_whenEmailInvalid() {

        String name = "Dmitriy";
        String email = "Dmitriyemailcom";
        Integer age = 40;

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(name, email, age)
        );

        assertTrue(ex.getMessage().contains("Некорректный формат"));

        verifyNoInteractions(userDao);
    }
    @Test
    @DisplayName("registerUser: должен выбросить исключение, если имя пустое")
    void registerUser_shouldThrow_whenNameEmpty() {

        String name = " ";
        String email = "test@example.com";
        Integer age = 62;

        assertThrows(IllegalArgumentException.class,
                () -> userService.registerUser(name, email, age));

        verifyNoInteractions(userDao);
    }
}