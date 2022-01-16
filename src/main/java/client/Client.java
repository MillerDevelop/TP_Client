package client;

import java.io.*;
import java.net.Socket;

/** Class responsible for Client-Server connection */

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private Controller controller;

    /** Constructor that fills the class`s controller, buffered reader and buffered writer, as well as socket.
     * Starts the thread receiving server messages.
     *
     * @param socket socket passed from controller.
     * @param controller an instance of the controller running at the moment from controller.
     */

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

    /** Method to send messages to server
     *
     * @param message message that will be sent from client
     */

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

    /** Method that runs a new thread to continuously wait for the messages to be recieved from server until the connection shuts */

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

    /** Method closes socket, buffered reader and buffered writer to disconnect from server */

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

    /**
     * Method for testing. Returns the information whether the socket is closed.
     *
     * @return if socket is closed.
     */

    protected boolean GetSocketClosed(){
        return socket.isClosed();
    }

    /**
     * Method for testing. Returns information whether the buffered reader is closed.
     *
     * @return if buffered reader closed.
     */

    protected boolean GetReaderClosed() {
        try {
            bufferedReader.ready();
        } catch (IOException e) {
            return true;
        }
        return false;
    }

    /**
     * Method for testing. Returns information whether the buffered writer is closed.
     *
     * @return if buffered writer closed.
     */

    protected boolean GetWriterClosed() {
        try {
            bufferedWriter.flush();
        } catch (IOException e) {
            return true;
        }
        return false;
    }

}
