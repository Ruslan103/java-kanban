import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Manager {
    double taskId = 0.1;
    double subtaskId = 0.3;
    double epicId = 0.2;
    HashMap<Double, Epic> epics = new HashMap<>(); // мапа с эпиком и его подзадачами
    ArrayList<Subtask> subtasksList = new ArrayList<>(); // список с подзадачами
    HashMap<Double, Subtask> subtasks = new HashMap<>();
    HashMap<Double, Task> tasks = new HashMap<>(); // список с задачами

    void CreateEpic(String title, String description, String status) {

        Epic epic = new Epic(title, description, status);

        epic.setId(epicId + 1);
        epicId = epic.getId();
        epics.put(epicId, epic);

        for (Subtask subtask : subtasksList) {
            epic.subtasksId.add(subtask.getId());
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
        if (id % 1 == 0.1) {
            for (Double i : epics.keySet()) {
                if (id == i) {
                    title = epics.get(i).getTitle();
                    return title;
                }
            }
        }
        if (id % 1 == 0.2) {
            for (Double i : tasks.keySet()) {
                if (id == i) {
                    title = tasks.get(i).getTitle();
                    return title;
                }
            }
        }
        if (id % 1 == 0.3) {
            for (Double i : subtasks.keySet()) {
                if (id == i) {
                    title = subtasks.get(i).getTitle();
                    return title;
                }
            }
        }
        return title;
    }

    void CreateTask(Task task) { // создание задачи п.2.4
        task.setId(taskId + 1);
        taskId = task.getId();
        tasks.put(taskId, task);
    }

    void CreateEpic(Epic epic) {  // создание эпика п.2.4
        epic.setId(epicId + 1);
        epicId = epic.getId();
        epics.put(epicId, epic);
        for (Subtask subtask : subtasksList) {
            epic.subtasksId.add(subtask.getId());
        }
        subtasksList = new ArrayList<>();
        epics.put(epicId, epic);
    }

    void CreateSubtask(Subtask subtask) { //создание подзадачи п.2.4
        subtask.setId(subtaskId + 1);
        subtaskId = subtask.getId();
        subtasksList.add(subtask);
        subtasks.put(subtaskId, subtask);
    }


    void updateTask(Double id, Task task) { //  обновление задачи п.2.5
        task.setId(id);
        tasks.put(id, task);
    }

    void updateEpics(Double id, Epic epic) { // обновление эпика п.2.5
        epic.setId(id);
        epics.put(id, epic);
    }

    void updateSubtasks(Double id, Subtask subtask) {// обновление подзадачи п.2.5 и п.4
        subtask.setId(id);
        subtasks.put(id, subtask);
        boolean l = true; // п.4
        if (Objects.equals(subtask.getStatus(), "DONE")) { // если у подзадачи статус DONE то
            for (Epic epic : epics.values()) { // проходим циклом по мапе эпика и ищем эпик в котором есть эта подзадача
                if (epic.subtasksId.contains(id)) { // если нашли эпик содержайщий id подзадачи

                    for (Double idSubtask : epic.subtasksId) { // проходим по всему списку id подзадач этого эпика

                        for (Double j : subtasks.keySet()) { // и с равниваем с ключом  мапы подзадач
                            if (idSubtask.equals(j)) { // если нашли id из списка то смотрим его статус;
                                if (!subtasks.get(j).getStatus().equals("DONE")) {
                                    l = false; // если хоть одина подзадача не имеет статус "DONE" то l=false
                                }
                            }

                        }
                    }
                }
                if (l) { // если l остался равен true то меняем статус эпика
                    epic.setStatus("DONE");
                }
            }
        }
    }

    void removeForTask(Double id) { // удаление задач по id п.2.6
        tasks.remove(id);
    }

    void removeForIdEpic(Double id) { // удаление эпика по id п.2.6
        epics.remove(id);
    }

    void removeForSubtasks(Double id) { // удаление подзадачи по id п.2.6
        subtasks.remove(id); // удаляю подзадачу из мапы подзадач
        for (Double i : epics.keySet()) { // прохожжу циклом по значениям мапы эпика, чтобы удалить из него id
            for (int j = 0; j < epics.get(i).subtasksId.size(); j++) { // прохожу по списку id подзадач в каждом эпике
                if (epics.get(i).subtasksId.get(j).equals(id)) {
                    epics.get(i).subtasksId.remove(id); // удаляем id из хэш мапы
                }
            }
        }
    }


    ArrayList<String> getSubtask(Epic epic) { // получение подзадач эпика п.3.1
        ArrayList<String> titleSubtask = new ArrayList<>();
        for (Epic i : epics.values()) {// проходим циклом по значениям мапы эпика
            if (i.equals(epic)) {// если принятый эпик равен эпикку в мапе то создаем список id одзадач эпика:
                ArrayList<Double> id = epic.subtasksId;
                for (Double j : subtasks.keySet()) { // прохожу по ключам мапы подзадач
                    for (Double k : id) { // прохожу по списку id
                        if (Objects.equals(j, k)) { // сравниваю ключ мапы подзадач с  каждым элементом из списка id если равны:
                            titleSubtask.add(subtasks.get(j).getTitle());// то записываю наименование задачи в titleSubtask
                        }
                    }
                }
            }
        }
        return titleSubtask; // возвращаю список с названием позадач
    }
}










