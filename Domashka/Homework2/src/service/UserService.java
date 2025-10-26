package org.LosenkovIvan.service;

import org.LosenkovIvan.dao.UserDao;
import org.LosenkovIvan.dao.UserDaoImpl;
import org.LosenkovIvan.model.User;
import org.LosenkovIvan.view.command.*;

import java.util.List;
import java.util.Optional;

public class UserService {
    private final UserDao userDao;

    public UserService() {
        this.userDao = new UserDaoImpl();
    }

    public boolean createUser(User user) {
        return userDao.save(user);
    }

    public Optional<User> findUserById(Long id) {
        return userDao.findById(id);
    }

    public List<User> findAllUsers() {
        return userDao.findAll();
    }

    public boolean updateUser(User user) {
        return userDao.update(user);
    }

    public boolean deleteUser(Long id) {
        return userDao.delete(id);
    }

    public Optional<User> findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }
}