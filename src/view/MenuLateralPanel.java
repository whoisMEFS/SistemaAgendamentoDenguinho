package view;

import javax.swing.*;
import java.awt.*;

public class MenuLateralPanel extends JPanel {
    public MenuLateralPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(new Color(44, 62, 80));
        setPreferredSize(new Dimension(180, 0));

        JLabel logoLabel = new JLabel();
        try {
            ImageIcon logo = new ImageIcon(getClass().getResource("/Imagens/denguinho.png"));
            logoLabel.setIcon(new ImageIcon(logo.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH)));
        } catch (Exception e) {
            logoLabel.setText("Denguinho");
        }
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(20));
        add(logoLabel);
        add(Box.createVerticalStrut(10));

        add(menuButton("Agenda", new Color(255, 140, 0), () -> new TelaAgenda().setVisible(true)));
        add(menuButton("Cadastro", new Color(41, 128, 185), () -> new TelaCadastro().setVisible(true)));
        add(menuButton("Caixa", new Color(53, 167, 110), () -> new TelaCaixaPDV().setVisible(true)));
        add(menuButton("Cliente", new Color(150, 65, 165), () -> new TelaClientes().setVisible(true)));
        add(menuButton("Relatórios", new Color(243, 156, 18), () -> new TelaRelatorios().setVisible(true)));
        add(menuButton("Menu Inicial", new Color(53, 167, 110), () -> new TelaInicial().setVisible(true)));
        add(Box.createVerticalGlue());
    }

    private JButton menuButton(String texto, Color cor, Runnable acao) {
        JButton btn = new JButton(texto);
        btn.setPreferredSize(new Dimension(150, 48));
        btn.setMaximumSize(btn.getPreferredSize());
        btn.setFont(new Font("Arial", Font.BOLD, 17));
        btn.setBackground(cor);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.addActionListener(e -> acao.run());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        
        btn.addActionListener(e -> {
        // Abre a nova tela
        acao.run();

        // Fecha a tela atual, se apropriado
        Window janelaAtual = SwingUtilities.getWindowAncestor(this);
        if (janelaAtual instanceof JFrame) {
            String titulo = ((JFrame) janelaAtual).getTitle();

            
            if (!texto.equals("Agenda")) {
                janelaAtual.dispose();
            }
        }
    });
        return btn;
    }
}