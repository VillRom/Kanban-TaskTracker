package manager;

import model.StatusTasks;
import model.Task;
import model.TypeTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HistoryManagerTest {
    HistoryManager historyManager = Managers.getDefaultHistory();

    @Test
    public void addTestWithEmptyHistory() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 7, 15);
        historyManager.add(testTask);
        Assertions.assertEquals(1, historyManager.getViewedTask().size(), "список не равен 1");
        Assertions.assertEquals(testTask, historyManager.getHead().data, "задачи не совпадают");
        Assertions.assertEquals(testTask, historyManager.getTail().data, "tail не равен head");
    }

    @Test
    public void addTestWithDuplicationTask() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 7, 15);
        final Task testTaskFirst = new Task("Проверка 2", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 1, 10);
        final Task testTaskSecond = new Task("Проверка 3", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 2, 25);
        historyManager.add(testTask);
        historyManager.add(testTaskFirst);
        historyManager.add(testTaskSecond);
        historyManager.add(testTask);
        Assertions.assertEquals(3, historyManager.getHistory().size(), "список не равен 3");
        Assertions.assertEquals(testTaskFirst, historyManager.getHead().data, "задачи не совпадают");
        Assertions.assertEquals(testTask, historyManager.getTail().data, "tail не равен добавленной задачи");
    }

    @Test
    public void removeTestWithEmptyHistory() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на удаление из истории просмотров", StatusTasks.IN_PROGRESS, 7, 15);
        historyManager.remove(7);
        Assertions.assertTrue(historyManager.getViewedTask().isEmpty(), "Список истории не пустой");
        Assertions.assertNull(historyManager.getHead(), "head не равен null");
        Assertions.assertNull(historyManager.getTail(), "tail не равен null");
    }

    @Test
    public void removeTestInHead() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 7, 15);
        final Task testTaskFirst = new Task("Проверка 2", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 1, 10);
        final Task testTaskSecond = new Task("Проверка 3", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 2, 25);
        historyManager.add(testTask);
        historyManager.add(testTaskFirst);
        historyManager.add(testTaskSecond);
        final int sizeBefore = historyManager.getViewedTask().size();
        historyManager.remove(7);
        Assertions.assertEquals(sizeBefore - 1, historyManager.getViewedTask().size(),
                "список не уменьшился на 1");
        Assertions.assertEquals(testTaskFirst, historyManager.getHead().data, "head не равен нужной задаче");
        Assertions.assertEquals(testTaskSecond, historyManager.getTail().data, "tail не равен нужной задаче");
    }

    @Test
    public void removeTestInMiddle() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 7, 15);
        final Task testTaskFirst = new Task("Проверка 2", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 1, 10);
        final Task testTaskSecond = new Task("Проверка 3", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 2, 25);
        historyManager.add(testTask);
        historyManager.add(testTaskFirst);
        historyManager.add(testTaskSecond);
        final int sizeBefore = historyManager.getViewedTask().size();
        historyManager.remove(1);
        Assertions.assertEquals(sizeBefore - 1, historyManager.getViewedTask().size(),
                "список не уменьшился на 1");
        Assertions.assertEquals(testTask, historyManager.getHead().data, "head не равен нужной задаче");
        Assertions.assertEquals(testTaskSecond, historyManager.getTail().data, "tail не равен нужной задаче");
    }

    @Test
    public void removeTestInEnd() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 7, 15);
        final Task testTaskFirst = new Task("Проверка 2", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 1, 10);
        final Task testTaskSecond = new Task("Проверка 3", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 2, 25);
        historyManager.add(testTask);
        historyManager.add(testTaskFirst);
        historyManager.add(testTaskSecond);
        final int sizeBefore = historyManager.getViewedTask().size();
        historyManager.remove(2);
        Assertions.assertEquals(sizeBefore - 1, historyManager.getViewedTask().size(),
                "список не уменьшился на 1");
        Assertions.assertEquals(testTask, historyManager.getHead().data, "head не равен нужной задаче");
        Assertions.assertEquals(testTaskFirst, historyManager.getTail().data, "tail не равен нужной задаче");
    }

    @Test
    public void getHistoryTestWithEmptyHistory() {
        List<Task> history = historyManager.getHistory();
        Assertions.assertTrue(history.isEmpty(), "история просмотров не пустая");
    }

    @Test
    public void getHistoryTestDuplicationTask() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 7, 15);
        final Task testTaskFirst = new Task("Проверка 2", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 1, 10);
        final Task testTaskSecond = new Task("Проверка 3", TypeTask.TASK,
                "Проверка на добавление в историю просмотров", StatusTasks.IN_PROGRESS, 2, 25);
        historyManager.add(testTask);
        historyManager.add(testTaskFirst);
        historyManager.add(testTaskSecond);
        historyManager.add(testTask);
        List<Task> testHistory = List.of(testTaskFirst, testTaskSecond, testTask);
        List<Task> history = historyManager.getHistory();
        Assertions.assertEquals(testHistory, history, "вывод истории не верный");
        Assertions.assertEquals(3, history.size(), "история задач не равна 3");
    }
}
