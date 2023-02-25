package tests;

import manager.InMemoryTaskManager;
import model.Status;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    public void BeforeEach() {
        manager = new InMemoryTaskManager();
    }

    // тест метода обновления статуса
    @Test
    public void fillEpicStatusTest(){
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
}
