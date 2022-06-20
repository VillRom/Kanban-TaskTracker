import manager.*;
import model.*;

import java.io.File;

public class Main {

    public static void main(String[] args) {
        FileBackedTasksManager taskmanager = new FileBackedTasksManager(new File("autosave.csv"));
        taskmanager.addTask(new Task("Погулять с собакой", TypeTask.TASK, "Выучить команду - лежать"
                , StatusTasks.NEW, 1));
        taskmanager.addTask(new Task("Заправить авто", TypeTask.TASK, "Олви 92-й", StatusTasks.NEW
                , 2));
        taskmanager.addEpic(new Epic("Сварить россольник",TypeTask.EPIC, "по рецепту бабушки"
                , StatusTasks.NEW, 3));
        taskmanager.addSubtask(new Subtask("Сварить бульон", TypeTask.SUBTASK, "Варить 40 минут"
                , StatusTasks.NEW,4, 3));
        taskmanager.addSubtask(new Subtask("Нарезать и добавить соленые огурцы", TypeTask.SUBTASK
                , "Резать кубиками", StatusTasks.NEW,5, 3));
        taskmanager.addSubtask(new Subtask("Добавить перловку и картошку",TypeTask.SUBTASK
                , "3 столовые ложки перловки", StatusTasks.NEW,6, 3));
        taskmanager.addEpic(new Epic("Пожарить гренки", TypeTask.EPIC, "к завтраку", StatusTasks.NEW
                , 7));
        taskmanager.addSubtask(new Subtask("Нарезать хлеб", TypeTask.SUBTASK, "брусочками"
                , StatusTasks.DONE,8, 7));
        taskmanager.addSubtask(new Subtask("Взбить яйца", TypeTask.SUBTASK, "3шт.", StatusTasks.NEW
                ,9, 7));
        taskmanager.addSubtask(new Subtask("Обвалять и обжарить", TypeTask.SUBTASK, "на среднем огне"
                , StatusTasks.NEW,10, 7));
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
                == taskmanager.getSubtasks().size()){
            System.out.println(true);
        } else {
            System.out.println(false);
        }
    }
}
