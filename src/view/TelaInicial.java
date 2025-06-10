package view;

import dao.AgendamentoDAO;
import dao.BancoInicializador;
import dao.MovimentoDAO;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Timer;
import java.util.TimerTask;

public class TelaInicial extends JFrame {
    private JLabel lblQtdBanhos;
    private JLabel lblTotalVendas;
    private JLabel lblDataHora;

    // Controle da janela externa aberta
    private JFrame janelaAberta = null;

    public TelaInicial() {
        setTitle("Denguinho - Para Cachorro");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1200, 680);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Painel menu lateral
        JPanel painelMenu = new JPanel();
        painelMenu.setLayout(new BoxLayout(painelMenu, BoxLayout.Y_AXIS));
        painelMenu.setBackground(new Color(44, 62, 80));
        painelMenu.setPreferredSize(new Dimension(180, 0));

        // Logo
        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logo = new ImageIcon(getClass().getResource("/Imagens/denguinho.png"));
            logoLabel.setIcon(new ImageIcon(logo.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            logoLabel.setText("Denguinho");
            logoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        painelMenu.add(Box.createVerticalStrut(20));
        painelMenu.add(logoLabel);
        painelMenu.add(Box.createVerticalStrut(10));

        // Botões do menu lateral
        painelMenu.add(criarBotaoMenu("Agenda", new Color(255, 140, 0), this::abrirTelaAgenda));
        painelMenu.add(criarBotaoMenu("Cadastro", new Color(41, 128, 185), this::abrirTelaCadastro));
        painelMenu.add(criarBotaoMenu("Caixa", new Color(53, 167, 110), this::abrirTelaCaixaPDV));
        painelMenu.add(criarBotaoMenu("Cliente", new Color(150, 65, 165), this::abrirTelaClientes));
        painelMenu.add(criarBotaoMenu("Relatórios", new Color(243, 156, 18), this::abrirTelaRelatorios));
        painelMenu.add(criarBotaoMenu("Menu Inicial", new Color(53, 167, 110), this::abrirTelaInicial));
        painelMenu.add(Box.createVerticalGlue());
        add(painelMenu, BorderLayout.WEST);

        // Painel principal central
        JPanel painelPrincipal = new JPanel(new BorderLayout());
        painelPrincipal.setBackground(Color.WHITE);

        // Topo com cards e relógio
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(Color.WHITE);

        JPanel painelCards = new JPanel(new GridLayout(1, 2, 20, 10));
        painelCards.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));
        painelCards.setBackground(Color.WHITE);

        lblQtdBanhos = new JLabel("0", SwingConstants.CENTER);
        painelCards.add(criarCard("Agenda", lblQtdBanhos, new Color(255, 140, 0)));

        lblTotalVendas = new JLabel("R$ 0,00", SwingConstants.CENTER);
        painelCards.add(criarCard("Vendas", lblTotalVendas, new Color(41, 128, 185)));

        painelTopo.add(painelCards, BorderLayout.CENTER);

        JPanel painelDataHora = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        painelDataHora.setBackground(Color.WHITE);
        lblDataHora = new JLabel();
        lblDataHora.setFont(new Font("Arial", Font.BOLD, 18));
        painelDataHora.add(lblDataHora);
        painelTopo.add(painelDataHora, BorderLayout.NORTH);

        painelPrincipal.add(painelTopo, BorderLayout.NORTH);

        // Painel de boas-vindas
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBackground(Color.WHITE);

        JLabel lblBemVindo = new JLabel("Bem vindo!");
        lblBemVindo.setFont(new Font("Arial", Font.BOLD, 32));
        lblBemVindo.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lblAjuda = new JLabel("Vamos começar?");
        lblAjuda.setFont(new Font("Arial", Font.PLAIN, 18));
        lblAjuda.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel painelVideo = new JPanel();
        painelVideo.setBackground(new Color(32, 207, 194));
        painelVideo.setPreferredSize(new Dimension(370, 120));
        painelVideo.setMaximumSize(painelVideo.getPreferredSize());
        painelVideo.setAlignmentX(Component.CENTER_ALIGNMENT);

        painelCentro.add(Box.createVerticalStrut(20));
        painelCentro.add(lblBemVindo);
        painelCentro.add(Box.createVerticalStrut(10));
        painelCentro.add(lblAjuda);
        painelCentro.add(Box.createVerticalStrut(38));
        painelCentro.add(painelVideo);

        painelPrincipal.add(painelCentro, BorderLayout.CENTER);

        add(painelPrincipal, BorderLayout.CENTER);

        atualizarResumo();
        iniciarRelogio();

        setVisible(true);
    }

    private JPanel criarCard(String titulo, JLabel valorLabel, Color cor) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(cor);
        card.setPreferredSize(new Dimension(220, 100));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 2, true));

        JLabel lblTitulo = new JLabel(titulo, SwingConstants.CENTER);
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(12, 0, 2, 0));

        valorLabel.setForeground(Color.WHITE);
        valorLabel.setFont(new Font("Arial", Font.BOLD, 30));

        card.add(lblTitulo, BorderLayout.NORTH);
        card.add(valorLabel, BorderLayout.CENTER);

        return card;
    }

    private JButton criarBotaoMenu(String texto, Color cor, Runnable acao) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(150, 48));
        btn.setMaximumSize(btn.getPreferredSize());
        btn.setFont(new Font("Arial", Font.BOLD, 17));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> acao.run());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private void atualizarResumo() {
        lblQtdBanhos.setText(String.valueOf(consultarBanhosHoje()));
        lblTotalVendas.setText("R$ " + String.format("%.2f", consultarTotalVendasHoje()));
    }

    private int consultarBanhosHoje() {
        AgendamentoDAO dao = new AgendamentoDAO();
        return dao.contarBanhosHoje();
    }

    private double consultarTotalVendasHoje() {
        MovimentoDAO dao = new MovimentoDAO();
        return dao.totalVendasHoje();
    }

    private void iniciarRelogio() {
        Timer timer = new Timer();
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SwingUtilities.invokeLater(() -> {
                    lblDataHora.setText("Hoje: " + LocalDateTime.now().format(formatador));
                    atualizarResumo();
                });
            }
        }, 0, 60000);
    }

    // Método genérico para abrir nova janela e fechar a anterior
    private void abrirNovaJanela(JFrame novaJanela) {
        if (janelaAberta != null) {
            if (janelaAberta.getClass() == novaJanela.getClass()) {
                return; // Já está aberta
            }
            janelaAberta.dispose(); // Fecha a anterior
        }
        janelaAberta = novaJanela;
        janelaAberta.setVisible(true);
    }

    private void abrirTelaCadastro() {
        abrirNovaJanela(new TelaCadastro());
    }

    private void abrirTelaAgenda() {
        abrirNovaJanela(new TelaAgenda());
    }

    private void abrirTelaClientes() {
        abrirNovaJanela(new TelaClientes());
    }

    private void abrirTelaCaixaPDV() {
        abrirNovaJanela(new TelaCaixaPDV());
    }

    private void abrirTelaRelatorios() {
        abrirNovaJanela(new TelaRelatorios());
    }

    private void abrirTelaInicial() {
        atualizarResumo(); 
    }

    public static void main(String[] args) {
        BancoInicializador.inicializarBanco();
        SwingUtilities.invokeLater(TelaInicial::new);
    }
}
