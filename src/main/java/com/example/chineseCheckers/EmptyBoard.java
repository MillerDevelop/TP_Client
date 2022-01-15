package com.example.chineseCheckers;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class EmptyBoard {

    private static Circle circle;
   /* private static int X;
    private static int Y;*/

    protected static void EmptyAllBoard() {

    }

    protected static void EmptyPlayerBoard(Color color, GridPane pane) {

        for (int Y = 0; Y<=16; Y++) {
            for (int X = 0; X<=24; X++) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == X && GridPane.getRowIndex(node) == Y) {
                        circle = (Circle) node;
                        if (circle.getFill().equals(color)) {
                            circle.setFill(Color.web("#a6abaf"));
                        }
                    }
                }
            }
        }
    }
}
