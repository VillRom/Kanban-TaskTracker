package manager;

import client.KVTaskClient;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import model.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HTTPTaskManager extends FileBackedTasksManager {
    private String url;
    private KVTaskClient kvTaskClient;
    private String key = "menegerStatus";
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public HTTPTaskManager(String url) {
        this.url = url;
        this.kvTaskClient = new KVTaskClient(url);
    }

    @Override
    protected void save() {
        String jsonMenegerStatus = new String();
        List<String> statusMeneger = new ArrayList<>();
        if (!getTasks().isEmpty()) {
            for (Object task : getTasks()) {
                statusMeneger.add(gson.toJson(task));
            }
        }
        if(!getEpics().isEmpty()) {
            for (Object epic : getEpics()) {
                statusMeneger.add(gson.toJson(epic));
            }
        }
        if(!getSubtasks().isEmpty()) {
            for (Object subtask : getSubtasks()) {
                statusMeneger.add(gson.toJson(subtask));
            }
        }
        if (!historyManager.getHistory().isEmpty()) {
            for(Task task : historyManager.getHistory()) {
                statusMeneger.add(gson.toJson(task.getTaskId()));
            }
        }
        for (String str : statusMeneger) {
            jsonMenegerStatus += str;
        }
        kvTaskClient.put(key, jsonMenegerStatus);
    }

    @Override
    public HTTPTaskManager loadFromFile() {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/");
        String statusNewMeneger = kvTaskClient.load(key);
        boolean isStringEndsWithParenthesis = statusNewMeneger.endsWith("}");
        List<String> listStatus = List.of(statusNewMeneger.split("}"));
        String afds = listStatus.get(0) + "}";
        Task task = gson.fromJson(afds, Task.class);
        for (String str : listStatus) {
            if (str.contains("TASK")) {
                try {
                    str = str + "}";
                    httpTaskManager.addTask(gson.fromJson(str, Task.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (str.contains("EPIC")) {
                str = str + "}";
                httpTaskManager.addEpic(gson.fromJson(str, Epic.class));
            } else if (str.contains("SUBTASK")) {
                try {
                    str = str + "}";
                    httpTaskManager.addSubtask(gson.fromJson(str, Subtask.class));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (!isStringEndsWithParenthesis) {
                for (int i = 0; i < str.length(); i++) {
                    httpTaskManager.getValueById(Integer.parseInt(String.valueOf(str.charAt(i))));
                }
            }
        }
        return httpTaskManager;
    }

    /*public static void main(String[] args) {
        HTTPTaskManager httpTaskManager = new HTTPTaskManager("http://localhost:8078/");
        Task task = new Task("Проверка Task", TypeTask.TASK,
                "Task для проверки", StatusTasks.NEW, 1, 10);
        Task task1 = new Task("Проверка Task1", TypeTask.TASK,
                "Task для проверки", StatusTasks.NEW, 2, 10);
        Task task2 = new Task("Проверка Task2", TypeTask.TASK,
                "Task для проверки", StatusTasks.NEW, 3, 10);
        task.setStartTime((LocalDateTime.now()));
        task1.setStartTime((LocalDateTime.now()).plusMinutes(60));
        task2.setStartTime((LocalDateTime.now()).plusMinutes(120));
        try {
            httpTaskManager.addTask(task);
            httpTaskManager.addTask(task1);
            httpTaskManager.addTask(task2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        HTTPTaskManager httpTaskManager222 = httpTaskManager.loadFromFile();
        System.out.println(123);
    }*/
}
