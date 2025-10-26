package org.LosenkovIvan.dao;

import org.LosenkovIvan.model.User;
import org.LosenkovIvan.exception.UserNotFoundException;
import org.LosenkovIvan.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UserDaoImpl implements UserDao {
    @Override
    public boolean save(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(user);
            transaction.commit();

            return true;
        }

        catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
                throw new RuntimeException("Ошибка при сохранении пользователя", e);
            }
        }
    }

    @Override
    public Optional<User> findById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            User user = session.find(User.class, id);

            if (user != null && user.getId() != null) {
                return Optional.of(user);
            }

            return Optional.empty();

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске пользователя", e);
        }
    }

    @Override
    public List<User> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User", User.class);
            List<User> users = query.getResultList();

            return users;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при получении пользователей", e);
        }
    }

    @Override
    public boolean update(User user) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(user);
            transaction.commit();

            return true;

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }

    @Override
    public boolean delete(Long id) {
        Transaction transaction = null;

        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            User user = session.getReference(User.class, id);

            if (user != null && user.getId() != null) {
                session.remove(user);
            } else {
                throw new UserNotFoundException(id);
            }
            transaction.commit();

            return true;

        } catch (UserNotFoundException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new RuntimeException("Ошибка при удалении пользователя", e);
        }
    }

    @Override
    public Optional<User> findByEmail(String email) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query.setParameter("email", email);
            User user = query.uniqueResult();

            return Optional.ofNullable(user);

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при поиске пользователя", e);
        }
    }

    @Override
    public boolean existsById(Long id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Long> query = session.createQuery("select count(*) from User where id = :id", Long.class);
            query.setParameter("id", id);
            Long count = query.uniqueResult();

            return count != null && count > 0;

        } catch (Exception e) {
            throw new RuntimeException("Ошибка при проверке пользователя", e);
        }
    }
}