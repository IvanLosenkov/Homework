package Homework2test.service;

import dao.UserDao;
import model.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.UserService;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceFindTest {

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("findUserById: возвращает пользователя, если ID валиден и пользователь найден")
    void findUserById_shouldReturnUser_whenExists() {

        Long id = 42L;
        User fromDao = new User("Денис", "denis@test.com", 27);

        when(userDao.getUserById(id)).thenReturn(Optional.of(fromDao));

        User result = userService.findUserById(id);

        assertNotNull(result, "Ожидаем, что метод вернёт пользователя");
        assertSame(fromDao, result, "Сервис должен вернуть объект из Dao без изменений");

        verify(userDao).getUserById(id);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @DisplayName("findUserById: бросает IllegalArgumentException, если ID == null")
    void findUserById_shouldThrow_whenIdIsNull() {

        Long id = null;

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUserById(id)
        );
        assertTrue(ex.getMessage().contains("ID"), "Сообщение должно содержать 'ID'");

        verifyNoInteractions(userDao);
    }

    @Test
    @DisplayName("findUserById: бросает IllegalArgumentException, если ID <= 0")
    void findUserById_shouldThrow_whenIdIsNonPositive() {

        Long id = 0L;

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.findUserById(id)
        );
        assertTrue(ex.getMessage().contains("ID"), "Сообщение должно содержать 'ID'");

        verifyNoInteractions(userDao);
    }

    @Test
    @DisplayName("findUserById: бросает RuntimeException, если пользователь не найден")
    void findUserById_shouldThrow_whenUserNotFound() {

        Long id = 777L;
        when(userDao.getUserById(id)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.findUserById(id)
        );
        assertTrue(ex.getMessage().contains("не найден"), "Сообщение должно содержать 'не найден'");

        verify(userDao).getUserById(id);
        verifyNoMoreInteractions(userDao);
    }
}