package dao;

import model.Movimento;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimentoDAO {
    private static final String URL = "jdbc:sqlite:banco/banco.db";

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void salvar(Movimento movimento) throws SQLException {
        String sql = "INSERT INTO movimento (id_Caixa, tipo, valor, descricao, data_Hora) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, movimento.getIdCaixa());
            stmt.setString(2, movimento.getTipo());
            stmt.setDouble(3, movimento.getValor());
            stmt.setString(4, movimento.getDescricao());
            stmt.setString(5, movimento.getDataHora().toString());
            stmt.executeUpdate();
        }
    }

    public void registrarMovimento(Movimento movimento) throws SQLException {
        salvar(movimento);
    }

    public List<Movimento> listarMovimentosPorCaixa(int idCaixa) throws SQLException {
        List<Movimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimento WHERE id_Caixa = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCaixa);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Movimento mov = new Movimento();
                    mov.setId(rs.getInt("id"));
                    mov.setIdCaixa(rs.getInt("id_Caixa"));
                    mov.setTipo(rs.getString("tipo"));
                    mov.setValor(rs.getDouble("valor"));
                    mov.setDescricao(rs.getString("descricao"));

                    String dataHoraStr = rs.getString("data_Hora");
                    if (dataHoraStr != null) {
                        mov.setDataHora(LocalDateTime.parse(dataHoraStr));
                    }

                    lista.add(mov);
                }
            }
        }
        return lista;
    }

    public List<Movimento> listarTodos() {
        List<Movimento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimento ORDER BY data_Hora";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Movimento mov = new Movimento();
                mov.setId(rs.getInt("id"));
                mov.setIdCaixa(rs.getInt("id_Caixa"));
                mov.setTipo(rs.getString("tipo"));
                mov.setValor(rs.getDouble("valor"));
                mov.setDescricao(rs.getString("descricao"));

                String dataHoraStr = rs.getString("data_Hora");
                if (dataHoraStr != null) {
                    mov.setDataHora(LocalDateTime.parse(dataHoraStr));
                }

                lista.add(mov);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("Erro ao listar todos os movimentos: " + ex.getMessage());
        }

        return lista;
    }

    public double totalVendasHoje() {
        double total = 0.0;
        String sql = "SELECT SUM(valor) FROM movimento WHERE DATE(data_Hora) = DATE('now')";

        try (Connection conn = conectar();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble(1);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao calcular total de vendas de hoje: " + e.getMessage());
        }

        return total;
    }

    
}
