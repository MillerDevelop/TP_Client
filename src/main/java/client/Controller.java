package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;

/**
 * Class that is responsible for @FXML variables passed from GUI. 
 * Processes all the data connected with GUI and passes more specified tasks to other classes.
 */

public class Controller {
    private Point2D startPoint;
    private Point2D endPoint;
    private static String Ip;
    private static int port;
    private static boolean connectionError, createError, joinError, finishTurn, yourTurn, twoPlayers, stepMade= false;
    private int greetingsChoice;
    private Color myColor;
    private final HashMap<String, Button> colorMap = new HashMap<>();
    private String oppositeColor;

    @FXML
    private TextField IpField, PortField;

    @FXML
    private Button button, REDcolorChoice, YELLOWcolorChoice, BLACKcolorChoice, WHITEcolorChoice, BLUEcolorChoice, GREENcolorChoice;

    @FXML
    private Label choosePlayersLabel, connectionErrorLabel, createErrorLabel, joinErrorLabel, ColorLabel, welcomeLabel, yourColorLabel, waitingForPlayersLabel, youWonLabel, youLostLabel;

    @FXML
    private VBox playerCount, chooseColor, connectServer, gameResults;

    @FXML
    private HBox buttonBoardBox;

    @FXML
    private AnchorPane playGame;

    @FXML
    private GridPane board;

    @FXML
    private Alert alertMessage;

    private Client client;

    /**
     * Method that defines the point chosen to show available moves on the game board (@param playGame) or make one and send to server.
     * When specified server signals come that turns on or off the booleans controlling the player`s step flow.
     * May show messages to finish turn or that it`s not the turn of the user.
     *
     * @param event parameter that defines the location in GUI window where #onMouseClicked action happened.
     */

    @FXML
    protected void gameBoardClicked(MouseEvent event) {
        Circle circle = (Circle) event.getSource();

        if (finishTurn) {
            alertMessage = new Alert(Alert.AlertType.WARNING);
            alertMessage.setTitle("Finish Your Move");
            alertMessage.setHeaderText("Please finish your move");
            alertMessage.setContentText("You have already made your steps. Please click 'FINISH MOVE' button.");
            alertMessage.showAndWait();
        } else if (yourTurn) {

            if (circle.getFill().equals(myColor)) {
                if (startPoint != null) {
                    MoveBoard.HidePreviousAvailibleMoves(board);
                }
                if (!stepMade) {
                    startPoint = new Point2D(GridPane.getColumnIndex((Node) event.getTarget()), GridPane.getRowIndex((Node) event.getTarget()));
                    MoveBoard.ShowAllAvailableMoves(startPoint, board);
                } else {
                    startPoint = endPoint;
                    if (GridPane.getColumnIndex((Node)event.getTarget()) == startPoint.getX() && GridPane.getRowIndex((Node) event.getTarget()) == startPoint.getY()) {
                        MoveBoard.ShowAllAvailableMoves(startPoint, board);
                    }
                }
            } else if (circle.getFill().equals(Color.web("#d7dcdf"))) {
                endPoint = new Point2D(GridPane.getColumnIndex((Node) event.getTarget()), GridPane.getRowIndex((Node) event.getTarget()));
                MoveBoard.HidePreviousAvailibleMoves(board);
                client.sendMessageToServer("MOVE " + (int) startPoint.getX() + " " + (int) startPoint.getY() + " " + (int) endPoint.getX() + " " + (int) endPoint.getY());
            }

        } else if (!yourTurn){
            alertMessage = new Alert(Alert.AlertType.ERROR);
            alertMessage.setTitle("Not Your Turn");
            alertMessage.setHeaderText("Please wait for your turn");
            alertMessage.setContentText("It`s somebody else`s turn. You can`t make steps now");
            alertMessage.showAndWait();
            client.sendMessageToServer("YOUR TURN");
        }
    }

    /**
     * Method covers the cases of different buttons clicked on the game board (@param playGame).
     *
     * @param event parameter that defines the location in GUI window where #onMouseClicked action happened.
     */

    @FXML
    protected void buttonBoardMouseClicked(MouseEvent event) {
        button = (Button) event.getSource();
        switch (button.getText()) {
            case "EXIT" :
                client.sendMessageToServer("EXIT");
                client.CloseEverything();
                Platform.exit();
                break;
            case "FINISH STEP" :
                client.sendMessageToServer("FINISH");
                MoveBoard.setHoppedFalse();
                buttonBoardBox.setDisable(true);
                MoveBoard.HidePreviousAvailibleMoves(board);
                MoveBoard.setTempPointNull();
                finishTurn = false;
                stepMade = false;
                break;
            case "SKIP STEP" :
                if (!stepMade) {
                    client.sendMessageToServer("FINISH");
                    buttonBoardBox.setDisable(true);
                } else {
                    alertMessage = new Alert(Alert.AlertType.WARNING);
                    alertMessage.setTitle("Cant`t Skip Step");
                    alertMessage.setHeaderText("Moves Made");
                    alertMessage.setContentText("You have already made steps so click 'FINISH STEP'");
                    alertMessage.showAndWait();
                }

        }

    }

    /**
     * When the button on the @param gameResults box is clicked the program aborts.
     */

    @FXML
    protected void resultClicked() {
        Platform.exit();
    }

    /**
     * When the button on the greetingsBox is clicked depending on which button player chooses sets @param greetingsChoice to 1 or 2.
     *
     * @param event parameter that defines the location in GUI window where #onMouseClicked action happened.
     */

    @FXML
    protected void greetingsClicked(MouseEvent event) {
        button = (Button) event.getSource();
        SceneContentHandler.SetBoxUnVisible(button.getParent());
        if (colorMap.isEmpty()) {
            fillHashMap();
        }

        switch (button.getId()) {
            case "Create" -> {
                greetingsChoice = 1;
                SceneContentHandler.SetBoxVisible(connectServer);
            }
            case "Join" -> {
                greetingsChoice = 2;
                SceneContentHandler.SetBoxVisible(connectServer);
            }
        }

    }

    /**
     * When button the @param chooseColor box is clicked sends the color written on the button to the server.
     * Sets the buttons and boxes not needed to not managable and not visible and the opposite for what is needed next.
     *
     * @param event parameter that defines the location in GUI window where #onMouseClicked action happened.
     */

    @FXML
    protected void chooseColorClicked(MouseEvent event) {
        button = (Button) event.getSource();
        myColor = Color.web(button.getText());
        yourColorLabel.setText("YOUR COLOR: " + button.getText());
        SceneContentHandler.SetLabelUnVisible(ColorLabel);
        SceneContentHandler.SetLabelUnVisible(welcomeLabel);
        SceneContentHandler.SetBoxUnVisible(button.getParent());
        SceneContentHandler.SetBoxUnVisible(ColorLabel);
        for (String s : colorMap.keySet()) {
            colorMap.get(s).setVisible(false);
            colorMap.get(s).setManaged(false);
        }
        SceneContentHandler.SetBoxVisible(playGame);
        client.sendMessageToServer("COLOR " + button.getText());
    }

    /**
     * Method responsible for server connection. If the connection did not succeed shows the @param connectionErrorLabel to player.
     * Does not allow user to proceed forward until the correct data is written.
     *
     * @param event parameter that defines the location in GUI window where #onMouseClicked action happened.
     */

    @FXML
    protected void connectServerClicked(MouseEvent event) {
        button = (Button) event.getSource();
        try {
            Ip = IpField.getText();
            port = Integer.parseInt(PortField.getText());
            connect();
        } catch (Exception e) {
            SceneContentHandler.SetLabelVisible(connectionErrorLabel);
            connectionError = true;
            return;
        }

        if (!connectionError) {

            switch (greetingsChoice) {
                case 1 -> client.sendMessageToServer("CREATE");
                case 2 -> client.sendMessageToServer("JOIN");
            }
        }

    }

    /**
     * Method that detects the player count written on the button and sends it to the server.
     * Handles scene content for further settings before the game.
     *
     * @param event parameter that defines the location in GUI window where #onMouseClicked action happened.
     */

    @FXML
    protected void playerCountClicked(MouseEvent event){
        button = (Button) event.getSource();
        client.sendMessageToServer(button.getText().toUpperCase());
        SceneContentHandler.SetBoxUnVisible(button.getParent());
        SceneContentHandler.SetLabelUnVisible(choosePlayersLabel);
        SceneContentHandler.SetBoxVisible(chooseColor);
        SceneContentHandler.SetLabelVisible(ColorLabel);
    }

    /**
     * Method that accepts different signals from server and manages the game flow.
     *
     * @param message message sent from server
     */

    protected void CheckServerMsg(String message) {
        System.out.println(message);
        if (message.contains("CREATE")) {
            switch (message) {
                case "CREATE OK" -> {

                    /* If creating is okay proceed, else you will get an error message */

                    if (createError) {
                        SceneContentHandler.SetLabelUnVisible(createErrorLabel);
                    }
                    SceneContentHandler.SetBoxUnVisible(connectServer);
                    SceneContentHandler.SetBoxVisible(playerCount);
                    SceneContentHandler.SetLabelVisible(choosePlayersLabel);
                }
                case "CREATE BAD" -> {
                    createError = true;
                    SceneContentHandler.SetLabelVisible(createErrorLabel);
                }
            }
        }
        else if (message.contains("JOIN")) {
            switch (message) {

                /* If joining is okay proceed, else you will get an error message */

                case "JOIN OK" -> {
                    if (joinError) {
                        SceneContentHandler.SetLabelUnVisible(joinErrorLabel);
                    }
                    SceneContentHandler.SetLabelVisible(ColorLabel);
                }
                case "JOIN BAD" -> {
                    joinError = true;
                    SceneContentHandler.SetLabelVisible(joinErrorLabel);
                }
            }
        }
        else if (message.contains("COLOR")) {

            String[] color = message.split(" ");
            FillBoard.FillColor(color[1], board);
            colorMap.get(color[1]).setDisable(true);
            if (twoPlayers) {

                /* Block every color except the opposite */

                switch (color[1]) {

                    case "YELLOW" -> oppositeColor = "BLUE";
                    case "RED" -> oppositeColor = "GREEN";
                    case "WHITE" -> oppositeColor = "BLACK";
                    case "BLACK" -> oppositeColor = "WHITE";
                    case "GREEN" -> oppositeColor = "RED";
                    case "BLUE" -> oppositeColor = "YELLOW";
                }

                for (String s : colorMap.keySet()) {
                    if (!s.equals(oppositeColor)) {
                        colorMap.get(s).setDisable(true);
                    }
                }
            }

            if (greetingsChoice == 2) {
                SceneContentHandler.SetBoxUnVisible(connectServer);
                SceneContentHandler.SetBoxVisible(chooseColor);
            }
        }
        else if (message.contains("2 PLAYERS")) {

            /* When you join a two player game there are special rules for choosing colors so when you are joining such game the boolean sets true */

            twoPlayers = true;
        }
        else if (message.equals("GAME STARTED")) {
            SceneContentHandler.SetLabelUnVisible(waitingForPlayersLabel);
        }
        else if (message.contains("MOVE")) {

            /* If the move is good you redraw the points on the board */

            switch (message) {
                case "MOVE OK" -> {
                    MoveBoard.RedrawBoardMove(startPoint, endPoint, board, myColor);
                    MoveBoard.blockPreviousStartPoint(startPoint);
                    if (!stepMade) {
                        stepMade = true;
                        MoveBoard.ifHopped(endPoint, board);
                    }
                    startPoint = null;
                }
                case "MOVE BAD" -> finishTurn = true;
            }
        }
        else if (message.contains("STEP")) {

            /* Redrawing the step of other user on the current board */

            String[] step = message.split(" ");
            Point2D opponentStartPoint = new Point2D(Double.parseDouble(step[1]), Double.parseDouble(step[2]));
            Point2D opponentEndPoint = new Point2D(Double.parseDouble(step[3]), Double.parseDouble(step[4]));
            MoveBoard.RedrawBoardMove(opponentStartPoint, opponentEndPoint, board, Color.web(step[5]));
        }
        else if (message.contains("TURN")) {

            /* Regulates the game flow.
             *  All your buttons are blocked and you cannot move when !your turn.
             *  You cannot make a move when finishMove.
             *  You can make moves and press buttons when yourTurn.
             */


            switch (message) {
                case "YOUR TURN" -> {yourTurn = true;
                                    buttonBoardBox.setDisable(false);}
                case "NOT YOUR TURN" -> yourTurn = false;

                case "FINISH TURN" -> finishTurn = true;
            }
        } else if (message.contains("DELETE")) {
            String [] remove = message.split(" ");
            EmptyBoard.EmptyPlayerBoard(Color.web(remove[1]), board);
        }
        else if (message.contains("YOU")) {

            /* if you won or lost it disconnects from the server and then takes you to the internal resultWindow */

            client.CloseEverything();
            SceneContentHandler.SetBoxUnVisible(playGame);
            SceneContentHandler.SetBoxVisible(gameResults);

            switch (message) {
                case "YOU WON" -> SceneContentHandler.SetLabelVisible(youWonLabel);
                case "YOU LOST" -> SceneContentHandler.SetLabelVisible(youLostLabel);
            }

        }
    }

    /**
     * Method that creates new clients and connects this controller to the server else shows error message
     */

    protected void connect(){
        try {
            this.client = new Client(new Socket(Ip, port), this);
            System.out.println("Connected to server");
            connectionError = false;
            SceneContentHandler.SetLabelUnVisible(connectionErrorLabel);
        } catch (IOException e) {
            System.out.println("Can`t connect");
            SceneContentHandler.SetLabelVisible(connectionErrorLabel);
            connectionError = true;
        }

    }

    /** Method to fill a hashmap with colors and corresponding color buttons to make easier access while editing color buttons parameters. */

    private void fillHashMap() {

        colorMap.put("RED", REDcolorChoice);
        colorMap.put("YELLOW", YELLOWcolorChoice);
        colorMap.put("WHITE", WHITEcolorChoice);
        colorMap.put("BLACK", BLACKcolorChoice);
        colorMap.put("BLUE", BLUEcolorChoice);
        colorMap.put("GREEN", GREENcolorChoice);
    }

    /**
     * Method for testing the connection.
     *
     * @return if error occurred.
     */

    protected boolean getConnectionError() {
        return connectionError;
    }

    /**
     * Method for testing.
     *
     * @return if the server message recieved and there are two players on game.
     */

    protected boolean get2Players() {
        return twoPlayers;
    }

}