package manager;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> history = new LinkedList<>();

    // метод обновления списка истории
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (history.size() > 9) {
            history.remove(0);
        }
        history.add(task);
    }

    // метод получения списка истории ТЗ4
    @Override
    public List<Task> getHistory() {
        return history;
    }
}
