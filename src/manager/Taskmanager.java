package manager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class Taskmanager {
    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public ArrayList getSubtaskFromEpic(Integer id){
        Epic epic = epics.get(id);
        return epic.getSubtaskIds();
    }

    public void addTask(Task task){
        tasks.put(task.getTaskId(), task);
    }

    public void addEpic(Epic epic){
        epics.put(epic.getTaskId(), epic);
    }

    public void addSubtask(Subtask subtask){
        subtasks.put(subtask.getTaskId(), subtask);
        epics.get(subtask.getEpicId()).getSubtaskIds().add(subtask.getTaskId());
    }

    public ArrayList getTasks(){
        return new ArrayList<>(this.tasks.values());
    }

    public ArrayList getEpics(){
        return new ArrayList<>(this.epics.values());
    }

    public ArrayList getSubtasks(){
        return new ArrayList<>(this.subtasks.values());
    }

    public void deleteAllTheTasksInListTask(){
        tasks.clear();
    }

    public void deleteAllTheTasksInListSubtask(){
        subtasks.clear();
    }

    public void deleteAllTheTasksInListEpic(){
        epics.clear();
        subtasks.clear();
    }

    public Task getValueById(Integer id){
        Task value;
        if (tasks.get(id) == null && epics.get(id) == null){
            value = subtasks.get(id);
        }else if(tasks.get(id) == null){
            value = epics.get(id);
        }else{
            value = tasks.get(id);
        }
        return value;
    }

    public void updateTask(Integer id, Task task){
        tasks.put(id, task);
    }

    public void updateSubtask(Integer id, Subtask subtask){
        if (!epics.containsKey(subtask.getEpicId())){
            return;
        }else {
            subtasks.put(id, subtask);
            updateEpicStatus(subtask.getEpicId());
        }

    }

    public void updateEpic(Integer id, Epic epic){
        epics.put(id, epic);
    }

    public void deleteValueTaskById(Integer id){
        tasks.remove(id);
    }

    public void deleteValueEpicById(Integer id){
        for (Integer idSubtask : epics.get(id).getSubtaskIds()){
            subtasks.remove(idSubtask);
        }
        epics.remove(id);
    }

    public void deleteValueSubtaskById(Integer id){
        int epicId = subtasks.get(id).getEpicId();
        subtasks.remove(id);
        updateEpicStatus(epicId);
    }

    private void updateEpicStatus(Integer idEpic) {
        int amountSubtaskNew = 0;
        int amountSubtaskDone = 0;
        for (Integer cheak : epics.get(idEpic).getSubtaskIds()){
            if (subtasks.get(cheak).getStatus() == "New"){
                amountSubtaskNew++;
            }else if (subtasks.get(cheak).getStatus() == "Done"){
                amountSubtaskDone++;
            }
        }
        if (epics.get(idEpic).getSubtaskIds().size() == amountSubtaskNew){
            epics.get(idEpic).setStatus("New");
        }else if (epics.get(idEpic).getSubtaskIds().size() == amountSubtaskDone){
            epics.get(idEpic).setStatus("Done");
        }else{
            epics.get(idEpic).setStatus("In progress");
        }
    }
}
