import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    double taskId = 0.1;
    double subtaskId = 0.3;
    double epicId = 0.2;
    HashMap<Double, Epic> epics = new HashMap<>(); // мапа с эпиком и его подзадачами
    ///ArrayList<Epic> epics = new ArrayList<>();

    ArrayList<Subtask> subtasksList = new ArrayList<>(); // список с подзадачами
    HashMap<Double, Subtask> subtasks = new HashMap<>();
    HashMap<Double, Task> tasks = new HashMap<>(); // список с задачами

    void CreateEpic(String title, String description, String status) {

        Epic epic = new Epic(title, description, status);

        epic.setId(epicId + 1);
        epicId = epic.getId();
        epics.put(epicId, epic);

        for (int i = 0; i < subtasksList.size(); i++) {
            epic.subtasksId.add(subtasksList.get(i).getId());
        }
        subtasksList = new ArrayList<>();
    }

    void CreateSubtasks(String title, String description, String status) {

        Subtask subtask = new Subtask(title, description, status);
        subtask.setId(subtaskId + 1);
        subtaskId = subtask.getId();
        subtasksList.add(subtask);
        subtasks.put(subtaskId, subtask);
    }

    void CreateTask(String title, String description, String status) {
        Task task = new Task(title, description, status);
        task.setId(taskId + 1);
        taskId = task.getId();
        tasks.put(taskId, task);
    }

    ArrayList<String> getEpics() { // метод для вывода названия эпиков (п.2.1 ТЗ)
        ArrayList<String> epicsTitle = new ArrayList<>();
        for (Double i : epics.keySet()) {
            epicsTitle.add(epics.get(i).getTitle());
        }
        return epicsTitle;
    }

    ArrayList<String> getTask() { // метод для вывода названия задач (п.2.1 ТЗ)
        ArrayList<String> taskTitle = new ArrayList<>();
        for (Double i : tasks.keySet()) {
            taskTitle.add(tasks.get(i).getTitle());

        }
        return taskTitle;
    }

    void clearAllTasks() { // удаление всех задач (п.2.2 ТЗ)
        epics.clear();
        tasks.clear();
        subtasks.clear();
    }

    String getTitleById(double id) { // Получение по идентификатору (п.2.3 ТЗ)
        String title = "Задача по id не найдена";
        for (Double i : epics.keySet()) {
            if (id == i) {
                title = epics.get(i).getTitle();
                return title;
            }
        }
        for (Double i : tasks.keySet()) {
            if (id == i) {
                title = tasks.get(i).getTitle();
                return title;
            }
        }
        for (Double i : subtasks.keySet()) {
            if (id == i) {
                title = subtasks.get(i).getTitle();
                return title;
            }
        }
        return title;
    }

    void CreateTask(Task task) { // создание задачи
        task.setId(taskId + 1);
        taskId = task.getId();
        tasks.put(taskId, task);
    }

    void CreateEpic(Epic epic) {  // создание эпика
        epic.setId(epicId + 1);
        epicId = epic.getId();
        epics.put(epicId, epic);
        for (int i = 0; i < subtasksList.size(); i++) {
            epic.subtasksId.add(subtasksList.get(i).getId());
        }
        subtasksList = new ArrayList<>();
        epics.put(epicId, epic);
    }
    void CreateEpic (Subtask subtask){ //создание подзадачи
        subtask.setId(subtaskId + 1);
        subtaskId = subtask.getId();
        subtasksList.add(subtask);
        subtasks.put(subtaskId, subtask);
    }

}








