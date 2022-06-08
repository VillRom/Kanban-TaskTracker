package manager;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private Node<Task> head;
    private Node<Task> tail;
    private Map<Integer, Node> viewedTask = new HashMap<>();

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
        if(viewedTask.containsKey(id)) {
            removeNode(viewedTask.get(id));
            viewedTask.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

        private void linkLast(Task task) {
            Node node = new Node<>(task, tail, null);
            if(head == null) {
                head = node;
            }else if(viewedTask.containsKey(task.getTaskId())) {
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
            List<Task> getHistoryList = new ArrayList<>();
            Node headNode = head;
            while(headNode != null) {
                getHistoryList.add((Task) headNode.data);
                headNode = headNode.next;
            }
            return (ArrayList<Task>) getHistoryList;
        }

        private void removeNode(Node node) {
            if(node == head) {
                head = node.next;
            }else if(node == tail) {
                node.prev.next = node.next;
            } else {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            }
        }
    }
