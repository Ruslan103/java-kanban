package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    int createEpic(Epic epic);

    Integer createSubtask(Subtask subtask);

    int createTask(Task task);

    // метод для вывода  эпиков (п.2.1 ТЗ)
    List<Epic> getEpics();

    List<Task> getTask();

    // метод для вывода названия задач (п.2.1 ТЗ)
    List<Subtask> getSubtasks();

    // удаление всех задач (п.2.2 ТЗ)
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
    void removeForIdSubtasks(Integer id);

    // получение подзадач эпика п.3.1
    ArrayList<Subtask> getSubtasksList(Epic epic);

    ArrayList<Node<Task>> getHistory();
}
