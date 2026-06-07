
package model;

import jakarta.persistence.*;
import java.util.Optional;

public class UserDAO {

    private static final EntityManagerFactory em =
            Persistence.createEntityManagerFactory("IllexPU");

    public Optional<User> findByEmail(String email) {
        EntityManager em = UserDAO.em.createEntityManager();
        try {
            TypedQuery<User> query = em.createQuery(
                    "SELECT u FROM Usuario u WHERE u.email = :email",
                    User.class
            );
            query.setParameter("email", email);
            return query.getResultStream().findFirst();
        } finally {
            em.close();
        }
    }

    public void create(User usuario) {
        EntityManager em = UserDAO.em.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(usuario);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }
}