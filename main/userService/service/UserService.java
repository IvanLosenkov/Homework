package org.LosenkovIvan.user_service.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.LosenkovIvan.userService.dto.UserDto;
import org.LosenkovIvan.userService.entity.User;
import org.LosenkovIvan.userService.mapper.UserMapper;
import org.LosenkovIvan.userService.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public UserDto create(UserDto dto) {
        log.debug("Создание нового пользователя: email={}", dto.getEmail());
        try {
            User user = UserMapper.toEntity(dto);
            if (user.getCreatedAt() == null) {
                user.setCreatedAt(LocalDateTime.now());
            }

            User saved = userRepository.save(user);
            log.info("Пользователь успешно создан: id={}, email={}", saved.getId(), saved.getEmail());

            return UserMapper.toDto(saved);

        } catch (Exception e) {
            log.error("Ошибка при создании пользователя: email={}", dto.getEmail(), e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public UserDto get(Long id) {
        log.debug("Получение пользователя по ID: {}", id);
        try {
            User user = userRepository.findById(id).orElseThrow(() -> {
                log.warn("Пользователь не найден: id={}", id);
                return new NoSuchElementException("User not found");
            });

            log.debug("Пользователь найден: id={}, email={}", user.getId(), user.getEmail());

            return UserMapper.toDto(user);

        } catch (NoSuchElementException e) {
            throw e;

        } catch (Exception e) {
            log.error("Ошибка при получении пользователя: id={}", id, e);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<UserDto> list() {
        log.debug("Получение списка всех пользователей");
        try {
            List<UserDto> users = userRepository.findAll().stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());

            log.info("Получен список пользователей: количество={}", users.size());

            return users;

        } catch (Exception e) {
            log.error("Ошибка при получении списка пользователей", e);
            throw e;
        }
    }

    public UserDto update(Long id, UserDto dto) {
        log.debug("Обновление пользователя: id={}", id);
        try {
            User user = userRepository.findById(id).orElseThrow(() -> {
                log.warn("Пользователь не найден для обновления: id={}", id);

                return new NoSuchElementException("User not found");
            });

            if (dto.getName() != null && !dto.getName().equals(user.getName())) {
                log.debug("Изменение имени: {} -> {}", user.getName(), dto.getName());
                user.setName(dto.getName());
            }

            if (dto.getEmail() != null && !dto.getEmail().equals(user.getEmail())) {
                log.debug("Изменение email: {} -> {}", user.getEmail(), dto.getEmail());
                user.setEmail(dto.getEmail());
            }

            if (dto.getAge() != null && !dto.getAge().equals(user.getAge())) {
                log.debug("Изменение возраста: {} -> {}", user.getAge(), dto.getAge());
                user.setAge(dto.getAge());
            }

            User saved = userRepository.save(user);
            log.info("Пользователь успешно обновлен: id={}, email={}", saved.getId(), saved.getEmail());

            return UserMapper.toDto(saved);

        } catch (NoSuchElementException e) {
            throw e;

        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя: id={}", id, e);
            throw e;
        }
    }

    public void delete(Long id) {
        log.debug("Удаление пользователя: id={}", id);
        try {
            if (!userRepository.existsById(id)) {
                log.warn("Попытка удалить несуществующего пользователя: id={}", id);
                throw new NoSuchElementException("User not found");
            }

            userRepository.deleteById(id);
            log.info("Пользователь успешно удален: id={}", id);
        } catch (NoSuchElementException e) {
            throw e;

        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя: id={}", id, e);
            throw e;
        }
    }
}