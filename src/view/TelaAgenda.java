package view;

import com.toedter.calendar.JDateChooser;
import dao.AgendamentoDAO;
import model.Agendamento;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TelaAgenda extends JFrame {
    private static TelaAgenda instanciaUnica;

    private JPanel painelCentral;
    private JComboBox<String> comboServico;
    private JComboBox<String> comboProfissional;
    private JButton btnNovoAgendamento;
    private JPanel painelAgenda;
    private JButton btnDiaAnterior;
    private JButton btnProximoDia;
    private JLabel labelData;
    private JDateChooser dateChooser;

    private final String[] profissionais = {"Marcia", "Bárbarah", "Sem profissional"};
    private LocalDate dataAtual;

    public TelaAgenda() {
        setTitle("Agenda Diária");
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                instanciaUnica = null;
            }
        });

        add(new MenuLateralPanel(), BorderLayout.WEST);

        painelCentral = new JPanel(new BorderLayout());
        painelCentral.setBackground(Color.WHITE);
        add(painelCentral, BorderLayout.CENTER);

        JPanel painelTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        comboProfissional = new JComboBox<>(new String[]{"Todos os profissionais", "Marcia", "Bárbarah", "Sem profissional"});
        comboServico = new JComboBox<>(new String[]{"Todos os serviços", "Banho", "Tosa", "Banho e Higienização"});
        labelData = new JLabel();
        dateChooser = new JDateChooser();

        painelTopo.add(new JLabel("Data:"));
        painelTopo.add(labelData);
        painelTopo.add(dateChooser);
        painelTopo.add(new JLabel("Profissional:"));
        painelTopo.add(comboProfissional);
        painelTopo.add(new JLabel("Serviço:"));
        painelTopo.add(comboServico);
        painelCentral.add(painelTopo, BorderLayout.NORTH);

        painelAgenda = new JPanel();
        painelCentral.add(new JScrollPane(painelAgenda), BorderLayout.CENTER);

        JPanel painelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnNovoAgendamento = new JButton("Novo Agendamento");
        btnDiaAnterior = new JButton("< Dia Anterior");
        btnProximoDia = new JButton("Próximo Dia >");
        painelBotoes.add(btnNovoAgendamento);
        painelBotoes.add(btnDiaAnterior);
        painelBotoes.add(btnProximoDia);
        painelCentral.add(painelBotoes, BorderLayout.SOUTH);

        dataAtual = LocalDate.now();
        atualizarLabelData();
        carregarAgendamentos();

        btnNovoAgendamento.addActionListener(e -> {
            TelaAgendamento tela = new TelaAgendamento();
            tela.setVisible(true);
            carregarAgendamentos();
        });

        comboProfissional.addActionListener(e -> carregarAgendamentos());
        comboServico.addActionListener(e -> carregarAgendamentos());

        btnDiaAnterior.addActionListener(e -> {
            dataAtual = dataAtual.minusDays(1);
            atualizarLabelData();
            carregarAgendamentos();
        });

        btnProximoDia.addActionListener(e -> {
            dataAtual = dataAtual.plusDays(1);
            atualizarLabelData();
            carregarAgendamentos();
        });

        dateChooser.addPropertyChangeListener("date", evt -> {
            Date selectedDate = dateChooser.getDate();
            if (selectedDate != null) {
                dataAtual = selectedDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                atualizarLabelData();
                carregarAgendamentos();
            }
        });
    }

    private void atualizarLabelData() {
        labelData.setText(dataAtual.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    }

    private void carregarAgendamentos() {
        painelAgenda.removeAll();
        painelAgenda.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;

        AgendamentoDAO agendamentoDAO = new AgendamentoDAO();
        List<Agendamento> agendamentos = agendamentoDAO.listar();

        String profissionalSelecionado = (String) comboProfissional.getSelectedItem();
        String servicoSelecionado = (String) comboServico.getSelectedItem();

        if (!"Todos os profissionais".equals(profissionalSelecionado)) {
            agendamentos = agendamentos.stream()
                    .filter(a -> profissionalSelecionado.equals(a.getProfissional()))
                    .collect(Collectors.toList());
        }

        if (!"Todos os serviços".equals(servicoSelecionado)) {
            agendamentos = agendamentos.stream()
                    .filter(a -> servicoSelecionado.equals(a.getServico()))
                    .collect(Collectors.toList());
        }

        gbc.gridy = 0;
        gbc.gridx = 0;
        painelAgenda.add(new JLabel("Horário", SwingConstants.CENTER), gbc);

        for (int i = 0; i < profissionais.length; i++) {
            gbc.gridx = i + 1;
            painelAgenda.add(new JLabel(profissionais[i], SwingConstants.CENTER), gbc);
        }

        DateTimeFormatter horaFormat = DateTimeFormatter.ofPattern("HH:mm");
        int linha = 1;

        for (LocalTime hora = LocalTime.of(8, 0); !hora.isAfter(LocalTime.of(17, 30)); hora = hora.plusMinutes(30)) {
            gbc.gridy = linha;
            gbc.gridx = 0;
            painelAgenda.add(new JLabel(hora.format(horaFormat), SwingConstants.CENTER), gbc);

            for (int i = 0; i < profissionais.length; i++) {
                String profissional = profissionais[i];
                gbc.gridx = i + 1;
                LocalTime horaAtual = hora;
                JPanel celula = new JPanel();
                celula.setPreferredSize(new Dimension(180, 60));
                celula.setLayout(new FlowLayout(FlowLayout.LEFT));
                celula.setBorder(BorderFactory.createLineBorder(Color.BLACK));

                List<Agendamento> agendamentosNaHora = agendamentos.stream()
                        .filter(a -> a.getData().equals(dataAtual)
                                && a.getProfissional().equals(profissional)
                                && !horaAtual.isBefore(a.getHora())
                                && horaAtual.isBefore(a.getHora().plusMinutes(a.getDuracao())))
                        .collect(Collectors.toList());

                if (!agendamentosNaHora.isEmpty()) {
                    for (Agendamento ag : agendamentosNaHora) {
                        JPanel painelInfo = new JPanel();
                        painelInfo.setLayout(new BoxLayout(painelInfo, BoxLayout.Y_AXIS));
                        painelInfo.setBackground(getCorServico(ag.getServico()));
                        painelInfo.setBorder(BorderFactory.createEmptyBorder(2, 2, 2, 2));
                        painelInfo.setToolTipText("Pet: " + ag.getNomePet() + " - " + ag.getServico());

                        JLabel label = new JLabel("" + ag.getNomePet() + " - " + ag.getServico());
                        label.setFont(new Font("Arial", Font.PLAIN, 9));
                        painelInfo.add(label);

                        painelInfo.addMouseListener(new java.awt.event.MouseAdapter() {
                            public void mouseClicked(java.awt.event.MouseEvent evt) {
                                Object[] opcoes = {"Editar", "Excluir", "Novo Agendamento", "Cancelar"};
                                int escolha = JOptionPane.showOptionDialog(null,
                                        "O que deseja fazer com este agendamento?",
                                        "Opções",
                                        JOptionPane.DEFAULT_OPTION,
                                        JOptionPane.QUESTION_MESSAGE,
                                        null,
                                        opcoes,
                                        opcoes[0]);

                                if (escolha == 0) {
                                    new TelaAgendamento(ag).setVisible(true);
                                    carregarAgendamentos();
                                } else if (escolha == 1) {
                                    int confirmar = JOptionPane.showConfirmDialog(null,
                                            "Tem certeza que deseja excluir este agendamento?",
                                            "Confirmar Exclusão",
                                            JOptionPane.YES_NO_OPTION);
                                    if (confirmar == JOptionPane.YES_OPTION) {
                                        new AgendamentoDAO().excluir(ag.getId());
                                        JOptionPane.showMessageDialog(null, "Agendamento excluído com sucesso!");
                                        carregarAgendamentos();
                                    }
                                } else if (escolha == 2) {
                                    new TelaAgendamento(dataAtual, horaAtual, profissional).setVisible(true);
                                    carregarAgendamentos();
                                }
                            }
                        });

                        celula.add(painelInfo);
                    }
                } else {
                    celula.setBackground(Color.WHITE);
                    celula.addMouseListener(new java.awt.event.MouseAdapter() {
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            int resp = JOptionPane.showConfirmDialog(null,
                                    "Deseja criar um agendamento neste horário?",
                                    "Novo Agendamento", JOptionPane.YES_NO_OPTION);
                            if (resp == JOptionPane.YES_OPTION) {
                                new TelaAgendamento(dataAtual, horaAtual, profissional).setVisible(true);
                                carregarAgendamentos();
                            }
                        }
                    });
                }

                painelAgenda.add(celula, gbc);
            }

            linha++;
        }

        painelAgenda.revalidate();
        painelAgenda.repaint();
    }

    private Color getCorServico(String servico) {
        return switch (servico.toLowerCase()) {
            case "banho" -> Color.CYAN;
            case "tosa" -> Color.GREEN;
            case "banho e higienização" -> Color.YELLOW;
            default -> Color.LIGHT_GRAY;
        };
    }

    // Método para abrir a janela com controle de instância
    public static void abrirTela() {
        if (instanciaUnica == null || !instanciaUnica.isDisplayable()) {
            instanciaUnica = new TelaAgenda();
            instanciaUnica.setVisible(true);
        } else {
            instanciaUnica.toFront();
            instanciaUnica.requestFocus();
        }
    }

    // Somente para teste
    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaAgenda::abrirTela);
    }
}
