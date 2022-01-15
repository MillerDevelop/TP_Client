package client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executors;

//Sever used just to echo the information sent!!!
//If you need it, you can use it`s parts

public class TestServer {
    public static void main(String[] args) throws Exception {
        try {
            ServerSocket listener = new ServerSocket(59898);
            System.out.println("The capitalization server is running...");
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                pool.execute(new Capitalizer(listener.accept()));
            }
        } catch (Exception e) {

        }
    }


    private static class Capitalizer implements Runnable {
        private Socket socket;
        BufferedReader bufferedReader;
        BufferedWriter bufferedWriter;

        Capitalizer(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            System.out.println("Connected: " + socket);
            try {
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                String line;
              try {
                while (socket.isConnected()) {
                    line = bufferedReader.readLine();
                    System.out.println(line);
                    if (line.contains("FINISH")) {
                        sendMessage("FINISH TURN");
                    } else if (line.contains("CREATE")) {
                        sendMessage("CREATE BAD");
                    }else if (line.contains("JOIN")) {
                        sendMessage("JOIN OK");
                        sendMessage("2 PLAYERS");
                        sendMessage("COLOR WHITE");
                    } else if (line.contains("MOVE")) {
                        sendMessage("MOVE OK");
                        sendMessage("STEP 0 12 17 5 WHITE");
                    }
                    else {
                        sendMessage(line);
                    }


                }
              }catch (IOException e) {
                  System.out.println("Error:" + socket);
              }
            } catch (IOException e) {
                System.out.println("Error:" + socket);
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.out.println("Error:" + socket);
                }
                System.out.println("Closed: " + socket);
            }
        }

        protected void sendMessage(String line) throws IOException {
            bufferedWriter.write(line.toUpperCase());
            bufferedWriter.newLine();
            bufferedWriter.flush();
        }
    }
}
