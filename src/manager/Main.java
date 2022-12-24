package manager;

import model.Epic;
import model.Subtask;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager();
        Epic epic = new Epic("title1", "description1", "NEW");
        Epic epic2 = new Epic("title2", "description2", "NEW");
        Epic epic3 = new Epic("title3", "description3", "NEW");
        epic3.setId(1);
        Subtask subtask1 = new Subtask("titleS1", "descriptionS1", "NEW", 1);
        Subtask subtask2 = new Subtask("titleS2", "descriptionS2", "IN_PROGRESS", 1);
        Subtask subtask3 = new Subtask("titleS3", "descriptionS3", "NEW", 1);
        Subtask subtask4 = new Subtask("titleS4", "descriptionS4", "NEW", 2);

        manager.createEpic(epic);
        manager.createEpic(epic2);
        manager.createSubtask(subtask1);
        manager.createSubtask(subtask2);
        manager.createSubtask(subtask3);
        manager.createSubtask(subtask4);
        manager.updateEpic(epic3);
        manager.removeForIdSubtasks(11);
    }
}
