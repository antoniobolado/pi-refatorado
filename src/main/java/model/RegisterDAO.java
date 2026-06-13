package model;

import jakarta.persistence.*;
import java.util.List;

public class RegisterDAO {

    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("IllexPU");

    public void create(Register registro) {
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(registro);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public List<Register> listarPorUsuario(int usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            TypedQuery<Register> query = em.createQuery(
                "SELECT r FROM Register r WHERE r.usuario.id = :uid ORDER BY r.data DESC",
                Register.class
            );
            query.setParameter("uid", usuarioId);
            return query.getResultList();
        } finally {
            em.close();
        }
    }
    
public void delete(int id) {
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    try {
        tx.begin();
        Register r = em.find(Register.class, id);
        if (r != null) {
            em.remove(r);
        }
        tx.commit();
    } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw e;
    } finally {
        em.close();
    }
}

public void update(Register registro) {

    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();

    try {
        tx.begin();
        em.merge(registro);
        tx.commit();
    } catch (Exception e) {
        if (tx.isActive()) tx.rollback();
        throw e;
    } finally {
        em.close();
    }
}

}