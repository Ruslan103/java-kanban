package manager;

import model.Task;

class Node<T> { // узел
    public Task task; // задача
    public Node<T> next; // ссылка на следующий элемент
    public Node<T> prev; // ссылка на предидущий элемент

    public Node(Node<T> prev, Task task, Node<T> next) {
        this.task = task;
        this.next = next;
        this.prev = prev;
    }
}