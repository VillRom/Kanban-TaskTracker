package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    public ArrayList getSubtaskFromEpic(Integer id);

    public void addTask(Task task);

    public void addEpic(Epic epic);

    public void addSubtask(Subtask subtask);

    public ArrayList getTasks();

    public ArrayList getEpics();

    public ArrayList getSubtasks();

    public void deleteAllTheTasksInListTask();

    public void deleteAllTheTasksInListSubtask();

    public void deleteAllTheTasksInListEpic();

    public Task getValueById(Integer id);

    public void updateTask(Integer id, Task task);

    public void updateSubtask(Integer id, Subtask subtask);

    public void updateEpic(Integer id, Epic epic);

    public void deleteValueTaskById(Integer id);

    public void deleteValueEpicById(Integer id);

    public void deleteValueSubtaskById(Integer id);

    public String toString(Task task);

    public List<Task> getHistory();

    void save() throws IOException;
}
