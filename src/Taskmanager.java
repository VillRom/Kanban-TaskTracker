import java.util.ArrayList;
import java.util.HashMap;

public class Taskmanager {
    HashMap<Integer,Task> tasks = new HashMap<>();
    HashMap<Integer,Subtask> subtasks = new HashMap<>();
    HashMap<Integer,Epic> epics = new HashMap<>();
    int id = 0;

    ArrayList showSubtaskFromEpic(Integer id){
        Epic epic = epics.get(id);
        return epic.getListSubtask();
    }

    void addTask(Task task){
        tasks.put(++id, task);
    }

    void addEpic(Epic epic){
        epics.put(++id, epic);
    }

    void addSubtask(Subtask subtask){
        subtasks.put(++id, subtask);
    }

    ArrayList showListTasks(){
        ArrayList<Object> listTask = new ArrayList<>();
        for(Integer cheak : tasks.keySet()){
            listTask.add(tasks.get(cheak));
        }
        return listTask;
    }

    ArrayList showListEpics(){
        ArrayList<Object> listTask = new ArrayList<>();
        for(Integer cheak : epics.keySet()){
            listTask.add(epics.get(cheak));
        }
        return listTask;
    }

    ArrayList showListSubtasks(){
        ArrayList<Object> listTask = new ArrayList<>();
        for(Integer cheak : subtasks.keySet()){
            listTask.add(subtasks.get(cheak));
        }
        return listTask;
    }

    void deleteAllTheTasksInListTask(){
        tasks.clear();
    }

    void deleteAllTheTasksInListSubtask(){
        subtasks.clear();
    }

    void deleteAllTheTasksInListEpic(){
        epics.clear();
    }

    Object getValueById(Integer id){
        Object value;
        if (tasks.get(id) == null && epics.get(id) == null){
            value = subtasks.get(id);
        }else if(tasks.get(id) == null){
            value = epics.get(id);
        }else{
            value = tasks.get(id);
        }
        return value;
    }

    void updateTask(Integer id, Task task){
        tasks.put(id, task);
    }

    void updateSubtask(Integer id, Subtask subtask){
        subtasks.put(id, subtask);

    }

    void updateEpic(Integer id, Epic epic){
        epics.put(id, epic);
    }

    void deleteValueById(Integer id){
        if(tasks.get(id) == null && epics.get(id) == null){
            subtasks.remove(id);
        }else if(tasks.get(id) == null){
            epics.remove(id);
        }else {
            tasks.remove(id);
        }
    }

    void addInListSubtask(Integer idEpic, Integer idSubtask){
        Epic epic = epics.get(idEpic);
        Subtask subtask = subtasks.get(idSubtask);
        epic.listSubtask.add(subtask);
    }

    void statusEpic(Integer idEpic) {
        Epic epic = epics.get(idEpic);
        for (Subtask subtask : epic.listSubtask)
            if(epic.listSubtask.isEmpty() || subtask.status == "New"){
                epic.status = "New";
            }else if(subtask.status == "Done"){
                epic.status = "Done";
            }else {
                epic.status = "In progress";
            }
    }
}
