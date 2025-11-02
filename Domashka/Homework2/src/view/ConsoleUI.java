package org.LosenkovIvan.view;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.LosenkovIvan.exception.UserNotFoundException;
import org.LosenkovIvan.model.User;
import org.LosenkovIvan.service.UserService;
import org.LosenkovIvan.util.HibernateUtil;
import org.LosenkovIvan.view.command.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ConsoleUI {
    private static final Logger logger = LogManager.getLogger(ConsoleUI.class);
    private final Scanner scanner;
    private final List<Command> commands;
    private final UserService userService;
    private boolean running;

    public ConsoleUI() {
        running = true;
        userService = new UserService();
        this.scanner = new Scanner(System.in);
        this.commands = new ArrayList<>();
        initializeCommands();
    }

    private void initializeCommands() {
        commands.add(new ExitCommand());
        commands.add(new CreateUserCommand(this));
        commands.add(new ReadUserCommand(this));
        commands.add(new ReadAllUsersCommand(this));
        commands.add(new UpdateUserCommand(this));
        commands.add(new DeleteUserCommand(this));
        commands.add(new FindByEmailCommand(this));
    }

    public void start() {
        while (running) {
            printMenu();
            String choice = scanner.nextLine();

            try {
                int choiceIndex = Integer.parseInt(choice);

                if (choiceIndex >= 0 && choiceIndex < commands.size()) {
                    Command selectedCommand = commands.get(choiceIndex);
                    selectedCommand.execute();

                    if (choiceIndex == 0) {
                        running = false;
                    }
                } else {
                    System.out.println("Неверный выбор. Попробуйте снова.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: введите число от 0 до " + (commands.size() - 1));
            }
            if (running && !choice.equals("3")) {
                System.out.println("\nНажмите Enter для продолжения...");
                scanner.nextLine();
            }
        }
        shutdown();
    }

    private void printMenu() {
        System.out.println("\n=== User Service Menu ===");

        for (int i = 1; i < commands.size(); i++) {
            Command command = commands.get(i);
            System.out.println(i + ". " + command.getDescription());
        }
        System.out.println("0. " + commands.get(0).getDescription());
        System.out.print("Выберите опцию: ");
    }

    private void shutdown() {
        HibernateUtil.shutdown();
        scanner.close();
    }

    public void createUser() {
        try {
            System.out.println("\n--- Создание пользователя ---");

            System.out.print("Введите имя: ");
            String name = scanner.nextLine();

            System.out.print("Введите email: ");
            String email = scanner.nextLine();

            System.out.print("Введите возраст: ");
            int age = Integer.parseInt(scanner.nextLine());

            User user = new User(name, email, age);
            boolean success = userService.createUser(user);

            if (success) {
                System.out.println("Пользователь создан успешно");
                logger.info("Пользователь создан успешно");
            } else {
                System.out.println("Не удалось создать пользователя");
                logger.error("Не удалось создать пользователя");
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: возраст должен быть числом");
            logger.error("Неверный формат возраста при создании пользователя");
        } catch (Exception e) {
            System.out.println("Ошибка при создании пользователя: " + e.getMessage());
            logger.error("Ошибка при создании пользователя", e);
        }
    }

    public void readUser() {
        try {
            System.out.println("\n--- Поиск пользователя по ID ---");
            System.out.print("Введите ID пользователя: ");
            Long id = Long.parseLong(scanner.nextLine());

            Optional<User> user = userService.findUserById(id);

            if (user.isPresent()) {
                System.out.printf("Найден пользователь: id=%s, name=%s%n", id, user.get().getName());
                logger.info("Найден пользователь: id={}, name={}", id, user.get().getName());
            } else {
                System.out.println("Пользователь с ID " + id + " не найден");
                logger.info("Пользователь с ID {} не найден", id);
            }

        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            logger.warn("Неверный формат ID при поиске пользователя: {}", e.getMessage());
        } catch (Exception e) {
            System.out.println("Ошибка при поиске пользователя: " + e.getMessage());
            logger.error("Ошибка при поиске пользователя по ID", e);
        }
    }

    public void readAllUsers() {
        try {
            System.out.println("\n--- Все пользователи ---");
            List<User> users = userService.findAllUsers();

            if (users.isEmpty()) {
                System.out.println("Пользователи не найдены");
                logger.info("Список пользователей пуст");
            } else {
                for (User user : users) {
                    System.out.printf("id=%s, name=%s, age=%s, email=%s%n", user.getId(), user.getName(), user.getAge(),user.getEmail());
                }
                System.out.println("Всего пользователей: " + users.size());
                logger.info("Выведено {} пользователей", users.size());
            }

        } catch (Exception e) {
            System.out.println("Ошибка при получении пользователей: " + e.getMessage());
            logger.error("Ошибка при получении всех пользователей", e);
        }
    }

    public void updateUser() {
        try {
            System.out.println("\n--- Обновление пользователя ---");
            System.out.print("Введите ID пользователя для обновления: ");
            Long id = Long.parseLong(scanner.nextLine());

            Optional<User> existingUser = userService.findUserById(id);

            if (existingUser.isEmpty()) {
                System.out.println("Пользователь с ID " + id + " не найден");
                return;
            }

            User user = existingUser.get();

            System.out.print("Введите новое имя (текущее: " + user.getName() + "): ");
            String name = scanner.nextLine();

            if (!name.trim().isEmpty()) {
                user.setName(name);
            }

            System.out.print("Введите новый email (текущий: " + user.getEmail() + "): ");
            String email = scanner.nextLine();

            if (!email.trim().isEmpty()) {
                user.setEmail(email);
            }

            System.out.print("Введите новый возраст (текущий: " + user.getAge() + "): ");
            String ageInput = scanner.nextLine();

            if (!ageInput.trim().isEmpty()) {
                user.setAge(Integer.parseInt(ageInput));
            }

            boolean success = userService.updateUser(user);

            if (success) {
                System.out.println("Пользователь обновлен");
                logger.info("Пользователь обновлен");
            } else {
                System.out.println("Не удалось обновить пользователя");
                logger.error("Не удалось обновить пользователя");
            }
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: возраст должен быть числом");
            logger.warn("Неверный формат возраста при обновлении пользователя");
        } catch (Exception e) {
            System.out.println("Ошибка при обновлении пользователя: " + e.getMessage());
            logger.error("Ошибка при обновлении пользователя", e);
        }
    }

    public void findByEmail() {
        try {
            System.out.println("\n--- Поиск пользователя по email ---");
            System.out.print("Введите email пользователя: ");
            String email = scanner.nextLine();

            if (email.isEmpty()) {
                System.out.println("Ошибка: email не может быть пустым");
                logger.warn("Попытка поиска по пустому email");

                return;
            }
            Optional<User> user = userService.findUserByEmail(email);

            if (user.isPresent()) {
                System.out.printf("Найден пользователь: id=%s, name=%s%n", user.get().getId(), user.get().getName());
                logger.info("Пользователь найден");
            } else {
                System.out.println("Пользователь с email " + email + " не найден");
                logger.info("Пользователь не найден");
            }

        } catch (Exception e) {
            System.out.println("Ошибка при поиске пользователя: " + e.getMessage());
            logger.error("Ошибка при поиске пользователя", e);
        }
    }

    public void deleteUser() {
        try {
            System.out.println("\n--- Удаление пользователя ---");
            System.out.print("Введите ID пользователя для удаления: ");
            Long id = Long.parseLong(scanner.nextLine());

            boolean success = userService.deleteUser(id);

            if (success) {
                System.out.println("Пользователь с ID " + id + " удален успешно");
                logger.info("Пользователь удален");
            } else {
                System.out.println("Не удалось удалить пользователя с ID " + id);
                logger.error("Не удалось удалить пользователя");
            }
        } catch (UserNotFoundException e) {
            System.out.println(e.getMessage());
            logger.warn("Попытка удаления несуществующего пользователя");
        } catch (NumberFormatException e) {
            System.out.println("Ошибка: ID должен быть числом");
            logger.warn("Неверный формат ID при удалении пользователя");
        } catch (Exception e) {
            System.out.println("Ошибка при удалении пользователя: " + e.getMessage());
            logger.error("Ошибка при удалении пользователя", e);
        }
    }
}