package manager;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int SIZE_LIST = 10;
    private final LinkedList<Task> history = new LinkedList<>();

    // метод обновления списка истории
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() > SIZE_LIST) {
            history.removeFirst();
        }
        history.add(task);
    }

    // метод получения списка истории ТЗ4
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
