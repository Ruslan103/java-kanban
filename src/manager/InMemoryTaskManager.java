package manager;

import history.HistoryManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalDateTime;
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
        sort.intersectionOfTasksException(subtask);
        id++;
        subtask.setId(id);
        subtasks.put(id, subtask);
        int epicID = subtask.getEpicID();
        epics.get(epicID).addSubtaskID(id);
        fillEpicStatus(epicID);
        getEndTimeForEpic(epicID);
        sort.setPrioritizedTasks(subtask);
    }

    // создание задачи п.2.4
    @Override
    public void createTask(Task task) {
        sort.intersectionOfTasksException(task);
        id++;
        task.setId(id);
        tasks.put(id, task);
        sort.setPrioritizedTasks(task);
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
        sort.prioritizedTasks.clear();
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
        sort.intersectionOfTasksException(task);
        tasks.put(task.getId(), task);
        sort.setPrioritizedTasks(task);
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
        sort.intersectionOfTasksException(subtask);
        subtasks.put(subtask.getId(), subtask);
        int epicID = subtask.getEpicID();
        fillEpicStatus(epicID);
        sort.setPrioritizedTasks(subtask);
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
        int idSubtask = epic.getSubtasksID().get(0); // id первой подзадачи в эпике
        LocalDateTime startTimeEpic = subtasks.get(idSubtask).getStartTime();
        long durationEpic = 0;
        LocalDateTime endTimeEpic = subtasks.get(idSubtask).getEndTime();
        for (int id : epic.getSubtasksID()) {
            if (subtasks.containsKey(idSubtask)) {
                if (subtasks.get(id).getStartTime() == null || subtasks.get(id).getEndTime() == null) {
                    return;
                }
                if (subtasks.get(id).getStartTime().isBefore(startTimeEpic)) {
                    startTimeEpic = subtasks.get(id).getStartTime();
                }
                if (subtasks.get(id).getEndTime().isAfter(endTimeEpic) && subtasks.get(id).getEndTime() != null) {
                    endTimeEpic = subtasks.get(id).getEndTime();
                }

                durationEpic += subtasks.get(id).getDuration();
            }
        }
        epic.setStartTime(startTimeEpic);
        epic.setDuration(durationEpic);
        epic.setEndTime(endTimeEpic);
    }

    // метод получения отсортированного списка задач
    public TreeSet<Task> getPrioritizedTasks() {
        return sort.getPrioritizedTasks();
    }

    private static class Sort {
        TaskComparator taskComparator = new TaskComparator();
        private final TreeSet<Task> prioritizedTasks = new TreeSet<>(taskComparator);

        public void intersectionOfTasksException(Task task) throws IntersectionOfTasksException {
            for (Task value : prioritizedTasks) {
                if (value.getStartTime() == null) {
                    return;
                }
                if (task.getStartTime() == null) {
                    return;
                }
                if (task.getStartTime().equals(value.getStartTime()) && task.getId() != value.getId()) {
                    throw new IntersectionOfTasksException("Задачи пересекаются по времени выполнения");
                }
                if (task.getEndTime() == null) {
                    return;
                }
                if (value.getEndTime() == null) {
                    return;
                }
                if (task.getEndTime().equals(value.getEndTime()) && task.getId() != value.getId()) {
                    throw new IntersectionOfTasksException("Задачи пересекаются по времени выполнения");
                }
                if (task.getStartTime().isAfter(value.getStartTime()) && task.getStartTime().isBefore(value.getEndTime())
                        || task.getEndTime().isAfter(value.getStartTime())
                        && task.getEndTime().isBefore(value.getEndTime())) {
                    throw new IntersectionOfTasksException("Задачи пересекаются по времени выполнения");
                }
            }
        }

        //метод удаление задач из списка
        public void clearTasks() {
            prioritizedTasks.removeIf(task -> task.getType() == TASK);
        }

        public void clearSubtask() {
            prioritizedTasks.removeIf(task -> task.getType() == SUBTASK);
        }

        public void removeTask(Task task) {
            prioritizedTasks.remove(task);
            prioritizedTasks.remove(task);
        }

        //создание отсортированного списка
        public void setPrioritizedTasks(Task task) {
            prioritizedTasks.add(task);
        }

        public TreeSet<Task> getPrioritizedTasks() {// получение отсортированного списка
            return prioritizedTasks;
        }
    }

    static class TaskComparator implements Comparator<Task> {
        @Override
        public int compare(Task task1, Task task2) {
            if (task1.getStartTime() == null && task2.getStartTime() != null) {
                return 1;
            }
            if (task2.getStartTime() == null && task1.getStartTime() != null) {
                return -1;
            }
            if (task1.getStartTime() == null && task2.getStartTime() == null) {
                return 1;
            }
            if (task1.getStartTime().isBefore(task2.getStartTime())) {
                return -1;
            }
            if (task1.getStartTime().isAfter(task2.getStartTime())) {
                return 1;
            }
            return 0;
        }
    }
}
