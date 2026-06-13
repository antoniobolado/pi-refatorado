
package model;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

/**
 *
 * @author anton
 */
public class RegisterService {
    
    private static final EntityManagerFactory emf =
            Persistence.createEntityManagerFactory("IllexPU");
    
    public double calcularSaldoPorUsuario(int usuarioId) {
        EntityManager em = emf.createEntityManager();
        try {
            Double total = em.createQuery(
                "SELECT SUM(r.valor) FROM Register r WHERE r.usuario.id = :uid",
                Double.class
            )
            .setParameter("uid", usuarioId)
            .getSingleResult();

            return total != null ? total : 0.0;
        } finally {
            em.close();
        }
    }
}
