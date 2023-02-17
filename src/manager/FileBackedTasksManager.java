package manager;

import history.ManagerSaveException;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTasksManager(File file) {
        this.file = new File("resources/text_file.csv");
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

    static Task TaskFromString(String value) {
        String[] tasks = value.split(",");
        Task task = new Task(tasks[2], tasks[4], Status.valueOf(tasks[3]));
        task.setId(Integer.parseInt(tasks[0]));
        return task;
    }

    static Epic EpicFromString(String value) {
        String[] epics = value.split(",");
        Epic epic = new Epic(epics[2], epics[4], Status.valueOf(epics[3]));
        epic.setId(Integer.parseInt(epics[0]));
        return epic;
    }

    static Subtask SubtaskFromString(String value) {
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
                taskFile.write(task.toStringTask() + "\n");
            }
            for (Epic epic : epics) {
                taskFile.write(epic.toStringEpic() + "\n");
            }
            for (Subtask subtask : subtasks) {
                taskFile.write(subtask.toStringSubtask() + "\n");
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
            while (bufferFile.ready()) {        //пока фил не считается
                String line = bufferFile.readLine();
                if (!line.isEmpty()) {         // если не пустая строка (пустая строка отделяет задачи от id истории в файле)
                    String[] typeTask = line.split(",");
                    if (typeTask[1].equals("TASK")) {
                        Task task = TaskFromString(line);  // TaskFromString(line) -метод создания задачи из строки
                        if (task.getId() > maxId) {
                            maxId = task.getId();
                        }
                        manager.tasks.put(task.getId(), task);
                    }
                    if (typeTask[1].equals("EPIC")) {
                        Epic epic = EpicFromString(line);
                        if (epic.getId() > maxId) {
                            maxId = epic.getId();
                        }
                        manager.epics.put(epic.getId(), epic);
                    }
                    if (typeTask[1].equals("SUBTASK")) {
                        Subtask subtask = SubtaskFromString(line);
                        if (subtask.getId() > maxId) {
                            maxId = subtask.getId();
                        }
                        manager.subtasks.put(subtask.getId(), subtask);
                    }
                    manager.setId(maxId + 1);
                } else {                          // иначе если встретилась пустая строка
                    line = bufferFile.readLine(); // считываем следующую строку - строку с id истории
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

    public static void main(String[] args) {

        // первый запуск с пустым файлом
        Epic epic1 = new Epic("epic1", "description1", Status.NEW);
        Epic epic2 = new Epic("epic2", "description2", Status.NEW);
        Epic epic3 = new Epic("epic3", "description3", Status.NEW);
        Task task1 = new Task("task1", "descriptionT1", Status.NEW);
        // epic3.setId(1);
        Subtask subtask1 = new Subtask("subtask1", "descriptionS1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("subtask2", "descriptionS2", Status.IN_PROGRESS, 1);
        File file = new File("resources/text_file.csv");
        FileBackedTasksManager fileManager = loadFromFile(file);
        fileManager.createEpic(epic1);
        fileManager.createEpic(epic2);
        fileManager.createEpic(epic3);
        fileManager.createSubtask(subtask1);
        // fileManager.createSubtask(subtask2);
        fileManager.createTask(task1);
        fileManager.getEpicForId(1);
        fileManager.getTaskForId(5);
        fileManager.getEpicForId(2);

//        //   второй запуск с заполненным файлом
//        File file = new File("resources/text_file.csv");
//        FileBackedTasksManager fileManager = loadFromFile(file);
//        Epic epic4 = new Epic("epic4", "description4", Status.NEW);
//        Epic epic5 = new Epic("epic5", "description5", Status.NEW);
//        Epic epic6 = new Epic("epic6", "description6", Status.NEW);
//        Task task2 = new Task("task2", "descriptionT2", Status.NEW);
//        // epic3.setId(1);
//        Subtask subtask3 = new Subtask("subtask3", "descriptionS3", Status.NEW, 1);
//        Subtask subtask4 = new Subtask("subtask4", "descriptionS4", Status.NEW, 2);
//        Subtask subtask5 = new Subtask("subtask5", "descriptionS5", Status.NEW, 2);
//        Subtask subtask6 = new Subtask("subtask6", "description6", Status.NEW, 3);
//        fileManager.createEpic(epic4);
//        fileManager.createEpic(epic5);
//        fileManager.createEpic(epic6);
//        fileManager.createSubtask(subtask3);
//        fileManager.createSubtask(subtask4);
//        fileManager.createTask(task2);
//        fileManager.getEpicForId(7);
//        fileManager.getTaskForId(12);
//        fileManager.getEpicForId(9);
    }
}
