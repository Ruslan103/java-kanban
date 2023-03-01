package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.List;
import java.util.Set;

public interface TaskManager {
    void createEpic(Epic epic);
    void createSubtask(Subtask subtask);
    void createTask(Task task);
    // метод для вывода эпиков (п.2.1 ТЗ)
    List<Epic> getEpics();

    List<Task> getTask();

    // метод для вывода названия задач (п.2.1 ТЗ)
    List<Subtask> getSubtasks();


    Task getTaskForId(int id);

    Subtask getSubtaskForId(int id);

    Epic getEpicForId(int id);

    void clearAllTasks();

    void clearTasks();

    void clearEpics();

    void clearSubtasks();

    //  обновление задачи п.2.5
    void updateTask(Task task);

    // обновление эпика п.2.5
    void updateEpic(Epic epic);

    // обновление подзадачи п.2.5
    void updateSubtask(Subtask subtask);

    // удаление задач по id п.2.6
    void removeForIdTask(Integer id);

    // удаление эпика по id п.2.6
    void removeForIdEpic(Integer id);

    // удаление подзадачи по id п.2.6
    void removeForIdSubtask(Integer id);

    // получение подзадач эпика п.3.1
    List<Subtask> getSubtasksList(Epic epic);

    List<Task> getHistory();

//    void setPrioritizedTasks(Task task);
//
   List<Task> getPrioritizedTasks();
}
