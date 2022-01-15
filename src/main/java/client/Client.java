package client;

import java.io.*;
import java.net.Socket;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Controller controller;

    protected Client(Socket socket, Controller controller) {
        try{
            this.socket = socket;
            this.controller = controller;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        }catch (IOException e){
            System.out.println("Error creating client");
            CloseEverything();
        }

        try {
            recieveMessageFromServer();
        } catch (Exception e) {
            System.out.println("Can`t recieve the server message");
        }

    }

    protected void sendMessageToServer(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        }catch (IOException e) {
            System.out.println("Couldn`t send message");
            e.printStackTrace();
        }
    }

    protected void recieveMessageFromServer(){
            new Thread (() -> {
                while (socket.isConnected()){
                    try {
                        String messageFromServer = bufferedReader.readLine();
                        controller.CheckServerMsg(messageFromServer);
                    }catch (IOException e) {
                        break;
                    }
                }
            }).start();
    }

    protected void CloseEverything(){
        try {
            if(socket != null) {
                socket.close();
                System.out.println("Disconnected socket");
            }
            if (bufferedReader != null){
                bufferedReader.close();
                System.out.println("Disconnected reader");
            }
            if (bufferedWriter != null){
                bufferedWriter.close();
                System.out.println("Disconnected writer");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
