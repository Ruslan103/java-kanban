package manager;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static model.TypeTask.*;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    // метод создания подзадач
    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();

    }

    @Override
    public Task getTaskForId(int id) {
        Task task = super.getTaskForId(id);
        save();
        return task;
    }

    @Override
    public Subtask getSubtaskForId(int id) {
        Subtask subtask = super.getSubtaskForId(id);
        save();
        return subtask;
    }

    @Override
    public Epic getEpicForId(int id) {
        Epic epic = super.getEpicForId(id);
        save();
        return epic;
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeForIdTask(Integer id) {
        super.removeForIdTask(id);
        save();
    }

    @Override
    public void removeForIdEpic(Integer id) {
        super.removeForIdEpic(id);
        save();
    }

    @Override
    public void removeForIdSubtask(Integer id) {
        super.removeForIdSubtask(id);
        save();
    }

    @Override
    public List<Subtask> getSubtasksList(Epic epic) {
        return super.getSubtasksList(epic);
    }

    @Override
    public List<Epic> getEpics() {
        return super.getEpics();
    }

    // метод для вывода названия задач (п.2.1 ТЗ)
    @Override
    public List<Task> getTask() {
        return super.getTask();
    }

    // метод для вывода названия задач (п.2.1 ТЗ)
    @Override
    public List<Subtask> getSubtasks() {
        return super.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    public static Task taskFromString(String value) {
        String[] tasks = value.split(",");
        Task task = new Task(tasks[2], tasks[4], Status.valueOf(tasks[3]));
        task.setId(Integer.parseInt(tasks[0]));
        return task;
    }

    public static Epic epicFromString(String value) {
        String[] epics = value.split(",");
        Epic epic = new Epic(epics[2], epics[4], Status.valueOf(epics[3]));
        epic.setId(Integer.parseInt(epics[0]));
        return epic;
    }

    public static Subtask subtaskFromString(String value) {
        String[] subtasks = value.split(",");
        Subtask subtask = new Subtask(subtasks[2], subtasks[4], Status.valueOf(subtasks[3]), Integer.parseInt(subtasks[5]));
        subtask.setId(Integer.parseInt(subtasks[0]));
        return subtask;
    }

    public void save() {
        try {
            Writer taskFile = new FileWriter(file);
            taskFile.write("id" + "," + "type" + "," + "name" + "," + "status" + "," + "description" + "," + "epic" + "\n");
            List<Task> tasks = new ArrayList<>(super.getTask());
            List<Epic> epics = new ArrayList<>(super.getEpics());
            List<Subtask> subtasks = new ArrayList<>(super.getSubtasks());
            List<Task> history = new ArrayList<>(super.getHistory());
            for (Task task : tasks) {
                taskFile.write(toStringTask(task) + "\n");
            }
            for (Epic epic : epics) {
                taskFile.write(toStringEpic(epic) + "\n");
            }
            for (Subtask subtask : subtasks) {
                taskFile.write(toStringSubtask(subtask) + "\n");
            }
            taskFile.write("\n");
            for (Task task : history) {
                taskFile.write(task.getId() + ",");
            }
            taskFile.close();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
    }

    // метод загрузки файла ТЗ6
    public static FileBackedTasksManager loadFromFile(File file) {
        var manager = new FileBackedTasksManager(file);
        try (Reader taskFile = new FileReader(file);
             BufferedReader bufferFile = new BufferedReader(taskFile)) {
            int maxId = 0;
            String line = bufferFile.readLine();
            while (bufferFile.ready()) {        //пока файл не считается
                line = bufferFile.readLine(); // переход на следующую строку чтобы пропустить id,type,name,status,description,epic
                if (!line.isEmpty()) {         // если не пустая строка (пустая строка отделяет задачи от id истории в файле)
                    String[] typeTask = line.split(",");
                    TypeTask type = TypeTask.valueOf(typeTask[1]);
                    if (type.equals(TASK)) {
                        Task task = taskFromString(line);  // TaskFromString(line) -метод создания задачи из строки
                        if (task.getId() > maxId) {
                            maxId = task.getId();
                        }
                        manager.tasks.put(task.getId(), task);
                    }
                    if (type.equals(EPIC)) {
                        Epic epic = epicFromString(line);
                        if (epic.getId() > maxId) {
                            maxId = epic.getId();
                        }
                        manager.epics.put(epic.getId(), epic);
                    }
                    if (type.equals(SUBTASK)) {
                        Subtask subtask = subtaskFromString(line);
                        if (subtask.getId() > maxId) {
                            maxId = subtask.getId();
                        }
                        manager.subtasks.put(subtask.getId(), subtask);
                    }
                    manager.setId(maxId + 1);
                } else {                          // иначе если встретилась пустая строка
                    line = bufferFile.readLine(); // считываем следующую строку - строку с id истории
                    if (line==null){
                        return manager;
                    }
                    String[] id = line.split(",");
                    for (String i : id) {
                        int parseId = Integer.parseInt(i);
                        if (manager.tasks.containsKey(parseId)) {
                            Task task = manager.getTaskForId(parseId);
                            manager.historyManager.add(task);
                        }
                        if (manager.epics.containsKey(parseId)) {
                            Epic epic = manager.getEpicForId(parseId);
                            manager.historyManager.add(epic);
                        }
                        if (manager.subtasks.containsKey(parseId)) {
                            Subtask subtask = manager.getSubtaskForId(parseId);
                            manager.historyManager.add(subtask);
                        }
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла", e);
        }
        return manager;
    }

    private String toStringTask(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getTitle() + "," + task.getStatus() + "," + task.getDescription();
    }

    private String toStringEpic(Epic epic) {
        return epic.getId() + "," + epic.getType() + "," + epic.getTitle() + "," + epic.getStatus() + "," + epic.getDescription();
    }

    private String toStringSubtask(Subtask subtask) {
        return subtask.getId() + "," + subtask.getType() + "," + subtask.getTitle() + "," + subtask.getStatus() + "," + subtask.getDescription() + "," + subtask.getEpicID();
    }
}
