package manager;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private CustomLinkedList<Task> customViewedTasks = new CustomLinkedList<>();
    private Map<Integer, Node> viewedTask = new HashMap<>();

    @Override
    public void add(Task task) {
        if (customViewedTasks.size >= 10) {
            customViewedTasks.removeNode(customViewedTasks.getHead());
        }
        if (viewedTask.containsKey(task.getTaskId())) {
            customViewedTasks.removeNode(viewedTask.get(task.getTaskId()));
            customViewedTasks.linkLast(task);
        }else {
            customViewedTasks.linkLast(task);
            viewedTask.put(task.getTaskId(), customViewedTasks.getTail());
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
            if(size == 0){
                tail = new Node<>(task);
                size++;
            }else if(size == 1){
                head = tail;
                tail = new Node<>(task);
                head.next = tail;
                tail.prev = head;
                size++;
            }else if(viewedTask.containsKey(task.getTaskId())){
                Node<Task> exTail = tail;
                tail = viewedTask.get(task.getTaskId());
                exTail.next = tail;
                tail.prev = exTail;
                tail.next = null;
                size++;
            }else{
                Node<Task> exTail = tail;
                tail = new Node<>(task);
                exTail.next = tail;
                tail.prev = exTail;
                tail.next = null;
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
            }else {
                node.next.prev = node.prev;
                node.prev.next = node.next;
                size = size -1;
            }
        }

        public void setTail(Node<Task> tail) {
            this.tail = tail;
        }

        public Node<Task> getHead() {
            return head;
        }

        public Node<Task> getTail() {
            return tail;
        }
    }
}
