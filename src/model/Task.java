package model;


import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String title; //Название, кратко описывающее суть задачи
    private int id; //Уникальный идентификационный номер задачи, по которому её можно будет найти
    // Для генерации идентификаторов можно использовать числовое поле класса менеджер, увеличивая его на 1, когда нужно получить новое значение.
    private Status status; // Статус, отображающий её прогресс
    private String description; // описание
    private LocalDateTime startTime; //дата, когда предполагается приступить к выполнению задачи
    private long duration; //продолжительность задачи, оценка того, сколько времени она займёт в минутах (число)


    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Task(String title, String description, Status status) {
        this.title = title;
        this.id = id;
        this.status = status;
        this.description = description;
    }

    public Task(String title, String description, Status status,int id) {
        this.title = title;
        this.id = id;
        this.status = status;
        this.description = description;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    //время завершения задачи, которое рассчитывается исходя из startTime и duration
    public LocalDateTime getEndTime() {
        if (startTime == null) {
            return null;
        }
        return startTime.plusMinutes(duration);
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(title, task.title) && Objects.equals(description, task.description) && id == task.getId() && Objects.equals(status, task.getStatus());
    }


}
