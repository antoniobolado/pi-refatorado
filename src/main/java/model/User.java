
package model;

import jakarta.persistence.*;

/**
 * Entidade Usuario baseada nos requisitos RF001 e RF002.
 * @author anton
 */
@Entity
@Table(name = "usuario")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nome; // Requisito RF001 

    @Column(nullable = false, unique = true)
    private String email; // Requisito RF001 

    @Column(nullable = false)
    private String senha; // Requisito RF001 

    public User() {
    }

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}