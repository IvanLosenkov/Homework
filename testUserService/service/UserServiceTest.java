package org.LosenkovIvan.userService.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.LosenkovIvan.userService.dto.UserDto;
import org.LosenkovIvan.userService.entity.User;
import org.LosenkovIvan.userService.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;
    private UserDto testUserDto;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Иван Иванов");
        testUser.setEmail("ivan@example.com");
        testUser.setAge(30);
        testUser.setCreatedAt(LocalDateTime.now());

        testUserDto = new UserDto();
        testUserDto.setId(1L);
        testUserDto.setName("Иван Иванов");
        testUserDto.setEmail("ivan@example.com");
        testUserDto.setAge(30);
        testUserDto.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Создание пользователя - успешно")
    void create_shouldReturnUserDto_whenUserCreatedSuccessfully() {
        // Given
        UserDto inputDto = new UserDto();
        inputDto.setName("Петр Петров");
        inputDto.setEmail("petr@example.com");
        inputDto.setAge(25);

        User savedUser = new User();
        savedUser.setId(2L);
        savedUser.setName("Петр Петров");
        savedUser.setEmail("petr@example.com");
        savedUser.setAge(25);
        savedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDto result = userService.create(inputDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getName()).isEqualTo("Петр Петров");
        assertThat(result.getEmail()).isEqualTo("petr@example.com");
        assertThat(result.getAge()).isEqualTo(25);
        assertThat(result.getCreatedAt()).isNotNull();

        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Создание пользователя с установленным createdAt")
    void create_shouldPreserveCreatedAt_whenProvidedInDto() {
        // Given
        LocalDateTime customCreatedAt = LocalDateTime.of(2023, 1, 1, 12, 0);
        UserDto inputDto = new UserDto();
        inputDto.setName("Анна Сидорова");
        inputDto.setEmail("anna@example.com");
        inputDto.setAge(28);
        inputDto.setCreatedAt(customCreatedAt);

        User savedUser = new User();
        savedUser.setId(3L);
        savedUser.setName("Анна Сидорова");
        savedUser.setEmail("anna@example.com");
        savedUser.setAge(28);
        savedUser.setCreatedAt(customCreatedAt);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDto result = userService.create(inputDto);

        // Then
        assertThat(result.getCreatedAt()).isEqualTo(customCreatedAt);
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Получение пользователя по ID - успешно")
    void get_shouldReturnUserDto_whenUserExists() {
        // Given
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));

        // When
        UserDto result = userService.get(userId);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Иван Иванов");
        assertThat(result.getEmail()).isEqualTo("ivan@example.com");
        assertThat(result.getAge()).isEqualTo(30);

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Получение пользователя по ID - пользователь не найден")
    void get_shouldThrowException_whenUserNotFound() {
        // Given
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.get(userId))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(userId);
    }

    @Test
    @DisplayName("Получение списка всех пользователей")
    void list_shouldReturnListOfUserDtos_whenUsersExist() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        user1.setName("Пользователь 1");
        user1.setEmail("user1@example.com");
        user1.setAge(25);

        User user2 = new User();
        user2.setId(2L);
        user2.setName("Пользователь 2");
        user2.setEmail("user2@example.com");
        user2.setAge(30);

        List<User> users = List.of(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDto> result = userService.list();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getName()).isEqualTo("Пользователь 1");
        assertThat(result.get(1).getId()).isEqualTo(2L);
        assertThat(result.get(1).getName()).isEqualTo("Пользователь 2");

        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Получение списка пользователей - пустой список")
    void list_shouldReturnEmptyList_whenNoUsersExist() {
        // Given
        when(userRepository.findAll()).thenReturn(List.of());

        // When
        List<UserDto> result = userService.list();

        // Then
        assertThat(result).isEmpty();
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Обновление пользователя - успешный сценарий")
    void update_shouldReturnUpdatedUserDto_whenUserExists() {
        // Given
        Long userId = 1L;
        UserDto updateDto = new UserDto();
        updateDto.setName("Обновленное имя");
        updateDto.setEmail("new@example.com");
        updateDto.setAge(35);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Старое имя");
        existingUser.setEmail("old@example.com");
        existingUser.setAge(30);

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setName("Обновленное имя");
        updatedUser.setEmail("new@example.com");
        updatedUser.setAge(35);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        // When
        UserDto result = userService.update(userId, updateDto);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(userId);
        assertThat(result.getName()).isEqualTo("Обновленное имя");
        assertThat(result.getEmail()).isEqualTo("new@example.com");
        assertThat(result.getAge()).isEqualTo(35);

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Обновление пользователя - частичное обновление полей")
    void update_shouldUpdateOnlyProvidedFields_whenSomeFieldsAreNull() {
        // Given
        Long userId = 1L;
        UserDto updateDto = new UserDto();
        updateDto.setName("Только имя");

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Старое имя");
        existingUser.setEmail("old@example.com");
        existingUser.setAge(30);

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // When
        UserDto result = userService.update(userId, updateDto);

        // Then
        assertThat(result.getName()).isEqualTo("Только имя");
        assertThat(result.getEmail()).isEqualTo("old@example.com");
        assertThat(result.getAge()).isEqualTo(30);

        verify(userRepository).findById(userId);
        verify(userRepository).save(existingUser);
    }

    @Test
    @DisplayName("Обновление пользователя - пользователь не найден")
    void update_shouldThrowException_whenUserNotFound() {
        // Given
        Long userId = 999L;
        UserDto updateDto = new UserDto();
        updateDto.setName("Новое имя");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> userService.update(userId, updateDto))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("User not found");

        verify(userRepository).findById(userId);
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Удаление пользователя")
    void delete_shouldCallRepositoryDelete_whenValidId() {
        // Given
        Long userId = 1L;

        // When
        userService.delete(userId);

        // Then
        verify(userRepository).deleteById(userId);
    }

    @Test
    @DisplayName("Удаление пользователя с null ID")
    void delete_shouldCallRepositoryDelete_whenIdIsNull() {
        // Given
        Long userId = null;

        // When
        userService.delete(userId);

        // Then
        verify(userRepository).deleteById(null);
    }

    @Test
    @DisplayName("Создание пользователя - проверка автоматической установки createdAt")
    void create_shouldSetCreatedAt_whenNotProvided() {
        // Given
        UserDto inputDto = new UserDto();
        inputDto.setName("Тестовый пользователь");
        inputDto.setEmail("test@example.com");
        inputDto.setAge(25);
        // createdAt не установлен

        User savedUser = new User();
        savedUser.setId(4L);
        savedUser.setName("Тестовый пользователь");
        savedUser.setEmail("test@example.com");
        savedUser.setAge(25);
        savedUser.setCreatedAt(LocalDateTime.now());

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // When
        UserDto result = userService.create(inputDto);

        // Then
        assertThat(result.getCreatedAt()).isNotNull();
        verify(userRepository).save(argThat(user -> user.getCreatedAt() != null));
    }

    @Test
    @DisplayName("Обновление пользователя - проверка неизменности createdAt")
    void update_shouldNotModifyCreatedAt_whenUpdatingUser() {
        // Given
        Long userId = 1L;
        LocalDateTime originalCreatedAt = LocalDateTime.of(2023, 1, 1, 10, 0);

        User existingUser = new User();
        existingUser.setId(userId);
        existingUser.setName("Существующий пользователь");
        existingUser.setEmail("existing@example.com");
        existingUser.setAge(25);
        existingUser.setCreatedAt(originalCreatedAt);

        UserDto updateDto = new UserDto();
        updateDto.setName("Обновленное имя");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        // When
        UserDto result = userService.update(userId, updateDto);

        // Then
        assertThat(result.getCreatedAt()).isEqualTo(originalCreatedAt);
        verify(userRepository).save(argThat(user ->
                user.getCreatedAt().equals(originalCreatedAt)));
    }
}