package mvc.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class GameFrame extends JFrame {

    private static final Dimension DIM = new Dimension(500, 800);

    private JPanel contentPane;
    private BorderLayout borderLayout1 = new BorderLayout();

    public GameFrame() {
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try {
            setupWindow(DIM);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Component initialization
    private void setupWindow(Dimension dimension) throws Exception {
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(borderLayout1);

        this.setSize(dimension);  // Dynamically set the size based on passed dimension
        this.setLocationRelativeTo(null);  // Center window
        this.setTitle("Tetris");
        this.setResizable(false);  // Fix window size
        this.setVisible(true);  // Make the window visible
    }

    @Override
    // Overridden to exit the program when the window is closed
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            System.exit(0);
        }
    }
}
