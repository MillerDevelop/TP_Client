package com.example.chineseCheckers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.Node;
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

import static java.lang.System.out;

public class Controller {
    private Point2D startPoint, endPoint;
    //later change to int, using String because running server on a local machine
    private static String Ip;
    private static int port;
    private static boolean connectionError,createRoom, joinRoom, yourTurn, twoPlayers= false;
    private int greetingsChoice;
    private static String [] color;
    private Color myColor;

    @FXML
    private TextField IpField, PortField;

    @FXML
    private Button button;

    //buttons for every color color. Write fx:id

    @FXML
    private Label choosePlayersLabel, errorMessage, chooseColorLabel, welcomeLabel, yourColorLabel;

    @FXML
    private VBox playerCount, greetings, chooseColor, connectServer, parentBox;

    @FXML
    private AnchorPane playGame;

    @FXML
    private GridPane board;

    private Client client;
    private FillBoard fillBoard = new FillBoard();

    @FXML
    protected void gameBoardClicked(MouseEvent event) {
        Circle circle = (Circle) event.getSource();
        out.println(circle.getFill().toString());
        client.sendMessageToServer("TURN");
        //turned true for testing
        yourTurn =true;
        if (yourTurn) {
            if (circle.getFill() == myColor) {
                if (startPoint != null) {
                    MoveBoard.HidePreviousAvailibleMoves(board);
                }
                startPoint = new Point2D(board.getColumnIndex((Node) event.getTarget()), board.getRowIndex((Node) event.getTarget()));
                MoveBoard.ShowAvailibleMoves(startPoint, board);

            } else if (circle.getFill() == Color.web("#4e7c9e")) {

                //write
                startPoint = null;
            }

        }
    }

    @FXML

    protected void buttonBoardMouseClicked(MouseEvent event) {

    }

    //finish with enabling labeles
    @FXML
    protected void greetingsClicked(MouseEvent event) {
        button = (Button) event.getSource();
        SceneContentHandler.SetBoxUnVisible(button.getParent());
        switch (button.getId()){
            case "Create":
                greetingsChoice = 1;
                SceneContentHandler.SetBoxVisible(connectServer);
                break;
            case "Join":
                greetingsChoice = 2;
                SceneContentHandler.SetBoxVisible(connectServer);
                break;
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
            errorMessage.setVisible(true);
            return;
        }

        if (!connectionError) {
            SceneContentHandler.SetBoxUnVisible(button.getParent());
            switch (greetingsChoice) {
                case 1:
                    client.sendMessageToServer("CREATE");
                    //if createRoom
                    SceneContentHandler.SetBoxVisible(playerCount);
                    choosePlayersLabel.setVisible(true);
                    break;
                case 2:
                    client.sendMessageToServer("JOIN");
                    //if joinRoom
                    SceneContentHandler.SetBoxVisible(chooseColor);
                    chooseColorLabel.setVisible(true);
                    break;
            }}

    }

    @FXML
    protected void playerCountClicked(MouseEvent event){
        button = (Button) event.getSource();
        client.sendMessageToServer(button.getText());
        SceneContentHandler.SetBoxUnVisible(button.getParent());
        SceneContentHandler.SetBoxVisible(chooseColor);
    }


    protected void CheckServerMsg(String message) {
        System.out.println(message);
        if (message.contains("CREATE")) {
            switch (message) {
                case "CREATE OK" :
                    //createRoom true
                     break;
                case "CREATE BAD" :
                    //message
                    break;
            }
        }
        if (message.contains("JOIN")) {
            switch (message) {
                case "JOIN OK" :
                    //joinRoom true
                    break;
                case "JOIN BAD" :
                    //message
                    break;
            }
        }
        if (message.contains("COLOR")) {
            color = message.split(" ");
            out.println(color);
            out.println(color[1]);
            System.out.println(board.getChildren().toString());
            FillBoard.FillColor(color[1], board);
            //disable color
            //if two players disable all colors except one or send

        }
        else if (message == "GAME STARTED") {
            //hide label waiting for players
        }
        else if (message.contains("MOVE")) {
           switch (message) {
               case "MOVE OK" :
                   //redraw
                   //startPoint = null
                   //endPoint = null
                   break;
               case "MOVE BAD" :
                   break;
           }
        }
        else if (message.contains("TURN")) {
             switch (message) {
                 case "YOUR TURN":
                     yourTurn = true;
                     break;
                 case "NOT YOUR TURN":
                     //not your turn message
                     break;
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

        //if needed more leave comment
        //currently thinking about making one scene instead of two. If there are two scenes getting some trouble with sending content.
    }

    public void connect() {
        try {
            this.client = new Client(new Socket(Ip, port), this);
            System.out.println("Connected to server");
        } catch (IOException e) {
            errorMessage.setVisible(true);
            connectionError = true;
        }

    }
}