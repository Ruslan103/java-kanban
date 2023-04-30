import server.CustomException;
import server.HttpTaskServer;
import server.KVServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, CustomException {
        KVServer s= new KVServer();
        s.start();
        HttpTaskServer h = new HttpTaskServer();
        h.httpTaskServer();
    }
}
