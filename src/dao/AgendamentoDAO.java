package dao;

import model.Agendamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Agendamento;


public class AgendamentoDAO {
    private static final String URL = "jdbc:sqlite:banco/banco.db";


    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void salvar(Agendamento ag) {
        String sql = "INSERT INTO agendamentos (cliente, nomePet, raca, dataHora, servico, duracao, profissional, valor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ag.getCliente());
            stmt.setString(2, ag.getNomePet());
            stmt.setString(3, ag.getRaca());
            stmt.setTimestamp(4, ag.getDataHora());
            stmt.setString(5, ag.getServico());
            stmt.setInt(6, ag.getDuracao());
            stmt.setString(7, ag.getProfissional());
            stmt.setDouble(8, ag.getValor());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void atualizar(Agendamento ag) {
        String sql = "UPDATE agendamentos SET cliente=?, nomePet=?, raca=?, dataHora=?, servico=?, duracao=?, profissional=?, valor=? WHERE id=?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, ag.getCliente());
            stmt.setString(2, ag.getNomePet());
            stmt.setString(3, ag.getRaca());
            stmt.setTimestamp(4, ag.getDataHora());
            stmt.setString(5, ag.getServico());
            stmt.setInt(6, ag.getDuracao());
            stmt.setString(7, ag.getProfissional());
            stmt.setDouble(8, ag.getValor());
            stmt.setInt(9, ag.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM agendamentos WHERE id=?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<Agendamento> listar() {
        List<Agendamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM agendamentos ORDER BY dataHora";
        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Agendamento ag = new Agendamento(
                    rs.getInt("id"),
                    rs.getString("cliente"),
                    rs.getString("nomePet"),
                    rs.getString("raca"),
                    new Timestamp(rs.getLong("dataHora")),
                    rs.getString("servico"),
                    rs.getInt("duracao"),
                    rs.getString("profissional"),
                    rs.getDouble("valor")
                        
                );
                lista.add(ag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public Agendamento buscarPorId(int id) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void baixarAgendamento(int idAgendamento) throws SQLException {
    String sql = "UPDATE agendamentos SET baixado = 1 WHERE id = ?";
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:banco/banco.db");
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, idAgendamento);
        stmt.executeUpdate();
    }
}


   public List<Agendamento> listarAgendamentosPendentes() {
    List<Agendamento> lista = new ArrayList<>();
    String sql = "SELECT * FROM agendamentos WHERE baixado = 0 ORDER BY dataHora";
    try (Connection conn = conectar();
         PreparedStatement stmt = conn.prepareStatement(sql);
         ResultSet rs = stmt.executeQuery()) {
        while (rs.next()) {
            Agendamento ag = new Agendamento();
            ag.setId(rs.getInt("id"));
            ag.setCliente(rs.getString("cliente"));
            ag.setNomePet(rs.getString("nomePet")); 
            ag.setRaca(rs.getString("raca"));
            ag.setDataHora(new Timestamp(rs.getLong("dataHora")));
            ag.setServico(rs.getString("servico"));
            ag.setDuracao(rs.getInt("duracao"));
            ag.setProfissional(rs.getString("profissional"));
            ag.setValor(rs.getDouble("valor"));
            ag.setBaixado(rs.getInt("baixado") == 1);
            lista.add(ag);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}

    public int contarBanhosHoje() {
    int total = 0;
    String sql = "SELECT COUNT(*) FROM agendamentos WHERE DATE(dataHora / 1000, 'unixepoch') = DATE('now') AND LOWER(servico) LIKE '%banho%'";

    try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {
        if (rs.next()) {
            total = rs.getInt(1);
        }
    } catch (SQLException e) {
        System.out.println("Erro ao contar banhos de hoje: " + e.getMessage());
    }

    return total;
}}


