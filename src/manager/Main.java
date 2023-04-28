package manager;

import model.Status;
import model.Task;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
       // Task task1 = new Task("task1", "descriptionT1", Status.NEW);
        KVServer s= new KVServer();
        s.start();
        HttpTaskServer h = new HttpTaskServer();
        h.httpTaskServer();
    }
}
