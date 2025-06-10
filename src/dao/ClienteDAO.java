package dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.Cliente;

public class ClienteDAO {
    private static final String URL = "jdbc:sqlite:banco/banco.db";

    public ClienteDAO() {
        criarTabelaSeNaoExistir();
    }

    private void criarTabelaSeNaoExistir() {
        String sql = "CREATE TABLE IF NOT EXISTS clientes (" +
                     "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                     "nomeCliente TEXT NOT NULL," +
                     "telefone TEXT," +
                     "nomePet TEXT," +
                     "raca TEXT)";
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void salvar(String nomeCliente, String telefone, String nomePet, String raca) {
        String sql = "INSERT INTO clientes (nomeCliente, telefone, nomePet, raca) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nomeCliente);
            stmt.setString(2, telefone);
            stmt.setString(3, nomePet);
            stmt.setString(4, raca);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

public List<Cliente> listar() {
    List<Cliente> lista = new ArrayList<>();
    String sql = "SELECT * FROM clientes";
    try (Connection conn = DriverManager.getConnection(URL);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            lista.add(new Cliente(
                rs.getInt("id"),
                rs.getString("nomeCliente"), // <- Corrigido aqui
                rs.getString("telefone"),
                rs.getString("nomePet"),
                rs.getString("raca")
            ));
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;

    }

    public void atualizar(Cliente cliente) {
        String sql = "UPDATE clientes SET nomeCliente = ?, telefone = ?, nomePet = ?, raca = ? WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNome());
            stmt.setString(2, cliente.getTelefone());
            stmt.setString(3, cliente.getNomePet());
            stmt.setString(4, cliente.getRaca());
            stmt.setInt(5, cliente.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void excluir(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";
        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
