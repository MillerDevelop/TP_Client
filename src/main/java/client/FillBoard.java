package client;

import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** Fills the board depending on the color sent by server */

public class FillBoard {

    private static int endpoint;
    private static int startpoint;
    private static int y;
    private static int x;
    private static Circle circle;

    /**
     * Accepts the color and board and depending on the color runs the corresponding methods
     *
     * @param color defines the color of points and its situation.
     * @param pane game board
     */

    protected static void FillColor(String color, GridPane pane) {
        switch (color) {
            case "YELLOW" -> fillYellow(pane);
            case "RED" -> fillRed(pane);
            case "WHITE" -> fillWhite(pane);
            case "BLACK" -> fillBlack(pane);
            case "BLUE" -> fillBlue(pane);
            case "GREEN" -> fillGreen(pane);
        }
    }

    /**
     * Fills part of the board for the color yellow.
     *
     * @param pane game board
     */

    private static void fillYellow(GridPane pane) {

        startpoint = 0;
        endpoint = 6;

        for (y = 4; y<=7; y++) {
            for (x= startpoint; x<=endpoint; x+=2 ) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                        circle = (Circle) node;
                        circle.setFill(Color.YELLOW);
                    }
                }
            }
            endpoint -=1;
            startpoint +=1;
        }

    }

    /**
     * Fills part of the board for the color red.
     *
     * @param pane game board
     */

    private static void fillRed(GridPane pane) {

        startpoint = 12;
        endpoint = 12;

        for (y = 0; y<=3; y++) {
            for (x = startpoint; x<=endpoint; x+=2) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                        circle = (Circle) node;
                        circle.setFill(Color.RED);
                    }
                }
            }
            endpoint +=1;
            startpoint -=1;
        }

    }

    /**
     * Fills part of the board for the color white.
     *
     * @param pane game board
     */

    private static void fillWhite(GridPane pane) {

        startpoint = 0;
        endpoint = 6;

        for(y = 12; y>=9; y--) {
            for (x = startpoint; x <=endpoint; x += 2) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                        circle = (Circle) node;
                        circle.setFill(Color.WHITE);
                        break;
                    }
                }
            }
            endpoint -=1;
            startpoint +=1;
        }

    }

    /**
     * Fills part of the board for the color black.
     *
     * @param pane game board
     */

    private static void fillBlack(GridPane pane) {

        startpoint = 18;
        endpoint = 24;

        for (y = 4; y<= 7; y++) {
            for (x = startpoint; x <= endpoint; x+=2) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                        circle = (Circle) node;
                        circle.setFill(Color.BLACK);
                        break;
                    }
                }
            }
            startpoint +=1;
            endpoint -=1;
        }
    }

    /**
     * Fills part of the board for the color blue.
     *
     * @param pane game board
     */

    private static void fillBlue(GridPane pane) {

        startpoint = 21;
        endpoint = 21;

        for (y = 9; y <= 12; y++) {
            for (x = startpoint; x <= endpoint; x+=2) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                        circle = (Circle) node;
                        circle.setFill(Color.BLUE);
                        break;
                    }
                }
            }
            startpoint -=1;
            endpoint +=1;
        }
    }

    /**
     * Fills part of the board for the color green.
     *
     * @param pane game board
     */

    private static void fillGreen(GridPane pane) {

        startpoint = 9;
        endpoint = 15;

        for (y = 13; y <= 16; y++) {
            for (x = startpoint; x <= endpoint; x+=2) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == x && GridPane.getRowIndex(node) == y) {
                        circle = (Circle) node;
                        circle.setFill(Color.GREEN);
                        break;
                    }
                }
            }
            startpoint +=1;
            endpoint -=1;
        }
    }
}
