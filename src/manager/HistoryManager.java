package manager;

import model.Task;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface HistoryManager {

    public void add(Task task);

    public void remove(int id);

    public List<Task> getHistory();

    Node getHead();

    Node getTail();

    Map<Integer, Node> getViewedTask();
}
