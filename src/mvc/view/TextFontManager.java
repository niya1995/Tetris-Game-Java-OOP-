package mvc.view;

import java.awt.*;

public class TextFontManager {
    private Font fnt;
    private FontMetrics fmt;

    public TextFontManager(Font defaultFont, Font largeFont) {
        this.fnt = defaultFont;
    }

    // Initialize font metrics (called with Graphics context)
    public void initFontMetrics(Graphics g) {
        g.setFont(fnt);
        this.fmt = g.getFontMetrics();
    }

    // Draw score on the screen
    public void drawScore(Graphics g, int score, int highScore, int nFontWidth, int nFontHeight) {
        g.setColor(Color.white);
        g.setFont(fnt);

        int adjustedY = nFontHeight + 120;

        String scoreText = "SCORE : " + score + "    HIGH SCORE : " + highScore;
        g.drawString(scoreText, nFontWidth, adjustedY);
    }

    // General method to draw text on the screen
    private void drawText(Graphics2D g2d, Dimension d, String[] text, int startY) {
        int lineHeight = fmt.getHeight() + 20; // Add extra spacing between lines
        int y = startY;

        for (String line : text) {
            int x = (d.width - fmt.stringWidth(line)) / 2;
            g2d.drawString(line, x, y);
            y += lineHeight; // Move to the next line
        }
    }

    // Display generic text block
    private void displayTextBlock(Graphics2D g2d, Dimension d, String[] text, int startY) {
        g2d.setColor(Color.white);
        g2d.setFont(fnt);
        drawText(g2d, d, text, startY);
    }

    // Display game-over text
    public void displayGameOverText(Graphics2D g2d, Dimension d) {
        String[] gameOverText = {
            "GAME OVER",
            "use the RIGHT and LEFT arrow keys to move the pieces horizontally",
            "use the UP arrow bar to rotate the piece",
            "'Space' to Start",
            "'P' to Pause",
            "'Q' to Quit",
            "'M' to Mute or Play Music"
        };

        // Display the text block
        displayTextBlock(g2d, d, gameOverText, d.height / 4);
    }

    // Display intro screen text
    public void displayStartText(Graphics2D g2d, Dimension d) {
        String[] instructions = {
            "TETRIS",
            "use the RIGHT and LEFT arrow keys to move the pieces horizontally",
            "use the UP arrow bar to rotate the piece",
            "black squares are bombs and will clear the board",
            "'Space' to Start",
            "'P' to Pause",
            "'Q' to Quit",
            "'M' to Mute or Play Music"
        };

        // Display the text block
        displayTextBlock(g2d, d, instructions, d.height / 4);
    }

    // Display loading sound text
    public void displayLoadingSoundText(Graphics2D g2d, Dimension d) {
        String[] loadingSoundText = { "Loading sound......" };

        // Display the text block
        displayTextBlock(g2d, d, loadingSoundText, d.height / 2);
    }

    // Display paused screen text
    public void displayPausedText(Graphics2D g2d, Dimension d) {
        String[] pausedText = { "Game Paused" };

        // Display the text block
        displayTextBlock(g2d, d, pausedText, d.height / 4);
    }
}
