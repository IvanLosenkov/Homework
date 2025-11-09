package org.LosenkovIvan.userService.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.LosenkovIvan.user_service.dto.UserDto;
import org.LosenkovIvan.user_service.service.UserService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> create(@Valid @RequestBody UserDto dto) {
        log.info("POST /api/users - создание пользователя: email={}", dto.getEmail());
        try {
            UserDto created = userService.create(dto);
            log.info("Пользователь создан успешно: id={}, email={}", created.getId(), created.getEmail());

            return ResponseEntity.status(HttpStatus.CREATED).body(created);

        } catch (Exception e) {
            log.error("Ошибка при создании пользователя через API: email={}", dto.getEmail(), e);
            throw e;
        }
    }

    @GetMapping("/{id}")
    public UserDto get(@PathVariable Long id) {
        log.info("GET /api/users/{} - получение пользователя", id);
        try {
            UserDto user = userService.get(id);
            log.debug("Пользователь получен: id={}, email={}", user.getId(), user.getEmail());

            return user;

        } catch (Exception e) {
            log.error("Ошибка при получении пользователя через API: id={}", id, e);
            throw e;
        }
    }

    @GetMapping
    public List<UserDto> list() {
        log.info("GET /api/users - получение списка пользователей");
        try {
            List<UserDto> users = userService.list();
            log.debug("Получен список пользователей: количество={}", users.size());

            return users;

        } catch (Exception e) {
            log.error("Ошибка при получении списка пользователей через API", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public UserDto update(@PathVariable Long id, @Valid @RequestBody UserDto dto) {
        log.info("PUT /api/users/{} - обновление пользователя", id);
        try {
            UserDto updated = userService.update(id, dto);
            log.info("Пользователь обновлен успешно: id={}, email={}", updated.getId(), updated.getEmail());

            return updated;

        } catch (Exception e) {
            log.error("Ошибка при обновлении пользователя через API: id={}", id, e);
            throw e;
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        log.info("DELETE /api/users/{} - удаление пользователя", id);
        try {
            userService.delete(id);
            log.info("Пользователь удален успешно: id={}", id);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            log.error("Ошибка при удалении пользователя через API: id={}", id, e);
            throw e;
        }
    }
}