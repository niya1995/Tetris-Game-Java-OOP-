package mvc.model;

import mvc.controller.Game;

import java.awt.*;


public abstract class Tetromino implements Movable {

    private final static int ORIENTATION = 4;
    private int mRow;
    private int mCol;
    private int mOrientation;
    private Color mColor;
    private boolean[][][] mColoredSquares;


    public Tetromino() {
        mCol = Game.randInstance().nextInt(Grid.getCols() - Grid.getDim());  //by subtracting DIM from Grid.COLS, you ensure that the Tetromino can always fit within the board's width without spilling over the right edge.
        mOrientation = Game.randInstance().nextInt(ORIENTATION);
        mColoredSquares = new boolean[ORIENTATION][Grid.getDim()][Grid.getDim()];    //DIM and ORIENTATION are constan
    }

    public static int getORIENTATION() {
        return ORIENTATION;
    }

    public int getRow() {
        return mRow;
    }

    public void setRow(int nRow) {
        this.mRow = nRow;
    }

    public int getCol() {
        return mCol;
    }

    public void setCol(int nCol) {
        this.mCol = nCol;
    }

    public int getOrientation() {
        return mOrientation;
    }

    public void setOrientation(int orientation) {
        this.mOrientation = orientation;
    }

    public Color getColor() {
        return mColor;
    }

    public void setColor(Color color) {
        this.mColor = color;
    }


    public void setColoredSquares(int i, int x, int y, boolean value) {
        mColoredSquares[i][x][y] = value;
    }


    public void moveDown() {
        mRow++;

    }


    public void moveLeft() {
        mCol--;
    }

    public void moveRight() {
        mCol++;
    }

    public void rotate() {
        if (mOrientation >= 3) {
            mOrientation = 0;
        } else {
            mOrientation++;
        }
    }
    public boolean[][][] getColoredSquares() {
        return mColoredSquares;
    }
    public void setColoredSquares(boolean[][][] coloredSquares) {
        this.mColoredSquares = coloredSquares;
    }
    
    
    

    public boolean[][] getColoredSquares(int nOrientation) {
        if (nOrientation < 0 || nOrientation >= ORIENTATION) {
            throw new IllegalArgumentException("Invalid orientation index: " + nOrientation);
        }
    
        boolean[][] bC = new boolean[Grid.getDim()][Grid.getDim()];
        for (int i = 0; i < Grid.getDim(); i++) {
            for (int j = 0; j < Grid.getDim(); j++) {
                bC[i][j] = mColoredSquares[nOrientation][i][j];
            }
        }
        return bC;
    }

    public abstract void initialize();
    

}
