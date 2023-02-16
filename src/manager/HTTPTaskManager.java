package manager;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import model.*;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class HTTPTaskManager extends FileBackedTasksManager {

    private final KVTaskClient kvTaskClient;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HTTPTaskManager(String url) {
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        if (!tasks.isEmpty()) {
            String jsonTasks = gson.toJson(new ArrayList<>(tasks.values()));
            kvTaskClient.put("tasks", jsonTasks);
        }
        if(!epics.isEmpty()) {
            String jsonEpics = gson.toJson(new ArrayList<>(epics.values()));
            kvTaskClient.put("epics", jsonEpics);
        }
        if(!subtasks.isEmpty()) {
            String jsonSubtasks = gson.toJson(new ArrayList<>(subtasks.values()));
            kvTaskClient.put("subtasks", jsonSubtasks);
        }
        if (getHistory().size() != 0) {
            String jsonHistory = gson.toJson(new ArrayList<>(getHistory()));
            kvTaskClient.put("history", jsonHistory);
        }
    }

    public void load() {
        ArrayList<Task> tasks = gson.fromJson(kvTaskClient.load("tasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(tasks);
        ArrayList<Task> epics = gson.fromJson(kvTaskClient.load("epics"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(epics);
        ArrayList<Task> subtasks = gson.fromJson(kvTaskClient.load("subtasks"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(subtasks);
        ArrayList<Task> history = gson.fromJson(kvTaskClient.load("history"), new TypeToken<ArrayList<Task>>() {
        }.getType());
        addTasks(history);
    }

    private void addTasks(ArrayList<Task> listTasks) {
        if (listTasks.stream().allMatch(task -> task.getType().equals(TypeTask.TASK)) & tasks.isEmpty()) {
            for (Task task : listTasks) {
                tasks.put(task.getTaskId(), task);
                sortTaskTime.put(task.getStartTime(), task);
            }
        } else if (listTasks.stream().allMatch(task -> task.getType().equals(TypeTask.EPIC)) & epics.isEmpty()) {
            for (Task epic : listTasks) {
                epics.put(epic.getTaskId(), (Epic) epic);
            }
        } else if (listTasks.stream().allMatch(task -> task.getType().equals(TypeTask.SUBTASK)) & subtasks.isEmpty()) {
            for (Task subtask : listTasks) {
                subtasks.put(subtask.getTaskId(), (Subtask) subtask);
                sortTaskTime.put(subtask.getStartTime(), subtask);
            }
        } else {
            for (Task tasks : listTasks) {
                historyManager.add(tasks);
            }
        }
    }
}
