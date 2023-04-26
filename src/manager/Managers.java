package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

import java.io.File;

public class Managers {
   static File file = new File("src/resources/text_file.csv");
   static String url= "http://localhost:8070";
    public static TaskManager getDefaultInMemoryTask() {
        return new InMemoryTaskManager();
    }
    // public static TaskManager getDefaultFileBacked(){return  new FileBackedTasksManager(file);}
    public static TaskManager getDefaultFileBacked(){return  new HttpTaskManager(url);}

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
