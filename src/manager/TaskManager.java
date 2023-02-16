package manager;

import model.Epic;
import model.Subtask;
import model.Task;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    List<Task> getPrioritizedTasks();

    ArrayList<Subtask> getSubtaskFromEpic(Integer id);

    void addTask(Task task) throws IOException;

    void addEpic(Epic epic);

    void addSubtask(Subtask subtask) throws IOException;

    ArrayList<Task> getTasks();

    List<Task> getEpics();

    List<Task> getSubtasks();

    void deleteAllTheTasksInListTask();

    void deleteAllTheTasksInListSubtask();

    void deleteAllTheTasksInListEpic();

    Task getValueById(Integer id);

    void updateTask(Integer id, Task task) throws IOException;

    void updateSubtask(Integer id, Subtask subtask) throws IOException;

    void updateEpic(Integer id, Epic epic);

    void deleteValueTaskById(Integer id);

    void deleteValueEpicById(Integer id);

    void deleteValueSubtaskById(Integer id);

    List<Task> getHistory();
}
