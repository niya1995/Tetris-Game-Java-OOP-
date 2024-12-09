package mvc.model;

import mvc.controller.Game;

import java.awt.*;


public class Tetromino extends Movable {

    public final static int ORIENTATION = 4;
    public final static int DIM = 4;
    public int mRow;
    public int mCol;
    public int mOrientation;
    public Color mColor;
    public boolean[][][] mColoredSquares;


    public Tetromino() {
        mCol = Game.R.nextInt(Grid.COLS - DIM);  //by subtracting DIM from Grid.COLS, you ensure that the Tetromino can always fit within the board's width without spilling over the right edge.
        mOrientation = Game.R.nextInt(ORIENTATION);
        mColoredSquares = new boolean[ORIENTATION][DIM][DIM];    //DIM and ORIENTATION are constand
    }

    public static int getORIENTATION() {
        return ORIENTATION;
    }

    public static int getDIM() {
        return DIM;
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

    public boolean[][][] getColoredSquares() {
        return mColoredSquares;
    }

    public void setColoredSquares(boolean[][][] coloredSquares) {
        this.mColoredSquares = coloredSquares;
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

    public boolean[][] getColoredSquares(int nOrientation) {
        boolean[][] bC = new boolean[DIM][DIM];
        for (int i = 0; i < DIM; i++) {
            for (int j = 0; j < DIM; j++) {
                bC[i][j] = mColoredSquares[nOrientation][i][j];
            }

        }
        return bC;

    }

    public Tetromino cloneTetromino() {
        Tetromino tetr = new Tetromino();
        tetr.mRow = mRow;
        tetr.mCol = mCol;
        tetr.mOrientation = mOrientation;
        tetr.mColor = mColor;
        tetr.mColoredSquares = mColoredSquares;

        return tetr;
    }

}
