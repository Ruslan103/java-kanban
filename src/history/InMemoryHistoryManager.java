package history;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private final CustomLinkedList customLinkedList = new CustomLinkedList();

    // реализацию двусвязного списка задач
    private static class CustomLinkedList {
        private Node head; // ссылка на первый элемент
        private Node tail; // ссылка на последний элемент
        private int size = 0;
        private final HashMap<Integer, Node> history = new HashMap<>();

        public void linkLast(Task task) {
            final Node oldTail = tail; // сохраняю ссылку на последний элемент
            final Node newNode = new Node(tail, task, null); // т.к. новый элемент последний то next =null
            tail = newNode;
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
            Node nextNode = head;
            while (nextNode != null) {
                nodeList.add(nextNode.getTask());
                nextNode = nextNode.getNext();
            }
            return nodeList;
        }

        // удаление элемента из списка
        public void removeNode(Node node) {
            if (node == null) {
                return;
            }
            Node nodeNext = node.getNext();
            Node nodePrev = node.getPrev();
            if (nodePrev != null) {
                nodePrev.setNext(nodeNext);
            }
            if (nodeNext != null) {
                nodeNext.setPrev(nodePrev);
            }
            if (node.getPrev() == null) {
                head = node.getNext();
            }
            if (node.getNext() == null) {
                tail = node.getPrev();
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
