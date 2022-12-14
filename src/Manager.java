import java.util.ArrayList;
import java.util.HashMap;

public class Manager {
    double subTaskId = 0.3;
    double epicId = 0.2;
    HashMap <Epic,ArrayList<Subtask>> epics=new HashMap<>();
    // ArrayList<Epic> epics = new ArrayList<>();
    ArrayList<Subtask> subtasks = new ArrayList<>();

    void CreateEpic(String title, String description, String status) {

        Epic epic = new Epic(title, description, status);

        epic.setId(epicId + 1);
        epicId = epic.getId();
        epics.put(epic,subtasks);
       // epic.subtasks = subtasks;
        subtasks = new ArrayList<>();
    }

    void CreateSubtasks(String title, String description, String status) {

        Subtask subtask = new Subtask(title, description, status);
        subtask.setId(subTaskId + 1);
        subTaskId = subtask.getId();
        subtasks.add(subtask);
    }


}



