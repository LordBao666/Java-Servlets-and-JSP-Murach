package com.lordbao.ch13ex1email.data;

import com.lordbao.ch13ex1email.business.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

/**
 * @Author Lord_Bao
 * @Date 2024/6/6 17:05
 * @Version 1.0
 */
public class UserDB {
    public static void insert(User user) {

        EntityManager manager = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction transaction = manager.getTransaction();
        transaction.begin();
        try {
            manager.persist(user);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            transaction.commit();
        } finally {
            manager.close();
        }

    }

    public static void update(User user) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.merge(user);
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public static void delete(User user) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        EntityTransaction trans = em.getTransaction();
        trans.begin();
        try {
            em.remove(em.merge(user));
            trans.commit();
        } catch (Exception e) {
            e.printStackTrace();
            trans.rollback();
        } finally {
            em.close();
        }
    }

    public static User selectUser(String email) {
        EntityManager em = DBUtil.getEmFactory().createEntityManager();
        String qString = "SELECT u FROM User u " +
                "WHERE u.email = :email";

        TypedQuery<User> q = em.createQuery(qString, User.class);
        q.setParameter("email", email);
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        } finally {
            em.close();
        }
    }

    public static boolean emailExists(String email) {
        User u = selectUser(email);
        return u != null;
    }

}
