package manager;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    CustomLinkedList<Task> customLinkedList = new CustomLinkedList();

    // реализацию двусвязного списка задач
    public static class CustomLinkedList<T> {
        private static final int SIZE_LIST = 10;
        private Node<T> head; // ссылка на первый элемент
        private Node<T> prev; // ссылка на последний элемент
        private int size = 0;
        private final HashMap<Integer, Node<T>> history = new HashMap<>();
        private int headId; // id первого элемента

        public void linkLast(Task task) {
            final Node<T> oldTail = prev; // сохраняю ссылку на последний элемент
            final Node<T> newNode = new Node<>(prev, task, null); // т.к. новый элемент последний то next =null
            prev = newNode;

            if (oldTail == null) {// если в списке нет задач
                head = newNode;
                headId = task.getId();
            } else {
                oldTail.next = newNode; // добавляем ссылку бывшего посленего элемента на новый последний элемент
            }
            size++;

            if (size == SIZE_LIST) { // если размер списка больше установленного
                Node<T> oldHead = head; // ссылка на первый элемент
                int oldHeadId = headId;
                head = head.next;
                headId = head.task.getId();
                history.remove(oldHeadId);
                removeNode(oldHead);
                size--;
            }
            history.put(task.getId(), newNode);
        }

        // получение списка задач из мапы
        public ArrayList<Node<T>> getTasks() {
            return new ArrayList<>(history.values());
        }

        // удаление элемента из списка
        public void removeNode(Node<T> node) {
            if (node != null) {
                Node<T> nodeNext = null;
                Node<T> nodePrev = null;
                if (node.next != null) {
                    nodeNext = node.next;
                }
                if (node.prev != null) {
                    nodePrev = node.prev;
                }
                if (nodePrev != null) {
                    nodePrev.next = nodeNext;
                }
                if (nodeNext != null) {
                    nodeNext.prev = nodePrev;
                }
                if (node.prev == null) {
                    head = node.next;
                }
                node.next = null;
                node.task = null;
                node.prev = null;
            }
        }
    }

    // метод обновления списка истории
    public void add(Task task) {
        if (task == null) {
            return;
        }
        if (customLinkedList.history.containsKey(task.getId())) { // если задача была ранее просмотрена
            Node<Task> node = customLinkedList.history.get(task.getId()); // узел задачи в двусвязном списке
            customLinkedList.removeNode(node); // то удаляем ее из двусвязного списка
        }
        customLinkedList.linkLast(task); // добавление задачи в список истории
    }

    // удаление задачи по id из истории просмотров ТЗ 5
    @Override
    public void removeForId(int id) {
        customLinkedList.history.remove(id);
        Node<Task> node = customLinkedList.history.get(id); // элемент в двусвязном списке
        customLinkedList.removeNode(node); // удаление элемента из двусвязного списка
    }

    // метод получения списка истории ТЗ4
    @Override
    public ArrayList<Node<Task>> getHistory() {
        return customLinkedList.getTasks();
    }
}
