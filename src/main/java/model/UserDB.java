
package model;

import java.sql.*;
import persistence.JPAUtil;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class UserDB {
    public static User validarUsuarioSeguro(User usuarioDigitado) {
    EntityManager em = JPAUtil.getEntityManager(); // Usando seu JPAUtil
    try {
        // 1. Busca o usuário apenas pelo e-mail
        TypedQuery<User> query = em.createQuery(
            "SELECT u FROM Usuario u WHERE u.email = :email", User.class);
        query.setParameter("email", usuarioDigitado.getEmail());
        
        User usuarioBanco = query.getSingleResult();

        // 2. Compara a senha digitada com o Hash BCrypt do banco
        // O checkpw faz a mágica de comparar texto puro com o hash
        if (org.mindrot.jbcrypt.BCrypt.checkpw(usuarioDigitado.getSenha(), usuarioBanco.getSenha())) {
            return usuarioBanco; // Senha correta
        }
    } catch (Exception e) {
        System.out.println("Usuário não encontrado ou erro: " + e.getMessage());
    } finally {
        em.close();
    }
    return null; // Login falhou
}
}