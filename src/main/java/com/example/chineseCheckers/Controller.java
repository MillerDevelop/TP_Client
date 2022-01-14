package com.example.chineseCheckers;

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
    private static boolean connectionError, createError, joinError, finishTurn, yourTurn, twoPlayers= false;
    private int greetingsChoice;
    private Color myColor;
    private final HashMap<String, Button> colorMap = new HashMap<>();
    private String oppositeColor;

    @FXML
    private TextField IpField, PortField;

    @FXML
    private Button button, REDcolorChoice, YELLOWcolorChoice, BLACKcolorChoice, WHITEcolorChoice, BLUEcolorChoice, GREENcolorChoice;

    @FXML
    private Label choosePlayersLabel, connectionErrorLabel, createErrorLabel, joinErrorLabel, chooseColorLabel, welcomeLabel, yourColorLabel;

    @FXML
    private VBox playerCount, greetings, chooseColor, connectServer, parentBox;

    @FXML
    private AnchorPane playGame;

    @FXML
    private GridPane board;

    @FXML
    private Alert alertMessage;

    private Client client;

    @FXML
    protected void gameBoardClicked(MouseEvent event) {
        Circle circle = (Circle) event.getSource();

        if (yourTurn) {

            if (circle.getFill().equals(myColor)) {
                if (startPoint != null) {
                    MoveBoard.HidePreviousAvailibleMoves(board);
                }
                startPoint = new Point2D(GridPane.getColumnIndex((Node) event.getTarget()), GridPane.getRowIndex((Node) event.getTarget()));
                MoveBoard.ShowAvailibleMoves(startPoint, board);

            } else if (circle.getFill().equals(Color.web("#d7dcdf"))) {
                endPoint = new Point2D(GridPane.getColumnIndex((Node) event.getTarget()), GridPane.getRowIndex((Node) event.getTarget()));
                MoveBoard.HidePreviousAvailibleMoves(board);
                client.sendMessageToServer("MOVE " + (int) startPoint.getX() + " " + (int) startPoint.getY() + " " + (int) endPoint.getX() + " " + (int) endPoint.getY());
            }

        } else if (finishTurn) {
            alertMessage = new Alert(Alert.AlertType.WARNING);
            alertMessage.setTitle("Finish Your Move");
            alertMessage.setHeaderText("Please finish your move");
            alertMessage.setContentText("You have already made your steps. Please click 'FINISH MOVE' button.");
            alertMessage.showAndWait();
        } else if (!yourTurn){
            System.out.println("I`m here notyourturn");
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

    }

    //finish with enabling labeles
    @FXML
    protected void greetingsClicked(MouseEvent event) {
        button = (Button) event.getSource();
        SceneContentHandler.SetBoxUnVisible(button.getParent());
        if (colorMap.isEmpty()) {
            fillHashMap();
        }
        colorMap.get("RED").setVisible(false);

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
        SceneContentHandler.SetBoxUnVisible(button.getParent());
        welcomeLabel.setVisible(false);
        welcomeLabel.setManaged(false);
        SceneContentHandler.SetBoxVisible(playGame);
        client.sendMessageToServer("COLOR " + button.getText());
        yourColorLabel.setText("YOUR COLOR: " + button.getText());
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
        SceneContentHandler.SetBoxVisible(chooseColor);
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
                    SceneContentHandler.SetBoxUnVisible(connectServer);
                    SceneContentHandler.SetBoxVisible(chooseColor);
                    SceneContentHandler.SetLabelVisible(chooseColorLabel);
                }
                case "JOIN BAD" -> {
                    joinError = true;
                    SceneContentHandler.SetLabelVisible(joinErrorLabel);
                }
            }
        }
        else if (message.contains("COLOR")) {

            //disable color after chosen
            //if two players : disable all colors except one opposite to the sent

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

        }
        else if (message.contains("2 PLAYERS")) {
            twoPlayers = true;
        }
        else if (message == "GAME STARTED") {
            //hide label waiting for players
        }
        else if (message.contains("MOVE")) {
            switch (message) {
                case "MOVE OK" -> {
                    MoveBoard.RedrawBoardMove(startPoint, endPoint, board, myColor);
                    startPoint = null;
                    endPoint = null;
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
                case "YOUR TURN" -> yourTurn = true;
                case "NOT YOUR TURN" -> yourTurn = false;
                case "FINISH TURN" -> finishTurn = true;
            }
        }
        else if (message.contains("YOU")) {
              switch (message) {
                  case "YOU WON":
                      //winning message and disconection. Enter main menu.
                      break;
                  case "YOU LOST":
                      //loosing message and disconnection. Enter main menu.
                      break;
              }

        }
        else if (message.contains("REMOVE")) {

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