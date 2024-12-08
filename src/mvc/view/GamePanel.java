package mvc.view;

import mvc.model.*;

import java.awt.*;
import javax.swing.*;


public class GamePanel extends JPanel {

    // ==============================================================
    // FIELDS
    // ==============================================================

    // The following "off" vars are used for the off-screen double-bufferred image.
    public Grid grid = new Grid();
    private GameFrame gmf;
    private Font fnt = new Font("SansSerif", Font.BOLD, 12);
    private Font fntBig = new Font("SansSerif", Font.BOLD + Font.ITALIC, 36);
    private FontMetrics fmt;
    private int nFontWidth;
    private int nFontHeight;
    private String strDisplay = "";
    public Tetromino tetrOnDeck;
    public Tetromino tetrCurrent;
    private static GamePanel instance = null;


    // ==============================================================
    // CONSTRUCTOR
    // ==============================================================

    private GamePanel(Dimension dim) {
        gmf = new GameFrame();
        gmf.getContentPane().add(this);
        gmf.pack();
        initView();

        gmf.setSize(dim);
        gmf.setTitle("Tetris");
        gmf.setResizable(false);
        gmf.setVisible(true);
        this.setFocusable(true);
        this.requestFocusInWindow();
    }
    public static GamePanel getInstance(Dimension dim) {
        if (instance == null) {
            instance = new GamePanel(dim); // Create the instance with the dimension parameter
        }
        return instance;
    }


    // ==============================================================
    // METHODS
    // ==============================================================

    private void drawScore(Graphics g) {
        g.setColor(Color.white);
        g.setFont(fnt);

        int adjustedY = nFontHeight + 120;

        if (CommandCenter.getInstance().getScore() != 0) {
            g.drawString("SCORE :  " + CommandCenter.getInstance().getScore() + "    HIGH SCORE : " + CommandCenter.getInstance().getHighScore(), nFontWidth, adjustedY);
        } else {
            g.drawString("SCORE : 0 " + "   HIGH SCORE : " + CommandCenter.getInstance().getHighScore(), nFontWidth, adjustedY);
        }
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Dimension d = this.getSize();
        
        // Get Graphics2D object for better control and performance
        Graphics2D g2d = (Graphics2D) g;
        
        // Fill the background with blue
        g2d.setColor(Color.blue);
        g2d.fillRect(0, 0, d.width, d.height);
        
        // Set font and color
        g2d.setColor(Color.white);
        g2d.setFont(fnt);
    
        // Use FontMetrics for centering text
        FontMetrics fmt = g2d.getFontMetrics(fnt);
    
        // Handle game over state
        if (CommandCenter.getInstance().isGameOver()) {
            displayTextOnScreen(g2d, fmt, d);
        }
        // Handle game not started yet
        else if (!CommandCenter.getInstance().isPlaying()) {
            g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 2);
            
            if (!CommandCenter.getInstance().isLoaded()) {
                strDisplay = "Loading sounds... ";
                g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4);
            } else {
                displayStartText(g2d, fmt, d);
            }
        }
        // Handle paused state
        else if (CommandCenter.getInstance().isPaused()) {
            strDisplay = "Game Paused";
            g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4);
        }
        // Playing and not paused
        else {
            int nBy = (d.height - 150) / Grid.ROWS;
            int nBx = (d.width - 150) / Grid.COLS;
            Block[][] b = grid.getBlocks();
    
            // Draw the grid blocks
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b[0].length; j++) {
                    g2d.setColor(b[i][j].getColor());
                    g2d.fill3DRect(j * nBx, i * nBy + 150, nBx, nBy, true);
                }
            }
    
            // Draw the border around the game area
            g2d.setColor(Color.white);
            g2d.draw3DRect(d.width - 150, 0, 150, d.height, true);
            g2d.draw3DRect(d.width - 140, 10 + 6 * nBy, 130, 130, true);
    
            // Draw the current Tetromino (tetrOnDeck)
            boolean[][] lts = tetrOnDeck.getColoredSquares(tetrOnDeck.getOrientation());
            Color c = tetrOnDeck.getColor();
            int rowOffset = 6 * nBy;  // Adjust the offset for proper positioning
            for (int i = 0; i < Grid.DIM; i++) {
                for (int j = 0; j < Grid.DIM; j++) {
                    if (lts[j][i]) {
                        g2d.setColor(c);
                        g2d.fill3DRect(i * nBx + 360, (j * nBy + 20) + rowOffset, nBx, nBy, true);
                    }
                }
            }
    
            // Draw the score
            drawScore(g2d);
        }
    }
    


    private void initView() {
        Graphics g = getGraphics();            // get the graphics context for the panel
        g.setFont(fnt);                        // take care of some simple font stuff
        fmt = g.getFontMetrics();
        nFontWidth = fmt.getMaxAdvance();
        nFontHeight = fmt.getHeight();
        g.setFont(fntBig);                    // set font info
    }

    // This method draws some text to the middle of the screen before/after a game
    // Refactor the method to accept Graphics2D, FontMetrics, and Dimension parameters
private void displayStartText(Graphics2D g2d, FontMetrics fmt, Dimension d) {
    String strDisplay;
    int nFontHeight = fmt.getHeight();  // Get font height to correctly offset text lines

    // Draw "TETRIS" centered at the top of the screen
    strDisplay = "TETRIS";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4);

    // Draw instructions for controls, each positioned below the previous one
    strDisplay = "use the RIGHT and LEFT arrow keys to move the pieces horizontally";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 40);

    strDisplay = "use the UP arrow bar to rotate the piece";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 80);

    strDisplay = "black squares are bombs and will clear the board";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 120);

    strDisplay = "'Space' to Start";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 160);

    strDisplay = "'P' to Pause";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 200);

    strDisplay = "'Q' to Quit";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 240);

    strDisplay = "'M' to Mute or Play Music";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 280);
}


    // In your class, define the method with parameters:
private void displayTextOnScreen(Graphics2D g2d, FontMetrics fmt, Dimension d) {
    String strDisplay;
    int nFontHeight = fmt.getHeight();  // Get font height to correctly offset text lines

    // Draw the "GAME OVER" message centered on the screen
    strDisplay = "GAME OVER";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4);

    // Draw instructions for controls, each positioned below the previous one
    strDisplay = "use the RIGHT and LEFT arrow keys to move the pieces horizontally";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 40);

    strDisplay = "use the UP arrow bar to rotate the piece";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 80);

    strDisplay = "'Space' to Start";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 120);

    strDisplay = "'P' to Pause";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 160);

    strDisplay = "'Q' to Quit";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 200);

    strDisplay = "'M' to Mute or Play Music";
    g2d.drawString(strDisplay, (d.width - fmt.stringWidth(strDisplay)) / 2, d.height / 4 + nFontHeight + 240);
}


    public GameFrame getFrm() {
        return this.gmf;
    }

    public void setFrm(GameFrame frm) {
        this.gmf = frm;
    }
}