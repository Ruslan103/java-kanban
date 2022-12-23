package model;

import java.util.ArrayList;

public class Epic extends Task {//Каждый эпик знает, какие подзадачи в него входят //
    //Завершение всех подзадач эпика считается завершением эпика.
    private ArrayList<Integer> subtasksID;

    public Epic(String title, String description, String status) {
        super(title, description, status);
        this.subtasksID = new ArrayList<>();
    }

    public ArrayList<Integer> getSubtasksID() {
        return subtasksID;
    }

    public void setSubtasksID(ArrayList<Integer> subtasks) {
        this.subtasksID = subtasks;
    }

    public void addSubtaskID(int id) {
        subtasksID.add(id);
    }

    public void removeSubtask(int subtaskId) {
        subtasksID.remove(Integer.valueOf(subtaskId)); // одельная благодарность за valueOf пришлось перечитывать всё заново чтобы понять что это))а то я до конца не понимал зачем нам эта упаковка
    }
}
