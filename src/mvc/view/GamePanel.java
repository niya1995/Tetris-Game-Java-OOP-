package mvc.view;

import mvc.model.*;

import java.awt.*;
import javax.swing.*;

public class GamePanel extends JPanel {

    // FIELDS
    private Grid grid = new Grid();
    private GameFrame gmf;
    private TextFontManager textManager;
    public Tetromino tetrOnDeck;
    private static GamePanel instance = null;

    private FontMetrics fmt;
    private int nFontWidth;
    private int nFontHeight;

    // CONSTRUCTOR
    private GamePanel() {
        gmf = new GameFrame();
        gmf.getContentPane().add(this);

        // Initialize TextFrontManager
        textManager = new TextFontManager();
        initView();

        this.setFocusable(true);
        this.requestFocusInWindow();
    }

    public static GamePanel getInstance() {
        if (instance == null) {
            instance = new GamePanel();
        }
        return instance;
    }

    // ==============================================================

    // METHODS
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = this.getSize();
        Graphics2D g2d = (Graphics2D) g;

        // Initialize font metrics
        textManager.initFontMetrics(g);

        // Draw background
        g2d.setColor(Color.blue);
        g2d.fillRect(0, 0, d.width, d.height);

        // Handle game states
        if (CommandCenter.getInstance().isGameOver()) {
            textManager.displayGameOverText(g2d, d);
        } else if (!CommandCenter.getInstance().isPlaying()) {          
            if (!CommandCenter.getInstance().isLoaded()) {
                textManager.displayLoadingSoundText(g2d, d);
            }else {
                textManager.displayStartText(g2d, d);
            }
            
        } else if (CommandCenter.getInstance().isPaused()) {
            textManager.displayPausedText(g2d, d);
        } else {
            drawGamePlaying(g2d, d);
        }
    }

    public Grid getGridObj(){
        return grid;
    }

    private void drawGamePlaying(Graphics2D g2d, Dimension d) {
        int nBy = (d.height - 150) / Grid.getRows();
        int nBx = (d.width - 150) / Grid.getCols();

        Block[][] b = grid.getBlocks();
        for (int i = 0; i < b.length; i++) {
            for (int j = 0; j < b[0].length; j++) {
                g2d.setColor(b[i][j].getColor());
                g2d.fill3DRect(j * nBx, i * nBy + 150, nBx, nBy, true);
            }
        }

        // Draw Tetromino
        drawTetromino(g2d, nBx, nBy, d);

        // Draw score
        textManager.drawScore(g2d, (int) CommandCenter.getInstance().getScore(), (int)CommandCenter.getInstance().getHighScore(), nFontWidth, nFontHeight);
    }

    private void drawTetromino(Graphics2D g2d, int nBx, int nBy, Dimension d) {
        boolean[][] lts = tetrOnDeck.getColoredSquares(tetrOnDeck.getOrientation());
        Color c = tetrOnDeck.getColor();
        for (int i = 0; i < Grid.getDim(); i++) {
            for (int j = 0; j < Grid.getDim(); j++) {
                if (lts[j][i]) {
                    g2d.setColor(c);
                    g2d.fill3DRect(i * nBx + 360, j * nBy + 150, nBx, nBy, true);
                }
            }
        }
    }

    private void initView() {
        Graphics g = getGraphics();
        g.setFont(textManager.fnt);
        fmt = g.getFontMetrics();
        nFontWidth = fmt.getMaxAdvance();
        nFontHeight = fmt.getHeight();
    }
}
