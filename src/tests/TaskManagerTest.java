package tests;

import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

abstract class TaskManagerTest<T extends TaskManager> {
    T manager;

    Task task1 = new Task("task1", "descriptionT1", Status.NEW);
    Task task2 = new Task("task2", "descriptionT2", Status.NEW);
    Task task3 = new Task("task3", "descriptionT3", Status.NEW);
    Epic epic1 = new Epic("epic1", "description1", Status.NEW);
    Epic epic2 = new Epic("epic2", "description2", Status.NEW);
    Epic epic3 = new Epic("epic3", "description3", Status.NEW);
    Subtask subtask1 = new Subtask("subtask1", "descriptionS1", Status.NEW, 1);
    Subtask subtask2 = new Subtask("subtask2", "descriptionS2", Status.NEW, 1);
    Subtask subtask3 = new Subtask("subtask3", "descriptionS3", Status.NEW, 2);

    //метод создания задач и добавления в историю
    protected void createTasksForTestWithHistory() {
        manager.createEpic(epic1);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1); // принадлежит epic1
        manager.createSubtask(subtask2);// принадлежит epic1
        manager.createSubtask(subtask3); // принадлежит epic2
        manager.createEpic(epic3);// эпик не содержит подзадач
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
        manager.getEpicForId(epic1.getId()); // добавляем в историю
        manager.getEpicForId(epic2.getId());
        manager.getEpicForId(epic3.getId());
        manager.getSubtaskForId(subtask1.getId());
        manager.getSubtaskForId(subtask2.getId());
        manager.getSubtaskForId(subtask3.getId());
        manager.getTaskForId(task1.getId());
        manager.getTaskForId(task2.getId());
        manager.getTaskForId(task3.getId());
    }

    //метод создания задач без добавления в историю
    protected void createTasksWithoutHistory() {
        manager.createEpic(epic1); //содержит две подзадачи
        manager.createEpic(epic2);// содержит одну подзадачу
        manager.createSubtask(subtask1); // принадлежит epic1
        manager.createSubtask(subtask2);// принадлежит epic1
        manager.createSubtask(subtask3); // принадлежит epic2
        manager.createEpic(epic3);// эпик не содержит подзадач
        manager.createTask(task1);
        manager.createTask(task2);
        manager.createTask(task3);
    }

    // тест метода обновления статуса
    @Test
    public void fillEpicStatusTest() {
        createTasksForTestWithHistory();
        subtask1.setStatus(Status.DONE);  // меняем статус подзадач
        subtask2.setStatus(Status.DONE);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        Assertions.assertEquals(Status.DONE, epic1.getStatus(), "Статус эпика не DONE"); // проверка изменения статуса Эпика
        subtask1.setStatus(Status.NEW);
        subtask2.setStatus(Status.DONE);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        Assertions.assertEquals(Status.NEW, epic1.getStatus(), "Статус эпика не NEW"); // проверка изменения статуса Эпика
        subtask1.setStatus(Status.IN_PROGRESS);
        subtask2.setStatus(Status.IN_PROGRESS);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        Assertions.assertEquals(Status.IN_PROGRESS, epic1.getStatus(), "Статус эпика не IN_PROGRESS");
    }

    // тест метода createEpic
    @Test
    public void createEpicTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        Epic saveEpic = manager.getEpicForId(epic1.getId());
        Assertions.assertNotNull(saveEpic, "Эпик не найден.");
        Assertions.assertEquals(epic1, saveEpic, "Эпики не совпадают.");
        final List<Epic> epics = manager.getEpics(); // тест метода  List<Epic> getEpicsTest()
        Assertions.assertNotNull(epics, "Эпики не возвращаются");
        Assertions.assertEquals(1, epics.size(), "Неверное количество эпиков.");
        Assertions.assertEquals(epic1, epics.get(0), "Эпики не совпадают");
    }

    @Test
    public void createSubtaskTest() {
        manager.createEpic(epic1);
        manager.createSubtask(subtask1);
        Subtask saveSubtask = manager.getSubtaskForId(subtask1.getId());
        Assertions.assertNotNull(saveSubtask, "Подзадача не найдена.");
        Assertions.assertEquals(subtask1, saveSubtask, "Подзадачи не совпадают.");
        int epicId = subtask1.getEpicID(); // id эпика в подзадаче
        Assertions.assertEquals(epicId, epic1.getId(), "id у подзадачи и эпика не совпадают"); // проверяем, что эпик с таким id есть в списке
        final List<Subtask> subtasks = manager.getSubtasks();
        Assertions.assertNotNull(subtasks, "Подзадачи не возвращаются");
        Assertions.assertEquals(1, subtasks.size(), "Неверное количество подзадач");
        Assertions.assertEquals(subtask1, subtasks.get(0), "Подзадачи не совпадают");
    }

    @Test
    void createTaskTest() {
        manager.createTask(task1);
        Task saveTask = manager.getTaskForId(task1.getId());
        Assertions.assertNotNull(task1, "Задача не найдена.");
        Assertions.assertEquals(task1, saveTask);
        final List<Task> tasks = manager.getTask();
        Assertions.assertNotNull(tasks, "Задачи не возвращаются");
        Assertions.assertEquals(1, tasks.size(), "Неверное количество задач");
    }

    // метод для вывода эпиков (п.2.1 ТЗ)
    @Test
    public void getEpicsTest() {
        createTasksForTestWithHistory();
        Assertions.assertNotNull(manager.getEpics(), "Список эпиков не возвращается");
        Assertions.assertEquals(3, manager.getEpics().size(), "Неверное количество эпиков");
        Assertions.assertEquals(epic1, manager.getEpics().get(0), "Эпики не совпадают");
        Assertions.assertEquals(epic2, manager.getEpics().get(1), "Эпики не совпадают");
        Assertions.assertEquals(epic3, manager.getEpics().get(2), "Эпики не совпадают");
    }

    @Test
    public void getTaskTest() {
        createTasksForTestWithHistory();
        Assertions.assertNotNull(manager.getTask(), "Список задач не возвращается");
        Assertions.assertEquals(3, manager.getTask().size(), "Не верное количество задач");
        Assertions.assertEquals(task1, manager.getTask().get(0), "Задачи не совпадают");
        Assertions.assertEquals(task2, manager.getTask().get(1), "Задави не совпадают");
        Assertions.assertEquals(task3, manager.getTask().get(2), "Задачи не совпадают");
    }

    @Test
    // метод для вывода названия задач (п.2.1 ТЗ)
    public void getSubtasksTest() {
        createTasksForTestWithHistory();
        Assertions.assertNotNull(manager.getSubtasks(), "Список задач не возвращается");
        Assertions.assertEquals(3, manager.getSubtasks().size(), "Неверное количество подзадач");
        Assertions.assertEquals(subtask1, manager.getSubtasks().get(0), "Подзадачи не совпадают");
        Assertions.assertEquals(subtask2, manager.getSubtasks().get(1), "Подзадачи не совпадают");
        Assertions.assertEquals(subtask3, manager.getSubtasks().get(2), "Подзадачи не совпадают");
    }

    @Test
    public void getTaskForIdTest() {
        createTasksForTestWithHistory();
        Assertions.assertNotNull(manager.getTaskForId(task1.getId()), "Задача с таким id не найдена");
        Assertions.assertEquals(task1, manager.getTaskForId(task1.getId()), "Задачи не совпадают");
        boolean isContainsInHistory = manager.getHistory().contains(task1); // проверяем содержится ли в истории эта задача
        Assertions.assertNotNull(manager.getHistory(), "Список истории пуст");
        Assertions.assertTrue(isContainsInHistory, "Задача отсутствует в списке истории");
    }

    @Test
    public void getSubtaskForIdTest() {
        createTasksForTestWithHistory();
        Assertions.assertNotNull(manager.getSubtaskForId(subtask1.getId()), "Подзадача с таким id не найдена");
        Assertions.assertEquals(subtask1, manager.getSubtaskForId(subtask1.getId()), "Подзадачи не совпадают");
        boolean isContainsInHistory = manager.getHistory().contains(subtask1);
        Assertions.assertNotNull(manager.getHistory(), "Список истории пуст");
        Assertions.assertTrue(isContainsInHistory, "Задача отсутствует в списке истории");
    }

    @Test
    public void getEpicForIdTest() {
        createTasksForTestWithHistory();
        Assertions.assertNotNull(manager.getEpicForId(epic1.getId()), "Эпик с таким id не найден");
        Assertions.assertEquals(epic1, manager.getEpicForId(epic1.getId()), "Эпики не совпадают");
        boolean isContainsInHistory = manager.getHistory().contains(epic1);
        Assertions.assertNotNull(manager.getHistory(), "Список истории пуст");
        Assertions.assertTrue(isContainsInHistory, "Задача отсутствует в списке истории");
    }

    @Test
    void clearAllTasksTest() {
        createTasksForTestWithHistory();
        manager.clearAllTasks();
        Assertions.assertTrue(manager.getTask().isEmpty(), "Список задач не очищен");
        Assertions.assertTrue(manager.getEpics().isEmpty(), "Список эпиков не очищен");
        Assertions.assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач не очищен");
        Assertions.assertTrue(manager.getHistory().isEmpty(), "Список истории не очищен");
        Assertions.assertTrue(manager.getPrioritizedTasks().isEmpty(), "Отсортированный список не очищен");
    }

    @Test
    void clearTasksTest() {
        createTasksForTestWithHistory();
        manager.clearTasks(); // запускаем метод очистки задач
        Assertions.assertTrue(manager.getTask().isEmpty(), "Список задач не очищен");
    }

    @Test
    void clearEpicsTest() {
        createTasksForTestWithHistory();
        manager.clearEpics();
        Assertions.assertTrue(manager.getEpics().isEmpty(), "Список эпиков не очищен");
    }

    @Test
    void clearSubtasksTest() {
        createTasksForTestWithHistory();
        manager.clearSubtasks();
        Assertions.assertTrue(manager.getSubtasks().isEmpty(), "Список подзадач не очищен");
    }

    @Test
    void updateTaskTest() {
        createTasksForTestWithHistory();
        Task testTask = new Task("TestTitle", "TestDescription", Status.NEW); // создаем новую задачу
        testTask.setId(1000);// проверяем работу метода с несуществующим id
        manager.updateTask(testTask);
        Assertions.assertFalse(manager.getTask().contains(testTask), "Задача с неверным  id добавлена в список");
        testTask.setId(task1.getId()); // проверяем работу метода с существующим id
        manager.updateTask(testTask);
        Assertions.assertFalse(manager.getTask().contains(task1), "Первая задача осталась в списке");
        Assertions.assertTrue(manager.getTask().contains(testTask), "Новая задача отсутствует в списке");
    }


    // обновление эпика п.2.5
    @Test
    void updateEpicTest() {
        createTasksForTestWithHistory();
        Epic testEpic = new Epic("TestTitle", "TestDescription", Status.NEW);
        testEpic.setId(1000); // проверяем работу метода с несуществующим id
        manager.updateEpic(testEpic);
        Assertions.assertFalse(manager.getEpics().contains(testEpic), "Эпик с неверным id добавлен в список");
        testEpic.setId(epic1.getId());// проверяем работу метода с существующим id
        manager.updateEpic(testEpic);
        Assertions.assertFalse(manager.getEpics().contains(epic1), "Первый эпик остался в списке");
        Assertions.assertTrue(manager.getEpics().contains(testEpic), "Новый эпик отсутствует в списке");
    }

    @Test
        // обновление подзадачи п.2.5
    void updateSubtaskTest() {
        createTasksForTestWithHistory();
        int epicID = subtask3.getEpicID();
        int subtaskID = subtask3.getId();
        Subtask testSubtask = new Subtask("TestTitle", "TestDescription", Status.DONE, epicID);
        testSubtask.setId(1000); // проверяем работу метода с несуществующим id
        manager.updateSubtask(testSubtask);
        Assertions.assertFalse(manager.getSubtasks().contains(testSubtask), "Подзадача с неверным id добавлена в список");
        testSubtask.setId(subtaskID);// проверяем работу метода с существующим id
        manager.updateSubtask(testSubtask);
        Assertions.assertFalse(manager.getSubtasks().contains(subtask3), "Подзадача №1 осталась в списке");
        Assertions.assertTrue(manager.getSubtasks().contains(testSubtask), "Новая подзадача отсутствует в списке");
        Epic epic = manager.getEpicForId(epicID);
        Assertions.assertEquals(Status.DONE, epic.getStatus(), "Cтатус эпика не изменился"); // проверяем смену статуса эпика на DONE
    }

    // тест метода removeForIdTask
    @Test
    void removeForIdTaskWithCorrectId() {
        createTasksForTestWithHistory();
        manager.removeForIdTask(task1.getId());
        Assertions.assertFalse(manager.getTask().contains(task1), "Задача не удалена");
        Assertions.assertFalse(manager.getHistory().contains(task1), "Задача не удалена из истории");
        Assertions.assertFalse(manager.getPrioritizedTasks().contains(task1), "Задача не удалена из сортированного списка");
    }

    // c не верным id
    @Test
    void removeForIdTaskWithUnCorrectId() {
        createTasksForTestWithHistory();
        List<Task> oldTasks = manager.getTask();
        manager.removeForIdTask(1000);
        List<Task> newTasks = manager.getTask();
        Assertions.assertEquals(oldTasks, newTasks, "Список задач изменился");
    }

    @Test
        // тест метода removeForIdEpic
    void removeForIdEpicWithCorrectId() {
        createTasksForTestWithHistory();
        manager.removeForIdEpic(epic1.getId());
        Assertions.assertFalse(manager.getEpics().contains(epic1), "Эпик не удалился");
        Assertions.assertFalse(manager.getHistory().contains(epic1), "Эпик не удалился из  истории");
        for (int idSubtask : epic1.getSubtasksID()) { // проверяем, что подзадача удалена
            Subtask subtask = manager.getSubtaskForId(idSubtask);
            Assertions.assertFalse(manager.getSubtasks().contains(subtask), "Подзадача не удалена");
            Assertions.assertFalse(manager.getHistory().contains(subtask), "Подзадача не удалена из истории");
        }
    }

    @Test
    void removeForIdEpicWithUnCorrectId() {
        createTasksForTestWithHistory();
        List<Epic> oldEpics = manager.getEpics();
        manager.removeForIdEpic(1000);
        List<Epic> newEpics = manager.getEpics();
        Assertions.assertEquals(oldEpics, newEpics, "Список эпиков изменился");
    }

    // тест метода removeForIdSubtask ()
    @Test
    void removeForIdSubtaskWithCorrectId() {
        createTasksForTestWithHistory();
        manager.removeForIdSubtask(subtask1.getId());
        Assertions.assertFalse(manager.getSubtasks().contains(subtask1), "Подзадача не удалена");
        Assertions.assertFalse(manager.getHistory().contains(subtask1), "Подзадача не удалена из истории");
        Assertions.assertFalse(manager.getPrioritizedTasks().contains(subtask1), "Подзадача не удалена из сортированного списка");
    }

    @Test
    void removeForIdSubtaskFromEpic() {
        createTasksForTestWithHistory();
        subtask1.setStatus(Status.DONE); //  эпик №1 содержит подзадачу №1 со статусом DONE и подзадачу №2 со статусом NEW
        int subtask2Id = subtask2.getId();
        manager.removeForIdSubtask(subtask2Id);
        Assertions.assertFalse(epic1.getSubtasksID().contains(subtask2Id), "Подзадача не удалена из эпика");
        Assertions.assertEquals(Status.DONE, epic1.getStatus(), "Статус эпика не изменился"); // проверяем смену статуса эпика
    }

    // тест метода getSubtasksList
    @Test
    public void getSubtasksListTest() {
        createTasksForTestWithHistory();
        for (Subtask subtask : manager.getSubtasksList(epic1)) {
            int subtaskId = subtask.getId(); // id подзадач возвращаемых методом
            List<Integer> epics = epic1.getSubtasksID(); // список подзадач эпика
            Assertions.assertTrue(epics.contains(subtaskId), "Возвращается не верный список подзадач");
        }
        int size1 = epic1.getSubtasksID().size(); // размер списка подзадач у эпика№1
        int size2 = manager.getSubtasksList(epic1).size(); // размер возвращаемого списка методом
        Assertions.assertEquals(size1, size2, "Возвращается не верное количество подзадач");
    }

    // тест метода getHistoryTest()
    @Test
    public void getHistoryTest() {
        createTasksForTestWithHistory();
        Assertions.assertTrue(manager.getHistory().contains(epic1), "Эпик не добавлен в историю");
        Assertions.assertTrue(manager.getHistory().contains(epic2), "Эпик не добавлен в историю");
        Assertions.assertTrue(manager.getHistory().contains(epic3), "Эпик не добавлен в историю");
        Assertions.assertTrue(manager.getHistory().contains(subtask1), "Подзадача не добавлен в историю");
        Assertions.assertTrue(manager.getHistory().contains(subtask2), "Подзадача не добавлен в историю");
        Assertions.assertTrue(manager.getHistory().contains(subtask3), "Подзадача не добавлен в историю");
        Assertions.assertTrue(manager.getHistory().contains(task1), "Задача не добавлен в историю");
        Assertions.assertTrue(manager.getHistory().contains(task2), "Задача не добавлен в историю");
        Assertions.assertTrue(manager.getHistory().contains(task3), "Задача не добавлен в историю");
    }

    // проверяю что у задачи корректно создаются startTime и getTime
    @Test
    public void starTimeAndendTimeTask() {
        task1.setDuration(30);
        LocalDateTime time = LocalDateTime.of(2023, 02, 26, 00, 00);
        LocalDateTime timeForTest = LocalDateTime.of(2023, 02, 26, 00, 30);
        task1.setStartTime(time);
        Assertions.assertEquals(timeForTest, task1.getEndTime(), "не верные даты");
    }

    // проверяю, что у эпика startTime и getTime корректно добавляются
    @Test
    public void starTimeAndendTimeEpic() {
        LocalDateTime subtask1StartTime = LocalDateTime.of(2023, 02, 26, 00, 30);
        subtask1.setStartTime(subtask1StartTime);
        subtask1.setDuration(15);
        LocalDateTime subtask2StartTime = LocalDateTime.of(2023, 02, 26, 00, 00);
        subtask2.setStartTime(subtask2StartTime);
        subtask2.setDuration(30);
        createTasksForTestWithHistory();
        Assertions.assertEquals(subtask2.getStartTime(), epic1.getStartTime(), "Не верная дата начала эпика");
        Assertions.assertEquals(subtask1.getEndTime(), epic1.getEndTime(), "Не верная дата завершения эпика");
    }

    //  проверяю сортировку
    @Test
    public void getPrioritizedTasksTest() {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 02, 26, 00, 03);//task1
        LocalDateTime startTime2 = LocalDateTime.of(2023, 02, 26, 00, 02); //task2
        LocalDateTime startTime3 = LocalDateTime.of(2023, 02, 26, 00, 01); // task3
        LocalDateTime startTime4 = LocalDateTime.of(2023, 02, 26, 00, 04);// epic
        task1.setStartTime(startTime1);
        task2.setStartTime(startTime2);
        task3.setStartTime(startTime3);
        subtask1.setStartTime(startTime4); // subtask1 принадлежит epic1 и у них одинаковые start
        createTasksForTestWithHistory();
        int firstTaskId = manager.getPrioritizedTasks().get(0).getId(); // получаем индекс задач
        int lostTaskId = manager.getPrioritizedTasks().get(manager.getPrioritizedTasks().size() - 1).getId();
        Assertions.assertTrue(manager.getEpics().contains(epic1), "не содержит эпик"); // проверяю, что эпик не удален т.к. есть пересечение по времени с его подзадачей
        Assertions.assertEquals(firstTaskId, task3.getId(), "Неверная сортировка");
        Assertions.assertEquals(lostTaskId, subtask3.getId(), "Неверная сортировка");
    }

    //проверяю, что задача обновится если startTime совпадает с предыдущей задачей
    @Test
    public void intersectionTimeOfTasksMethodeUpdateTask() {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 02, 26, 00, 00);//task1
        Task taskTest = new Task("taskTest", "descriptionTest", Status.NEW);
        taskTest.setId(1); // добавляю одинаковые id и startTime для task1 и taskTest
        taskTest.setStartTime(startTime1);
        task1.setStartTime(startTime1);
        manager.createTask(task1);
        manager.updateTask(taskTest);
        Assertions.assertTrue(manager.getPrioritizedTasks().contains(taskTest), "Новая задача не содержится в отсортированном списке ");
        Assertions.assertTrue(manager.getTask().contains(taskTest), "Новая задача не содержится в списке задач");
    }

    //проверяю что задача не добавляются задачи с одинаковым временем
    @Test
    public void equalsTimeOfTasks() {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 02, 26, 00, 00);//task1
        task2.setStartTime(startTime1);
        task1.setStartTime(startTime1);
        manager.createTask(task1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.createTask(task2), "Исключение не выбрасывается");
    }

    @Test
    public void intersectionTimeOfTasks() {
        LocalDateTime startTime1 = LocalDateTime.of(2023, 02, 26, 00, 00);//task1
        LocalDateTime startTime2 = LocalDateTime.of(2023, 02, 26, 00, 05); //task2
        task1.setStartTime(startTime1);
        task1.setDuration(30);
        task2.setStartTime(startTime2);
        manager.createTask(task1);
        Assertions.assertThrows(RuntimeException.class, () -> manager.createTask(task2), "Исключение не выбрасывается");
    }
}
