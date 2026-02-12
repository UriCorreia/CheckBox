package br.com.uriel.view;

import br.com.uriel.dao.TasksDAO;
import br.com.uriel.model.Tasks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class Index extends JFrame {

    private JPanel painelTarefas;
    private JTextField campoNovaTarefa;
    private Point pClick;

    public Index() {
        setTitle("CheckBox Widget");
        setSize(350, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setUndecorated(true);
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);

        getContentPane().setBackground(new Color(45, 52, 54, 240));
        setLayout(new BorderLayout());

        JLabel labelTitulo = new JLabel("Tarefas Pendentes", SwingConstants.CENTER);
        labelTitulo.setForeground(Color.WHITE);
        labelTitulo.setFont(new Font("SansSerif", Font.BOLD, 18));
        labelTitulo.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        labelTitulo.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) { pClick = e.getPoint(); }
        });
        labelTitulo.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point curr = e.getLocationOnScreen();
                setLocation(curr.x - pClick.x, curr.y - pClick.y);
            }
        });

        labelTitulo.setToolTipText("Botão direito para sair");
        labelTitulo.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) System.exit(0);
            }
        });

        add(labelTitulo, BorderLayout.NORTH);

        painelTarefas = new JPanel();
        painelTarefas.setLayout(new BoxLayout(painelTarefas, BoxLayout.Y_AXIS));
        painelTarefas.setOpaque(false);

        JScrollPane scrollPane = new JScrollPane(painelTarefas);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane, BorderLayout.CENTER);

        campoNovaTarefa = new JTextField();
        campoNovaTarefa.setBackground(new Color(60, 63, 65));
        campoNovaTarefa.setForeground(Color.WHITE);
        campoNovaTarefa.setCaretColor(Color.WHITE);
        campoNovaTarefa.setFont(new Font("SansSerif", Font.PLAIN, 14));
        campoNovaTarefa.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.DARK_GRAY),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        campoNovaTarefa.addActionListener(e -> {
            String texto = campoNovaTarefa.getText().trim();
            if (!texto.isEmpty()) {
                Tasks novaTarefa = new Tasks();
                novaTarefa.setTitulo(texto);
                novaTarefa.setPrioridade(1);
                novaTarefa.setConcluida(false);

                TasksDAO dao = new TasksDAO();
                dao.save(novaTarefa);

                campoNovaTarefa.setText("");
                renderizarTarefas(); // Atualiza a lista na tela
            }
        });

        add(campoNovaTarefa, BorderLayout.SOUTH);

        renderizarTarefas();
    }

    public void renderizarTarefas() {
        painelTarefas.removeAll();

        TasksDAO dao = new TasksDAO();
        List<Tasks> lista = dao.listAll();

        for (Tasks t : lista) {
            JCheckBox cb = new JCheckBox(t.getTitulo());
            cb.setSelected(t.isConcluida());
            cb.setForeground(Color.WHITE);
            cb.setOpaque(false);
            cb.setFont(new Font("SansSerif", Font.PLAIN, 15));
            cb.setFocusable(false);
            cb.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));

            cb.addActionListener(e -> {
                t.setConcluida(cb.isSelected());
                new TasksDAO().update(t);
                System.out.println("Status atualizado: " + t.getTitulo());
            });

            painelTarefas.add(cb);
        }

        painelTarefas.revalidate();
        painelTarefas.repaint();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { e.printStackTrace(); }

        SwingUtilities.invokeLater(() -> new Index().setVisible(true));
    }
}