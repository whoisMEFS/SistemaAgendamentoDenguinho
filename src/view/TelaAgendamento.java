package view;

import dao.AgendamentoDAO;
import dao.ClienteDAO;
import model.Agendamento;
import model.Cliente;
import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.NumberFormatter;
import java.sql.Timestamp;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.awt.Component; 
import java.util.List;
import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;



public class TelaAgendamento extends JFrame {
    private JTextField campoCliente;
    private JPopupMenu sugestoesPopup;

    private JDateChooser dateChooser;
    private JSpinner spinnerHora;
    private JComboBox<String> comboServico;
    private JComboBox<String> comboRaca;
    private JComboBox<String> comboProfissional;
    private JTextField campoDuracao;
    private JFormattedTextField campoValor;
    private JButton btnAgendar;

    private AgendamentoDAO agendamentoDAO;
    private ClienteDAO clienteDAO;
    private List<Cliente> clientes;

    private Agendamento agendamentoExistente = null;

    public TelaAgendamento() {
        super("Novo Agendamento");
        setLayout(null);

        agendamentoDAO = new AgendamentoDAO();
        clienteDAO = new ClienteDAO();

        JLabel lblCliente = new JLabel("Cliente ou Pet:");
        lblCliente.setBounds(10, 10, 100, 25);
        add(lblCliente);

        campoCliente = new JTextField();
        campoCliente.setBounds(120, 10, 200, 25);
        add(campoCliente);

        sugestoesPopup = new JPopupMenu();
        campoCliente.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { mostrarSugestoes(); }
            public void removeUpdate(DocumentEvent e) { mostrarSugestoes(); }
            public void changedUpdate(DocumentEvent e) { mostrarSugestoes(); }
        });

        JLabel lblData = new JLabel("Data:");
        lblData.setBounds(10, 45, 80, 25);
        add(lblData);

        dateChooser = new JDateChooser();
        dateChooser.setBounds(120, 45, 200, 25);
        add(dateChooser);

        JLabel lblHora = new JLabel("Hora:");
        lblHora.setBounds(10, 80, 80, 25);
        add(lblHora);

        spinnerHora = new JSpinner(new SpinnerDateModel());
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(spinnerHora, "HH:mm");
        spinnerHora.setEditor(timeEditor);
        spinnerHora.setBounds(120, 80, 100, 25);
        add(spinnerHora);

        JLabel lblServico = new JLabel("Serviço:");
        lblServico.setBounds(10, 115, 100, 25);
        add(lblServico);

        String[] servicos = {"Banho", "Tosa", "Banho + Higiênica"};
        comboServico = new JComboBox<>(servicos);
        comboServico.setBounds(120, 115, 200, 25);
        add(comboServico);

        JLabel lblRaca = new JLabel("Raça:");
        lblRaca.setBounds(10, 150, 100, 25);
        add(lblRaca);

        String[] racas = {
            "SRD", "Labrador", "Poodle", "Bulldog", "Yorkshire", "Golden Retriever",
            "Shih Tzu", "Beagle", "Pinscher", "Pastor Alemão", "Rottweiler",
            "Persa", "Siamês", "Maine Coon", "Angorá", "Sphynx", "Bengal", "Birmanês"
        };
        comboRaca = new JComboBox<>(racas);
        comboRaca.setBounds(120, 150, 200, 25);
        add(comboRaca);

        JLabel lblProfissional = new JLabel("Profissional:");
        lblProfissional.setBounds(10, 185, 100, 25);
        add(lblProfissional);

        String[] profissionais = {"Marcia", "Bárbarah", "Sem profissional"};
        comboProfissional = new JComboBox<>(profissionais);
        comboProfissional.setBounds(120, 185, 200, 25);
        add(comboProfissional);

        JLabel lblDuracao = new JLabel("Duração (min):");
        lblDuracao.setBounds(10, 220, 100, 25);
        add(lblDuracao);

        campoDuracao = new JTextField();
        campoDuracao.setBounds(120, 220, 100, 25);
        add(campoDuracao);

        JLabel lblValor = new JLabel("Valor (R$):");
        lblValor.setBounds(10, 255, 100, 25);
        add(lblValor);

        NumberFormat formatoMoeda = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        NumberFormatter formatadorMoeda = new NumberFormatter(formatoMoeda);
        formatadorMoeda.setValueClass(Double.class);
        formatadorMoeda.setMinimum(0.0);
        formatadorMoeda.setMaximum(10000.0);
        formatadorMoeda.setAllowsInvalid(false);

        campoValor = new JFormattedTextField(formatadorMoeda);
        campoValor.setBounds(120, 255, 100, 25);
        campoValor.setValue(0.0);
        add(campoValor);

        btnAgendar = new JButton("Agendar");
        btnAgendar.setBounds(120, 295, 120, 30);
        add(btnAgendar);

        btnAgendar.addActionListener(e -> agendar());

        setSize(370, 380);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setVisible(true);

        carregarClientes();
    }

    public TelaAgendamento(Agendamento agendamento) {
        this(); 

        this.agendamentoExistente = agendamento;

        campoCliente.setText(agendamento.getCliente() + " (Pet: " + agendamento.getNomePet() + ")");
        comboRaca.setSelectedItem(agendamento.getRaca());
        dateChooser.setDate(agendamento.getDataHora());
        spinnerHora.setValue(agendamento.getDataHora());
        comboServico.setSelectedItem(agendamento.getServico());
        comboProfissional.setSelectedItem(agendamento.getProfissional());
        campoDuracao.setText(String.valueOf(agendamento.getDuracao()));
        campoValor.setValue(agendamento.getValor());

        btnAgendar.setText("Salvar Alterações");
    }

    public TelaAgendamento(LocalDate dataAtual, LocalTime horaAtual, String profissional) {
        this(); 

        Calendar calendario = Calendar.getInstance();
        calendario.set(Calendar.YEAR, dataAtual.getYear());
        calendario.set(Calendar.MONTH, dataAtual.getMonthValue() - 1);
        calendario.set(Calendar.DAY_OF_MONTH, dataAtual.getDayOfMonth());
        calendario.set(Calendar.HOUR_OF_DAY, horaAtual.getHour());
        calendario.set(Calendar.MINUTE, horaAtual.getMinute());
        calendario.set(Calendar.SECOND, 0);
        calendario.set(Calendar.MILLISECOND, 0);

        Date dataHora = calendario.getTime();

        dateChooser.setDate(dataHora);
        spinnerHora.setValue(dataHora);
        comboProfissional.setSelectedItem(profissional);
    }

    private void carregarClientes() {
        clientes = clienteDAO.listar();
    }

    private void mostrarSugestoes() {
        sugestoesPopup.setVisible(false);
        sugestoesPopup.removeAll();

        String texto = campoCliente.getText().toLowerCase();
        if (texto.length() < 2) return;

        for (Cliente c : clientes) {
            if (c.getNome().toLowerCase().contains(texto) ||
                c.getPet().toLowerCase().contains(texto)) {
                JMenuItem item = new JMenuItem(c.getNome() + " (Pet: " + c.getPet() + ")");
                item.addActionListener(e -> {
                    campoCliente.setText(c.getNome() + " (Pet: " + c.getPet() + ")");
                    sugestoesPopup.setVisible(false);
                });
                sugestoesPopup.add(item);
            }
        }

        if (sugestoesPopup.getComponentCount() > 0) {
            sugestoesPopup.show(campoCliente, 0, campoCliente.getHeight());
        }
    }

    private void agendar() {
        try {
            String entradaCliente = campoCliente.getText().trim();
            String cliente;
            String nomePet;

            if (entradaCliente.contains("(Pet:")) {
                int idx = entradaCliente.indexOf("(Pet:");
                cliente = entradaCliente.substring(0, idx).trim();
                nomePet = entradaCliente.substring(idx + 6, entradaCliente.length() - 1).trim();
            } else {
                cliente = entradaCliente;
                nomePet = "Desconhecido";
            }

            Date data = dateChooser.getDate();
            Date hora = (Date) spinnerHora.getValue();

            if (data == null || hora == null) {
                JOptionPane.showMessageDialog(this, "Selecione uma data e hora válidas.");
                return;
            }

            Calendar calendario = Calendar.getInstance();
            calendario.setTime(data);
            Calendar horaSelecionada = Calendar.getInstance();
            horaSelecionada.setTime(hora);
            calendario.set(Calendar.HOUR_OF_DAY, horaSelecionada.get(Calendar.HOUR_OF_DAY));
            calendario.set(Calendar.MINUTE, horaSelecionada.get(Calendar.MINUTE));
            calendario.set(Calendar.SECOND, 0);
            calendario.set(Calendar.MILLISECOND, 0);

            Timestamp dataHora = new Timestamp(calendario.getTimeInMillis());

            String servico = (String) comboServico.getSelectedItem();
            String profissional = (String) comboProfissional.getSelectedItem();
            int duracao = Integer.parseInt(campoDuracao.getText());

            double valor = ((Number) campoValor.getValue()).doubleValue();
            String raca = (String) comboRaca.getSelectedItem();

            if (agendamentoExistente != null) {
                agendamentoExistente.setCliente(cliente);
                agendamentoExistente.setNomePet(nomePet);
                agendamentoExistente.setRaca(raca);
                agendamentoExistente.setDataHora(dataHora);
                agendamentoExistente.setServico(servico);
                agendamentoExistente.setDuracao(duracao);
                agendamentoExistente.setProfissional(profissional);
                agendamentoExistente.setValor(valor);

                agendamentoDAO.atualizar(agendamentoExistente);
                JOptionPane.showMessageDialog(this, "Agendamento atualizado com sucesso!");
            } else {
                Agendamento ag = new Agendamento(cliente, nomePet, raca, dataHora, servico, duracao, profissional, valor);
                agendamentoDAO.salvar(ag);
                JOptionPane.showMessageDialog(this, "Agendamento salvo com sucesso!");
            }

            new TelaAgenda().setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TelaAgendamento::new);
    }
}
