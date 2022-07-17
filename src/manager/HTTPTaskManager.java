package manager;

import client.KVTaskClient;
import com.google.gson.Gson;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HTTPTaskManager extends FileBackedTasksManager {
    private String url;
    private KVTaskClient kvTaskClient;
    private File autosave;
    Gson gson = new Gson();

    public HTTPTaskManager(File autoSave, String url) {
        super(autoSave);
        this.autosave = autoSave;
        this.url = url;
        this.kvTaskClient = new KVTaskClient(url);
    }

    private String managerStatusInString() {
        String managerStatus= null;
        try (BufferedReader br = new BufferedReader(new FileReader(autosave))) {
            List<String> taskSave = new ArrayList<>();
            while (br.ready()) {
                taskSave.add(br.readLine());
            }
            taskSave.remove(0);
            taskSave.removeIf(String::isEmpty);
            managerStatus = taskSave.stream().collect(Collectors.joining(","));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return managerStatus;
    }

    @Override
    public void addTask(Task task) throws IOException {
        super.addTask(task);
        kvTaskClient.put("save manager", managerStatusInString());
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        kvTaskClient.put("save manager", gson.toJson(managerStatusInString()));
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException {
        super.addSubtask(subtask);
        kvTaskClient.put("save manager", gson.toJson(managerStatusInString()));
    }

    @Override
    public void deleteAllTheTasksInListTask() {
        super.deleteAllTheTasksInListTask();
        kvTaskClient.put("save manager", gson.toJson(managerStatusInString()));
    }

    @Override
    public void deleteAllTheTasksInListSubtask() {
        super.deleteAllTheTasksInListSubtask();
        kvTaskClient.put("save manager", gson.toJson(managerStatusInString()));
    }

    @Override
    public void deleteAllTheTasksInListEpic() {
        super.deleteAllTheTasksInListEpic();
        kvTaskClient.put("save manager", gson.toJson(managerStatusInString()));
    }

    @Override
    public Task getValueById(Integer id) {
        Task task = super.getValueById(id);
        kvTaskClient.put("save manager", gson.toJson(managerStatusInString()));
        return task;
    }

    @Override
    public void deleteValueEpicById(Integer id) {
        super.deleteValueEpicById(id);
        kvTaskClient.put("save manager", gson.toJson(managerStatusInString()));
    }
}
