import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameOfLife extends JFrame {
    private static final long serialVersionUID = -4365934143909541035L;
    private static final Logger log = LoggerFactory.getLogger(GameOfLife.class.getSimpleName());
    private static Color ALIVE = Color.BLACK;
    private static Color DEAD = Color.WHITE;
    private static int ROWS = 30;
    private static int COLS = 50;

    private Label[][] grid = new Label[ROWS][COLS];
    private Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            performStep();
        }
    });

    public GameOfLife() {
        super(GameOfLife.class.getSimpleName());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        final JSlider slider = new JSlider(0, 1000);
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                timer.setDelay(slider.getValue());
            }
        });
        add(slider, BorderLayout.NORTH);
        add(newGrid(), BorderLayout.CENTER);
        add(newGoButton(), BorderLayout.SOUTH);

        pack();
        setVisible(true);
    }

    private JPanel newGrid() {
        log.debug("creating grid");
        JPanel panel = new JPanel(new GridLayout(ROWS, COLS, 1, 1));
        panel.setBackground(Color.BLACK);
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                Label label = new Label();
                label.setPreferredSize(new Dimension(15, 15));
                label.setBackground(DEAD);
                label.addMouseListener(newMouseAdapter(label));
                panel.add(label);
                grid[row][col] = label;
            }
        }
        return panel;
    }

    private JButton newGoButton() {
        final JButton button = new JButton("Go");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (timer.isRunning()) {
                    timer.stop();
                    button.setText("Go");
                } else {
                    timer.start();
                    button.setText("Stop");
                }
            }
        });
        return button;
    }

    private MouseAdapter newMouseAdapter(final Label label) {
        return new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                process(e);
            }

            public void mouseEntered(MouseEvent e) {
                process(e);
            }

            private void process(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    label.setBackground(label.getBackground() == ALIVE ? DEAD : ALIVE);
                }
            }
        };
    }

    private void performStep() {
        log.trace("\ncalculating");
        int[][] neighbours = new int[ROWS][COLS];
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (grid[row][col].getBackground() == ALIVE) {
                    log.trace("[{},{}] is alive", row, col);
                    for (int rowNeighbour = row - 1; rowNeighbour <= row + 1; rowNeighbour++) {
                        for (int colNeighbour = col - 1; colNeighbour <= col + 1; colNeighbour++) {
                            if (rowNeighbour != row || colNeighbour != col) {
                                int r = rowNeighbour < 0 ? ROWS - 1 : (rowNeighbour < ROWS ? rowNeighbour : 0);
                                int c = colNeighbour < 0 ? COLS - 1 : (colNeighbour < COLS ? colNeighbour : 0);
                                neighbours[r][c]++;
                                log.trace("increment neighbour count [{},{}] to {}", new Object[] { r, c,
                                        neighbours[r][c] });
                            }
                        }
                    }
                }
            }
        }
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                int numNeighbors = neighbours[row][col];
                if (grid[row][col].getBackground() == ALIVE) {
                    if (numNeighbors < 2 || numNeighbors > 3) {
                        log.trace("[{},{}] dies", row, col);
                        grid[row][col].setBackground(DEAD);
                    } else {
                        log.trace("[{},{}] survives", row, col);
                    }
                } else {
                    if (numNeighbors == 3) {
                        log.trace("[{},{}] is born", row, col);
                        grid[row][col].setBackground(ALIVE);
                    }
                }
            }
        }
    }

    public static void main(String[] arg) {
        log.debug("starting application");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new GameOfLife();
            }
        });
    }
}
