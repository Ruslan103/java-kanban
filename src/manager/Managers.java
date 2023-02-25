package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

import java.io.File;

public class Managers {
    public static TaskManager getDefaultInMemoryTask() {
        return new InMemoryTaskManager();
    }
    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
