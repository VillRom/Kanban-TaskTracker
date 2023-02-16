package test;

import manager.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {
    private File file;

    @BeforeEach
    public void setUp() throws IOException {
        file = new File("autosave.csv");
        taskManager = new FileBackedTasksManager(file);
        initTasks();
    }

    @Override
    void initTasks() throws IOException {
        super.initTasks();
    }

    @Override
    @Test
    void getTasks() {
        super.getTasks();
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

    @Override
    @Test
    public void intersectionsOfTimeTest() {
        super.intersectionsOfTimeTest();
    }

    @Override
    @Test
    public void calculatingTheEpicExecutionTime() {
        super.calculatingTheEpicExecutionTime();
    }

    @Test
    public void saveTestWithEpicWithEmptyHistory() {
        List<String> taskSave = new ArrayList<>();
        String header = "id,type,name,status,description,duration,epic";
        String task = "1,TASK,Проверка Task,NEW,Task для проверки,10";
        String taskFirst = "2,EPIC,Проверка Epic,IN_PROGRESS,Epic для проверки,15";
        String taskSecond = "3,SUBTASK,Проверка Subtask,IN_PROGRESS,Subtask для проверки,15,2";
        List<String> testTaskSave = List.of(header, task, taskFirst, taskSecond);
        try (BufferedReader br = new BufferedReader(new FileReader(("autosave.csv")))) {
            while (br.ready()){
                taskSave.add(br.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(testTaskSave, taskSave, "в файл записывается неверная информация");
    }

    @Test
    public void loadFromFileTestWithEmptyHistory() {
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        Assertions.assertEquals(1, fileBackedTasksManager.getTasks().size(),
                "в списке тасков не 1 задача");
        Assertions.assertEquals(1, fileBackedTasksManager.getSubtasks().size(),
                "в списке сабтасков не 1 задача");
        Assertions.assertEquals(1, fileBackedTasksManager.getEpics().size(),
                "в списке эпиков не 1 задача");
        Assertions.assertTrue(fileBackedTasksManager.getHistory().isEmpty(), "список просмотров не пустой");
    }
}
