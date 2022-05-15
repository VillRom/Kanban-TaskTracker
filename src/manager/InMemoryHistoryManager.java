package manager;

import model.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    List<Task> viewedTasks = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (viewedTasks.size() >= 10) {
            viewedTasks.remove(0);
            viewedTasks.add(task);
        }else {
            viewedTasks.add(task);
        }
    }

    @Override
    public List<Task> getHistory() {
        return viewedTasks;
    }
}
