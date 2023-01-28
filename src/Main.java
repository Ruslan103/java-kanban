import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Status;
import model.Subtask;

public class Main {
    public static void main(String[] args) {

        TaskManager manager = new InMemoryTaskManager();

        Epic epic = new Epic("epic1", "description1", Status.NEW);
        Epic epic2 = new Epic("epic2", "description2", Status.NEW);
        Epic epic3 = new Epic("epic3", "description3", Status.NEW);
        epic3.setId(1);
        Subtask subtask1 = new Subtask("subtask1", "descriptionS1", Status.NEW, 1);
        Subtask subtask2 = new Subtask("subtask2", "descriptionS2", Status.IN_PROGRESS, 1);
        Subtask subtask3 = new Subtask("subtask3", "descriptionS3", Status.NEW, 1);
        Subtask subtask4 = new Subtask("subtask4", "descriptionS4", Status.NEW, 2);
        Subtask subtask5 = new Subtask("subtask5", "descriptionS5", Status.NEW, 2);
        Subtask subtask6=new Subtask("subtask6","description6",Status.NEW,3);

        manager.createEpic(epic); // id 1
        manager.createEpic(epic2); // id 2
        manager.createEpic(epic3); // id 3
        manager.createSubtask(subtask1);//id 4 e-1
        manager.createSubtask(subtask2);//id 5 e-1
        manager.createSubtask(subtask3); // id 6 e-1
        manager.createSubtask(subtask4);// id 7 e-2
        manager.createSubtask(subtask5); // id 8 e-2
        manager.createSubtask(subtask6); //id 9 e-3
        manager.getEpicForId(2);
        manager.getEpicForId(1);
        manager.getEpicForId(3);
        manager.getSubtaskForId(4); // s-1 e-1
        manager.getSubtaskForId(5); // s-2 e-1
        manager.getSubtaskForId(8); //
        manager.getSubtaskForId(6);
        manager.getSubtaskForId(7);
        manager.getSubtaskForId(9);
       manager.removeForIdEpic(2);
        //manager.clearAllTasks();
     //  manager.clearEpics();
      //  manager.getHistory();
        System.out.println(manager.getHistory());
    }
}
