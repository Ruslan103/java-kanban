import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        Manager manager=new Manager();
       // SubtaskManager subtaskManager = new SubtaskManager();

        while (true) {
            System.out.println("1-Ввести задачу,2- посмотреть список задач, 3 - Выход");
            Integer i = scanner.nextInt();
            if (i == 1) {
                System.out.println("1-Ввести задачу, 2 - ввести эпик, 3 -ввести подзадачу в эпик");
                Integer j = scanner.nextInt();
                if (j==2) {
                    System.out.println(" Введите название,описание, статус");
                    String title = "Название эпика - переезд";
                    String description = " описание эпика - переезд в другую квартиру";
                    String status = "new";


                    System.out.println("Ввод подзадачи");
                    String titleSub = "собрать вещи";
                    String descriptionSub = "собрать коробки и.т.д";
                    String statusSub = "new";
                    manager.CreateSubtasks(titleSub,descriptionSub,statusSub);

                    System.out.println("Вводите подзадачи");
                    titleSub = "упоковать одежду";
                    descriptionSub = "собрать упаковать одежду и.т.д";
                    statusSub = "new";
                    manager.CreateSubtasks(titleSub,descriptionSub,statusSub);

                    manager.CreateEpic(title,description,status);


                    title = "пробежка";
                    description = "пробежка утром";
                    status = "new";
                    System.out.println("Вводите подзадачи");
                    titleSub = "проснуться";
                    descriptionSub = "умыться и побриться";
                    statusSub = "new";
                    manager.CreateSubtasks(titleSub,descriptionSub,statusSub);
                    manager.CreateEpic(title,description,status);

                }

            }


        }
    }
}