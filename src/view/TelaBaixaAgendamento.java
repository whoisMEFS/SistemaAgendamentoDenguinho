package view;

import dao.AgendamentoDAO;
import dao.MovimentoDAO;
import model.Agendamento;
import model.Movimento;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TelaBaixaAgendamento extends JFrame {
    private JTable tabelaAgendamentos;
    private DefaultTableModel modeloTabela;
    private JLabel lblSubtotal;
    private JButton btnDarBaixa;
    private TelaCaixaPDV telaCaixa;

    private AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
    private MovimentoDAO movimentoDAO = new MovimentoDAO();

    public TelaBaixaAgendamento() {
        inicializarTela();
    }

    public TelaBaixaAgendamento(TelaCaixaPDV telaCaixa) {
        this();
        this.telaCaixa = telaCaixa;
    }

    private void inicializarTela() {
        setTitle("Caixa - Agendamentos");
        setSize(1366, 768);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        modeloTabela = new DefaultTableModel(new Object[]{"ID", "Cliente", "Pet", "Serviço", "Valor"}, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tabelaAgendamentos = new JTable(modeloTabela);
        JScrollPane scrollPane = new JScrollPane(tabelaAgendamentos);
        add(scrollPane, BorderLayout.CENTER);

        JPanel rodape = new JPanel(new BorderLayout());
        lblSubtotal = new JLabel("Subtotal: R$ 0.00");
        lblSubtotal.setFont(new Font("Arial", Font.BOLD, 18));
        lblSubtotal.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        rodape.add(lblSubtotal, BorderLayout.WEST);

        btnDarBaixa = new JButton("Dar Baixa");
        btnDarBaixa.setFont(new Font("Arial", Font.BOLD, 16));
        btnDarBaixa.addActionListener(e -> {
            try {
                darBaixaNosSelecionados();
            } catch (SQLException ex) {
                Logger.getLogger(TelaBaixaAgendamento.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        rodape.add(btnDarBaixa, BorderLayout.EAST);

        add(rodape, BorderLayout.SOUTH);

        carregarAgendamentosPendentes();
    }

    private void carregarAgendamentosPendentes() {
        modeloTabela.setRowCount(0);
        List<Agendamento> lista = agendamentoDAO.listarAgendamentosPendentes();
        for (Agendamento ag : lista) {
            modeloTabela.addRow(new Object[]{
                ag.getId(), ag.getCliente(), ag.getNomePet(), ag.getServico(), String.format("R$ %.2f", ag.getValor())
            });
        }
        atualizarSubtotal();
    }

    private void atualizarSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < modeloTabela.getRowCount(); i++) {
            String valorTexto = modeloTabela.getValueAt(i, 4).toString().replace("R$", "").trim().replace(",", ".");
            subtotal += Double.parseDouble(valorTexto);
        }
        lblSubtotal.setText(String.format("Subtotal: R$ %.2f", subtotal));
    }

    private void darBaixaNosSelecionados() throws SQLException {
        int[] linhas = tabelaAgendamentos.getSelectedRows();
        if (linhas.length == 0) {
            JOptionPane.showMessageDialog(this, "Selecione um ou mais agendamentos para dar baixa.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Deseja realmente dar baixa nos agendamentos selecionados?", "Confirmar",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) return;

        List<Movimento> listaMovimentos = new ArrayList<>();

        for (int linha : linhas) {
            int idAgendamento = (int) modeloTabela.getValueAt(linha, 0);
            String cliente = modeloTabela.getValueAt(linha, 1).toString();
            String pet = modeloTabela.getValueAt(linha, 2).toString();
            String servico = modeloTabela.getValueAt(linha, 3).toString();
            double valor = Double.parseDouble(modeloTabela.getValueAt(linha, 4).toString().replace("R$", "").replace(",", ".").trim());

            Movimento m = new Movimento();
            m.setIdCaixa(1); // ajustar conforme necessário
            m.setTipo("ENTRADA");
            m.setValor(valor);
            m.setDescricao("Agendamento ID: " + idAgendamento);
            m.setDataHora(LocalDateTime.now());

            m.setCliente(cliente);
            m.setPet(pet);
            m.setServico(servico);

            movimentoDAO.salvar(m);
            agendamentoDAO.baixarAgendamento(idAgendamento);

            listaMovimentos.add(m);
        }

        if (telaCaixa != null) {
            telaCaixa.adicionarItensImportados(listaMovimentos);
            telaCaixa.setVisible(true); // Garante visibilidade
            telaCaixa.toFront();        // Traz para frente
        }

        JOptionPane.showMessageDialog(this, "Baixa realizada com sucesso!");
        dispose(); // Fecha a tela de agendamento.
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new TelaBaixaAgendamento().setVisible(true));
    }
}