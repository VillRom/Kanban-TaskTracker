package manager;

import model.Task;

import java.util.List;
import java.util.Map;

public interface HistoryManager {

    void add(Task task);

    void remove(int id);

    List<Task> getHistory();

    Node<Task> getHead();

    Node<Task> getTail();

    Map<Integer, Node<Task>> getViewedTask();
}
