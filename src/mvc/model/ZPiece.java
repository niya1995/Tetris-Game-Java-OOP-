package mvc.model;

import java.awt.*;

public class ZPiece extends Tetromino{
    public ZPiece() {
        super();
        initialize();
        setColor(Color.orange);
    }

    protected void initialize() {
        for (int i = 0; i < Grid.getDim(); i++) {
            if (i == 0) {
                setColoredSquares(i, 0, 0, false);
                setColoredSquares(i, 0, 1, true);
                setColoredSquares(i, 0, 2, false);
                setColoredSquares(i, 0, 3, false);
            
                setColoredSquares(i, 1, 0, false);
                setColoredSquares(i, 1, 1, true);
                setColoredSquares(i, 1, 2, true);
                setColoredSquares(i, 1, 3, false);
            
                setColoredSquares(i, 2, 0, false);
                setColoredSquares(i, 2, 1, false);
                setColoredSquares(i, 2, 2, true);
                setColoredSquares(i, 2, 3, false);
            
                setColoredSquares(i, 3, 0, false);
                setColoredSquares(i, 3, 1, false);
                setColoredSquares(i, 3, 2, false);
                setColoredSquares(i, 3, 3, false);
            
            } else if (i == 1) {
                setColoredSquares(i, 0, 0, false);
                setColoredSquares(i, 0, 1, false);
                setColoredSquares(i, 0, 2, false);
                setColoredSquares(i, 0, 3, false);
            
                setColoredSquares(i, 1, 0, false);
                setColoredSquares(i, 1, 1, false);
                setColoredSquares(i, 1, 2, true);
                setColoredSquares(i, 1, 3, true);
            
                setColoredSquares(i, 2, 0, false);
                setColoredSquares(i, 2, 1, true);
                setColoredSquares(i, 2, 2, true);
                setColoredSquares(i, 2, 3, false);
            
                setColoredSquares(i, 3, 0, false);
                setColoredSquares(i, 3, 1, false);
                setColoredSquares(i, 3, 2, false);
                setColoredSquares(i, 3, 3, false);
            
            } else if (i == 2) {
                setColoredSquares(i, 0, 0, false);
                setColoredSquares(i, 0, 1, true);
                setColoredSquares(i, 0, 2, false);
                setColoredSquares(i, 0, 3, false);
            
                setColoredSquares(i, 1, 0, false);
                setColoredSquares(i, 1, 1, true);
                setColoredSquares(i, 1, 2, true);
                setColoredSquares(i, 1, 3, false);
            
                setColoredSquares(i, 2, 0, false);
                setColoredSquares(i, 2, 1, false);
                setColoredSquares(i, 2, 2, true);
                setColoredSquares(i, 2, 3, false);
            
                setColoredSquares(i, 3, 0, false);
                setColoredSquares(i, 3, 1, false);
                setColoredSquares(i, 3, 2, false);
                setColoredSquares(i, 3, 3, false);
            
            } else {
                setColoredSquares(i, 0, 0, false);
                setColoredSquares(i, 0, 1, false);
                setColoredSquares(i, 0, 2, false);
                setColoredSquares(i, 0, 3, false);
            
                setColoredSquares(i, 1, 0, false);
                setColoredSquares(i, 1, 1, false);
                setColoredSquares(i, 1, 2, true);
                setColoredSquares(i, 1, 3, true);
            
                setColoredSquares(i, 2, 0, false);
                setColoredSquares(i, 2, 1, true);
                setColoredSquares(i, 2, 2, true);
                setColoredSquares(i, 2, 3, false);
            
                setColoredSquares(i, 3, 0, false);
                setColoredSquares(i, 3, 1, false);
                setColoredSquares(i, 3, 2, false);
                setColoredSquares(i, 3, 3, false);
            }
            
        }

    }
}
