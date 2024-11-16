import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Path2D;

public class HexGridApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Hex Grid");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(800, 600);
            frame.setResizable(false);
            frame.add(new HexGridPanel());
            frame.setVisible(true);
        });
    }
}

class HexGridPanel extends JPanel {
    private final int hexSize = 100;
    private final int rows = 6;
    private final int cols = 9;
    private final Color[][] hexColors;

    public HexGridPanel() {
        hexColors = new Color[rows][cols];
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                hexColors[row][col] = Color.BLUE;
            }
        }

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickedPoint = e.getPoint();
                for (int row = 0; row < rows; row++) {
                    for (int col = 0; col < cols; col++) {
                        if (isInsideHex(clickedPoint, row, col)) {
                            hexColors[row][col] = 
                                hexColors[row][col] == Color.BLUE ? Color.WHITE : Color.BLUE;
                            repaint();
                            return;
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                drawHex(g2d, row, col, hexColors[row][col]);
            }
        }
    }

    private void drawHex(Graphics2D g2d, int row, int col, Color color) {
        double xOffset = col * (1.5 * hexSize);
        double yOffset = row * (Math.sqrt(3) * hexSize) + ((col % 2) * (Math.sqrt(3) / 2 * hexSize));
        Path2D hex = createHex(xOffset, yOffset);

        g2d.setColor(color);
        g2d.fill(hex);
        g2d.setColor(Color.BLACK);
        g2d.draw(hex);
    }

    private Path2D createHex(double xOffset, double yOffset) {
        Path2D hex = new Path2D.Double();
        for (int i = 0; i < 6; i++) {
            double angle = Math.PI / 3 * i;
            double x = xOffset + hexSize * Math.cos(angle);
            double y = yOffset + hexSize * Math.sin(angle);
            if (i == 0) {
                hex.moveTo(x, y);
            } else {
                hex.lineTo(x, y);
            }
        }
        hex.closePath();
        return hex;
    }

    private boolean isInsideHex(Point point, int row, int col) {
        double xOffset = col * (1.5 * hexSize);
        double yOffset = row * (Math.sqrt(3) * hexSize) + ((col % 2) * (Math.sqrt(3) / 2 * hexSize));
        Path2D hex = createHex(xOffset, yOffset);
        return hex.contains(point);
    }
}
