package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static manager.Status.*;

class InMemoryTaskManager implements TaskManager {
    private int id = 0;
    private final HashMap<Integer, Epic> epics = new HashMap<>(); // мапа с эпиком и его подзадачами
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>(); // список с задачами
    HistoryManager historyManager = new InMemoryHistoryManager();

    // создание эпика п.2.4
    @Override
    public int createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
        return id;
    }

    @Override
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

    @Override
    // создание задачи п.2.4
    public int createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        return id;
    }

    @Override
    // метод для вывода  эпиков (п.2.1 ТЗ)
    public ArrayList<Epic> getEpics() {

        return new ArrayList<>(epics.values());
    }

    @Override
    public ArrayList<Task> getTask() { // метод для вывода названия задач (п.2.1 ТЗ)
        return new ArrayList<>(tasks.values());
    }

    @Override
    // метод для вывода названия задач (п.2.1 ТЗ)
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    // удаление всех задач (п.2.2 ТЗ)
    public void clearAllTasks() {
        epics.clear();
        tasks.clear();
        subtasks.clear();
    }

    @Override
    public void clearTasks() {
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        epics.clear();
    }

    @Override
    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public Task getTaskForId(int id) { //п.2.3
        historyManager.add(tasks.get(id)); // проверяю размер списка просмотра истории
        return tasks.get(id);

    }

    @Override
    public Subtask getSubtaskForId(int id) {//п.2.3
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpicForId(int id) {//п.2.3
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    //  обновление задачи п.2.5
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    @Override
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

    @Override
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
            epic.setStatus(NEW);
            return;
        }
        for (Integer i : epic.getSubtasksID()) { // прохожу циклом по списку id подзадач в эпике
            if (subtasks.containsKey(i)) {
                if (subtasks.get(i).getStatus().equals(IN_PROGRESS)) { // если хоть у одной подзадачи статус в процессе
                    isStatusProgress = true;
                }
                if (subtasks.get(i).getStatus().equals(NEW)) {
                    isStatusNew = true;
                }
            }
        }
        if (isStatusProgress) {
            epic.setStatus(IN_PROGRESS);
        } else if (isStatusNew) {
            epic.setStatus(NEW);
        } else {
            epic.setStatus(DONE);
        }
    }

    @Override
    // удаление задач по id п.2.6
    public void removeForIdTask(Integer id) {
        if (!tasks.containsKey(id)) {
            return;
        }
        tasks.remove(id);
    }

    @Override
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

    @Override
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

    @Override
    // получение подзадач эпика п.3.1
    public ArrayList<Subtask> getSubtasksList(Epic epic) {
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer subtaskId : epic.getSubtasksID()) {
            subtaskList.add(subtasks.get(subtaskId));
        }
        return subtaskList;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
