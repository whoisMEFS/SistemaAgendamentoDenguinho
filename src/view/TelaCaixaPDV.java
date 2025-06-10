package view;

import dao.CaixaDAO;
import dao.MovimentoDAO;
import model.Caixa;
import model.Movimento;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class TelaCaixaPDV extends JFrame {
    private JTable tabela;
    private DefaultTableModel modeloTabela;
    private JLabel lblTotal, lblTroco;
    private JTextField txtValorRecebido;
    private List<Movimento> itensImportados = new ArrayList<>();
    private double total = 0.0;

    private JTextField txtCliente, txtPet, txtServico, txtValor;
    private JButton btnAdicionarItem;

    public TelaCaixaPDV() {
        setTitle("PDV - Caixa");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel lblTitulo = new JLabel("PDV - Denguinho", JLabel.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 28));
        lblTitulo.setForeground(Color.BLUE);
        add(lblTitulo, BorderLayout.NORTH);

        JPanel painelEntrada = new JPanel(new FlowLayout());

        txtCliente = new JTextField(10);
        txtPet = new JTextField(8);
        txtServico = new JTextField(10);
        txtValor = new JTextField(6);
        btnAdicionarItem = new JButton("Adicionar");

        painelEntrada.add(new JLabel("Cliente:"));
        painelEntrada.add(txtCliente);

        painelEntrada.add(new JLabel("Pet:"));
        painelEntrada.add(txtPet);

        painelEntrada.add(new JLabel("Produto/Serviço:"));
        painelEntrada.add(txtServico);

        painelEntrada.add(new JLabel("Valor:"));
        painelEntrada.add(txtValor);

        painelEntrada.add(btnAdicionarItem);

        add(painelEntrada, BorderLayout.BEFORE_FIRST_LINE);

        modeloTabela = new DefaultTableModel(new Object[]{"Cliente", "Pet", "Serviço/Produto", "Valor"}, 0);
        tabela = new JTable(modeloTabela);
        JScrollPane scroll = new JScrollPane(tabela);
        add(scroll, BorderLayout.CENTER);

        JPanel painelSul = new JPanel(new BorderLayout());

        JPanel painelPag = new JPanel(new FlowLayout(FlowLayout.LEFT));
        lblTotal = new JLabel("Total: R$ 0,00");
        lblTotal.setFont(new Font("Arial", Font.BOLD, 20));
        painelPag.add(lblTotal);

        painelPag.add(new JLabel("  Valor Recebido:"));
        txtValorRecebido = new JTextField(8);
        painelPag.add(txtValorRecebido);

        lblTroco = new JLabel("Troco: R$ 0,00");
        lblTroco.setFont(new Font("Arial", Font.BOLD, 16));
        painelPag.add(lblTroco);

        painelSul.add(painelPag, BorderLayout.CENTER);

        JPanel botoes = new JPanel();
        JButton btnImportar = new JButton("Importar Agendamentos");
        JButton btnFinalizar = new JButton("Finalizar Venda");
        JButton btnAbrirCaixa = new JButton("Abrir Caixa");
        JButton btnFecharCaixa = new JButton("Fechar Caixa");

        botoes.add(btnImportar);
        botoes.add(btnFinalizar);
        botoes.add(btnAbrirCaixa);
        botoes.add(btnFecharCaixa);

        painelSul.add(botoes, BorderLayout.SOUTH);
        add(painelSul, BorderLayout.SOUTH);

        // Ações
        btnImportar.addActionListener(e -> abrirTelaBaixaAgendamentos());
        btnFinalizar.addActionListener(e -> finalizarVenda());
        btnAdicionarItem.addActionListener(e -> adicionarItemManual());

        btnAbrirCaixa.addActionListener(e -> {
            try {
                String valorInicialStr = JOptionPane.showInputDialog(this, "Valor de abertura do caixa:", "Abrir Caixa", JOptionPane.PLAIN_MESSAGE);
                if (valorInicialStr == null || valorInicialStr.trim().isEmpty()) return;

                double valorInicial = Double.parseDouble(valorInicialStr.replace(",", "."));

                Caixa caixa = new Caixa();
                caixa.setDataHoraAbertura(LocalDateTime.now());
                caixa.setSaldoInicial(valorInicial);
                caixa.setAberto(true);

                CaixaDAO dao = new CaixaDAO();
                dao.abrirCaixa(caixa);

                JOptionPane.showMessageDialog(this, "Caixa aberto com sucesso!");
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao abrir caixa.");
            }
        });

        btnFecharCaixa.addActionListener(e -> {
            try {
                CaixaDAO dao = new CaixaDAO();
                Caixa caixa = dao.buscarCaixaAberto();
                if (caixa == null) {
                    JOptionPane.showMessageDialog(this, "Nenhum caixa aberto.");
                    return;
                }

                double valorFinal = caixa.getSaldoInicial() + total;
                caixa.setDataHoraFechamento(LocalDateTime.now());
                caixa.setSaldoFinal(valorFinal);
                caixa.setAberto(false);

                dao.fecharCaixa(caixa.getId(), valorFinal);

                JOptionPane.showMessageDialog(this, "Caixa fechado com sucesso!\nTotal final: R$ " + String.format("%.2f", valorFinal));
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Erro ao fechar caixa.");
            }
        });

        txtValorRecebido.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { atualizarTroco(); }
            public void removeUpdate(DocumentEvent e) { atualizarTroco(); }
            public void changedUpdate(DocumentEvent e) { atualizarTroco(); }
        });

        setVisible(true);
    }

    private void abrirTelaBaixaAgendamentos() {
        TelaBaixaAgendamento tela = new TelaBaixaAgendamento(this);
        tela.setVisible(true);
    }

    public void adicionarItensImportados(List<Movimento> lista) {
        for (Movimento m : lista) {
            modeloTabela.addRow(new Object[]{
                m.getCliente(), m.getPet(), m.getServico(), String.format("R$ %.2f", m.getValor())
            });
            itensImportados.add(m);
            total += m.getValor();
        }
        atualizarTotal();
    }

    private void atualizarTotal() {
        lblTotal.setText(String.format("Total: R$ %.2f", total));
        atualizarTroco();
    }

    private void atualizarTroco() {
        String valorRecebidoStr = txtValorRecebido.getText().replace(",", ".").trim();
        double valorRecebido = 0.0;
        if (!valorRecebidoStr.isEmpty()) {
            try {
                valorRecebido = Double.parseDouble(valorRecebidoStr);
            } catch (NumberFormatException e) {
                lblTroco.setText("Troco: R$ ---");
                return;
            }
        }
        double troco = valorRecebido - total;
        lblTroco.setText(String.format("Troco: R$ %.2f", troco));
    }

    private void finalizarVenda() {
        try {
            CaixaDAO caixaDAO = new CaixaDAO();
            Caixa caixaAberto = caixaDAO.buscarCaixaAberto();
            if (caixaAberto == null) {
                JOptionPane.showMessageDialog(this, "Não há caixa aberto!");
                return;
            }

            if (total <= 0) {
                JOptionPane.showMessageDialog(this, "Nenhum item para finalizar!");
                return;
            }

            String valorRecebidoStr = txtValorRecebido.getText().replace(",", ".").trim();
            double valorRecebido;
            try {
                valorRecebido = Double.parseDouble(valorRecebidoStr);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Valor recebido inválido.");
                return;
            }

            double troco = valorRecebido - total;
            if (troco < 0) {
                JOptionPane.showMessageDialog(this, String.format("Pagamento insuficiente!\nFaltam R$ %.2f.", -troco));
                return;
            }

            MovimentoDAO movDAO = new MovimentoDAO();
            for (Movimento mov : itensImportados) {
                mov.setDataHora(LocalDateTime.now());
                movDAO.registrarMovimento(mov);
            }

            JOptionPane.showMessageDialog(this, String.format("Venda registrada com sucesso!\nTroco: R$ %.2f", troco));

            modeloTabela.setRowCount(0);
            itensImportados.clear();
            total = 0.0;
            txtValorRecebido.setText("");
            atualizarTotal();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao registrar venda.");
        }
    }

    private void adicionarItemManual() {
        String cliente = txtCliente.getText().trim();
        String pet = txtPet.getText().trim();
        String servico = txtServico.getText().trim();
        String valorStr = txtValor.getText().trim();

        if (servico.isEmpty() || valorStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Preencha serviço/produto e valor.");
            return;
        }

        double valor;
        try {
            valor = Double.parseDouble(valorStr.replace(",", "."));
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valor inválido.");
            return;
        }

        modeloTabela.addRow(new Object[]{cliente, pet, servico, String.format("R$ %.2f", valor)});

        Movimento mov = new Movimento();
        mov.setCliente(cliente);
        mov.setPet(pet);
        mov.setServico(servico);
        mov.setValor(valor);
        mov.setTipo("ENTRADA");
        itensImportados.add(mov);

        total += valor;
        atualizarTotal();

        txtCliente.setText("");
        txtPet.setText("");
        txtServico.setText("");
        txtValor.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaCaixaPDV tela = new TelaCaixaPDV();
            tela.setVisible(true);
        });
    }
}
