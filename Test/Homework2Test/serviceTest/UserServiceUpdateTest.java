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
class UserServiceUpdateTest {

    @Mock
    private UserDAO userDao;

    @InjectMocks
    private UserService userService;


    @Test
    @DisplayName("updateUserEmail: успешно обновляет email, если новый email валиден и свободен")
    void updateUserEmail_shouldUpdate_whenValidAndUnique() {

        Long userId = 1L;
        String oldEmail = "old@mail.com";
        String newEmail = "new@mail.com";

        User user = new User("Денис", oldEmail, 27);
        user.setId(userId);

        when(userDao.getUserById(userId)).thenReturn(Optional.of(user));
        when(userDao.getUserByEmail(newEmail)).thenReturn(Optional.empty());
        when(userDao.updateUser(any(User.class))).thenReturn(true);

        userService.updateUserEmail(userId, newEmail);

        assertEquals(newEmail, user.getEmail(), "Email должен быть обновлён");
        verify(userDao).getUserById(userId);
        verify(userDao).getUserByEmail(newEmail);
        verify(userDao).updateUser(user);
        verifyNoMoreInteractions(useruserDao);
    }

    @Test
    @DisplayName("updateUserEmail: бросает IllegalArgumentException, если email пустой")
    void updateUserEmail_shouldThrow_whenEmailEmpty() {

        Long userId = 1L;
        String newEmail = " ";

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserEmail(userId, newEmail)
        );

        assertTrue(ex.getMessage().contains("пуст"), "Ожидаем сообщение о пустом email");
        verifyNoInteractions(userDao);
    }

    @Test
    @DisplayName("updateUserEmail: бросает IllegalArgumentException, если email некорректного формата")
    void updateUserEmail_shouldThrow_whenEmailInvalid() {

        Long userId = 1L;
        String newEmail = "supercomcomcom";

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserEmail(userId, newEmail)
        );

        assertTrue(ex.getMessage().contains("Некорректный"), "Ожидаем сообщение о некорректном формате");
        verifyNoInteractions(userDao);
    }

    @Test
    @DisplayName("updateUserEmail: бросает IllegalArgumentException, если новый email занят другим пользователем")
    void updateUserEmail_shouldThrow_whenEmailAlreadyTaken() {

        Long userId = 1L;
        String oldEmail = "old@mail.com";
        String newEmail = "used@mail.com";

        User currentUser = new User("Денис", oldEmail, 27);
        currentUser.setId(userId);

        User anotherUser = new User("Михаил", newEmail, 57);
        anotherUser.setId(2L);

        when(userDao.getUserById(userId)).thenReturn(Optional.of(currentUser));
        when(userDao.getUserByEmail(newEmail)).thenReturn(Optional.of(anotherUser));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.updateUserEmail(userId, newEmail)
        );

        assertTrue(ex.getMessage().contains("занят"), "Ожидаем сообщение о занятом email");

        verify(userDao).getUserById(userId);
        verify(userDao).getUserByEmail(newEmail);
        verifyNoMoreInteractions(userDao);
    }

    @Test
    @DisplayName("updateUserEmail: бросает RuntimeException, если не удалось обновить пользователя")
    void updateUserEmail_shouldThrow_whenUpdateFails() {

        Long userId = 1L;
        String oldEmail = "old@mail.com";
        String newEmail = "new@mail.com";

        User user = new User("Денис", oldEmail, 27);
        user.setId(userId);

        when(userDao.getUserById(userId)).thenReturn(Optional.of(user));
        when(userDao.getUserByEmail(newEmail)).thenReturn(Optional.empty());
        when(userDao.updateUser(any(User.class))).thenReturn(false); // имитация сбоя при обновлении

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> userService.updateUserEmail(userId, newEmail)
        );

        assertTrue(ex.getMessage().contains("Не удалось"), "Ожидаем сообщение о неудачном обновлении");

        verify(userDao).getUserById(userId);
        verify(userDao).getUserByEmail(newEmail);
        verify(userDao).updateUser(user);
        verifyNoMoreInteractions(userDao);
    }
}