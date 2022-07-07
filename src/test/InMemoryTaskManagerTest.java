package test;

import manager.InMemoryTaskManager;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import java.io.IOException;
import java.time.LocalDateTime;


public class InMemoryTaskManagerTest extends TaskManagerTest {

    @Test
    @Override
    void getTasks() {
        super.getTasks();
    }

    @BeforeEach
    private void setUp() throws IOException {
        taskManager = new InMemoryTaskManager();
        initTasks();
    }

    @Override
    @Test
    void getPrioritizedTasksTest() {
        super.getPrioritizedTasksTest();
    }

    @Override
    @Test
    void getSubtaskFromEpicTest() {
        super.getSubtaskFromEpicTest();
    }

    @Override
    @Test
    void addTaskTest() throws IOException {
        super.addTaskTest();
    }

    @Override
    @Test
    void addEpicTest() {
        super.addEpicTest();
    }

    @Override
    @Test
    void addSubtask() throws IOException {
        super.addSubtask();
    }

    @Override
    @Test
    void getEpics() {
        super.getEpics();
    }

    @Override
    @Test
    void getSubtask() {
        super.getSubtask();
    }

    @Override
    @Test
    void deleteAllTheTasksInListTaskTest() {
        super.deleteAllTheTasksInListTaskTest();
    }

    @Override
    @Test
    void deleteAllTheTasksInListSubtaskTest() {
        super.deleteAllTheTasksInListSubtaskTest();
    }

    @Override
    @Test
    void deleteAllTheTasksInListEpicTest() {
        super.deleteAllTheTasksInListEpicTest();
    }

    @Override
    @Test
    void getValueByIdTest() {
        super.getValueByIdTest();
    }

    @Override
    @Test
    void updateTaskTest() throws IOException {
        super.updateTaskTest();
    }

    @Override
    @Test
    void updateSubtaskTest() throws IOException {
        super.updateSubtaskTest();
    }

    @Override
    @Test
    void updateEpicTestStandart() {
        super.updateEpicTestStandart();
    }

    @Override
    @Test
    void initTasks() throws IOException {
        super.initTasks();
    }

    @Override
    @Test
    void deleteValueTaskByIdTest() {
        super.deleteValueTaskByIdTest();
    }

    @Override
    @Test
    void deleteValueEpicByIdTest() {
        super.deleteValueEpicByIdTest();
    }

    @Override
    @Test
    void deleteValueSubtaskByIdTest() {
        super.deleteValueSubtaskByIdTest();
    }

    @Override
    @Test
    void getHistoryTest() {
        super.getHistoryTest();
    }

    @Test
    public void intersectionsOfTimeTest() {
        final IOException exception = Assertions.assertThrows(IOException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                task.setStartTime(task.getStartTime().plusMinutes(120));
                taskManager.addTask(task);
            }
        });
        Assertions.assertEquals("Пересечение по времени выполнения c задачей - Проверка Subtask",
                exception.getMessage(), "сообщение об исключении не совпало");
    }

    @Test
    public void calculatingTheEpicExecutionTime() {
        Epic epictest = new Epic("Проверка Epic", TypeTask.EPIC, "Epic для проверки", StatusTasks.NEW, 2,
                5);
        final long durationTest = ((Subtask)taskManager.getSubtasks().get(0)).getDuration();
        LocalDateTime dateTime = ((Subtask)taskManager.getSubtasks().get(0)).getStartTime().plusMinutes(durationTest);
        Assertions.assertNotEquals(epictest.getStartTime(), ((Epic)taskManager.getEpics().get(0)).getStartTime(),
                "время начала не изменилось");
        Assertions.assertEquals(((Epic)taskManager.getEpics().get(0)).getStartTime(), ((Subtask)taskManager
                .getSubtasks().get(0)).getStartTime(), "время начала не совпадает с сабтаском");
        Assertions.assertEquals(durationTest, ((Epic)taskManager.getEpics().get(0)).getDuration(),
                "время выполнения не совпадает с сабтаском");
        Assertions.assertEquals(dateTime, ((Epic)taskManager.getEpics().get(0)).getEndTime(),
                "время окончания не совпадает с сабтаском");
    }
}
