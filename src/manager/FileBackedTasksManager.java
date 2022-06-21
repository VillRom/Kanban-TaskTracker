package manager;

import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static File autoSave;

    public FileBackedTasksManager(File autoSave) {
        this.autoSave = autoSave;
    }

    public void save() {
        try {
            if (autoSave.exists()) {
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(autoSave))) {
                    String header = "id,type,name,status,description,epic";
                    bufferedWriter.write(header);
                    bufferedWriter.newLine();
                    for (Object value : getTasks()) {
                        bufferedWriter.write(toString((Task) value));
                        bufferedWriter.newLine();
                    }
                    for (Object value : getEpics()) {
                        bufferedWriter.write(toString((Epic) value));
                        bufferedWriter.newLine();
                    }
                    for (Object value : getSubtasks()) {
                        bufferedWriter.write(toString((Subtask) value));
                        bufferedWriter.newLine();
                    }
                    bufferedWriter.newLine();
                    bufferedWriter.write(toString(historyManager));
                } catch (IOException e) {
                    System.out.println("Произошла ошибка записи в файл");
                }
            } else {
                autoSave.createNewFile();
            }
        } catch (ManagerSaveException e) {
            System.out.println("Произошла ошибка записи в файл. Ошибка: " + e.getMessages());
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка записи в файл.");
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void deleteAllTheTasksInListTask() {
        super.deleteAllTheTasksInListTask();
        save();
    }

    @Override
    public void deleteAllTheTasksInListSubtask() {
        super.deleteAllTheTasksInListSubtask();
        save();
    }

    @Override
    public void deleteAllTheTasksInListEpic() {
        super.deleteAllTheTasksInListEpic();
        save();
    }

    @Override
    public Task getValueById(Integer id) {
        return super.getValueById(id);
    }

    @Override
    public void deleteValueEpicById(Integer id) {
        super.deleteValueEpicById(id);
        save();
    }

    @Override
    public String toString(Task task) {
        if (task.getType() == TypeTask.SUBTASK) {
            return task.getTaskId() + "," + task.getType() + "," + task.getName() + ","
                    + task.getStatus() + "," + task.getDescription() + "," + ((Subtask) task).getEpicId();
        } else {
            return task.getTaskId() + "," + task.getType() + "," + task.getName() + ","
                    + task.getStatus() + "," + task.getDescription();
        }
    }

    private static Task fromStrings(String value) {
        String[] split = value.split(",");
        if (split[1].equals("TASK")) {
            return new Task(split[2], TypeTask.valueOf(split[1]), split[4], StatusTasks.valueOf(split[3])
                    , Integer.parseInt(split[0].trim()));
        } else if (split[1].equals("EPIC")) {
            return new Epic(split[2], TypeTask.valueOf(split[1]), split[4], StatusTasks.valueOf(split[3])
                    , Integer.parseInt(split[0].trim()));
        } else {
            return new Subtask(split[2], TypeTask.valueOf(split[1]), split[4], StatusTasks.valueOf(split[3])
                    , Integer.parseInt(split[0].trim()), Integer.parseInt(split[5]));
        }
    }

    private static String toString(HistoryManager manager) {
        List<String> idTaskHistory = new ArrayList<>();
        for (int i = 0; i < manager.getHistory().size(); i++) {
            idTaskHistory.add(String.valueOf((manager.getHistory().get(i).getTaskId())));
        }
        return String.join(",", idTaskHistory);
    }

    private static List<Integer> fromString(String value) {
        List<Integer> listIdTasks = new ArrayList<>();
        String[] arrayIdTasks = value.split(",");
        for (int i = 0; i < arrayIdTasks.length; i++) {
            listIdTasks.add(Integer.valueOf(arrayIdTasks[i]));
        }
        return listIdTasks;
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        FileBackedTasksManager managerSave = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            List<String> taskSave = new ArrayList<>();
            while (br.ready()) {
                taskSave.add(br.readLine());
            }
            for (int i = 1; i < taskSave.size() -2; i++) {
                if (fromStrings(taskSave.get(i)).getType().equals(TypeTask.TASK)) {
                    managerSave.addTask(fromStrings(taskSave.get(i)));
                } else if(fromStrings(taskSave.get(i)).getType().equals(TypeTask.EPIC)) {
                    managerSave.addEpic((Epic) fromStrings(taskSave.get(i)));
                } else {
                    managerSave.addSubtask((Subtask) fromStrings(taskSave.get(i)));
                }
            }
            for (Integer id : fromString(taskSave.get(taskSave.size() - 1))) {
                managerSave.getValueById(id);
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка чтения файла");
        }
        return managerSave;
    }
}
