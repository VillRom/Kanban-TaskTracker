package manager;

import model.Epic;
import model.StatusTasks;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public ArrayList getSubtaskFromEpic(Integer id) {
        ArrayList<Subtask> listSubtaskFromEpic = new ArrayList<>();
        Epic epic = epics.get(id);
        for (Integer idSubtask : epic.getSubtaskIds()){
            listSubtaskFromEpic.add(subtasks.get(idSubtask));
        }
        return listSubtaskFromEpic;
    }

    @Override
    public void addTask(Task task) {
        tasks.put(task.getTaskId(), task);
    }

    @Override
    public void addEpic(Epic epic) {
        epics.put(epic.getTaskId(), epic);
    }

    @Override
    public void addSubtask(Subtask subtask) {
        subtasks.put(subtask.getTaskId(), subtask);
        epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getTaskId());
        updateEpicStatus(subtask.getEpicId());
    }

    @Override
    public ArrayList getTasks() {
        return new ArrayList<>(this.tasks.values());
    }

    @Override
    public ArrayList getEpics() {
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public ArrayList getSubtasks() {
        return new ArrayList<>(this.subtasks.values());
    }

    @Override
    public void deleteAllTheTasksInListTask() {
        for (Integer keys: tasks.keySet()) {
            historyManager.remove(keys);
        }
        tasks.clear();
    }

    @Override
    public void deleteAllTheTasksInListSubtask() {
        for (Integer keys: subtasks.keySet()) {
            historyManager.remove(keys);
        }
        subtasks.clear();
        for (Integer idEpic : epics.keySet()) {
            epics.get(idEpic).setStatus(StatusTasks.NEW);
        }
    }

    @Override
    public void deleteAllTheTasksInListEpic() {
        for (Integer keys: epics.keySet()) {
            historyManager.remove(keys);
        }
        for (Integer keys: subtasks.keySet()) {
            historyManager.remove(keys);
        }
        epics.clear();
        subtasks.clear();
    }

    @Override
    public Task getValueById(Integer id) {
        Task value;
        if (tasks.get(id) == null && epics.get(id) == null) {
            value = subtasks.get(id);
            historyManager.add(value);
        }else if(tasks.get(id) == null){
            value = epics.get(id);
            historyManager.add(value);
        }else{
            value = tasks.get(id);
            historyManager.add(value);
        }
        return value;
    }

    @Override
    public void updateTask(Integer id, Task task) {
        tasks.put(id, task);
    }

    @Override
    public void updateSubtask(Integer id, Subtask subtask) {
        if (!epics.containsKey(subtask.getEpicId())){
            return;
        }else {
            subtasks.put(id, subtask);
            updateEpicStatus(subtask.getEpicId());
        }

    }

    @Override
    public void updateEpic(Integer id, Epic epic) {
        epics.put(id, epic);
    }

    @Override
    public void deleteValueTaskById(Integer id) {
        if(tasks.containsKey(id)) {
            historyManager.remove(id);
            tasks.remove(id);
        }
    }

    @Override
    public void deleteValueEpicById(Integer id) {
        if(epics.containsKey(id)) {
            for (Integer idSubtask : epics.get(id).getSubtaskIds()) {
                historyManager.remove(idSubtask);
                subtasks.remove(idSubtask);
            }
            historyManager.remove(id);
            epics.remove(id);
        }
    }

    @Override
    public void deleteValueSubtaskById(Integer id) {
        if(subtasks.containsKey(id)) {
            int epicId = subtasks.get(id).getEpicId();
            historyManager.remove(id);
            epics.get(subtasks.get(id).getEpicId()).getSubtaskIds().remove(id);
            subtasks.remove(id);
            updateEpicStatus(epicId);
        }
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    private void updateEpicStatus(Integer idEpic) {
        int amountSubtaskNew = 0;
        int amountSubtaskDone = 0;
        for (Integer cheak : epics.get(idEpic).getSubtaskIds()) {
            if (subtasks.get(cheak).getStatus() == StatusTasks.NEW) {
                amountSubtaskNew++;
            }else if (subtasks.get(cheak).getStatus() == StatusTasks.DONE) {
                amountSubtaskDone++;
            }
        }
        if (epics.get(idEpic).getSubtaskIds().size() == amountSubtaskNew) {
            epics.get(idEpic).setStatus(StatusTasks.NEW);
        }else if (epics.get(idEpic).getSubtaskIds().size() == amountSubtaskDone) {
            epics.get(idEpic).setStatus(StatusTasks.DONE);
        }else{
            epics.get(idEpic).setStatus(StatusTasks.IN_PROGRESS);
        }
    }
}