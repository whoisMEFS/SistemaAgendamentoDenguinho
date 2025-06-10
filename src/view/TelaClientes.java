package view;

import dao.ClienteDAO;
import model.Cliente;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class TelaClientes extends JFrame {
    private static TelaClientes instancia; // Controle de instância única

    private JTable tabelaClientes;
    private JButton btnEditar, btnExcluir, btnAtualizar, btnNovoCadastro;
    private DefaultTableModel modelo;
    private ClienteDAO clienteDAO;

    public TelaClientes() {
        super("Clientes Cadastrados");
        clienteDAO = new ClienteDAO();
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Quando fechar a janela, limpa a instância
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                instancia = null;
            }
        });

        // Menu lateral padrão
        add(new MenuLateralPanel(), BorderLayout.WEST);

        // Painel central
        JPanel painelCentral = new JPanel(null);
        painelCentral.setBackground(Color.WHITE);

        modelo = new DefaultTableModel(new String[]{"ID", "Nome", "Telefone", "Pet", "Raça"}, 0);
        tabelaClientes = new JTable(modelo);
        JScrollPane scroll = new JScrollPane(tabelaClientes);
        scroll.setBounds(10, 10, 1100, 600);
        painelCentral.add(scroll);

        btnEditar = new JButton("Editar");
        btnEditar.setBounds(10, 620, 100, 30);
        painelCentral.add(btnEditar);

        btnExcluir = new JButton("Excluir");
        btnExcluir.setBounds(120, 620, 100, 30);
        painelCentral.add(btnExcluir);

        btnAtualizar = new JButton("Atualizar Lista");
        btnAtualizar.setBounds(230, 620, 150, 30);
        painelCentral.add(btnAtualizar);

        btnNovoCadastro = new JButton("Novo Cadastro");
        btnNovoCadastro.setBounds(400, 620, 150, 30);
        painelCentral.add(btnNovoCadastro);

        carregarDados();

        btnExcluir.addActionListener(e -> excluirCliente());
        btnEditar.addActionListener(e -> editarCliente());
        btnAtualizar.addActionListener(e -> carregarDados());
        btnNovoCadastro.addActionListener(e -> TelaCadastro.mostrarTela()); // Chamada segura

        add(painelCentral, BorderLayout.CENTER);
        setVisible(true);
    }

    private void carregarDados() {
        modelo.setRowCount(0);
        List<Cliente> lista = clienteDAO.listar();
        for (Cliente c : lista) {
            modelo.addRow(new Object[]{c.getId(), c.getNome(), c.getTelefone(), c.getNomePet(), c.getRaca()});
        }
    }

    private void excluirCliente() {
        int linha = tabelaClientes.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modelo.getValueAt(linha, 0);
            clienteDAO.excluir(id);
            carregarDados();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para excluir.");
        }
    }

    private void editarCliente() {
        int linha = tabelaClientes.getSelectedRow();
        if (linha >= 0) {
            int id = (int) modelo.getValueAt(linha, 0);
            String nome = (String) modelo.getValueAt(linha, 1);
            String telefone = (String) modelo.getValueAt(linha, 2);
            String pet = (String) modelo.getValueAt(linha, 3);
            String raca = (String) modelo.getValueAt(linha, 4);

            JTextField campoNome = new JTextField(nome);
            JTextField campoTelefone = new JTextField(telefone);
            JTextField campoPet = new JTextField(pet);
            JTextField campoRaca = new JTextField(raca);

            Object[] campos = {
                    "Nome:", campoNome,
                    "Telefone:", campoTelefone,
                    "Pet:", campoPet,
                    "Raça:", campoRaca
            };

            int opcao = JOptionPane.showConfirmDialog(this, campos, "Editar Cliente", JOptionPane.OK_CANCEL_OPTION);
            if (opcao == JOptionPane.OK_OPTION) {
                Cliente cliente = new Cliente(id, campoNome.getText(), campoTelefone.getText(), campoPet.getText(), campoRaca.getText());
                clienteDAO.atualizar(cliente);
                carregarDados();
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um cliente para editar.");
        }
    }

    // Método estático para abrir a tela sem duplicação
    public static void mostrarTela() {
        if (instancia == null) {
            instancia = new TelaClientes();
            instancia.setVisible(true);
        } else {
            instancia.toFront();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> TelaClientes.mostrarTela());
    }
}
