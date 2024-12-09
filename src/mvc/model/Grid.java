package mvc.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;


public class Grid {

    public static final int ROWS = 20;
    public static final int COLS = 10;
    public static final int DIM = 4;

    Block[][] mBlock;

    ArrayList<Block> mOccupiedBlocks;

    public Grid() {
        mBlock = new Block[ROWS][COLS];
        initializeBlocks();
        mOccupiedBlocks = new ArrayList<Block>();
    }

    public Block[][] getBlocks() {
        return mBlock;
    }

    synchronized public void initializeBlocks() {
//paints board with blue blocks
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                mBlock[i][j] = new Block(false, Color.blue, i, j);
            }
        }
    }


    synchronized public boolean requestDown(Tetromino tetr) {
        boolean[][] bC;
        bC = tetr.getColoredSquares(tetr.getOrientation());
        for (int i = tetr.getCol(); i < tetr.getCol()  + DIM; i++) {
            for (int j = tetr.getRow(); j < tetr.getRow() + DIM; j++) {
//                if goes out of bounds
                if (bC[j - tetr.getRow()][i - tetr.getCol()]) {
                    if (j >= Grid.ROWS || i < 0 || i >= Grid.COLS || mBlock[j][i].isOccupied()) {
                        return false;
                    }
                }

            }

        }
//        ok to move down
        return true;
    }

    synchronized public void addToOccupied(Tetromino tetr) {
        boolean[][] bC;
        bC = tetr.getColoredSquares(tetr.getOrientation());
        Color color = tetr.getColor();
        for (int i = tetr.getCol() ; i < tetr.getCol()  + DIM; i++) {
            for (int j = tetr.getRow(); j < tetr.getRow() + DIM; j++) {
                if (bC[j - tetr.getRow()][i - tetr.getCol() ]) {
                    mOccupiedBlocks.add(new Block(true, color, j, i));
                }

            }

        }
    }

    synchronized public void checkTopRow() {
        for (Object mOccupiedBlock : mOccupiedBlocks) {
            Block block = (Block) mOccupiedBlock;
            if (block.getRow() <= 0) {
//                game ends and clear board
                CommandCenter.getInstance().setPlaying(false);
                CommandCenter.getInstance().setGameOver(true);
                clearGrid();
            }

        }
    }

    synchronized public void clearGrid() {
        initializeBlocks();
        mOccupiedBlocks.clear();
    }


    synchronized public void checkCompletedRow() {
        boolean rowCleared; // To track if any row was cleared in the current iteration.
        do {
            rowCleared = false; // Reset flag at the start of each iteration.
            LinkedList<Block> fullRowItems = new LinkedList<Block>();
            LinkedList<Block> repositioningItems = new LinkedList<Block>();
    
            int nRows = Grid.ROWS - 1; // Start from the bottom row.
            while (nRows >= 0) {
                // Check if the row is full
                for (int i = mOccupiedBlocks.size() - 1; i >= 0; i--) {
                    Block block = mOccupiedBlocks.get(i);
                    if (block.getRow() == nRows) {
                        fullRowItems.add(block);
                    }
                }
    
                // If the row is full, clear it
                if (fullRowItems.size() == Grid.COLS) {
                    rowCleared = true; // Indicate that a row was cleared.
    
                    // Remove the blocks from the full row
                    while (fullRowItems.size() > 0) {
                        Block blck = fullRowItems.removeFirst();
                        mOccupiedBlocks.remove(blck);
                        CommandCenter.getInstance().addScore(blck.getPoints());
                    }
    
                    // Update high score if necessary
                    if (CommandCenter.getInstance().getScore() > CommandCenter.getInstance().getHighScore()) {
                        CommandCenter.getInstance().setHighScore(CommandCenter.getInstance().getScore());
                    }
    
                    // Check for difficulty increase if needed
                    CommandCenter.getInstance().checkThreshold();
    
                    // Move blocks above the cleared row down
                    for (int j = mOccupiedBlocks.size() - 1; j >= 0; j--) {
                        Block blk = mOccupiedBlocks.get(j);
                        if (blk.getRow() < nRows) {
                            mOccupiedBlocks.remove(j);
                            blk.setRow(blk.getRow() + 1);
                            repositioningItems.add(blk);
                        }
                    }
    
                    // Add repositioned blocks back
                    while (repositioningItems.size() > 0) {
                        mOccupiedBlocks.add(repositioningItems.removeLast());
                    }
                    break; // Exit the row-checking loop to recheck from the bottom.
                } else {
                    fullRowItems.clear();
                    nRows--;
                }
            }
        } while (rowCleared); // Repeat until no rows are cleared in the iteration.
    }
    

    synchronized public void setBlocks(Tetromino tetr) {
        boolean[][] bC;
        bC = tetr.getColoredSquares(tetr.getOrientation());
        Color clr = tetr.getColor();

//        sets blocks to blue, unoccupied
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLS; j++) {
                mBlock[i][j] = new Block(false, Color.blue, i, j);
            }
        }
        for (int i = tetr.getCol() ; i < tetr.getCol()  + DIM; i++) {
            for (int j = tetr.getRow(); j < tetr.getRow() + DIM; j++) {
                if (bC[j - tetr.getRow()][i - tetr.getCol() ]) {
                    mBlock[j][i] = new Block(false, clr, j - tetr.getRow(), i - tetr.getCol() );
                }

            }

        }
//occupied blocks
        for (Object mOccupiedBlock : mOccupiedBlocks) {
            Block b = (Block) mOccupiedBlock;
            if (b.getRow() >= 0 && b.getRow() < ROWS && b.getCol() >= 0 && b.getCol() < COLS) {
                mBlock[b.getRow()][b.getCol()] = new Block(true, b.getColor(), b.getRow(), b.getCol());
            }
            
        }
    }

    synchronized public boolean requestLateral(Tetromino tetr) {
        boolean[][] bC;
        bC = tetr.getColoredSquares(tetr.getOrientation());
        for (int i = tetr.getCol() ; i < tetr.getCol()  + DIM; i++) {
            for (int j = tetr.getRow(); j < tetr.getRow() + DIM; j++) {
                if (bC[j - tetr.getRow()][i - tetr.getCol()]) {
                    if (i < 0 || i >= Grid.COLS || j >= Grid.ROWS || mBlock[j][i].isOccupied()) {
                        return false;
                    }
                }
                

            }

        }
        return true;
    }
}
