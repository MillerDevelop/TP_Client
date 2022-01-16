package client;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testCloseEverything() throws IOException {
        new ServerSocket(59898);
        Controller controller = new Controller();
        Client client = new Client(new Socket("localhost", 59898), controller);
        client.CloseEverything();

        assertTrue(client.GetSocketClosed());
        assertTrue(client.GetReaderClosed());
        assertTrue(client.GetWriterClosed());
    }

}