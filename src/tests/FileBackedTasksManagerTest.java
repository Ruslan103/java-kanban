package tests;

import manager.FileBackedTasksManager;
import manager.ManagerSaveException;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static model.TypeTask.*;

public class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {
    File file = new File("src/tests/text_file.csv");
    List<Task> saveTasks = new ArrayList<>();
    List<Epic> saveEpics = new ArrayList<>();
    List<Subtask> saveSubtasks = new ArrayList<>();
    List<Integer> saveHistory = new ArrayList<>();

    @BeforeEach
    public void BeforeEach() {
        manager = new FileBackedTasksManager(file);
    }

    // метод считывает задачи из файла и записывает их в списки
    private void readFile() {
        try {
            Reader taskFile = new FileReader(file); // считываю файл, чтобы проверить корректность записи
            BufferedReader bufferFile = new BufferedReader(taskFile);
            String line = bufferFile.readLine();
            while (bufferFile.ready()) {
                line = bufferFile.readLine();
                if (!line.isEmpty()) {
                    String[] typeTask = line.split(",");
                    TypeTask type = TypeTask.valueOf(typeTask[1]);
                    if (type.equals(TASK)) { // добавляю задачи из файла в лист
                        Task task = FileBackedTasksManager.taskFromString(line);
                        saveTasks.add(task);
                    }
                    if (type.equals(EPIC)) {
                        Epic epic = FileBackedTasksManager.epicFromString(line);
                        saveEpics.add(epic);
                    }
                    if (type.equals(SUBTASK)) {
                        Subtask subtask = FileBackedTasksManager.subtaskFromString(line);
                        saveSubtasks.add(subtask);
                    }
                } else {
                    line = bufferFile.readLine(); // считываем следующую строку - строку с id истории
                    if (line == null) {
                        return;
                    }
                    String[] id = line.split(",");
                    for (String i : id) {
                        int parseId = Integer.parseInt(i);
                        saveHistory.add(parseId); // добавляем id истории в список
                    }
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи файла", e);
        }
    }

    // тест метода save c историей
    @Test
    public void saveTest() {
        super.createTasksForTestWithHistory(); // создал список задач c историей
        manager.save();
        readFile(); // считываю файл после его заполнения
        Assertions.assertEquals(saveTasks, manager.getTask(), "Некорректные задачи задач в файле"); //equals переопределен в классе Task
        Assertions.assertEquals(saveEpics, manager.getEpics(), "Некорректные эпики в файле"); // сравниваем эпики из файла и в менеджере
        Assertions.assertEquals(saveSubtasks, manager.getSubtasks(), "Некорректные подзадачи в файле");
        Assertions.assertEquals(saveHistory.size(), manager.getHistory().size(), "Неверное количество задач в файле");  // сравниваю количество id в файле с количеством задач в истории менеджера
        for (Task task : manager.getHistory()) { //проверяю, что id задач в истории менеджера содержится в файле
            Assertions.assertTrue(saveHistory.contains(task.getId()), "id задачи не содержится в файле");
        }
    }

    // тест метода save без истории
    @Test
    public void saveWithoutHistory() {
        super.createTasksWithoutHistory();
        manager.save();
        readFile();
        Assertions.assertEquals(saveTasks, manager.getTask(), "Некорректные задачи задач в файле"); //equals переопределен в классе Task
        Assertions.assertEquals(saveEpics, manager.getEpics(), "Некорректные эпики в файле"); // сравниваем эпики из файла и в менеджере
        Assertions.assertEquals(saveSubtasks, manager.getSubtasks(), "Некорректные подзадачи в файле");
        Assertions.assertTrue(manager.getHistory().isEmpty(), "Список истории не пуст");
    }
// тест метода save с пустым списком задач
    @Test
    public void saveWithEmptyTaskList() {
        manager.save();
        Assertions.assertTrue(manager.getTask().isEmpty(),"Список задач не пуст");
        Assertions.assertTrue(manager.getEpics().isEmpty(),"Список эпиков не пуст");
        Assertions.assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач не пуст");
        Assertions.assertTrue(manager.getHistory().isEmpty(),"Список истории не пуст");

    }

    // тест метода loadFile
    @Test
    public void loadFromFileTest() {
        super.createTasksForTestWithHistory();
        FileBackedTasksManager.loadFromFile(file);
        readFile(); // считываю файл и сохраняю в списки
        Assertions.assertEquals(saveTasks, manager.getTask(), "Некорректные задачи задач в файле"); //equals переопределен в классе Task
        Assertions.assertEquals(saveEpics, manager.getEpics(), "Некорректные эпики в файле"); // сравниваем эпики из файла и в менеджере
        Assertions.assertEquals(saveSubtasks, manager.getSubtasks(), "Некорректные подзадачи в файле");
        Assertions.assertEquals(saveHistory.size(), manager.getHistory().size(), "Неверное количество задач в файле");  // сравниваю количество id в файле с количеством задач в истории менеджера
        for (Task task : manager.getHistory()) { //проверяю, что id задач в истории менеджера содержится в файле
            Assertions.assertTrue(saveHistory.contains(task.getId()), "id задачи не содержится в файле");
        }
    }

    //тест метода load с пустым списком истории
    @Test
    public void loadFromFileWithoutHistory() {
        super.createTasksWithoutHistory(); // метод создания задач без добавления в историю
        FileBackedTasksManager.loadFromFile(file); // (нашел ошибку, добавил в метод loadFromFile()  - if (line==null))
        readFile(); // считываю файл и сохраняю в списки
        Assertions.assertEquals(saveTasks, manager.getTask(), "Некорректные задачи задач в файле"); //equals переопределен в классе Task
        Assertions.assertEquals(saveEpics, manager.getEpics(), "Некорректные эпики в файле"); // сравниваем эпики из файла и в менеджере
        Assertions.assertEquals(saveSubtasks, manager.getSubtasks(), "Некорректные подзадачи в файле");
        Assertions.assertTrue(manager.getHistory().isEmpty(), "Список истории не пуст");
    }

    //тест метода loadFromFile с пустым списком задач
    @Test
    public void loadFromFileWithEmptyTaskList(){
        FileBackedTasksManager.loadFromFile(file);
        Assertions.assertTrue(manager.getTask().isEmpty(),"Список задач не пуст");
        Assertions.assertTrue(manager.getEpics().isEmpty(),"Список эпиков не пуст");
        Assertions.assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач не пуст");
        Assertions.assertTrue(manager.getHistory().isEmpty(),"Список истории не пуст");
    }
}
