import java.util.ArrayList;

public class Epic extends Task {//Каждый эпик знает, какие подзадачи в него входят //
    //Завершение всех подзадач эпика считается завершением эпика.
    private ArrayList<Integer> subtasksID;
    //оставил название subtasksID т.к. предложенное имя subtasks есть в менеджере и возникает путаница. Плюс "ID" в конце имени поля говорит что список хранит id

    public ArrayList<Integer> getSubtasksID() {
        return subtasksID;
    }

    public void setSubtasksID(ArrayList<Integer> subtasks) {
        this.subtasksID = subtasks;
    }

    public Epic(String title, String description, String status) {
        super(title, description, status);
        this.subtasksID = new ArrayList<>();
    }
}
