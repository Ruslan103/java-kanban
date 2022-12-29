package manager;

import model.Epic;
import model.Subtask;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = new InMemoryTaskManager();

        Epic epic = new Epic("title1", "description1", Status.NEW);
        Epic epic2 = new Epic("title2", "description2", Status.NEW);
        Epic epic3 = new Epic("title3", "description3", Status.NEW);
        epic3.setId(1);
        Subtask subtask1 = new Subtask("titleS1", "descriptionS1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("titleS2", "descriptionS2", Status.IN_PROGRESS, 1);
        Subtask subtask3 = new Subtask("titleS3", "descriptionS3", Status.NEW, 1);
        Subtask subtask4 = new Subtask("titleS4", "descriptionS4", Status.NEW, 2);

        manager.createEpic(epic);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createSubtask(subtask4);
        manager.getEpicForId(1);
        manager.getEpicForId(2);
        manager.getHistory();
        System.out.println(manager.getHistory());
    }
}
