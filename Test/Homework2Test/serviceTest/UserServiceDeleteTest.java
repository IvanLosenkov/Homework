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
class UserServiceDeleteTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("deleteUser: успешно удаляет пользователя, если он существует")
    void deleteUser_shouldReturnTrue_whenUserExistsAndDeleted() {

        Long userId = 1L;
        User user = new User("Денис", "denis@test.com", 27);
        user.setId(userId);

        when(userDao.getUserById(userId)).thenReturn(Optional.of(user));
        when(userDao.deleteUser(userId)).thenReturn(true);

        boolean result = userService.deleteUser(userId);

        assertTrue(result, "Метод должен вернуть true, если удаление прошло успешно");

        verify(userDao).getUserById(userId);
        verify(userDao).deleteUser(userId);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @DisplayName("deleteUser: бросает RuntimeException, если не удалось удалить пользователя")
    void deleteUser_shouldThrow_whenDaoDeleteFails() {

        Long userId = 1L;
        User user = new User("Михаил", "mihail@test.com", 57);
        user.setId(userId);

        when(userDao.getUserById(userId)).thenReturn(Optional.of(user));
        when(userDao.deleteUser(userId)).thenReturn(false);

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(userId)
        );

        assertTrue(ex.getMessage().contains("Не удалось"), "Сообщение должно говорить о неудачном удалении");

        verify(userDao).getUserById(userId);
        verify(userDao).deleteUser(userId);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @DisplayName("deleteUser: бросает RuntimeException, если пользователь не найден")
    void deleteUser_shouldThrow_whenUserNotFound() {

        Long userId = 999L;

        when(userDao.getUserById(userId)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.deleteUser(userId)
        );

        assertTrue(ex.getMessage().contains("не найден"), "Сообщение должно указывать, что пользователь не найден");

        verify(userDao).getUserById(userId);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @DisplayName("deleteUser: бросает IllegalArgumentException, если ID некорректный")
    void deleteUser_shouldThrow_whenIdInvalid() {

        Long userId = 0L;

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.deleteUser(userId)
        );

        assertTrue(ex.getMessage().contains("ID"), "Сообщение должно содержать слово ID");
        verifyNoInteractions(userDao);
    }
}