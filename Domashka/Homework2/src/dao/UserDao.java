package org.LosenkovIvan.dao;

import org.LosenkovIvan.model.User;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    boolean save(User user);
    boolean update(User user);
    boolean delete(Long id);
    boolean existsById(Long id);

    Optional<User> findById(Long id);
    List<User> findAll();
    Optional<User> findByEmail(String email);
}