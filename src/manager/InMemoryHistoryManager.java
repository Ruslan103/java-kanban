package manager;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();
    private static final int sizeList = 10;

    // метод обновления списка истории
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() > sizeList) {
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
