package model;

import java.util.ArrayList;

import static history.TypeTask.EPIC;
import static history.TypeTask.SUBTASK;

public class Epic extends Task {
    private ArrayList<Integer> subtasksID;

    public Epic(String title, String description, Status status) {
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
        subtasksID.remove(Integer.valueOf(subtaskId));
    }
    @Override
    public String toString() {
        return  getId() +","+EPIC+","+getTitle()+","+ getStatus() +","+ getDescription();
    }
}
