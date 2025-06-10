package dao;

import model.Caixa;
import model.Movimento;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CaixaDAO {
    private static final String URL = "jdbc:sqlite:banco/banco.db";

    public CaixaDAO() {
        criarTabelaCaixaSeNaoExistir();
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    public void criarTabelaCaixaSeNaoExistir() {
        String sql = """
            CREATE TABLE IF NOT EXISTS caixa (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                dataHoraAbertura TEXT NOT NULL,
                dataHoraFechamento TEXT,
                saldoInicial REAL NOT NULL,
                saldoFinal REAL,
                aberto BOOLEAN NOT NULL
            );
        """;

        try (Connection conn = conectar(); Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela 'caixa' verificada/criada com sucesso.");
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Erro ao criar/verificar tabela 'caixa'.");
        }
    }

    public void abrirCaixa(Caixa caixa) throws SQLException {
        String sql = "INSERT INTO caixa (dataHoraAbertura, saldoInicial, aberto) VALUES (?, ?, ?)";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, caixa.getDataHoraAbertura().toString());
            stmt.setDouble(2, caixa.getSaldoInicial());
            stmt.setBoolean(3, true);
            stmt.executeUpdate();
        }
    }

    public void fecharCaixa(int idCaixa, double saldoFinal) throws SQLException {
        String sql = "UPDATE caixa SET dataHoraFechamento = ?, saldoFinal = ?, aberto = ? WHERE id = ?";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, LocalDateTime.now().toString());
            stmt.setDouble(2, saldoFinal);
            stmt.setBoolean(3, false);
            stmt.setInt(4, idCaixa);
            stmt.executeUpdate();
        }
    }

    public Caixa buscarCaixaAberto() throws SQLException {
        String sql = "SELECT * FROM caixa WHERE aberto = 1 LIMIT 1";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Caixa caixa = new Caixa();
                caixa.setId(rs.getInt("id"));
                caixa.setDataHoraAbertura(LocalDateTime.parse(rs.getString("dataHoraAbertura")));
                caixa.setSaldoInicial(rs.getDouble("saldoInicial"));
                caixa.setAberto(rs.getBoolean("aberto"));
                return caixa;
            }
        }
        return null;
    }

    public double getSaldoAtual() throws SQLException {
        Caixa caixa = buscarCaixaAberto();
        if (caixa == null) return 0.0;

        MovimentoDAO movDAO = new MovimentoDAO();
        List<Movimento> movimentos = movDAO.listarMovimentosPorCaixa(caixa.getId());
        double entradas = movimentos.stream()
                .filter(m -> "ENTRADA".equalsIgnoreCase(m.getTipo()))
                .mapToDouble(Movimento::getValor)
                .sum();
        double saidas = movimentos.stream()
                .filter(m -> "SAÍDA".equalsIgnoreCase(m.getTipo()))
                .mapToDouble(Movimento::getValor)
                .sum();

        return caixa.getSaldoInicial() + entradas - saidas;
    }

    public List<Caixa> listarCaixas() throws SQLException {
        List<Caixa> lista = new ArrayList<>();
        String sql = "SELECT * FROM caixa";
        try (Connection conn = conectar(); PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Caixa caixa = new Caixa();
                caixa.setId(rs.getInt("id"));
                caixa.setDataHoraAbertura(LocalDateTime.parse(rs.getString("dataHoraAbertura")));
                if (rs.getString("dataHoraFechamento") != null) {
                    caixa.setDataHoraFechamento(LocalDateTime.parse(rs.getString("dataHoraFechamento")));
                }
                caixa.setSaldoInicial(rs.getDouble("saldoInicial"));
                caixa.setSaldoFinal(rs.getDouble("saldoFinal"));
                caixa.setAberto(rs.getBoolean("aberto"));
                lista.add(caixa);
            }
        }
        return lista;
    }
}
