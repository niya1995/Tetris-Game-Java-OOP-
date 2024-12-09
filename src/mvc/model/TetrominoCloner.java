package mvc.model;

public class TetrominoCloner{
    public Tetromino cloneTetromino() {
        Tetromino tetr = new Tetromino();
        tetr.setRow(tetr.getRow());
        tetr.setCol(tetr.getCol());
        tetr.setOrientation(tetr.getOrientation());
        tetr.setColor(tetr.getColor());
        tetr.setColoredSquaresForClone(tetr.getColoredSquaresForClone());

        return tetr;
    }

}
