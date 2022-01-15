package com.example.chineseCheckers;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class MoveBoard {

    private static double startPointX;
    private static double startPointY;
    private static final double [] Xmove = new double[] {1, 1, -1, 2, -2, -1};
    private static final double [] Ymove = new double[] {-1, 1, 1, 0, 0, -1};
    private static Circle circle;
    private static final Color availible= Color.web("#d7dcdf");
    private static final Color notTaken = Color.web("#a6abaf");
    private static boolean hopped = false;

    protected static void setHopped (boolean bool) {
        hopped = bool;
    }

    protected static void ShowAllAvailibleMoves(Point2D startPoint, GridPane pane) {

        startPointX = startPoint.getX();
        startPointY = startPoint.getY();

        for (int i = 0; i < 6; i++) {
            for (Node node : pane.getChildren()) {
                if (GridPane.getColumnIndex(node) == startPointX + Xmove[i] && GridPane.getRowIndex(node) == startPointY + Ymove[i]){
                    circle = (Circle) node;


                    if (circle.getFill().equals(notTaken)) {
                        if (!hopped) {
                            circle.setFill(availible);
                            circle.setStrokeWidth(2);
                        }
                    }
                    else {
                        if (Ymove [i] != 0) {
                            for (Node hop : pane.getChildren()) {
                                if (GridPane.getColumnIndex(hop) == (startPointX + (2 * Xmove[i])) && GridPane.getRowIndex(hop) == (startPointY + (2 * Ymove[i]))) {
                                    circle = (Circle) hop;
                                    if (circle.getFill().equals(notTaken)) {
                                        circle.setFill(availible);
                                        circle.setStrokeWidth(2);
                                        hopped = true;
                                    }
                                }
                            }
                        }
                        else for (Node hop : pane.getChildren()) {
                            if (GridPane.getColumnIndex(hop) == startPointX + 2 * Xmove[i] && GridPane.getRowIndex(hop) == startPointY) {
                                circle = (Circle) hop;
                                if (circle.getFill().equals(notTaken)) {
                                    circle.setFill(availible);
                                    circle.setStrokeWidth(2);
                                    hopped = true;
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
                if (GridPane.getColumnIndex(node) == startPointX + Xmove[i] && GridPane.getRowIndex(node) == startPointY + Ymove[i]){
                    circle = (Circle) node;

                    if (circle.getFill().equals(availible)) {
                        circle.setFill(notTaken);
                        circle.setStrokeWidth(1);
                    }
                    else {
                        if (Ymove [i] != 0) {
                            for (Node hop : pane.getChildren()) {
                                if (GridPane.getColumnIndex(hop) == startPointX + 2 * Xmove[i] && GridPane.getRowIndex(hop) == startPointY + 2 * Ymove[i]) {
                                    circle = (Circle) hop;
                                    if (circle.getFill().equals(availible)) {
                                        circle.setFill(notTaken);
                                        circle.setStrokeWidth(1);
                                    }
                                }
                            }
                        }
                        else for (Node hop : pane.getChildren()) {
                            if (GridPane.getColumnIndex(hop) == startPointX + 2 * Xmove[i] && GridPane.getRowIndex(hop) == startPointY) {
                                circle = (Circle) hop;
                                if (circle.getFill().equals(availible)) {
                                    circle.setFill(notTaken);
                                    circle.setStrokeWidth(1);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    protected static void RedrawBoardMove(Point2D startPoint, Point2D endPoint, GridPane pane, Color playerColor) {

            for (Node node : pane.getChildren()) {

                    if (GridPane.getColumnIndex(node) == startPoint.getX() && GridPane.getRowIndex(node) == startPoint.getY()) {
                        circle = (Circle) node;
                        circle.setFill(notTaken);
                    }
                    else if (GridPane.getColumnIndex(node) == endPoint.getX() && GridPane.getRowIndex(node) == endPoint.getY()) {
                        circle = (Circle) node;
                        circle.setFill(playerColor);
                    }
                }
            }





}
