package Homework2Test.daoTest;

import dao.UserDao;
import model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {

    @Container
    private static final PostgreSQLContainer<?> postgres =
            new PostgreSQLContainer<>("postgres:16-alpine")
                    .withDatabaseName("test_db")
                    .withUsername("test_user")
                    .withPassword("test_pass");

    private static UserDAO userDAO;

    @BeforeAll
    static void setUp() {

        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "org.postgresql.Driver");
        configuration.setProperty("hibernate.connection.url", postgres.getJdbcUrl());
        configuration.setProperty("hibernate.connection.username", postgres.getUsername());
        configuration.setProperty("hibernate.connection.password", postgres.getPassword());
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        configuration.setProperty("hibernate.show_sql", "true");
        configuration.addAnnotatedClass(User.class);

        SessionFactory sessionFactory = configuration.buildSessionFactory();

        userDAO = new UserDAO();
        userDAO.setSessionFactory(sessionFactory);
    }


    /**
    Test for saveUser()
     */

    @Test
    @Order(1)
    @DisplayName("saveUser: сохраняет пользователя в БД")
    void saveUser_shouldPersistUser() {
        User user = new User("Денис", "denis@test.com", 27);
        Long id = userDAO.saveUser(user);

        assertNotNull(id, "ID должен быть не null после сохранения");
        Optional<User> found = userDAO.getUserById(id);
        assertTrue(found.isPresent(), "Пользователь должен быть найден в базе");
        assertEquals("Денис", found.get().getName());
    }


    /**
    Test for getUserById()
     */

    @Test
    @Order(2)
    @DisplayName("getUserById: возвращает сохранённого пользователя")
    void getUserById_shouldReturnUser() {
        User user = new User("Михаил", "mihail@test.com", 57);
        Long id = userDAO.saveUser(user);

        Optional<User> found = userDAO.getUserById(id);
        assertTrue(found.isPresent());
        assertEquals("Михаил", found.get().getName());
    }


    /**
    Test for getAllUsers()
     */

    @Test
    @Order(3)
    @DisplayName("getAllUsers: возвращает список всех пользователей")
    void getAllUsers_shouldReturnAll() {
        List<User> users = userDAO.getAllUsers();
        assertFalse(users.isEmpty(), "Список пользователей не должен быть пустым");
        assertTrue(users.size() >= 2, "Ожидаем хотя бы 2 пользователя");
    }


    /**
    Test for updateUser()
     */

    @Test
    @Order(4)
    @DisplayName("updateUser: обновляет данные пользователя")
    void updateUser_shouldUpdateFields() {
        User user = new User("Вера", "vera@test.com", 44);
        Long id = userDAO.saveUser(user);
        user.setId(id);

        user.setName("Вероника");
        boolean updated = userDAO.updateUser(user);

        assertTrue(updated, "updateUser должен вернуть true");
        Optional<User> updatedUser = userDAO.getUserById(id);
        assertTrue(updatedUser.isPresent());
        assertEquals("Вероника", updatedUser.get().getName());
    }


    /**
    Test for deleteUser()
     */

    @Test
    @Order(5)
    @DisplayName("deleteUser: удаляет пользователя по ID")
    void deleteUser_shouldRemoveUser() {
        User user = new User("Антон", "anton@test.com", 25);
        Long id = userDAO.saveUser(user);

        boolean deleted = userDAO.deleteUser(id);
        assertTrue(deleted, "Метод должен вернуть true");

        Optional<User> found = userDAO.getUserById(id);
        assertFalse(found.isPresent(), "Пользователь должен быть удалён");
    }


    /**
    Test for isEmailExists()
    */

    @Test
    @Order(6)
    @DisplayName("isEmailExists: возвращает true, если email уже есть в БД")
    void isEmailExists_shouldReturnTrue_whenEmailExists() {

        User user = new User("Василий", "vasiliy@test.com", 29);
        userDAO.saveUser(user);

        boolean exists = userDAO.isEmailExists("vasiliy@test.com");

        assertTrue(exists, "Метод должен вернуть true для существующего email");
    }

    @Test
    @Order(7)
    @DisplayName("isEmailExists: возвращает false, если email не найден")
    void isEmailExists_shouldReturnFalse_whenEmailNotExists() {

        boolean exists = userDAO.isEmailExists("unknown@test.com");

        assertFalse(exists, "Метод должен вернуть false для несуществующего email");
    }


    /**
    Test for findUsersByName()
    */

    @Test
    @Order(8)
    @DisplayName("findUsersByName: возвращает пользователей по частичному совпадению имени")
    void findUsersByName_shouldReturnMatchingUsers() {

        userDAO.saveUser(new User("Мария", "mariya@test.com", 19));
        userDAO.saveUser(new User("Марина", "marina@test.com", 18));
        userDAO.saveUser(new User("Рушад", "rushad@test.com", 62));

        List<User> result = userDAO.findUsersByName("Мари");

        assertFalse(result.isEmpty(), "Список не должен быть пустым");
        assertTrue(result.stream().allMatch(u -> u.getName().contains("Мари")),
                "Все найденные пользователи должны содержать 'Мари' в имени");
    }

    @Test
    @Order(9)
    @DisplayName("findUsersByName: возвращает пустой список, если совпадений нет")
    void findUsersByName_shouldReturnEmpty_whenNoMatches() {

        List<User> result = userDAO.findUsersByName("НесуществующееИмя");

        assertNotNull(result, "Метод должен возвращать непустой список (возможно, пустой)");
        assertTrue(result.isEmpty(), "Если совпадений нет — возвращаем пустой список");
    }
}