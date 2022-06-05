import manager.*;
import model.Epic;
import model.StatusTasks;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskmanager = Managers.getDefault();
        taskmanager.addTask(new Task("Посмотреть урок анг. языка", "Урок №25", StatusTasks.NEW
                , 1));
        taskmanager.addTask(new Task("Погулять с собакой", "Вечером в 7 часов", StatusTasks.NEW
                , 2));
        taskmanager.addEpic(new Epic("Уборка", "уборка в доме", StatusTasks.NEW, 3));
        taskmanager.addSubtask(new Subtask("Вытерить пыль", "на полках шкафа", StatusTasks.NEW, 4
                , 3));
        taskmanager.addSubtask(new Subtask("Помыть полы", "в комнате", StatusTasks.NEW, 5,
                3));
        taskmanager.addSubtask(new Subtask("Пропылесосить", "в зале", StatusTasks.NEW, 6,
                3));
        taskmanager.addEpic(new Epic("Навестить рабочих", "проверить статус ремонта в квартире"
                , StatusTasks.NEW, 7));
        taskmanager.getValueById(1);
        taskmanager.getValueById(2);
        System.out.println("Список просмотренных задач: " + taskmanager.getHistory().size() + taskmanager.getHistory());
        taskmanager.getValueById(3);
        taskmanager.getValueById(2);
        taskmanager.getValueById(5);
        System.out.println("Список просмотренных задач: " + taskmanager.getHistory().size() + taskmanager.getHistory());
        taskmanager.getValueById(4);
        taskmanager.getValueById(7);
        taskmanager.getValueById(1);
        System.out.println("Список просмотренных задач: " + taskmanager.getHistory().size() + taskmanager.getHistory());
        taskmanager.deleteValueEpicById(3);
        System.out.println("Список просмотренных задач: " + taskmanager.getHistory().size() + taskmanager.getHistory());
    }
}
