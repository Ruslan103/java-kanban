package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList<Task> customLinkedList = new CustomLinkedList();

    // реализацию двусвязного списка задач
    private static class CustomLinkedList<T> {
        //private static final int SIZE_LIST = 10;
        private Node head; // ссылка на первый элемент
        private Node prev; // ссылка на последний элемент
        private int size = 0;
        private final HashMap<Integer, Node> history = new HashMap<>();

        public void linkLast(Task task) {
            final Node oldTail = prev; // сохраняю ссылку на последний элемент
            final Node newNode = new Node(prev, task, null); // т.к. новый элемент последний то next =null
            prev = newNode;
            if (oldTail == null) {// если в списке нет задач
                head = newNode;
            } else {
                oldTail.setNext(newNode); // добавляем ссылку бывшего посленего элемента на новый последний элемент
            }
            size++;
            history.put(task.getId(), newNode);
        }

        // получение списка задач из мапы
        public List<Task> getTasks() {
            List<Task> nodeList = new ArrayList<>();
            Node nextNode = null;
            for (Node node : history.values()) { // находим первую просмотренную задачу
                if (node.getPrev() == null) {
                    nodeList.add(node.getTask());
                    nextNode = node.getNext();
                }
                while (nextNode != null) {
                    nodeList.add(nextNode.getTask());
                    nextNode = nextNode.getNext();
                }
            }
            return nodeList;
        }

        // удаление элемента из списка
        public void removeNode(Node node) {
            if (node == null) {
                return;
            }
                Node nodeNext = null;
                Node nodePrev = null;
                if (node.getNext() != null) {
                    nodeNext = node.getNext();
                }
                if (node.getPrev() != null) {
                    nodePrev = node.getPrev();
                }
                if (nodePrev != null) {
                    nodePrev.setNext(nodeNext);
                }

                if (nodeNext != null) {
                    nodeNext.setPrev(nodePrev);
                }
                if (node.getPrev() == null) {
                    head = node.getNext();
                }

                node.setNext(null);
                node.setTask(null);
                node.setPrev(null);
                size--;
            }



    }


    // метод обновления списка истории
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (customLinkedList.history.containsKey(task.getId())) { // если задача была ранее просмотрена
            Node node = customLinkedList.history.get(task.getId()); // узел задачи в двусвязном списке
            customLinkedList.removeNode(node); // то удаляем ее из двусвязного списка
        }
        customLinkedList.linkLast(task); // добавление задачи в список истории
    }


    // удаление задачи по id из истории просмотров ТЗ 5
    @Override
    public void removeForId(int id) {

        Node node = customLinkedList.history.get(id); // элемент в двусвязном списке
        customLinkedList.removeNode(node); // удаление элемента из двусвязного списка
        customLinkedList.history.remove(id);
    }

    // метод получения списка истории ТЗ4
    @Override
    public List<Task> getHistory() {
        return customLinkedList.getTasks();
    }
}
