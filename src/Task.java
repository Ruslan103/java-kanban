import java.util.Objects;

public class Task {
    private String title; //Название, кратко описывающее суть задачи
    private int id; //Уникальный идентификационный номер задачи, по которому её можно будет найти
    // Для генерации идентификаторов можно использовать числовое поле класса менеджер, увеличивая его на 1, когда нужно получить новое значение.
    private String status; // Статус, отображающий её прогресс
    private String description; // описание

    public Task(String title, String description, String status) {
        this.title = title;
        this.id = id;
        this.status = status;
        this.description = description;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
