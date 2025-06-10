package view;

import javax.swing.*;
import java.awt.*;

public class TelaCadastro extends JFrame {
    private static TelaCadastro instancia; // ✅ Instância única da tela

    private final String[] racas = {"SRD", "Labrador", "Poodle", "Bulldog", "Yorkshire", "Golden Retriever",
            "Shih Tzu", "Beagle", "Pinscher", "Pastor Alemão", "Rottweiler",
            "Persa", "Siamês", "Maine Coon", "Angorá", "Sphynx", "Bengal", "Birmanês"};

    private JTextField txtNomeCliente, txtTelefone, txtNomePet;
    private JComboBox<String> comboRaca;
    private JButton btnSalvar;

    public TelaCadastro() {
        setTitle("Cadastro");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Menu lateral
        add(new MenuLateralPanel(), BorderLayout.WEST);

        // Painel central
        JPanel painelCentral = new JPanel(null);
        painelCentral.setBackground(Color.WHITE);

        JLabel jLabel1 = new JLabel("Nome");
        jLabel1.setFont(new Font("Segoe UI", 0, 24));
        jLabel1.setBounds(450, 180, 80, 30);
        painelCentral.add(jLabel1);

        txtNomeCliente = new JTextField();
        txtNomeCliente.setBounds(600, 180, 300, 30);
        painelCentral.add(txtNomeCliente);

        JLabel jLabel2 = new JLabel("Telefone");
        jLabel2.setFont(new Font("Segoe UI", 0, 24));
        jLabel2.setBounds(450, 230, 100, 30);
        painelCentral.add(jLabel2);

        txtTelefone = new JTextField();
        txtTelefone.setBounds(600, 230, 300, 30);
        painelCentral.add(txtTelefone);

        JLabel jLabel4 = new JLabel("Nome do Pet");
        jLabel4.setFont(new Font("Segoe UI", 0, 24));
        jLabel4.setBounds(450, 280, 150, 30);
        painelCentral.add(jLabel4);

        txtNomePet = new JTextField();
        txtNomePet.setBounds(600, 280, 300, 30);
        painelCentral.add(txtNomePet);

        JLabel jLabel5 = new JLabel("Raça");
        jLabel5.setFont(new Font("Segoe UI", 0, 24));
        jLabel5.setBounds(450, 330, 100, 30);
        painelCentral.add(jLabel5);

        comboRaca = new JComboBox<>(racas);
        comboRaca.setBounds(600, 330, 300, 30);
        painelCentral.add(comboRaca);

        btnSalvar = new JButton("Salvar");
        btnSalvar.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnSalvar.setBounds(600, 390, 150, 40);
        btnSalvar.addActionListener(evt -> btnSalvarActionPerformed());
        painelCentral.add(btnSalvar);

        add(painelCentral, BorderLayout.CENTER);
    }

    private void btnSalvarActionPerformed() {
        String nomeCliente = txtNomeCliente.getText();
        String telefone = txtTelefone.getText();
        String nomePet = txtNomePet.getText();
        String raca = (String) comboRaca.getSelectedItem();

        dao.ClienteDAO dao = new dao.ClienteDAO();
        dao.salvar(nomeCliente, telefone, nomePet, raca);

        JOptionPane.showMessageDialog(this, "Cadastro salvo com sucesso!");

        txtNomeCliente.setText("");
        txtTelefone.setText("");
        txtNomePet.setText("");
        comboRaca.setSelectedIndex(0);
    }

    // ✅ Método para garantir que só uma instância seja aberta
    public static void mostrarTela() {
        if (instancia == null || !instancia.isDisplayable()) {
            instancia = new TelaCadastro();
            instancia.setVisible(true);
        } else {
            instancia.toFront();
            instancia.requestFocus();
        }
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> mostrarTela());
    }
}
