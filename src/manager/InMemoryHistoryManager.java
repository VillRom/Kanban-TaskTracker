package manager;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> customViewedTasks = new CustomLinkedList<>();
    private Map<Integer, Node> viewedTask = new HashMap<>();

    @Override
    public void add(Task task) {
        if (viewedTask.containsKey(task.getTaskId())) {
            customViewedTasks.removeNode(viewedTask.get(task.getTaskId()));
            customViewedTasks.linkLast(task);
        } else {
            customViewedTasks.linkLast(task);
            viewedTask.put(task.getTaskId(), customViewedTasks.tail);
        }
    }

    @Override
    public void remove(int id) {
        if(viewedTask.containsKey(id)){
            customViewedTasks.removeNode(viewedTask.get(id));
            viewedTask.remove(id);
        }
    }

    @Override
    public List<Task> getHistory() {
        return customViewedTasks.getTasks();
    }

    public class CustomLinkedList<T> {
        private Node<Task> head;
        private Node<Task> tail;

        private void linkLast(Task task) {
            Node node = new Node<>(task, tail, null);
            if(head == null){
                head = node;
            }else if(viewedTask.containsKey(task.getTaskId())){
                node = viewedTask.get(task.getTaskId());
                node.prev = tail;
                node.next = null;
                tail.next = node;
            } else{
                tail.next = node;
            }
            tail = node;
        }

        private ArrayList<Task> getTasks() {
            List<Task> getHistoryList = new ArrayList<>();
            Node headNode = customViewedTasks.head;
            while(headNode != null){
                getHistoryList.add((Task) headNode.data);
                headNode = headNode.next;
            }
            return (ArrayList<Task>) getHistoryList;
        }

        private void removeNode(Node node){
            if(node == customViewedTasks.head) {
                customViewedTasks.head = node.next;
            }else if(node == customViewedTasks.tail){
                node.prev.next = node.next;
            } else {
                node.next.prev = node.prev;
                node.prev.next = node.next;
            }
        }
    }
}
