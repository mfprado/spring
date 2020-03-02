import server.Server;

import java.io.IOException;

public class App {

    public static void main(String[] args) {
        Server server = new Server(9090);

        try {
            server.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.exit(-1);
    }
}
