package manager;

import history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.Duration;
import java.util.*;

import static model.Status.*;
import static model.TypeTask.SUBTASK;
import static model.TypeTask.TASK;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Epic> epics = new HashMap<>(); // мапа с эпиком и его подзадачами
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Task> tasks = new HashMap<>(); // список с задачами
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    private final Sort sort = new Sort();

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
        try {
            sort.intersectionOfTasksException(subtask);
            id++;
            subtask.setId(id);
            subtasks.put(id, subtask);
            int epicID = subtask.getEpicID();
            epics.get(epicID).addSubtaskID(id);
            fillEpicStatus(epicID);
            getEndTimeForEpic(epicID);
            sort.setPrioritizedTasks(subtask);
        } catch (IntersectionOfTasks i) {
            System.err.println(i.getMessage());
        }
    }

    // создание задачи п.2.4
    @Override
    public void createTask(Task task) {
        try {
            sort.intersectionOfTasksException(task);
            id++;
            task.setId(id);
            tasks.put(id, task);
            sort.setPrioritizedTasks(task);
        } catch (IntersectionOfTasks i) {
            System.err.println(i.getMessage());
        }
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
        sort.sortStartTime.clear();
        sort.nullStartTime.clear();
        id = 0;
    }

    @Override
    public void clearTasks() {
        for (Integer i : tasks.keySet()) {
            historyManager.removeForId(i);
        }
        tasks.clear();
        sort.clearTasks(); // удаляю задачи из сортированного списка
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
        sort.clearSubtask();
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
        try {
            sort.intersectionOfTasksException(task);
            tasks.put(task.getId(), task);
            sort.setPrioritizedTasks(task);

        } catch (IntersectionOfTasks e) {
            System.err.println(e.getMessage());
        }
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
        try {
            sort.intersectionOfTasksException(subtask);
            subtasks.put(subtask.getId(), subtask);
            int epicID = subtask.getEpicID();
            fillEpicStatus(epicID);
            sort.setPrioritizedTasks(subtask);
        } catch (IntersectionOfTasks e) {
            System.err.println(e.getMessage());
        }
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
        sort.removeTask(tasks.get(id));
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
        sort.removeTask(subtasks.get(id));
        int epicID = subtasks.get(id).getEpicID(); // id эпика в подзадаче
        Epic epic = epics.get(epicID); // эпик содержащий id подзадачи
        epic.removeSubtask(id);// удаляю подзадачу из эпика
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

    //метод добавления endTime для эпика
    private void getEndTimeForEpic(int epicId) {
        Epic epic = epics.get(epicId);
        int idFirstSubtask = epic.getSubtasksID().get(0);
        int sizeSubtasksID = epic.getSubtasksID().size();
        int idLostSubtask = epic.getSubtasksID().get(sizeSubtasksID - 1);
        Subtask firstSubtask = subtasks.get(idFirstSubtask);
        Subtask lostSubtask = subtasks.get(idLostSubtask);
        if (firstSubtask.getStartTime() == null) {
            return;
        }
        epic.setStartTime(firstSubtask.getStartTime());
        if (lostSubtask.getDuration() == 0) {
            return;
        }
        Duration time = Duration.between(firstSubtask.getStartTime(), lostSubtask.getEndTime());
        epic.setDuration(time.toMinutes());
        epic.setEndTime(lostSubtask.getEndTime());
    }

    // метод получения отсортированного списка задач
    public List<Task> getPrioritizedTasks() {
        return sort.getPrioritizedTasks();
    }

    private static class Sort {
        private final List<Task> prioritizedTasks = new ArrayList<>();
        private final List<Task> nullStartTime = new ArrayList<>();
        private final List<Task> sortStartTime = new ArrayList<>();

        public void intersectionOfTasksException(Task task) throws IntersectionOfTasks {
            for (Task value : sortStartTime) {
                if (task.getStartTime() == null) {
                    return;
                }
                if (task.getStartTime().equals(value.getStartTime()) && task.getId() != value.getId()) {
                    throw new IntersectionOfTasks("Задачи пересекаются по времени выполнения");
                }
                if (task.getEndTime() == null) {
                    return;
                }
                if (task.getEndTime().equals(value.getEndTime()) && task.getId() != value.getId()) {
                    throw new IntersectionOfTasks("Задачи пересекаются по времени выполнения");
                }
                if (task.getStartTime().isAfter(value.getStartTime()) && task.getStartTime().isBefore(value.getEndTime())
                        || task.getEndTime().isAfter(value.getStartTime())
                        && task.getEndTime().isBefore(value.getEndTime())) {
                    throw new IntersectionOfTasks("Задачи пересекаются по времени выполнения");
                }
            }
        }

        //метод удаление задач из списка
        public void clearTasks() {
            nullStartTime.removeIf(task -> task.getType() == TASK);
            for (Task task : sortStartTime) {
                if (task.getType() == TASK) {
                    nullStartTime.remove(task);
                }
            }
        }

        public void clearSubtask() {
            nullStartTime.removeIf(task -> task.getType() == SUBTASK);
            for (Task task : sortStartTime) {
                if (task.getType() == SUBTASK) {
                    nullStartTime.remove(task);
                }
            }
        }

        public void removeTask(Task task) {
            nullStartTime.remove(task);
            sortStartTime.remove(task);
        }

        //создание отсортированного списка
        public void setPrioritizedTasks(Task task) {
            if (task.getStartTime() == null) { //добавляем в список в котором startTime=null
                nullStartTime.add(task);
            } else {
                sortStartTime.add(task); // добавляем в отсортированный список
            }
            sortStartTime.sort(Comparator.comparing(Task::getStartTime));
        }

        public List<Task> getPrioritizedTasks() {// получение отсортированного списка
            prioritizedTasks.clear(); //очищаем список чтобы не дублировать задачи

            prioritizedTasks.addAll(sortStartTime);
            prioritizedTasks.addAll(nullStartTime);
            return prioritizedTasks;
        }
    }
}
