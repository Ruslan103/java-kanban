package manager;

public abstract class Managers {

    public TaskManager getDefault(TaskManager manager) {
        return manager;
    }

    HistoryManager getDefaultHistory(HistoryManager historyManager) {
        return historyManager;
    }
}
