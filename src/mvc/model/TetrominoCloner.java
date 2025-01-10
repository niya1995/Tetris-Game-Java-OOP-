package mvc.model;

public class TetrominoCloner {

    public Tetromino cloneTetromino(Tetromino original) {

        if (original == null) return null;

        Tetromino cloned = new ObjectOfTetromino();
        cloned.setRow(original.getRow());
        cloned.setCol(original.getCol());
        cloned.setOrientation(original.getOrientation());
        cloned.setColor(original.getColor());
        cloned.setColoredSquares(original.getColoredSquares());
        
        return cloned;
    }
}
