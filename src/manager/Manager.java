package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    private int id = 0;
    private final HashMap<Integer, Epic> epics = new HashMap<>(); // мапа с эпиком и его подзадачами
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>(); // список с задачами

    public int createEpic(Epic epic) { // создание эпика п.2.4
        id ++;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public Integer createSubtasks(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicID())) {
            return null;
        }
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        int epicID = subtask.getEpicID();
        epics.get(epicID).addSubtaskID(id);
        fillEpicStatus(epicID);
        return id;
    }

    public int createTask(Task task) { // создание задачи п.2.4
        id++;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    ArrayList<Epic> getEpics() { // метод для вывода  эпиков (п.2.1 ТЗ)
        return new ArrayList<>(epics.values());
    }

    ArrayList<Task> getTask() { // метод для вывода названия задач (п.2.1 ТЗ)
        return new ArrayList<>(tasks.values());
    }

    ArrayList<Subtask> getSubtasks() { // метод для вывода названия задач (п.2.1 ТЗ)
        return new ArrayList<>(subtasks.values());
    }

    void clearAllTasks() { // удаление всех задач (п.2.2 ТЗ)
        epics.clear();
        tasks.clear();
        subtasks.clear();
    }

    void updateTask(Task task) { //  обновление задачи п.2.5
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    void updateEpics(Epic epic) {// обновление эпика п.2.5
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        String newTitle = epic.getTitle(); // вытаскиваю из переданного эпика его название
        String newDescription = epic.getDescription(); // вытаскиваю из переданного эпика его описание
        Epic newEpic = epics.get(epic.getId());
        newEpic.setTitle(newTitle); // меняю на новое название
        newEpic.setDescription(newDescription); // меняю на новое описание
        epics.put(epic.getId(), newEpic);
    }

    void updateSubtasks(Subtask subtask) { // обновление подзадачи п.2.5
        if (!subtasks.containsValue(subtask)) {
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        int epicID = subtask.getEpicID();
        fillEpicStatus(epicID);
    }

    private void fillEpicStatus(int epicID) {
        boolean isStatusProgress = false;
        boolean isStatusNew = false;
        for (Integer i : epics.get(epicID).getSubtasksID()) { // прохожу циклом по списку id подзадач в эпике
            if (subtasks.containsKey(i)) {
                if (subtasks.get(i).getStatus().equals("IN_PROGRESS")) { // если хоть у одной подзадачи статус в процессе
                    isStatusProgress = true;
                }
                if (subtasks.get(i).getStatus().equals("NEW") || epics.get(epicID).getSubtasksID().isEmpty()) {
                    isStatusNew = true;
                }
            }
        }
        if (isStatusProgress) {
            epics.get(epicID).setStatus("IN_PROGRESS");
        } else if (isStatusNew) {
            epics.get(epicID).setStatus("NEW");
        } else {
            epics.get(epicID).setStatus("DONE");
        }
    }

    void removeForTask(Integer id) { // удаление задач по id п.2.6
        tasks.remove(id);
    }

    void removeForIdEpic(Integer id) { // удаление эпика по id п.2.6
        for (Integer i : epics.get(id).getSubtasksID()) {
            subtasks.remove(i);
        }
        epics.remove(id);
    }

    void removeForSubtasks(Integer id) { // удаление подзадачи по id п.2.6
        int epicID = subtasks.get(id).getEpicID(); // id эпика в подзадаче
        Epic epic = epics.get(epicID); // эпик содержащий id подзадачи
        for (Integer i : epic.getSubtasksID()) {
            if (i.equals(id)) {
                epic.removeSubtask(id); // удаляю подзадачу из эпика
            }
        }
        subtasks.remove(id); // удаляю подзадачу из мапы подзадач
        fillEpicStatus(epicID);
    }

    ArrayList<Subtask> getSubtasksList(Epic epic) { // получение подзадач эпика п.3.1
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksID()) {
            subtaskList.add(subtasks.get(subtaskId));
        }
        return subtaskList;
    }
}
