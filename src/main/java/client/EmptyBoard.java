package client;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** Clears board from the points of the player that had won or left */

public class EmptyBoard {

    /**
     * Finds the points of the corresponding color to the color of the winning player or the one that left.
     *
     * @param color color of the player
     * @param pane the board on which the game is being played
     */

    protected static void EmptyPlayerBoard(Color color, GridPane pane) {

        for (int Y = 0; Y<=16; Y++) {
            for (int X = 0; X<=24; X++) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == X && GridPane.getRowIndex(node) == Y) {
                        Circle circle = (Circle) node;
                        if (circle.getFill().equals(color)) {
                            circle.setFill(Color.web("#a6abaf"));
                        }
                    }
                }
            }
        }
    }
}
