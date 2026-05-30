package dao;

import confiq.JPAUtil;
import models.User;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;
import javafx.scene.control.Alert;

public class UserDAO {

    public User login(String username, String password) {
        EntityManager em = JPAUtil.getEntityManager();
        try {
            return em.createQuery( // login using username and password
                    "SELECT u FROM User u WHERE u.userName = :username AND u.password = :password",
                    User.class)
                    .setParameter("username", username)
                    .setParameter("password", password)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    // return list of users
    public List<User> getAllUsers() {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            return em.createQuery("SELECT u FROM User u", User.class).getResultList();
        } finally {
            em.close();
        }
    }

    //adding new user
    public boolean addUser(User user) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }

    }

//    update user
    public boolean updateUser(User user) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            em.getTransaction().begin();
            em.merge(user);
            em.getTransaction().commit();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

//    Delete User
public boolean deleteUser(int userId) {
    EntityManager em = null;
    try {
        em = JPAUtil.getEntityManager();
        em.getTransaction().begin();

        User user = em.find(User.class, userId);
        if (user == null) return false;

        em.createQuery(
            "DELETE FROM AidDistribution a WHERE a.distributedBy.userId = :uid")
            .setParameter("uid", userId)
            .executeUpdate();
        em.remove(user);
        em.getTransaction().commit();
        return true;

    } catch (Exception e) {
        e.printStackTrace();
        return false;
    } finally {
        em.close();
    }
}

//is the user exist
    public boolean usernameExist(String username) {
        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            Long count = em.createQuery(
                    "SELECT COUNT(u) FROM User u WHERE u.userName = :username", Long.class)
                    .setParameter("username", username)
                    .getSingleResult();
            return count > 0;

        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }
    }

//email exist
    public boolean emailExists(String email) {

        EntityManager em = null;
        try {
            em = JPAUtil.getEntityManager();
            Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.email = :email", Long.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return count > 0;
        } catch (Exception e) {
            return false;
        } finally {
            em.close();
        }

    }

}
