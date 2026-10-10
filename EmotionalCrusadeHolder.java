// RECOVERY STUB (2026): original launcher class was not in surviving source.
// This is a minimal replacement to make GameHolder.java compilable.
// Uses the recovered original title and kingdom artwork.
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class EmotionalCrusadeHolder {
    public Image getImage(String name) {
        try {
            File file = new File(name);
            if (file.isFile()) return ImageIO.read(file);
        } catch (Exception e) {
            System.err.println("Unable to load " + name + ": " + e.getMessage());
        }
        // Visible neutral placeholder; not the game's original artwork.
        BufferedImage placeholder = new BufferedImage(1360, 720, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = placeholder.createGraphics();
        g.setColor(new Color(65, 65, 65));
        g.fillRect(0, 0, 1360, 720);
        g.setColor(Color.WHITE);
        g.drawString("Missing original image: " + name, 40, 40);
        g.dispose();
        return placeholder;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (!new File("passages.txt").isFile()) {
                JOptionPane.showMessageDialog(null,
                    "Original passages.txt is missing. The menu may appear, but question-based gameplay will not work.\n" +
                    "This recovered project is source-only until the original data is restored.",
                    "Incomplete Recovery", JOptionPane.WARNING_MESSAGE);
            }
            JFrame frame = new JFrame("Emotional Crusade - Recovered Source Preview");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            EmotionalCrusadeHolder holder = new EmotionalCrusadeHolder();
            CardLayout screens = new CardLayout();
            JPanel root = new JPanel(screens);
            Image titleImage = holder.getImage("ech_start_panel.png");
            JPanel menu = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(titleImage, 0, 0, getWidth(), getHeight(), this);
                }
            };
            JButton play = new JButton("Play");
            play.setFont(new Font("Serif", Font.BOLD, 28));
            play.addActionListener(event -> {
                root.add(new GameHolder(holder), "Game");
                screens.show(root, "Game");
            });
            JPanel buttons = new JPanel();
            buttons.setOpaque(false);
            buttons.add(play);
            menu.add(buttons, BorderLayout.SOUTH);
            root.add(menu, "Menu");
            frame.setContentPane(root);
            frame.setSize(1360, 800);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}
