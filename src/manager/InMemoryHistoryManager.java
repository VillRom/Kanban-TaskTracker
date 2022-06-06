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
        private int size = 0;

        private void linkLast(Task task) {
            if(tail == null){
                tail = new Node<>(task, null, null);
                head = tail;
                size++;
            }else if(viewedTask.containsKey(task.getTaskId())){
                Node<Task> exTail = tail;
                tail = viewedTask.get(task.getTaskId());
                exTail.next = tail;
                tail.prev = exTail;
                tail.next = null;
                size++;
            } else{
                Node<Task> exTail = tail;
                tail = new Node<>(task, exTail, null);
                exTail.next = tail;
                size++;
            }
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
            if(node == customViewedTasks.head && size == 1) {
                customViewedTasks.head = null;
                size = size -1;
            }else if(node == customViewedTasks.head && size > 1){
                customViewedTasks.head = node.next;
                size = size -1;
            }else if(node == customViewedTasks.tail){
                node.prev.next = node.next;
                size = size -1;
            } else {
                node.next.prev = node.prev;
                node.prev.next = node.next;
                size = size -1;
            }
        }
    }
}
