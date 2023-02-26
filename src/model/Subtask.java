package model;

import java.time.LocalDateTime;
import java.util.Objects;

import static model.TypeTask.SUBTASK;

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
    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

//    @Override
//    public LocalDateTime getEndTime() {
//        return getStartTime().plusMinutes(getDuration());
//    }
}
