import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {
    private int id = 0;
    HashMap<Integer, Epic> epics = new HashMap<>(); // мапа с эпиком и его подзадачами
    ArrayList<Subtask> subtasksList = new ArrayList<>(); // список с подзадачами
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Task> tasks = new HashMap<>(); // список с задачами

    public int createEpic(Epic epic) { // создание эпика п.2.4
        epic.setId(id + 1);
        id = epic.getId();
        epics.put(id, epic);
        return id;
    }

    public Integer createSubtasks(Subtask subtask) {
        for (Integer i:epics.keySet()){
            if (i==subtask.getEpicID()) {
                subtask.setId(id + 1);
                id = subtask.getId();
                subtasks.put(id, subtask);
                epics.get(subtask.getEpicID()).getSubtasksID().add(id);
                return id;
            }
        }
        return null;
    }

    public int createTask(Task task) { // создание задачи п.2.4
        task.setId(id + 1);
        id = task.getId();
        tasks.put(id, task);
        return id;
    }

    //    ArrayList<Epic> getEpics() { // метод для вывода  эпиков (п.2.1 ТЗ)
//        ArrayList<Epic> epicsList = new ArrayList<>();
//        for (Integer i: epics.keySet()) {
//            epicsList.add(epics.get(i));
//        }
//        return epicsList;
//    }
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

    String getTitleById(int id) { // Получение по идентификатору (п.2.3 ТЗ)
        String title = "Задача по id не найдена";
        for (Integer i : epics.keySet()) {
            if (id == i) {
                title = epics.get(i).getTitle();
                return title;
            }
        }
        for (Integer i : tasks.keySet()) {
            if (id == i) {
                title = tasks.get(i).getTitle();
                return title;
            }
        }
        for (Integer i : subtasks.keySet()) {
            if (id == i) {
                title = subtasks.get(i).getTitle();
                return title;
            }
        }
        return title;
    }

    void updateTask(Task task) { //  обновление задачи п.2.5
        tasks.put(task.getId(), task);

    }

    void updateEpics(Epic epic) { // обновление эпика п.2.5
        epics.put(epic.getId(), epic);
    }

    void updateSubtasks(Subtask subtask) { // обновление подзадачи п.2.5
        subtasks.put(subtask.getId(), subtask);
    }

    public void fillEpicStatus(int epicId) {//если я Вас правильно понял, метод одновления статуса эпика принимает id эпика?
        boolean l = true;
        for (Integer i : epics.get(epicId).getSubtasksID()) { // прохожу циклом по списку подзадач эпика
            for (Integer j : subtasks.keySet()) {  // и с равниваем с ключом  мапы подзадач
                if (Objects.equals(i, j)) {
                    if (!subtasks.get(j).getStatus().equals("DONE")) { // проверяю статус подзадачи (если хоть у одной подзадаче статус не == "DONE":
                        l = false;
                    }
                }
            }
        }
        if (l) { // если l остался равен true то меняем статус эпика
            epics.get(epicId).setStatus("DONE");
        }

    }
    void removeForTask(Integer id) { // удаление задач по id п.2.6
        tasks.remove(id);
    }

    void removeForIdEpic(Integer id) { // удаление эпика по id п.2.6
        epics.remove(id);
        for (Integer i : subtasks.keySet()) {
            if (subtasks.get(i).getEpicID() == id) {
                subtasks.remove(i);
            }
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








