package br.com.uriel.view;

import br.com.uriel.dao.TasksDAO;
import br.com.uriel.model.Tasks;
import com.formdev.flatlaf.FlatDarkLaf;

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
        setBackground(new Color(0, 0, 0, 0));
        setAlwaysOnTop(true);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        JPanel glassPanel = new JPanel(new BorderLayout());
        glassPanel.setBackground(new Color(45, 52, 54, 210));
        setContentPane(glassPanel);

        JPanel barraTitulo = new JPanel(new BorderLayout());
        barraTitulo.setOpaque(false);
        barraTitulo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 10));

        JLabel titulo = new JLabel("Tarefas Pendentes");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 16));

        JLabel botaoFechar = new JLabel("✕");
        botaoFechar.setForeground(new Color(220, 220, 220));
        botaoFechar.setFont(new Font("SansSerif", Font.BOLD, 16));
        botaoFechar.setCursor(new Cursor(Cursor.HAND_CURSOR));

        Color normal = new Color(220, 220, 220);
        Color hover = new Color(255, 100, 100);

        botaoFechar.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                botaoFechar.setForeground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                botaoFechar.setForeground(normal);
            }

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                System.exit(0);
            }
        });

        barraTitulo.add(titulo, BorderLayout.WEST);
        barraTitulo.add(botaoFechar, BorderLayout.EAST);

        add(barraTitulo, BorderLayout.NORTH);

        barraTitulo.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                pClick = e.getPoint();
            }
        });

        barraTitulo.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point curr = e.getLocationOnScreen();
                setLocation(curr.x - pClick.x, curr.y - pClick.y);
            }
        });

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
        campoNovaTarefa.setFont(new Font("Segoe UI", Font.PLAIN, 15));
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
                renderizarTarefas();
            }
        });

        add(campoNovaTarefa, BorderLayout.SOUTH);

        renderizarTarefas();

        setShape(new java.awt.geom.RoundRectangle2D.Double(
                0, 0, getWidth(), getHeight(), 30, 30));

        addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent e) {
                setShape(new java.awt.geom.RoundRectangle2D.Double(
                        0, 0, getWidth(), getHeight(), 30, 30));
            }
        });
    }

    public void renderizarTarefas() {
        painelTarefas.removeAll();

        TasksDAO dao = new TasksDAO();
        List<Tasks> lista = dao.listAll();

        for (Tasks t : lista) {

            JPanel card = new JPanel(new BorderLayout());

            Color normal = new Color(255, 255, 255, 30);
            Color hover = new Color(45, 52, 54, 210);

            card.setBackground(normal);
            card.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
            card.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            card.setOpaque(true);

            JCheckBox cb = new JCheckBox(t.getTitulo());
            cb.setSelected(t.isConcluida());
            cb.setForeground(Color.WHITE);
            cb.setOpaque(false);
            cb.setFont(new Font("Segoe UI", Font.PLAIN, 15));
            cb.setFocusable(false);

            cb.addActionListener(e -> {

                TasksDAO DAO = new TasksDAO();

                if (cb.isSelected()) {

                    t.setConcluida(true);
                    DAO.update(t);

                    cb.setText("<html><strike>" + t.getTitulo() + "</strike></html>");

                    Timer timer = new Timer(5000, ev -> {
                        DAO.delete(t.getId());
                        renderizarTarefas();
                    });

                    timer.setRepeats(false);
                    timer.start();

                    cb.putClientProperty("deleteTimer", timer);

                } else {

                    t.setConcluida(false);
                    DAO.update(t);

                    cb.setText(t.getTitulo());

                    Timer timer = (Timer) cb.getClientProperty("deleteTimer");
                    if (timer != null) {
                        timer.stop();
                    }
                }
            });


            card.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    card.setBackground(hover);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    card.setBackground(normal);
                }

                @Override
                public void mouseClicked(java.awt.event.MouseEvent e) {
                    cb.setSelected(!cb.isSelected());
                    t.setConcluida(cb.isSelected());
                    new TasksDAO().update(t);
                }
            });

            cb.addMouseListener(new java.awt.event.MouseAdapter() {
                @Override
                public void mouseEntered(java.awt.event.MouseEvent e) {
                    card.setBackground(hover);
                }

                @Override
                public void mouseExited(java.awt.event.MouseEvent e) {
                    card.setBackground(normal);
                }
            });

            card.add(cb, BorderLayout.CENTER);

            painelTarefas.add(card);
            painelTarefas.add(Box.createVerticalStrut(8));
        }

        painelTarefas.revalidate();
        painelTarefas.repaint();
    }

    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel(new FlatDarkLaf());
        } catch (Exception e) {
            System.out.println("Erro ao iniciar tema");
        }

        SwingUtilities.invokeLater(() -> new Index().setVisible(true));
    }
}
