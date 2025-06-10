package model;

import java.time.LocalDateTime;

public class Caixa {
    private int id;
    private LocalDateTime dataHoraAbertura;
    private LocalDateTime dataHoraFechamento;
    private double saldoInicial;
    private double saldoFinal;
    private boolean aberto;

    // Getters e setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public LocalDateTime getDataHoraAbertura() { return dataHoraAbertura; }
    public void setDataHoraAbertura(LocalDateTime dataHoraAbertura) { this.dataHoraAbertura = dataHoraAbertura; }

    public LocalDateTime getDataHoraFechamento() { return dataHoraFechamento; }
    public void setDataHoraFechamento(LocalDateTime dataHoraFechamento) { this.dataHoraFechamento = dataHoraFechamento; }

    public double getSaldoInicial() { return saldoInicial; }
    public void setSaldoInicial(double saldoInicial) { this.saldoInicial = saldoInicial; }

    public double getSaldoFinal() { return saldoFinal; }
    public void setSaldoFinal(double saldoFinal) { this.saldoFinal = saldoFinal; }

    public boolean isAberto() { return aberto; }
    public void setAberto(boolean aberto) { this.aberto = aberto; }
}
