package model;

import java.time.LocalDateTime;

public class Movimento {
    private int id;
    private int idCaixa;
    private String tipo; // "ENTRADA", "SAÍDA", "CANCELAMENTO"
    private double valor;
    private String descricao;
    private LocalDateTime dataHora;

    // Campos auxiliares para exibição (não persistem no banco)
    private transient String cliente;
    private transient String pet;
    private transient String servico;

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getIdCaixa() { return idCaixa; }
    public void setIdCaixa(int idCaixa) { this.idCaixa = idCaixa; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }

    public String getPet() { return pet; }
    public void setPet(String pet) { this.pet = pet; }

    public String getServico() { return servico; }
    public void setServico(String servico) { this.servico = servico; }
}
