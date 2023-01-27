package manager;

import model.Task;


import java.util.ArrayList;
import java.util.List;

public interface HistoryManager {
    void add(Task task);

    // удаление из истории просмотров по id ТЗ 5
    void removeForId(int id);


    List<Task> getHistory();
}
