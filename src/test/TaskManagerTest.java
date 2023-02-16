package test;

import manager.TaskManager;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected Subtask subtask;
    protected Epic epic;

    void initTasks() throws IOException {
        task = new Task("Проверка Task", TypeTask.TASK,
                "Task для проверки", StatusTasks.NEW, 1, 10);
        epic = new Epic("Проверка Epic", TypeTask.EPIC, "Epic для проверки", StatusTasks.NEW, 2,
                5);
        subtask = new Subtask("Проверка Subtask", TypeTask.SUBTASK, "Subtask для проверки"
                , StatusTasks.IN_PROGRESS,3, 2, 15);
        task.setStartTime((LocalDateTime.now()));
        epic.setStartTime(LocalDateTime.now().plusMinutes(60));
        subtask.setStartTime(LocalDateTime.now().plusMinutes(120));
        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subtask);
    }

    @Test
    void getTasks() {
        List<Task> tasksList = taskManager.getTasks();
        Assertions.assertNotNull(tasksList, "список равен null, метод не возвращает задачи");
        Assertions.assertEquals(1, tasksList.size(), " размер списка не совпадает");
        Assertions.assertEquals(task, tasksList.get(0), "задачи не совпали");
    }

    @Test
    void getPrioritizedTasksTest() {
        final List<Task> sortTasks = taskManager.getPrioritizedTasks();
        Assertions.assertNotNull(sortTasks, "список пустой, метод не возвращает отсортированный список");
        Assertions.assertEquals(2, sortTasks.size(), "размер списка не совпал");
        Assertions.assertEquals(task, sortTasks.get(0), "первая задача не совпала");
        Assertions.assertEquals(subtask, sortTasks.get(1), "вторая задача не совпала");
    }
    @Test
    void getSubtaskFromEpicTest() {
        final List<Subtask> subtasksFromEpic = taskManager.getSubtaskFromEpic(2);
        Assertions.assertNotNull(subtasksFromEpic, "метод не возвращает список сабтасков из эпика");
        Assertions.assertEquals(1, subtasksFromEpic.size(), "размер списка не совпадает");
        Assertions.assertEquals(subtask, subtasksFromEpic.get(0), "задача не совпала");
    }

    @Test
    void addTaskTest() throws IOException {
        final Task task = new Task("Проверка Task2", TypeTask.TASK,
                "Task2 для проверки", StatusTasks.NEW, 4, 15);
        task.setStartTime(LocalDateTime.now().plusMinutes(180));
        taskManager.addTask(task);
        Assertions.assertEquals(2, taskManager.getTasks().size(), "размер списка не совпал");
        Assertions.assertEquals(3, taskManager.getPrioritizedTasks().size(),
                "размер отсортированного списка не совпадает");
        final IOException exception = Assertions.assertThrows(IOException.class, () -> {
            task.setStartTime(taskManager.getSubtasks().get(0).getStartTime().plusMinutes(1));
            taskManager.addTask(task);
        });
        Assertions.assertEquals("Пересечение по времени выполнения c задачей - Проверка Subtask",
                exception.getMessage(), "сообщение об исключении не совпало");
    }

    @Test
    void addEpicTest() {
        final Epic epic = new Epic("Проверка Epic", TypeTask.EPIC, "Epic для проверки", StatusTasks.NEW,
                4, 5);
        taskManager.addEpic(epic);
        Assertions.assertEquals(2, taskManager.getEpics().size(), "размер списка не совпадает");
        Assertions.assertEquals(epic, taskManager.getEpics().get(1), "задача не совпала");
    }

    @Test
    void addSubtask() throws IOException {
        final Subtask subtask = new Subtask("Проверка Subtask", TypeTask.SUBTASK, "Subtask для проверки"
                , StatusTasks.IN_PROGRESS,5, 2, 15);
        subtask.setStartTime(LocalDateTime.now().plusMinutes(240));
        taskManager.addTask(task);
        taskManager.addSubtask(subtask);
        Assertions.assertEquals(2, taskManager.getSubtasks().size(), "размер списка не совпадает");
        Assertions.assertEquals(3, taskManager.getPrioritizedTasks().size(),
                "размер сортированного списка не совпадает");
        Assertions.assertEquals(subtask, taskManager.getSubtasks().get(1), "задача не совпала");
        Assertions.assertEquals(StatusTasks.IN_PROGRESS, taskManager.getEpics().get(0).getStatus(),
                "статус эпика не совпал");
    }

    @Test
    void getEpics() {
        List<Task> tasksList = taskManager.getEpics();
        Assertions.assertNotNull(tasksList, "список равен null, метод не возвращает задачи");
        Assertions.assertEquals(1, tasksList.size(), " размер списка не совпадает");
        Assertions.assertEquals(epic, tasksList.get(0), "задачи не совпали");
    }

    @Test
    void getSubtask() {
        List<Task> tasksList = taskManager.getSubtasks();
        Assertions.assertNotNull(tasksList, "список равен null, метод не возвращает задачи");
        Assertions.assertEquals(1, tasksList.size(), " размер списка не совпадает");
        Assertions.assertEquals(subtask, tasksList.get(0), "задачи не совпали");
    }

    @Test
    void deleteAllTheTasksInListTaskTest() {
        final List<Task> testListHistory = List.of(epic);
        taskManager.getValueById(1);
        taskManager.getValueById(2);
        taskManager.deleteAllTheTasksInListTask();
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "Список не пустой");
    }

    @Test
    void deleteAllTheTasksInListSubtaskTest() {
        taskManager.getValueById(1);
        taskManager.getValueById(3);
        final List<Object> testListHistory = List.of(task);
        taskManager.deleteAllTheTasksInListSubtask();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "Список не пустой");
        Assertions.assertTrue(epic.getSubtaskIds().isEmpty(), "Список подзадач в эпике не пустой");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
        Assertions.assertEquals(StatusTasks.NEW, epic.getStatus(), "Cтатус не поменялся");
        Assertions.assertNull(epic.getStartTime(), "старт эпика не null");
    }

    @Test
    void deleteAllTheTasksInListEpicTest() {
        taskManager.getValueById(2);
        taskManager.getValueById(3);
        taskManager.deleteAllTheTasksInListEpic();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "Список подзадач не пустой");
        Assertions.assertTrue(taskManager.getEpics().isEmpty(), "Список Эпиков не пустой");
        Assertions.assertTrue(taskManager.getHistory().isEmpty(), "Список не пустой");
    }

    @Test
    void getValueByIdTest() {
        final Epic epicTest = epic;
        taskManager.getValueById(2);
        final List<Task> testListHistory = new ArrayList<>();
        testListHistory.add(epic);
        Assertions.assertEquals(TypeTask.EPIC, epicTest.getType(), "Тип задач не совпадает");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
        final Task taskTest = task;
        taskManager.getValueById(1);
        testListHistory.add(task);
        Assertions.assertEquals(TypeTask.TASK, taskTest.getType(), "Тип задач не совпадает");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
        final Subtask subtaskTest = subtask;
        taskManager.getValueById(3);
        testListHistory.add(subtask);
        Assertions.assertEquals(TypeTask.SUBTASK, subtaskTest.getType(), "Тип задач не совпадает");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
    }

    @Test
    void updateTaskTest() throws IOException {
        final Task taskTest = new Task("Проверка Task2", TypeTask.TASK,
                "Task2 для проверки", StatusTasks.NEW, 1, 10);
        taskTest.setStartTime(LocalDateTime.now().plusMinutes(3600));
        taskManager.updateTask(1, taskTest);
        Assertions.assertFalse(taskManager.getPrioritizedTasks().contains(task)
                , "задача не удалилась из сортированного списка");
        Assertions.assertEquals(taskTest, taskManager.getTasks().get(0), "задача не изменилась");
        Assertions.assertTrue(taskManager.getPrioritizedTasks().contains(taskTest),
                "задача не добавилась в сортированный список");
    }

    @Test
    void updateSubtaskTest() throws IOException {
        final Subtask subtaskTest = new Subtask("Проверка Subtask", TypeTask.SUBTASK,
                "Subtask для проверки", StatusTasks.IN_PROGRESS,3, 2, 5);
        subtaskTest.setStartTime(LocalDateTime.now().plusMinutes(60));
        taskManager.updateSubtask(3, subtaskTest);
        Assertions.assertFalse(taskManager.getPrioritizedTasks().contains(subtask)
                , "задача не удалилась из сортированного списка");
        Assertions.assertEquals(subtaskTest, taskManager.getSubtasks().get(0), "задача не изменилась");
        Assertions.assertTrue(taskManager.getPrioritizedTasks().contains(subtaskTest),
                "задача не добавилась в сортированный список");
    }

    @Test
    void updateEpicTestStandart() {
        final Epic epicTest = new Epic("Проверка Epic1", TypeTask.EPIC, "Epic1 для проверки",
                StatusTasks.NEW, 2, 5);
        epicTest.setStartTime(LocalDateTime.now().plusMinutes(60));
        taskManager.updateEpic(2, epicTest);
        Assertions.assertEquals(epicTest, taskManager.getEpics().get(0), "задача не изменилась");
        Assertions.assertEquals(epicTest.getSubtaskIds(), ((Epic)taskManager.getEpics().get(0)).getSubtaskIds(),
                "список id сабтасков в эпике изменился");
    }

    @Test
    void deleteValueTaskByIdTest() {
        taskManager.getValueById(1);
        taskManager.deleteValueTaskById(1);
        Assertions.assertTrue(taskManager.getHistory().isEmpty(),
                "задача не удалилась из истории просмотра");
        Assertions.assertEquals(1, taskManager.getPrioritizedTasks().size(),
                "список истории не равен 1");
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "список задач не пустой");
    }

    @Test
    void deleteValueEpicByIdTest() {
        taskManager.getValueById(1);
        taskManager.getValueById(2);
        taskManager.getValueById(3);
        taskManager.deleteValueEpicById(2);
        final List<Task> history = List.of(task);
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "список сабтасков не удалился");
        Assertions.assertEquals(1, taskManager.getHistory().size(),
                "просмотр задач не изменился");
        Assertions.assertEquals(history, taskManager.getHistory(),
                "список просмотра не совпадает");
        Assertions.assertTrue(taskManager.getEpics().isEmpty(), "список эпиков не удалился");
    }

    @Test
    void deleteValueSubtaskByIdTest() {
        taskManager.getValueById(1);
        taskManager.getValueById(3);
        taskManager.deleteValueSubtaskById(3);
        final List<Task> history = List.of(task);
        Assertions.assertEquals(history, taskManager.getHistory(),
                "просмотр задач не изменился");
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "подзадача не удалилась");
        Assertions.assertEquals(StatusTasks.NEW, taskManager.getEpics().get(0).getStatus(),
                "статус эпика изменился");
        Assertions.assertTrue(((Epic) taskManager.getEpics().get(0)).getSubtaskIds().isEmpty(),
                "список id сабтасков в эпике не пустой");
    }

    @Test
    void getHistoryTest() {
        taskManager.getValueById(1);
        taskManager.getValueById(2);
        final List<Task> history = taskManager.getHistory();
        Assertions.assertNotNull(history, "список равен null, метод не возвращает задачи");
        Assertions.assertEquals(2, history.size(), " размер списка не совпадает");
        Assertions.assertEquals(task, history.get(0), "задачи не совпали");
        Assertions.assertEquals(epic, history.get(1), "задачи не совпали");
    }
}
