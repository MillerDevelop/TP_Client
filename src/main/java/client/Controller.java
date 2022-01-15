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


public class Controller {
    private Point2D startPoint;
    private Point2D endPoint;
    //later change to int, using String because running server on a local machine
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

    //FINISH CREATE SO THAT BUTTON WONT BE SEEN
    //WRITE LOGIC FOR NOT GOING BACK ON THE SAME
    //BLOCK OTHER STARTPOINTS WHEN STEP MADE
    //SHOWING ONLY HOPS AFTER CLICKING FEW POINTS
    //HIDE AVAILIBLE STEPS WHEN CLICKED ON OTHER POINT NOT YOUR COLOR

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
                    MoveBoard.ShowAllAvailibleMoves(startPoint, board);
                } else {
                    startPoint = endPoint;
                    if (GridPane.getColumnIndex((Node)event.getTarget()) == startPoint.getX() && GridPane.getRowIndex((Node) event.getTarget()) == startPoint.getY()) {
                        MoveBoard.ShowAllAvailibleMoves(startPoint, board);
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

    @FXML

    protected void resultClicked() {
        Platform.exit();
    }

    //finish with enabling labeles
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

    @FXML
    protected void connectServerClicked(MouseEvent event) {
        button = (Button) event.getSource();
        //Change to int later
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

    @FXML
    protected void playerCountClicked(MouseEvent event){
        button = (Button) event.getSource();
        if (button.getText().equals("2 players")) {
            twoPlayers = true;
        }
        client.sendMessageToServer(button.getText().toUpperCase());
        SceneContentHandler.SetBoxUnVisible(button.getParent());
        SceneContentHandler.SetLabelUnVisible(choosePlayersLabel);
        SceneContentHandler.SetBoxVisible(chooseColor);
        SceneContentHandler.SetLabelVisible(ColorLabel);
    }


    protected void CheckServerMsg(String message) {
        System.out.println(message);
        if (message.contains("CREATE")) {
            switch (message) {
                case "CREATE OK" -> {
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

            //disable color after chosen
            //if two players : disable all colors except one opposite to the one sent.

            String[] color = message.split(" ");
            FillBoard.FillColor(color[1], board);
            colorMap.get(color[1]).setDisable(true);
            if (twoPlayers) {
                switch (color[1]) {
                    //Block every color except opposite
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
            twoPlayers = true;
        }
        else if (message.equals("GAME STARTED")) {
            //hide label waiting for players
            waitingForPlayersLabel.setManaged(false);
            waitingForPlayersLabel.setVisible(false);
        }
        else if (message.contains("MOVE")) {
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
            String[] step = message.split(" ");
            Point2D opponentStartPoint = new Point2D(Double.parseDouble(step[1]), Double.parseDouble(step[2]));
            Point2D opponentEndPoint = new Point2D(Double.parseDouble(step[3]), Double.parseDouble(step[4]));
            MoveBoard.RedrawBoardMove(opponentStartPoint, opponentEndPoint, board, Color.web(step[5]));
        }
        else if (message.contains("TURN")) {
            switch (message) {
                case "YOUR TURN" -> {yourTurn = true;
                                    buttonBoardBox.setDisable(false);}
                case "NOT YOUR TURN" -> yourTurn = false;
                //disable buttons;
                //if step made and you know the player can`t make more moves
                //maybe wont be needed
                case "FINISH TURN" -> finishTurn = true;
            }
        } else if (message.contains("DELETE")) {
            String [] remove = message.split(" ");
            EmptyBoard.EmptyPlayerBoard(Color.web(remove[1]), board);
        }
        else if (message.contains("YOU")) {
            client.CloseEverything();
            SceneContentHandler.SetBoxUnVisible(playGame);
            SceneContentHandler.SetBoxVisible(gameResults);

            switch (message) {
                case "YOU WON" -> SceneContentHandler.SetLabelVisible(youWonLabel);
                case "YOU LOST" -> SceneContentHandler.SetLabelVisible(youLostLabel);

                //loosing box and disconnection.
            }

        }


        //if needed more leave comment
        //currently thinking about making one scene instead of two. If there are two scenes getting some trouble with sending content.
    }

    protected void connect(){
        try {
            this.client = new Client(new Socket(Ip, port), this);
            System.out.println("Connected to server");
            connectionError = false;
            SceneContentHandler.SetLabelUnVisible(connectionErrorLabel);
        } catch (IOException e) {
            SceneContentHandler.SetLabelVisible(connectionErrorLabel);
            connectionError = true;
        }

    }

    private void fillHashMap() {

        colorMap.put("RED", REDcolorChoice);
        colorMap.put("YELLOW", YELLOWcolorChoice);
        colorMap.put("WHITE", WHITEcolorChoice);
        colorMap.put("BLACK", BLACKcolorChoice);
        colorMap.put("BLUE", BLUEcolorChoice);
        colorMap.put("GREEN", GREENcolorChoice);
    }
}