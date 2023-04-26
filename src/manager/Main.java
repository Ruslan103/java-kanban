package manager;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        HttpTaskServer h = new HttpTaskServer();
        KVServer s= new KVServer();
        h.httpTaskServer();
        s.start();
    }
}
