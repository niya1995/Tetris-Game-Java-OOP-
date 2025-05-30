package mvc.model;

import java.awt.*;
import java.util.ArrayList;
import java.util.LinkedList;


public class Grid {

    private static final int ROWS = 20;
    private static final int COLS = 10;
    private static final int DIM = 4;

    private Block[][] mBlock;

    private ArrayList<Block> mOccupiedBlocks;

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

    public static int getRows(){
        return ROWS;
    }
    public static int getCols(){
        return COLS;
    }
    public static int getDim(){
        return DIM;
    }


    synchronized public boolean requestDown(Tetromino tetr) {

        try{
            boolean[][] bC;
            bC = tetr.getColoredSquares(tetr.getOrientation());
            for (int i = tetr.getCol(); i < tetr.getCol()  + DIM; i++) {
                for (int j = tetr.getRow(); j < tetr.getRow() + DIM; j++) {
                    // if goes out of bounds
                    if (bC[j - tetr.getRow()][i - tetr.getCol()]) {
                        if (j >= Grid.ROWS || i < 0 || i >= Grid.COLS || mBlock[j][i].isOccupied()) {
                            return false;
                        }
                    }

                }

            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("Error: Attempted to access grid out of bounds - " + e.getMessage());
            return false;
        }catch (Exception e) {
            System.err.println("Unexpected error during requestDown: " + e.getMessage());
            return false;
        }
    //ok to move down
        return true;
    }

    synchronized public void addToOccupied(Tetromino tetr) {
        try{
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
        } catch (Exception e) {
            System.err.println("Error adding Tetromino to occupied blocks: " + e.getMessage());
        }
    }


    synchronized public void checkTopRow() {
        try {
            for (Block block : mOccupiedBlocks) {
                if (block.getRow() <= 0) {
                    // Game ends as soon as one block reaches or goes beyond the top row
                    CommandCenter.getInstance().setPlaying(false);
                    CommandCenter.getInstance().setGameOver(true);
                    clearGrid();
                    
                    System.out.println("Game Over! A block has reached the top row.");
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Error while checking the top row: " + e.getMessage());
        }
    }
    

    synchronized public void clearGrid() {
        try{
            initializeBlocks();
            mOccupiedBlocks.clear();
        } catch (Exception e) {
            System.err.println("Error while clearing the grid: " + e.getMessage());
        }
    }


    synchronized public void checkCompletedRow() {
        try{
            boolean rowCleared;
            do {
                rowCleared = false;
                int nRows = Grid.ROWS - 1; // Start from the bottom row.
        
                while (nRows >= 0) {
                    LinkedList<Block> fullRowItems = getFullRowItems(nRows);
        
                    // If the row is full, clear it
                    if (fullRowItems.size() == Grid.COLS) {
                        rowCleared = true; // Indicate that a row was cleared.
        
                        clearRow(fullRowItems);
                        updateHighScore(); 
                        CommandCenter.getInstance().checkThreshold();
        
                        repositionBlocksAboveRow(nRows); // Move blocks above the cleared row down
                        break; // Exit the row-checking loop to recheck from the bottom.
                    } else {
                        nRows--;
                    }
                }
            } while (rowCleared); // Repeat until no rows are cleared in the iteration.
        }catch (Exception e) {
            System.err.println("Error while checking completed rows: " + e.getMessage());
        }
    }
    
    // Method to get the blocks that fill a specific row
    private LinkedList<Block> getFullRowItems(int nRows) {
        LinkedList<Block> fullRowItems = new LinkedList<Block>();
        try{
            for (int i = mOccupiedBlocks.size() - 1; i >= 0; i--) {
                Block block = mOccupiedBlocks.get(i);
                if (block.getRow() == nRows) {
                    fullRowItems.add(block);
                }
            }
        }catch (Exception e) {
            System.err.println("Error retrieving full row items: " + e.getMessage());
        }
        return fullRowItems;
    }
    
    // Method to clear the row and update the score
    private void clearRow(LinkedList<Block> fullRowItems) {
        try{
            while (fullRowItems.size() > 0) {
                Block blck = fullRowItems.removeFirst();
                mOccupiedBlocks.remove(blck);
            }
            CommandCenter.getInstance().setRowClearScore();
        }catch (Exception e) {
            System.err.println("Error clearing row: " + e.getMessage());
        }
    }
    
    // Method to update high score
    private void updateHighScore() {
        try{
            if (CommandCenter.getInstance().getScore() > CommandCenter.getInstance().getHighScore()) {
                CommandCenter.getInstance().setHighScore(CommandCenter.getInstance().getScore());
            }
        }catch (NullPointerException e) {
            System.err.println("Error: CommandCenter instance is null.");
        }catch (Exception e) {
            System.err.println("Error updating high score: " + e.getMessage());
        }
    }
    
    // Method to reposition blocks above the cleared row
    private void repositionBlocksAboveRow(int nRows) {
        try{
            LinkedList<Block> repositioningItems = new LinkedList<Block>();
            for (int j = mOccupiedBlocks.size() - 1; j >= 0; j--) {
                Block blk = mOccupiedBlocks.get(j);
                if (blk.getRow() < nRows) {
                    mOccupiedBlocks.remove(j);
                    blk.setRow(blk.getRow() + 1);
                    repositioningItems.add(blk);
                }
            }
            // Add repositioned blocks back in the correct order
            mOccupiedBlocks.addAll(repositioningItems);
        }catch(Exception e){
            System.err.println("Error repositioning blocks: " + e.getMessage());
        }
    }
    
    

    synchronized public void setBlocks(Tetromino tetr) {
        boolean[][] bC;
        bC = tetr.getColoredSquares(tetr.getOrientation());
        Color clr = tetr.getColor();

        //sets blocks to blue, unoccupied
        initializeBlocks();

        // part of the falling Tetromino
        for (int i = tetr.getCol() ; i < tetr.getCol()  + DIM; i++) {
            for (int j = tetr.getRow(); j < tetr.getRow() + DIM; j++) {
                if (bC[j - tetr.getRow()][i - tetr.getCol() ]) {
                    mBlock[j][i] = new Block(false, clr, j - tetr.getRow(), i - tetr.getCol() );
                }

            }

        }
        //occupied blocks
        for (Block b : mOccupiedBlocks) {
            if (b.getRow() >= 0 && b.getRow() < ROWS && b.getCol() >= 0 && b.getCol() < COLS) {
                mBlock[b.getRow()][b.getCol()] = new Block(true, b.getColor(), b.getRow(), b.getCol());
            }
            
        }
    }
    //left or right movement
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
