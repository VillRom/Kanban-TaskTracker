package manager;

import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {
    private static File autoSave;

    public FileBackedTasksManager(File autoSave) {
        this.autoSave = autoSave;
    }

    private void save() {
        try {
            if (autoSave.exists()) {
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(autoSave))) {
                    String header = "id,type,name,status,description,duration,epic";
                    bufferedWriter.write(header);
                    if (!getTasks().isEmpty()) {
                        for (Object value : getTasks()) {
                            bufferedWriter.newLine();
                            bufferedWriter.write(toString((Task) value));
                        }
                    }
                    if (!getEpics().isEmpty()) {
                        for (Object value : getEpics()) {
                            bufferedWriter.newLine();
                            bufferedWriter.write(toString((Epic) value));
                        }
                    }
                    if (!getSubtasks().isEmpty()) {
                        for (Object value : getSubtasks()) {
                            bufferedWriter.newLine();
                            bufferedWriter.write(toString((Subtask) value));
                        }
                    }
                    if (!historyManager.getHistory().isEmpty()) {
                        bufferedWriter.newLine();
                        bufferedWriter.newLine();
                        bufferedWriter.write(toString(historyManager));
                    }
                } catch (IOException e) {
                    System.out.println("Произошла ошибка записи в файл");
                }
            } else {
                autoSave.createNewFile();
                try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(autoSave))) {
                    String header = "id,type,name,status,description,duration,epic";
                    bufferedWriter.write(header);
                    if (!getTasks().isEmpty()) {
                        for (Object value : getTasks()) {
                            bufferedWriter.newLine();
                            bufferedWriter.write(toString((Task) value));
                        }
                    }
                    if (!getEpics().isEmpty()) {
                        for (Object value : getEpics()) {
                            bufferedWriter.newLine();
                            bufferedWriter.write(toString((Epic) value));
                        }
                    }
                    if (!getSubtasks().isEmpty()) {
                        for (Object value : getSubtasks()) {
                            bufferedWriter.newLine();
                            bufferedWriter.write(toString((Subtask) value));
                        }
                    }
                    if (!historyManager.getHistory().isEmpty()) {
                        bufferedWriter.newLine();
                        bufferedWriter.newLine();
                        bufferedWriter.write(toString(historyManager));
                    }
                } catch (IOException e) {
                    System.out.println("Произошла ошибка записи в файл");
                }
            }
        } catch (ManagerSaveException e) {
            System.out.println("Произошла ошибка записи в файл. Ошибка: " + e.getMessages());
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла ошибка записи в файл.");
        }
    }

    @Override
    public void addTask(Task task) throws IOException {
        super.addTask(task);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) throws IOException {
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
        super.getValueById(id);
        save();
        return null;
    }

    @Override
    public void deleteValueEpicById(Integer id) {
        super.deleteValueEpicById(id);
        save();
    }


    private static String toString(Task task) {
        if (task.getType() == TypeTask.SUBTASK) {
            return task.getTaskId() + "," + task.getType() + "," + task.getName() + ","
                    + task.getStatus() + "," + task.getDescription() + "," + task.getDuration() + "," +
                    ((Subtask) task).getEpicId();
        } else {
            return task.getTaskId() + "," + task.getType() + "," + task.getName() + ","
                    + task.getStatus() + "," + task.getDescription() + "," + task.getDuration();
        }
    }

    private static Task fromStrings(String value) {
        String[] split = value.split(",");
        if (split[1].equals("TASK")) {
            return new Task(split[2], TypeTask.valueOf(split[1]), split[4], StatusTasks.valueOf(split[3])
                    , Integer.parseInt(split[0].trim()), Long.parseLong(split[5]));
        } else if (split[1].equals("EPIC")) {
            return new Epic(split[2], TypeTask.valueOf(split[1]), split[4], StatusTasks.valueOf(split[3])
                    , Integer.parseInt(split[0].trim()), Long.parseLong(split[5]));
        } else {
            return new Subtask(split[2], TypeTask.valueOf(split[1]), split[4], StatusTasks.valueOf(split[3])
                    , Integer.parseInt(split[0].trim()), Integer.parseInt(split[6]), Long.parseLong(split[5]));
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
            if (taskSave.size() >= 2 && taskSave.get(taskSave.size() - 2).equals("")) {
                for (int i = 1; i < taskSave.size() - 2; i++) {
                    if (fromStrings(taskSave.get(i)).getType().equals(TypeTask.TASK)) {
                        managerSave.addTask(fromStrings(taskSave.get(i)));
                    } else if (fromStrings(taskSave.get(i)).getType().equals(TypeTask.EPIC)) {
                        managerSave.addEpic((Epic) fromStrings(taskSave.get(i)));
                    } else {
                        managerSave.addSubtask((Subtask) fromStrings(taskSave.get(i)));
                    }
                }
                for (Integer id : fromString(taskSave.get(taskSave.size() - 1))) {
                    managerSave.getValueById(id);
                }
            } else if (taskSave.size() >= 2) {
                for (int i = 1; i < taskSave.size(); i++) {
                    if (fromStrings(taskSave.get(i)).getType().equals(TypeTask.TASK)) {
                        managerSave.addTask(fromStrings(taskSave.get(i)));
                    } else if (fromStrings(taskSave.get(i)).getType().equals(TypeTask.EPIC)) {
                        managerSave.addEpic((Epic) fromStrings(taskSave.get(i)));
                    } else {
                        managerSave.addSubtask((Subtask) fromStrings(taskSave.get(i)));
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Произошла ошибка чтения файла");
        }
        return managerSave;
    }

    /*public static void main(String[] args) throws IOException {
        FileBackedTasksManager taskmanager = new FileBackedTasksManager(new File("autosave.csv"));
        taskmanager.addTask(new Task("Погулять с собакой", TypeTask.TASK, "Выучить команду - лежать"
                , StatusTasks.NEW, 1, 2));
        taskmanager.addTask(new Task("Заправить авто", TypeTask.TASK, "Олви 92-й", StatusTasks.NEW
                , 2, 1));
        taskmanager.addEpic(new Epic("Сварить россольник", TypeTask.EPIC, "по рецепту бабушки"
                , StatusTasks.NEW, 3, 10));
        taskmanager.addSubtask(new Subtask("Сварить бульон", TypeTask.SUBTASK, "Варить 40 минут"
                , StatusTasks.NEW, 4, 3, 5));
        taskmanager.addSubtask(new Subtask("Нарезать и добавить соленые огурцы", TypeTask.SUBTASK
                , "Резать кубиками", StatusTasks.NEW, 5, 3, 4));
        taskmanager.addSubtask(new Subtask("Добавить перловку и картошку", TypeTask.SUBTASK
                , "3 столовые ложки перловки", StatusTasks.NEW, 6, 3, 6));
        taskmanager.addEpic(new Epic("Пожарить гренки", TypeTask.EPIC, "к завтраку", StatusTasks.NEW
                , 7, 8));
        taskmanager.addSubtask(new Subtask("Нарезать хлеб", TypeTask.SUBTASK, "брусочками"
                , StatusTasks.DONE, 8, 7, 9));
        taskmanager.addSubtask(new Subtask("Взбить яйца", TypeTask.SUBTASK, "3шт.", StatusTasks.NEW
                , 9, 7, 10));
        taskmanager.addSubtask(new Subtask("Обвалять и обжарить", TypeTask.SUBTASK, "на среднем огне"
                , StatusTasks.NEW, 10, 7, 5));
        taskmanager.getValueById(2);
        taskmanager.getValueById(7);
        System.out.println("Список просмотренных задач: " + taskmanager.getHistory().size() + taskmanager.getHistory());
        taskmanager.getValueById(3);
        taskmanager.getValueById(7);
        taskmanager.getValueById(2);
        taskmanager.getValueById(5);
        taskmanager.getValueById(1);
        System.out.println("Список просмотренных задач: " + taskmanager.getHistory().size() + taskmanager.getHistory());
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile
                (new File("autosave.csv"));
        System.out.println("Список просмотренных задач: " + fileBackedTasksManager.getHistory().size()
                + fileBackedTasksManager.getHistory());
        if (taskmanager.getTasks().size() == fileBackedTasksManager.getTasks().size() && taskmanager.getEpics().size()
                == fileBackedTasksManager.getEpics().size() && fileBackedTasksManager.getSubtasks().size()
                == taskmanager.getSubtasks().size()) {
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }*/
}

