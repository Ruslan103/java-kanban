package manager;

import java.util.Scanner;

public class Main { // по правде говоря не успеваю сделать метод маин, как я понял он не обязателен в рамках этого ТЗ
                    // если не критично прошу проверить в таком варианте, ну или если нужно то сделаю

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Manager manager = new Manager();
        Epic epic = new Epic("переезд", " описание эпика - переезд в другую квартиру", "NEW");
        Subtask subtask = new Subtask("собрать вещи", "собрать коробки и.т.д", "NEW", 1);
        manager.createSubtasks(subtask);

    }
}