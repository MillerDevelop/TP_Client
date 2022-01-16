package client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ControllerTest {

    @Test
    void testIsConnected() throws IOException {
        new ServerSocket(59898);
        String Ip = "localhost";
        int port = 59898;
        Controller controller = new Controller();
        new Client(new Socket(Ip, port), controller);

        assertEquals(false, controller.getConnectionError());
    }

    @Test
    void testAcceptingServerMsg() {
        Controller controller = new Controller();
        controller.CheckServerMsg("2 PLAYERS");

        assertTrue(controller.get2Players());
    }
}