package model;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "registro_financeiro")
public class Register {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    
    private String tipo; // Entrada ou Saída
    private double valor;
    
    @Temporal(TemporalType.DATE)
    private Date data;
    
    private String categoria;
    private String descricao;

    // --- IMPLEMENTAÇÃO DO RELACIONAMENTO ---
    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private User usuario;

    // Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    // Getter e Setter para o Usuario
    public User getUsuario() {
        return usuario;
    }

    public void setUsuario(User usuario) {
        this.usuario = usuario;
    }
}