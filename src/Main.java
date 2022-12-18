import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        // ввод эпика №1
        String title = "переезд";
        String description = " описание эпика - переезд в другую квартиру";
        String status = "NEW";
        // ввод подзадачи №1
        String titleSub = "собрать вещи";
        String descriptionSub = "собрать коробки и.т.д";
        String statusSub = "NEW";
        manager.CreateSubtasks(titleSub, descriptionSub, statusSub);
        // ввод подзадачи №2
        titleSub = "упоковать одежду";
        descriptionSub = "собрать упаковать одежду и.т.д";
        statusSub = "new";
        manager.CreateSubtasks(titleSub, descriptionSub, statusSub);
        manager.CreateEpic(title, description, status);
        // ввод эпика №2
        title = "пробежка";
        description = "пробежка утром";
        status = "new";
        System.out.println("Вводите подзадачи");
        titleSub = "проснуться";
        descriptionSub = "умыться и побриться";
        statusSub = "new";
        manager.CreateSubtasks(titleSub, descriptionSub, statusSub);
        manager.CreateEpic(title, description, status);

        while (true) {
            System.out.println("1-получить список всех задач");
            System.out.println("2- удалить все задачи");
            System.out.println("3-получение по идентификатору");
            System.out.println("4 - Создание");
            System.out.println("5 - Обновление");
            System.out.println("6 - удаление по id");
            System.out.println("7-получение списка всех подзадач определённого эпика");
            int i = scanner.nextInt();
            if (i == 1) {
                System.out.println("Cписок эпиков - " + manager.getEpics());
            }
            if (i == 2) {
                manager.clearAllTasks();
            }
            if (i == 3) {
                double id = 2.1;
                System.out.println(manager.getTitleById(id));
            }
            if (i == 4) {
                Epic epic = new Epic("тренажерный зал", "ходить позаниматься", "NEW");
                Subtask subtask = new Subtask("присед", "Поприседать там...", "NEW");
                manager.CreateSubtask(subtask);
                manager.CreateEpic(epic);
            }
            if (i == 5) {
                Subtask subtask = new Subtask("отдохнуть", "прогуляться", "DONE");
                manager.updateSubtasks(3.3, subtask);
            }
            if (i == 6) {
                double id = 1.2;
                manager.removeForIdEpic(id);
            }
            if (i == 7) {
                Epic epic = manager.epics.get(1.2);
                System.out.println(manager.getSubtask(epic));
            }
        }
    }
}
