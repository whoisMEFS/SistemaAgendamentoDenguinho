package model;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;

public class Agendamento {
    private int id;
    private String cliente;
    private String nomePet;
    private String raca;
    private Timestamp dataHora;
    private String servico;
    private int duracao;
    private String profissional;
    private double valor;

    // Construtor com ID
    public Agendamento(int id, String cliente, String nomePet, String raca, Timestamp dataHora, String servico, int duracao, String profissional, double valor) {
        this.id = id;
        this.cliente = cliente;
        this.nomePet = nomePet;
        this.raca = raca;
        this.dataHora = dataHora;
        this.servico = servico;
        this.duracao = duracao;
        this.profissional = profissional;
        this.valor = valor;
    }

    // Construtor sem ID
    public Agendamento(String cliente, String nomePet, String raca, Timestamp dataHora, String servico, int duracao, String profissional, double valor) {
        this(-1, cliente, nomePet, raca, dataHora, servico, duracao, profissional, valor);
    }

    public Agendamento() {
    // Construtor padrão vazio
}


    // Getters
    public int getId() { return id; }
    public String getCliente() { return cliente; }
    public String getNomePet() { return nomePet; }
    public String getRaca() { return raca; }
    public Timestamp getDataHora() { return dataHora; }
    public String getServico() { return servico; }
    public int getDuracao() { return duracao; }
    public String getProfissional() { return profissional; }
    public double getValor() { return valor; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public void setNomePet(String nomePet) { this.nomePet = nomePet; }
    public void setRaca(String raca) { this.raca = raca; }
    public void setDataHora(Timestamp dataHora) { this.dataHora = dataHora; }
    public void setServico(String servico) { this.servico = servico; }
    public void setDuracao(int duracao) { this.duracao = duracao; }
    public void setProfissional(String profissional) { this.profissional = profissional; }
    public void setValor(double valor) { this.valor = valor; }

    // Métodos auxiliares para facilitar filtragens por data e hora
    public LocalDate getData() {
        return dataHora.toLocalDateTime().toLocalDate();
    }

    public LocalTime getHora() {
        return dataHora.toLocalDateTime().toLocalTime();
    }

    public void setHora(LocalTime horaAtual) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setData(LocalDate dataAtual) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setPet(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private boolean baixado;

public void setBaixado(boolean baixado) {
    this.baixado = baixado;
}

public boolean isBaixado() {
    return baixado;
}

}
