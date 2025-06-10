package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class BancoInicializador {

    private static final String URL = "jdbc:sqlite:banco/banco.db";

    public static void inicializarBanco() {
        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            // Tabela agendamentos (com campo 'baixado')
            stmt.execute("CREATE TABLE IF NOT EXISTS agendamentos ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "cliente TEXT,"
                    + "nomePet TEXT,"
                    + "raca TEXT,"
                    + "dataHora INTEGER,"
                    + "servico TEXT,"
                    + "duracao INTEGER,"
                    + "profissional TEXT,"
                    + "valor REAL,"
                    + "baixado INTEGER DEFAULT 0"
                    + ");");

            // Tabela movimento (sem 's' no final, conforme seu DAO)
            stmt.execute("CREATE TABLE IF NOT EXISTS movimento ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "id_Caixa INTEGER,"
                    + "tipo TEXT,"
                    + "valor REAL,"
                    + "descricao TEXT,"
                    + "data_Hora TEXT"
                    + ");");

            // Você pode criar outras tabelas aqui, se necessário (ex: caixa)
            stmt.execute("CREATE TABLE IF NOT EXISTS caixa ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + "data_abertura TEXT,"
                    + "data_fechamento TEXT,"
                    + "valor_abertura REAL,"
                    + "valor_fechamento REAL"
                    + ");");

            System.out.println("Banco de dados inicializado com sucesso.");

        } catch (Exception e) {
            System.err.println("Erro ao inicializar banco: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
