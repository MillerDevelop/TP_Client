package client;

import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/** The class regulates all about the board moves */

public class MoveBoard {

    private static double startPointX, startPointY;
    private static final double[] Xmove = new double[]{1, 1, -1, 2, -2, -1};
    private static final double[] Ymove = new double[]{-1, 1, 1, 0, 0, -1};
    private static Circle circle;
    private static final Color availible = Color.web("#d7dcdf");
    private static final Color notTaken = Color.web("#a6abaf");
    private static boolean hopped = false;
    private static Point2D tempPoint;

    /** When hopped is true that means that the point made a hop and cannot make regular moves anymore.
     * Method makes possible to reset hopped when the player finishes his move, so he can make ordinary moves.
      */

    protected static void setHoppedFalse() {
        hopped = false;
    }

    /**
     * Temporary point blocks the ability to go back to the previous step in this turn.
     * Method gives the ability to clear the point after the turn is finished.
     */

    protected static void setTempPointNull() {
        tempPoint = null;
    }

    /** The method checks if the hop was made by comparing starting and ending point on the board.
     *  If the hop was made makes @param hopped true. You cannot set hopped true by yourself.
     *
     * @param endPoint the point for comparing with the X and Y of the starting point stored inside the class.
     * @param pane game board
     */

    protected static void ifHopped(Point2D endPoint, GridPane pane) {
        for (int i = 0; i < 6; i++) {
            for (Node node : pane.getChildren()) {
                if (Ymove[i] != 0) {
                    if (GridPane.getColumnIndex(node) == (startPointX + (2 * Xmove[i])) && GridPane.getColumnIndex(node) == endPoint.getX() && GridPane.getRowIndex(node) == (startPointY + (2 * Ymove[i])) && GridPane.getRowIndex(node) == endPoint.getY()) {
                        hopped = true;
                    }
                } else {
                    if (GridPane.getColumnIndex(node) == startPointX + 2 * Xmove[i] && GridPane.getColumnIndex(node) == endPoint.getX() && GridPane.getRowIndex(node) == endPoint.getY() && GridPane.getRowIndex(node) == startPointY) {
                        hopped = true;
                    }
                }
            }
        }
    }

    /**
     * Method that accepts the start point of the previous move and stores it to block making a step back in your turn.
     *
     * @param prevPoint previous start point
     */

    protected static void blockPreviousStartPoint(Point2D prevPoint) {
        tempPoint = prevPoint;
    }

    /**
     * Method shows available moves for the point clicked if it is of your color and it is your turn.
     * After you make a step shows available steps only for the point chosen in the first step and only if it was a hop.
     *
     * @param startPoint the point that was clicked.
     * @param pane game board
     */

    protected static void ShowAllAvailableMoves(Point2D startPoint, GridPane pane) {


            startPointX = startPoint.getX();
            startPointY = startPoint.getY();

            for (int i = 0; i < 6; i++) {
                for (Node node : pane.getChildren()) {
                    if (GridPane.getColumnIndex(node) == startPointX + Xmove[i] && GridPane.getRowIndex(node) == startPointY + Ymove[i]) {
                        circle = (Circle) node;


                        if (circle.getFill().equals(notTaken)) {
                            if (!hopped) {
                                circle.setFill(availible);
                                circle.setStrokeWidth(2);
                            }
                        } else {

                            /* Checking if the hops are availible when the point nearby is not. */

                            if (Ymove[i] != 0) {
                                for (Node hop : pane.getChildren()) {
                                    if (GridPane.getColumnIndex(hop) == (startPointX + (2 * Xmove[i])) && GridPane.getRowIndex(hop) == (startPointY + (2 * Ymove[i]))) {
                                        circle = (Circle) hop;
                                        if (!hopped) {
                                            if (circle.getFill().equals(notTaken)) {
                                                circle.setFill(availible);
                                                circle.setStrokeWidth(2);
                                            }
                                        } else {
                                            if (!(GridPane.getColumnIndex(hop) == tempPoint.getX() && GridPane.getRowIndex(hop) == tempPoint.getY())) {
                                                if (circle.getFill().equals(notTaken)) {
                                                    circle.setFill(availible);
                                                    circle.setStrokeWidth(2);
                                                }
                                            }
                                        }

                                    }
                                }
                            } else for (Node hop : pane.getChildren()) {
                                if (GridPane.getColumnIndex(hop) == startPointX + 2 * Xmove[i] && GridPane.getRowIndex(hop) == startPointY) {
                                    circle = (Circle) hop;
                                    if (!hopped) {
                                        if (circle.getFill().equals(notTaken)) {
                                            circle.setFill(availible);
                                            circle.setStrokeWidth(2);
                                        }
                                    } else {
                                        if (!(GridPane.getColumnIndex(hop) == tempPoint.getX() && GridPane.getRowIndex(hop) == tempPoint.getY())) {
                                            if (circle.getFill().equals(notTaken)) {
                                                circle.setFill(availible);
                                                circle.setStrokeWidth(2);
                                            }
                                        }
                                    }

                                }
                            }

                        }}
                }
            }
    }

    /**
     * Hides previous available moves when the other point is chosen or the step is made and turn finished.
     *
     * @param pane game board.
     */

    protected static void HidePreviousAvailibleMoves(GridPane pane) {

        for (int i = 0; i < 6; i++) {
            for (Node node : pane.getChildren()) {
                if (GridPane.getColumnIndex(node) == startPointX + Xmove[i] && GridPane.getRowIndex(node) == startPointY + Ymove[i]) {
                    circle = (Circle) node;

                    if (circle.getFill().equals(availible)) {
                        circle.setFill(notTaken);
                        circle.setStrokeWidth(1);
                    } else {
                        if (Ymove[i] != 0) {
                            for (Node hop : pane.getChildren()) {
                                if (GridPane.getColumnIndex(hop) == startPointX + 2 * Xmove[i] && GridPane.getRowIndex(hop) == startPointY + 2 * Ymove[i]) {
                                    circle = (Circle) hop;
                                    if (circle.getFill().equals(availible)) {
                                        circle.setFill(notTaken);
                                        circle.setStrokeWidth(1);
                                    }
                                }
                            }
                        } else for (Node hop : pane.getChildren()) {
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

    /** When the points are approved by the server the start point is redrawn to be available for moves and the end point is set to the player`s color.
     *
     * @param startPoint move`s start point.
     * @param endPoint move`s endpoint.
     * @param pane game board.
     * @param playerColor the color of the player whose move it is.
     */

    protected static void RedrawBoardMove(Point2D startPoint, Point2D endPoint, GridPane pane, Color playerColor) {

        for (Node node : pane.getChildren()) {

            if (GridPane.getColumnIndex(node) == startPoint.getX() && GridPane.getRowIndex(node) == startPoint.getY()) {
                circle = (Circle) node;
                circle.setFill(notTaken);
            } else if (GridPane.getColumnIndex(node) == endPoint.getX() && GridPane.getRowIndex(node) == endPoint.getY()) {
                circle = (Circle) node;
                circle.setFill(playerColor);
            }
        }
    }

}