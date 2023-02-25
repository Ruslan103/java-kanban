package manager;

import history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static model.Status.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Epic> epics = new HashMap<>(); // мапа с эпиком и его подзадачами
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Task> tasks = new HashMap<>(); // список с задачами
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private int id = 0;

    public int getId() {
        return id;
    }

    protected void setId(int id) {
        this.id = id;
    }

    // создание эпика п.2.4
    @Override
    public void createEpic(Epic epic) {
        id++;
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void createSubtask(Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicID())) {
            return;
        }
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        int epicID = subtask.getEpicID();
        epics.get(epicID).addSubtaskID(id);
        fillEpicStatus(epicID);
        // return id;
    }

    // создание задачи п.2.4
    @Override
    public void createTask(Task task) {
        id++;
        task.setId(id);
        tasks.put(id, task);
        // return id;
    }

    // метод для вывода  эпиков (п.2.1 ТЗ)
    @Override
    public List<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    // метод для вывода названия задач (п.2.1 ТЗ)
    @Override
    public List<Task> getTask() {
        return new ArrayList<>(tasks.values());
    }

    // метод для вывода названия задач (п.2.1 ТЗ)
    @Override
    public List<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    // удаление всех задач (п.2.2 ТЗ)
    @Override
    public void clearAllTasks() {
        for (Integer i : epics.keySet()) {
            historyManager.removeForId(i);
        }
        epics.clear();
        for (Integer i : tasks.keySet()) {
            historyManager.removeForId(i);
        }
        tasks.clear();
        for (Integer i : subtasks.keySet()) {
            historyManager.removeForId(i);
        }
        subtasks.clear();
        id = 0;
    }

    @Override
    public void clearTasks() {
        for (Integer i : tasks.keySet()) {
            historyManager.removeForId(i);
        }
        tasks.clear();
    }

    @Override
    public void clearEpics() {
        for (Integer epicId : epics.keySet()) {
            historyManager.removeForId(epicId);
            for (Integer subtaskId : epics.get(epicId).getSubtasksID()) {
                subtasks.remove(subtaskId);
                historyManager.removeForId(subtaskId);
            }
        }
        epics.clear();
    }

    @Override
    public void clearSubtasks() {
        for (Integer subtasksId : subtasks.keySet()) { // удаляем из истории просмотров и обновляем статус эпика
            historyManager.removeForId(subtasksId);
            int epicID = subtasks.get(subtasksId).getEpicID(); //id эпика
            fillEpicStatus(epicID);// обновляем статус эпика
        }
        subtasks.clear();
    }

    //п.2.3
    @Override
    public Task getTaskForId(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    //п.2.3
    @Override
    public Subtask getSubtaskForId(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    //п.2.3
    @Override
    public Epic getEpicForId(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    //  обновление задачи п.2.5
    @Override
    public void updateTask(Task task) {
        if (!tasks.containsKey(task.getId())) {
            return;
        }
        tasks.put(task.getId(), task);
    }

    // обновление эпика п.2.5
    @Override
    public void updateEpic(Epic epic) {
        if (!epics.containsKey(epic.getId())) {
            return;
        }
        epics.put(epic.getId(), epic);
    }

    // обновление подзадачи п.2.5
    @Override
    public void updateSubtask(Subtask subtask) {
        if (!subtasks.containsKey(subtask.getId())) {
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        int epicID = subtask.getEpicID();
        fillEpicStatus(epicID);
    }

    // обновление статуса эпика
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

    // удаление задач по id п.2.6
    @Override
    public void removeForIdTask(Integer id) {
        if (!tasks.containsKey(id)) {
            return;
        }
        tasks.remove(id);
        historyManager.removeForId(id); // удаление задачи из истории просмотров
    }

    // удаление эпика по id п.2.6
    @Override
    public void removeForIdEpic(Integer id) {
        if (!epics.containsKey(id)) {
            return;
        }
        for (Integer i : epics.get(id).getSubtasksID()) {
            subtasks.remove(i);
            historyManager.removeForId(i);
        }
        historyManager.removeForId(id); // удаление задачи из истории просмотров ТЗ 5
        epics.remove(id);
    }

    // удаление подзадачи по id п.2.6
    @Override
    public void removeForIdSubtask(Integer id) {
        if (!subtasks.containsKey(id)) {
            return;
        }
        int epicID = subtasks.get(id).getEpicID(); // id эпика в подзадаче
        Epic epic = epics.get(epicID); // эпик содержащий id подзадачи
        epic.removeSubtask(id);// даляю подзадачу из эпика
        subtasks.remove(id); // удаляю подзадачу из мапы подзадач
        historyManager.removeForId(id); // удаление задачи из истории просмотров ТЗ 5
        fillEpicStatus(epicID);
    }

    // получение подзадач эпика п.3.1
    @Override
    public List<Subtask> getSubtasksList(Epic epic) {
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
