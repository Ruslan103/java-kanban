package manager;

import history.HistoryManager;
import history.InMemoryHistoryManager;

public class Managers {
    public static TaskManager getDefaultInMemoryTask() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}
