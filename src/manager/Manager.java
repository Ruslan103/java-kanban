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

    // создание эпика п.2.4
    public int createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    public Integer createSubtask(Subtask subtask) {
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

    // создание задачи п.2.4
    public int createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    // метод для вывода  эпиков (п.2.1 ТЗ)
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public ArrayList<Task> getTask() { // метод для вывода названия задач (п.2.1 ТЗ)
        return new ArrayList<>(tasks.values());
    }

    // метод для вывода названия задач (п.2.1 ТЗ)
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // удаление всех задач (п.2.2 ТЗ)
    public void clearAllTasks() {
        epics.clear();
        tasks.clear();
        subtasks.clear();
    }

    public void clearTasks() {
        tasks.clear();
    }

    public void clearEpics() {
        epics.clear();
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    //  обновление задачи п.2.5
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    // обновление эпика п.2.5
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        Epic newEpic = epics.get(epic.getId());
        newEpic.setTitle(epic.getTitle()); // меняю на новое название
        newEpic.setDescription(epic.getDescription()); // меняю на новое описание
        epics.put(epic.getId(), newEpic);
    }

    // обновление подзадачи п.2.5
    public void updateSubtask(Subtask subtask) {
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
        Epic epic = epics.get(epicID);
        ArrayList<Integer> subtaskIds = epic.getSubtasksID();
        if (subtaskIds.isEmpty()) {
            epic.setStatus("NEW");
            return;
        }
        for (Integer i : epic.getSubtasksID()) { // прохожу циклом по списку id подзадач в эпике
            if (subtasks.containsKey(i)) {
                if (subtasks.get(i).getStatus().equals("IN_PROGRESS")) { // если хоть у одной подзадачи статус в процессе
                    isStatusProgress = true;
                }
                if (subtasks.get(i).getStatus().equals("NEW")) {
                    isStatusNew = true;
                }
            }
        }
        if (isStatusProgress) {
            epic.setStatus("IN_PROGRESS");
        } else if (isStatusNew) {
            epic.setStatus("NEW");
        } else {
            epic.setStatus("DONE");
        }
    }

    // удаление задач по id п.2.6
    public void removeForIdTask(Integer id) {
        if (!tasks.containsKey(id)) {
            return;
        }
        tasks.remove(id);
    }

    // удаление эпика по id п.2.6
    public void removeForIdEpic(Integer id) {
        if (!epics.containsKey(id)) {
            return;
        }
        for (Integer i : epics.get(id).getSubtasksID()) {
            subtasks.remove(i);
        }
        epics.remove(id);
    }

    // удаление подзадачи по id п.2.6
    public void removeForIdSubtasks(Integer id) {
        if (!subtasks.containsKey(id)) {
            return;
        }
        int epicID = subtasks.get(id).getEpicID(); // id эпика в подзадаче
        Epic epic = epics.get(epicID); // эпик содержащий id подзадачи
        epic.removeSubtask(id);// даляю подзадачу из эпика
        subtasks.remove(id); // удаляю подзадачу из мапы подзадач
        fillEpicStatus(epicID);
    }

    // получение подзадач эпика п.3.1
    public ArrayList<Subtask> getSubtasksList(Epic epic) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksID()) {
            subtaskList.add(subtasks.get(subtaskId));
        }
        return subtaskList;
    }
}
