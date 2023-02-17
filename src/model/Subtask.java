package model;

import static history.TypeTask.SUBTASK;
import static history.TypeTask.TASK;

public class Subtask extends Task { //Для каждой подзадачи известно, в рамках какого эпика она выполняется.
    private int epicID;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        this.epicID = epicID;
    }

    public int getEpicID() {
        return epicID;
    }

    public void setEpicID(int epicID) {
        this.epicID = epicID;
    }
    @Override
    public String toString() {
        return  getId() +","+SUBTASK+","+getTitle()+","+ getStatus() +","+ getDescription()+","+epicID;
    }
}
