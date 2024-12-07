package mvc.model;

import java.awt.*;

public abstract class AbstractTetromino implements Movable {
    protected static final int DIM = 4; // Grid dimensions
    protected boolean[][][] mColoredSquares; // Rotation states
    protected Color mColor;

    protected AbstractTetromino() {
        initialize(); // Force subclasses to initialize their configurations
    }

    public Color getColor() {
        return mColor;
    }

    protected abstract void initialize();

    @Override
    public void moveLeft() {
        // Logic for moving left
    }

    @Override
    public void moveRight() {
        // Logic for moving right
    }

    @Override
    public void moveDown() {
        // Logic for moving down
    }

    @Override
    public void rotate() {
        // Logic for rotation
    }

    public boolean[][] getCurrentState(int rotationIndex) {
        return mColoredSquares[rotationIndex];
    }
}
