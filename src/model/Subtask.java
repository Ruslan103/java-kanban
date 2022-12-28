package model;

import manager.Status;

public class Subtask extends Task { //Для каждой подзадачи известно, в рамках какого эпика она выполняется.
    private int EpicID;

    public Subtask(String title, String description, Status status, int epicID) {
        super(title, description, status);
        EpicID = epicID;
    }

    public int getEpicID() {
        return EpicID;
    }

    public void setEpicID(int epicID) {
        EpicID = epicID;
    }
}
