package tests;

import history.HistoryManager;
import history.InMemoryHistoryManager;
import model.Status;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HistoryManagerTest {
    Task task1 = new Task("task1", "descriptionT1", Status.NEW);
    Task task2 = new Task("task2", "descriptionT2", Status.NEW);
    Task task3 = new Task("task3", "descriptionT3", Status.NEW);

    HistoryManager historyManager = new InMemoryHistoryManager();

    //тест метода add
    @Test
    public void addTest() {
        historyManager.add(task1);
        Assertions.assertEquals(task1, historyManager.getHistory().get(0), "Задача отсутствует в истории");
    }

    //добавляю две одинаковые задачи
    @Test
    public void doubleAddTask() {
        historyManager.add(task1);
        historyManager.add(task1);
        Assertions.assertEquals(task1, historyManager.getHistory().get(0), "Задача отсутствует в истории");
        Assertions.assertEquals(1, historyManager.getHistory().size(), "Неверное количество задач");
    }

    @Test
    public void removeForIdFirsTask() {
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.removeForId(1);
        Assertions.assertEquals(2, historyManager.getHistory().size(), "Задача не удалена");
        Assertions.assertFalse(historyManager.getHistory().contains(task1), "Задача не удалена");
    }

    //удаляю задачу из середины истории
    @Test
    public void removeForIdMiddleTask() {
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.removeForId(2);
        Assertions.assertEquals(2, historyManager.getHistory().size(), "Задача не удалена");
        Assertions.assertFalse(historyManager.getHistory().contains(task2), "Задача не удалена");
    }

    // удаляю задачу в конце истории
    @Test
    public void removeForIdLostTask() {
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.removeForId(3);
        Assertions.assertEquals(2, historyManager.getHistory().size(), "Задача не удалена");
        Assertions.assertFalse(historyManager.getHistory().contains(task3), "Задача не удалена");
    }

    @Test
    public void getHistoryTest() {
        task1.setId(1);
        task2.setId(2);
        task3.setId(3);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        Assertions.assertEquals(3, historyManager.getHistory().size(), "Неверное количество задач");
        Assertions.assertTrue(historyManager.getHistory().contains(task1), "Задача не найдена");
        Assertions.assertTrue(historyManager.getHistory().contains(task2), "Задача не найдена");
        Assertions.assertTrue(historyManager.getHistory().contains(task3), "Задачане найдена");
    }

    @Test
    public void getTaskFromEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        Assertions.assertTrue(history.isEmpty(), "Список не пуст");
    }
}
