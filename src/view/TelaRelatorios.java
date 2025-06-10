package view;

import com.toedter.calendar.JDateChooser;
import dao.MovimentoDAO;
import model.Movimento;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

public class TelaRelatorios extends JFrame {
    private JDateChooser dateChooserInicio = new JDateChooser();
    private JDateChooser dateChooserFim = new JDateChooser();

    public TelaRelatorios() {
        setTitle("Relatórios");
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setSize(520, 250);
        setLocationRelativeTo(null);
        setLayout(new GridBagLayout());

        dateChooserInicio.setDateFormatString("yyyy-MM-dd");
        dateChooserFim.setDateFormatString("yyyy-MM-dd");

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Filtros de Data com calendário
        JPanel painelFiltro = new JPanel();
        painelFiltro.add(new JLabel("Data Inicial:"));
        painelFiltro.add(dateChooserInicio);
        painelFiltro.add(new JLabel("Data Final:"));
        painelFiltro.add(dateChooserFim);

        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2; gbc.anchor = GridBagConstraints.WEST;
        add(painelFiltro, gbc);

        // Botões
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JButton btnExcelVendas = new JButton("Exportar Vendas (Excel XLS)");
        add(btnExcelVendas, gbc);
        gbc.gridx = 1;
        JButton btnVoltar = new JButton("Voltar");
        add(btnVoltar, gbc);

        btnExcelVendas.addActionListener(e -> exportarXlsVendas());
        btnVoltar.addActionListener(e -> {
            new TelaInicial().setVisible(true);
            dispose();
        });
    }

    private void exportarXlsVendas() {
        try {
            java.util.Date utilInicio = dateChooserInicio.getDate();
            java.util.Date utilFim = dateChooserFim.getDate();

            LocalDateTime inicio = (utilInicio == null)
                    ? LocalDate.MIN.atStartOfDay()
                    : utilInicio.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().atStartOfDay();

            LocalDateTime fim = (utilFim == null)
                    ? LocalDate.MAX.atTime(23, 59, 59)
                    : utilFim.toInstant().atZone(java.time.ZoneId.systemDefault()).toLocalDate().atTime(23, 59, 59);

            List<Movimento> todos = new MovimentoDAO().listarTodos();
            List<Movimento> lista = new ArrayList<>();
            for (Movimento m : todos) {
                if (!m.getDataHora().isBefore(inicio) && !m.getDataHora().isAfter(fim)) {
                    lista.add(m);
                }
            }

            JFileChooser salvar = new JFileChooser();
            salvar.setDialogTitle("Salvar Excel");
            if (salvar.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File arquivo = salvar.getSelectedFile();
                if (!arquivo.getName().endsWith(".xls")) {
                    arquivo = new File(arquivo.getAbsolutePath() + ".xls");
                }
                ExportadorExcelJXL.exportarVendas(lista, arquivo);
                JOptionPane.showMessageDialog(this, "Relatório exportado com sucesso!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erro ao exportar: " + ex.getMessage());
        }
    }
}