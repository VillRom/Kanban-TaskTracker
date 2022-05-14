import manager.InMemoryTaskManager;
import manager.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;

public class Main {

    public static void main(String[] args) {
        TaskManager taskmanager = new InMemoryTaskManager();
        taskmanager.addEpic(new Epic("Уборка", "уборка в доме", "New", 4));
        taskmanager.addSubtask(new Subtask("Вытерить пыль", "на полках шкафа", "New", 1
                , 4));
        taskmanager.addSubtask(new Subtask("Помыть полы", "в комнате", "Done", 2, 4));
        taskmanager.addTask(new Task("Посмотреть урок анг. языка", "Урок №25", "New"
                , 3));
        taskmanager.addTask(new Task("Погулять с собакой", "Вечером в 7 часов", "New"
                , 5));
        taskmanager.addEpic(new Epic("Навестить рабочих", "проверить статус ремонта в квартире"
                , "New", 6));
        taskmanager.addSubtask(new Subtask("Проверить штукатурку стен"
                , "к пятнице обещали сделать корридор", "New", 7, 6));
        System.out.println("Подзадачи --" + taskmanager.getSubtasks());
        System.out.println("Эпики --" + taskmanager.getEpics());
        System.out.println("Задачи --" + taskmanager.getTasks());
        taskmanager.updateTask(3, new Task("Посмотреть урок анг. языка", "Урок №25"
                , "In progress", 3));
        taskmanager.updateSubtask(1, new Subtask("Вытерить пыль", "на полках шкафа", "Done"
                , 1, 4));
        taskmanager.getValueById(1);
        taskmanager.getValueById(2);
        taskmanager.getValueById(3);
        taskmanager.getValueById(2);
        System.out.println("Список просмотров " + taskmanager.getHistory());
        System.out.println("Новый статус подзадачи №1 --" + taskmanager.getSubtasks());
        System.out.println("Новый статус задачи №3 --" + taskmanager.getTasks());
        System.out.println("Новый статус эпика №4 --" + taskmanager.getEpics());
        /*taskmanager.deleteValueTaskById(3);
        taskmanager.deleteValueEpicById(4);
        System.out.println("Удаление в эпике подзадачи №4 --" + taskmanager.getSubtasks());
        System.out.println("Удаление задачи №3 --" + taskmanager.getTasks());
        System.out.println("Удаление эпика №4 --" + taskmanager.getEpics());*/
    }
}
