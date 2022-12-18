import java.util.ArrayList;

public class Epic extends Task {//Каждый эпик знает, какие подзадачи в него входят //
    //Завершение всех подзадач эпика считается завершением эпика.
    ArrayList<Double> subtasksId;

    public Epic(String title, String description, String status) {
        super(title, description, status);
        this.subtasksId = new ArrayList<>();
    }
}
