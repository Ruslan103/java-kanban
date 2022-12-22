package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {
    private int id = 0;
    private final HashMap<Integer, Epic> epics = new HashMap<>(); // мапа с эпиком и его подзадачами
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>(); // список с задачами

    public int createEpic(Epic epic) { // создание эпика п.2.4
        epic.setId(id ++);
        id = epic.getId();
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
        int epicID=subtask.getEpicID();
        epics.get(epicID).SubtaskID(id);
        boolean isStatusProgress =false;
        boolean isStatusNew = false;
        for (Integer i : epics.get(epicID).getSubtasksID()) { // прохожу циклом по списку
            if (subtasks.containsKey(i)){
                if (subtasks.get(i).getStatus().equals("IN_PROGRESS")) { // если хоть у одной подзадачи статус в процессе
                 isStatusProgress=true;
                }
                if (subtasks.get(i).getStatus().equals("NEW")){
                    isStatusNew=true;
                }
            }
            if (isStatusProgress) {
                epics.get(epicID).setStatus("IN_PROGRESS");
                return id;
            }
            else  if (isStatusNew) {
                epics.get(epicID).setStatus("NEW");
                return id;
            }
            else {
                epics.get(epicID).setStatus("DONE");
                return id;
                }
        }
        return id;
    }

    public int createTask(Task task) { // создание задачи п.2.4
        task.setId(id + 1);
        id = task.getId();
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
        if (!tasks.containsKey(task.getId())){
            return;
        }
        tasks.put(task.getId(), task);
    }
    void updateEpics(Epic epic) {// обновление эпика п.2.5
        // ?? из условия задачи не понятно, что будет передаваться в этот метод (сам эпик или его id, названи и описание)
        // если метод всетаки принимает эпик то реализую следующим образом:
        if (!epics.containsKey(epic.getId())){
            return;
        }
        String newTitle=epic.getTitle(); // вытаскиваю из переданного эпика его название
        String newDescription =epic.getDescription(); // вытаскиваю из переданного эпика его описание
        Epic newEpic = epics.get(epic.getId());
        newEpic.setTitle(newTitle); // меняю на новое название
        newEpic.setDescription(newDescription); // меняю на новое описание
        epics.put(epic.getId(),newEpic);
    }

    void updateSubtasks(Subtask subtask) { // обновление подзадачи п.2.5
        if (!subtasks.containsValue(subtask)){
            return;
        }
        subtasks.put(subtask.getId(), subtask);
        int epicID=subtask.getEpicID();
        boolean isStatusProgress =false;
        boolean isStatusNew = false;
        for (Integer i : epics.get(epicID).getSubtasksID()) { // прохожу циклом по списку id подзадач в эпике
            if (subtasks.containsKey(i)){
                if (subtasks.get(i).getStatus().equals("IN_PROGRESS")) { // если хоть у одной подзадачи статус в процессе
                    isStatusProgress=true;
                }
                if (subtasks.get(i).getStatus().equals("NEW")){
                    isStatusNew=true;
                }
            }
            if (isStatusProgress) {
                epics.get(epicID).setStatus("IN_PROGRESS");
                return;
            }
            else  if (isStatusNew) {
                epics.get(epicID).setStatus("NEW");
                return;
            }
            else {
                epics.get(epicID).setStatus("DONE");
                return;
            }
        }
    }
    public void fillEpicStatus(int epicID) {
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
            if (isStatusProgress) {
                epics.get(epicID).setStatus("IN_PROGRESS");
                return;
            } else if (isStatusNew) {
                epics.get(epicID).setStatus("NEW");
                return;
            } else {
                epics.get(epicID).setStatus("DONE");
                return;
            }
        }
    }
    void removeForTask(Integer id) { // удаление задач по id п.2.6
        tasks.remove(id);
    }

    void removeForIdEpic(Integer id) { // удаление эпика по id п.2.6
        epics.remove(id);
        for (Integer i:epics.get(id).getSubtasksID()){
            subtasks.remove(i);
        }
    }

    void removeForSubtasks(Integer id) { // удаление подзадачи по id п.2.6
        subtasks.remove(id); // удаляю подзадачу из мапы подзадач
        for (Integer i : epics.keySet()) { // прохожжу циклом по значениям мапы эпика, чтобы удалить из него id
            for (int j = 0; j < epics.get(i).getSubtasksID().size(); j++) { // прохожу по списку id подзадач в каждом эпике
                if (epics.get(i).getSubtasksID().get(j).equals(id)) {
                    epics.get(i).getSubtasksID().remove(id); // удаляем id из хэш мапы
                }
            }
        }
    }

    ArrayList<Subtask> getSubtasksList(Epic epic) { // получение подзадач эпика п.3.1
        ArrayList<Subtask> subtaskList = new ArrayList<>();
        for (Integer i : epic.getSubtasksID()) {
            for (Integer j : subtasks.keySet()) {
                if (i.equals(j)) {
                    subtaskList.add(subtasks.get(j));
                }
            }
        }
        return subtaskList;
    }
}








