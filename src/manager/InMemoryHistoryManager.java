package manager;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private final Map<Integer, Node<Task>> viewedTask = new HashMap<>();

    @Override
    public void add(Task task) {
        if (viewedTask.containsKey(task.getTaskId())) {
            removeNode(viewedTask.get(task.getTaskId()));
            linkLast(task);
        } else {
            linkLast(task);
            viewedTask.put(task.getTaskId(), tail);
        }
    }

    @Override
    public void remove(int id) {
        if (viewedTask.containsKey(id)) {
            removeNode(viewedTask.get(id));
            viewedTask.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    private void linkLast(Task task) {
        Node<Task> node = new Node<>(task, tail, null);
        if (head == null) {
            head = node;
        } else if(viewedTask.containsKey(task.getTaskId())) {
            node = viewedTask.get(task.getTaskId());
            node.prev = tail;
            node.next = null;
            tail.next = node;
        } else {
            tail.next = node;
        }
        tail = node;
    }

    private ArrayList<Task> getTasks() {
        ArrayList<Task> getHistoryList = new ArrayList<>();
        Node<Task> headNode = head;
        while(headNode != null) {
            getHistoryList.add(headNode.data);
            headNode = headNode.next;
        }
        return getHistoryList;
    }

    private void removeNode(Node<Task> node) {
        if (node == head) {
            head = node.next;
        } else if(node == tail) {
            node.prev.next = node.next;
            tail = node.prev;
        } else {
            node.next.prev = node.prev;
            node.prev.next = node.next;
        }
    }

    public Node<Task> getHead() {
        return head;
    }

    public Node<Task> getTail() {
        return tail;
    }

    public Map<Integer, Node<Task>> getViewedTask() {
        return viewedTask;
    }
}
