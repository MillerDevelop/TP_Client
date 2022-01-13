package com.example.chineseCheckers;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MoveBoard {

    private static double X;
    private static double Y;
    private static final double [] Xmove = new double[] {1, 1, -1, 2, -2, -1};
    private static final double [] Ymove = new double[] {-1, 1, 1, 0, 0, -1};
    private static Circle circle;
    private static Color availible= Color.web("#d7dcdf");
    private static Color notAvailible = Color.web("#a6abaf");

    protected static void ShowAvailibleMoves(Point2D startPoint, GridPane pane) {

        X = startPoint.getX();
        Y = startPoint.getY();

        for (int i = 0; i < 6; i++) {
            for (Node node : pane.getChildren()) {
                if (GridPane.getColumnIndex(node) == X + Xmove[i] && GridPane.getRowIndex(node) == Y + Ymove[i]){
                    circle = (Circle) node;

                    if (circle.getFill().toString().equals("0xa6abafff")) {
                        circle.setFill(availible);
                        System.out.println(circle.getFill().toString());
                        circle.setStrokeWidth(2);
                    }
                    else {
                        if (Ymove [i] != 0) {
                            for (Node hop : pane.getChildren()) {
                                if (GridPane.getColumnIndex(hop) == (X + (2 * Xmove[i])) && GridPane.getRowIndex(hop) == (Y + (2 * Ymove[i]))) {
                                    circle = (Circle) hop;
                                    if (circle.getFill().toString().equals("0xa6abafff")) {
                                        circle.setFill(availible);
                                        circle.setStrokeWidth(2);
                                    }
                                }
                            }
                        }
                        else {
                            for (Node hop : pane.getChildren()) {
                                if (GridPane.getColumnIndex(hop) == X + 2 * Xmove[i] && GridPane.getRowIndex(hop) == Y) {
                                    circle = (Circle) hop;
                                    if (circle.getFill().toString().equals("0xa6abafff")) {
                                        circle.setFill(availible);
                                        circle.setStrokeWidth(2);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected static void HidePreviousAvailibleMoves(GridPane pane) {

        for (int i = 0; i < 6; i++) {
            for (Node node : pane.getChildren()) {
                if (GridPane.getColumnIndex(node) == X + Xmove[i] && GridPane.getRowIndex(node) == Y + Ymove[i]){
                    circle = (Circle) node;

                    if (circle.getFill().toString().equals("0xd7dcdfff")) {
                        circle.setFill(notAvailible);
                        System.out.println(circle.getFill().toString());
                        circle.setStrokeWidth(1);
                    }
                    else {
                        if (Ymove [i] != 0) {
                            for (Node hop : pane.getChildren()) {
                                if (GridPane.getColumnIndex(hop) == X + 2 * Xmove[i] && GridPane.getRowIndex(hop) == Y + 2 * Ymove[i]) {
                                    circle = (Circle) hop;
                                    if (circle.getFill().toString().equals("0xd7dcdfff")) {
                                        circle.setFill(notAvailible);
                                        circle.setStrokeWidth(1);
                                    }
                                }
                            }
                        }
                        else {
                            for (Node hop : pane.getChildren()) {
                                if (GridPane.getColumnIndex(hop) == X + 2 * Xmove[i] && GridPane.getRowIndex(hop) == Y) {
                                    circle = (Circle) hop;
                                    if (circle.getFill().toString().equals("0xd7dcdfff")) {
                                        circle.setFill(notAvailible);
                                        circle.setStrokeWidth(1);
                                    }
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    protected static void RedrawBoardMove() {

    }



}
