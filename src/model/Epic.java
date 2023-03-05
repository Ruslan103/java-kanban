package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private LocalDateTime endTime; // время окончания эпика
    private ArrayList<Integer> subtasksID;

    public Epic(String title, String description, Status status) {
        super(title, description, status);
        this.subtasksID = new ArrayList<>();
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
    public TypeTask getType() {
        return TypeTask.EPIC;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }
}
