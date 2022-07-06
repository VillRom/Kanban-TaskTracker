package manager;

import model.Epic;
import model.StatusTasks;
import model.Subtask;
import model.TypeTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManagerTest extends InMemoryTaskManagerTest{

    private FileBackedTasksManager taskManager = createTaskManagerWithTasks();

    @BeforeEach
    public void reloadTaskManager() {
        taskManager = createTaskManagerWithTasks();
    }

    public FileBackedTasksManager createTaskManagerWithTasks() {
        FileBackedTasksManager tasksManager = new FileBackedTasksManager(new File("autosave.csv"));
        tasksManager.addEpic(new Epic("Сварить россольник", TypeTask.EPIC, "по рецепту бабушки"
                , StatusTasks.IN_PROGRESS, 3, 10));
        tasksManager.addSubtask(new Subtask("Сварить бульон", TypeTask.SUBTASK, "Варить 40 минут"
                , StatusTasks.NEW,4, 3, 15));
        return tasksManager;
    }

    @Override
    @Test
    protected void saveTestWithEmptyListTasks() {
        taskManager.getValueById(3);
        taskManager.deleteAllTheTasksInListEpic();
        taskManager.save();
        List<String> taskSave = new ArrayList<>();
        List<String> testTaskSave = List.of("id,type,name,status,description,duration,epic");
        try (BufferedReader br = new BufferedReader(new FileReader(("autosave.csv")))) {
            while (br.ready()){
                taskSave.add(br.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(testTaskSave, taskSave, "в файл записывается неверная информация");
    }

    @Override
    @Test
    protected void saveTestWithEpicWithoutSubtask() {
        taskManager.getValueById(3);
        taskManager.save();
        List<String> taskSave = new ArrayList<>();
        String header = "id,type,name,status,description,duration,epic";
        String task = "3,EPIC,Сварить россольник,NEW,по рецепту бабушки,15";
        String taskFirst = "4,SUBTASK,Сварить бульон,NEW,Варить 40 минут,15,3";
        List<String> testTaskSave = List.of(header, task,taskFirst, "", "3");
        try (BufferedReader br = new BufferedReader(new FileReader(new File("autosave.csv")))) {
            while (br.ready()){
                taskSave.add(br.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(testTaskSave, taskSave, "в файл записывается неверная информация");
    }

    @Override
    @Test
    protected void saveTestWithEpicWithEmptyHistory() {
        taskManager.save();
        List<String> taskSave = new ArrayList<>();
        String header = "id,type,name,status,description,duration,epic";
        String task = "3,EPIC,Сварить россольник,NEW,по рецепту бабушки,15";
        String taskFirst = "4,SUBTASK,Сварить бульон,NEW,Варить 40 минут,15,3";
        List<String> testTaskSave = List.of(header, task, taskFirst);
        try (BufferedReader br = new BufferedReader(new FileReader(("autosave.csv")))) {
            while (br.ready()){
                taskSave.add(br.readLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assertions.assertEquals(testTaskSave, taskSave, "в файл записывается неверная информация");
    }

    @Override
    @Test
    protected void loadFromFileTestWithEmptyListTasks() {
        taskManager.getValueById(3);
        taskManager.deleteAllTheTasksInListEpic();
        taskManager.save();
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.
                loadFromFile(new File("autosave.csv"));
        Assertions.assertTrue(fileBackedTasksManager.getTasks().isEmpty(), "список тасков не пустой");
        Assertions.assertTrue(fileBackedTasksManager.getEpics().isEmpty(), "список эпиков не пустой");
        Assertions.assertTrue(fileBackedTasksManager.getEpics().isEmpty(), "список эпиков не пустой");
        Assertions.assertTrue(fileBackedTasksManager.getSubtasks().isEmpty(), "список подзадач не пустой");
        Assertions.assertTrue(fileBackedTasksManager.getHistory().isEmpty(), "список просмотров не пустой");
    }

    @Override
    @Test
    protected void loadFromFileTestWithEpicWithoutSubtask() {
        taskManager.deleteAllTheTasksInListSubtask();
        taskManager.getValueById(3);
        taskManager.save();
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.
                loadFromFile(new File("autosave.csv"));
        Assertions.assertTrue(fileBackedTasksManager.getTasks().isEmpty(), "список тасков не пустой");
        Assertions.assertFalse(fileBackedTasksManager.getEpics().isEmpty(), "список эпиков пустой");
        Assertions.assertEquals(1, fileBackedTasksManager.getEpics().size(),
                "в списке эпиков не 1 задача");
        Assertions.assertTrue(fileBackedTasksManager.getSubtasks().isEmpty(), "список подзадач не пустой");
        Assertions.assertEquals(1, fileBackedTasksManager.getHistory().size(),
                "размер списка просмотра не совпадает");
    }

    @Override
    @Test
    protected void loadFromFileTestWithEpicWithEmptyHistory() {
        taskManager.save();
        FileBackedTasksManager fileBackedTasksManager = FileBackedTasksManager.
                loadFromFile(new File("autosave.csv"));
        Assertions.assertTrue(fileBackedTasksManager.getTasks().isEmpty(), "список тасков не пустой");
        Assertions.assertFalse(fileBackedTasksManager.getEpics().isEmpty(), "список эпиков пустой");
        Assertions.assertEquals(1, fileBackedTasksManager.getEpics().size(),
                "в списке эпиков не 1 задача");
        Assertions.assertFalse(fileBackedTasksManager.getSubtasks().isEmpty(), "список подзадач пустой");
        Assertions.assertEquals(1, fileBackedTasksManager.getSubtasks().size(),
                "в списке подзадач не 1 задача");
        Assertions.assertTrue(fileBackedTasksManager.getHistory().isEmpty(), "список просмотров не пустой");
    }

    @Override
    void checkAvailableEpicInSubtask() {
        super.checkAvailableEpicInSubtask();
    }

    @Override
    @Test
    public void updateEpicStatusWithEmptylistSubtask() {
        super.updateEpicStatusWithEmptylistSubtask();
    }

    @Override
    @Test
    public void updateEpicStatusWithAllSubtaskNew() {
        super.updateEpicStatusWithAllSubtaskNew();
    }

    @Override
    @Test
    public void updateEpicStatusWithAllSubtaskStatusDone() {
        super.updateEpicStatusWithAllSubtaskStatusDone();
    }

    @Override
    @Test
    public void updateEpicStatusWithStatusSubTaskNewAndDone() {
        super.updateEpicStatusWithStatusSubTaskNewAndDone();
    }

    @Override
    @Test
    public void updateEpicStatusWithAllSubtaskStatusInProgress() {
        super.updateEpicStatusWithAllSubtaskStatusInProgress();
    }

    @Override
    @Test
    void addTaskTestStandart() {
        super.addTaskTestStandart();
    }

    @Override
    @Test
    public void addTaskTestWithEmptyListTask() {
        super.addTaskTestWithEmptyListTask();
    }

    @Override
    @Test
    void addEpicTestStandart() {
        super.addEpicTestStandart();
    }

    @Override
    @Test
    void addEpicTestWithEmptyListEpics() {
        super.addEpicTestWithEmptyListEpics();
    }

    @Override
    @Test
    void addSubtaskTestStandart() {
        super.addSubtaskTestStandart();
    }

    @Override
    @Test
    void addSubtaskWithEmptyListSubtask() {
        super.addSubtaskWithEmptyListSubtask();
    }

    @Override
    @Test
    void getTasksTestStandart() {
        super.getTasksTestStandart();
    }

    @Override
    @Test
    void getTasksTestWithEmptyListTask() {
        super.getTasksTestWithEmptyListTask();
    }

    @Override
    @Test
    void getEpicsTestStandart() {
        super.getEpicsTestStandart();
    }

    @Override
    @Test
    void getEpicsWithEmptyListEpics() {
        super.getEpicsWithEmptyListEpics();
    }

    @Override
    @Test
    void getSubtaskTestStandart() {
        super.getSubtaskTestStandart();
    }

    @Override
    @Test
    void deleteAllTheTasksInListTaskTestStandart() {
        super.deleteAllTheTasksInListTaskTestStandart();
    }

    @Override
    @Test
    void deleteAllTheTasksInListTaskEmptyTest() {
        super.deleteAllTheTasksInListTaskEmptyTest();
    }

    @Override
    @Test
    void getSubtaskTestWithEmptyListSubtask() {
        super.getSubtaskTestWithEmptyListSubtask();
    }

    @Override
    @Test
    void deleteAllTheTasksInListSubtaskTestStandart() {
        super.deleteAllTheTasksInListSubtaskTestStandart();
    }

    @Override
    @Test
    void deleteAllTheTasksInListSubtaskEmptyTest() {
        super.deleteAllTheTasksInListSubtaskEmptyTest();
    }

    @Override
    @Test
    void deleteAllTheTasksInListEpicTestStandart() {
        super.deleteAllTheTasksInListEpicTestStandart();
    }

    @Override
    @Test
    void deleteAllTheTasksInListEmptyEpicTest() {
        super.deleteAllTheTasksInListEmptyEpicTest();
    }

    @Override
    @Test
    void getValueByIdTestStandart() {
        super.getValueByIdTestStandart();
    }

    @Override
    @Test
    void getValueByIdTestWithEmptyListsTasks() {
        super.getValueByIdTestWithEmptyListsTasks();
    }

    @Override
    @Test
    void getValueByIdTestWithWrongId() {
        super.getValueByIdTestWithWrongId();
    }

    @Override
    @Test
    void updateTaskTestStandart() {
        super.updateTaskTestStandart();
    }

    @Override
    @Test
    void updateTaskTestWithEmptyListTasks() {
        super.updateTaskTestWithEmptyListTasks();
    }

    @Override
    @Test
    void updateTaskTestWithWrongId() {
        super.updateTaskTestWithWrongId();
    }

    @Override
    @Test
    void updateEpicTestStandart() {
        super.updateEpicTestStandart();
    }

    @Override
    @Test
    void updateEpicTestWithEmptyListTasks() {
        super.updateEpicTestWithEmptyListTasks();
    }

    @Override
    @Test
    void updateEpicTestWithWrongId() {
        super.updateEpicTestWithWrongId();
    }

    @Override
    @Test
    void deleteValueTaskByIdTestStandart() {
        super.deleteValueTaskByIdTestStandart();
    }

    @Override
    @Test
    void deleteValueTaskByIdTestWithEmptyListTasks() {
        super.deleteValueTaskByIdTestWithEmptyListTasks();
    }

    @Override
    @Test
    void deleteValueTaskByIdTestWithWrongId() {
        super.deleteValueTaskByIdTestWithWrongId();
    }

    @Override
    @Test
    void deleteValueEpicByIdTestStandart() {
        super.deleteValueEpicByIdTestStandart();
    }

    @Override
    @Test
    void deleteValueEpicByIdTestWithEmptyListEpics() {
        super.deleteValueEpicByIdTestWithEmptyListEpics();
    }

    @Override
    @Test
    void deleteValueEpicByIdTestWithWrongId() {
        super.deleteValueEpicByIdTestWithWrongId();
    }

    @Override
    @Test
    void deleteValueSubtaskByIdTestStandart() {
        super.deleteValueSubtaskByIdTestStandart();
    }

    @Override
    @Test
    void deleteValueSubtaskByIdTestWithEmptyListSubtask() {
        super.deleteValueSubtaskByIdTestWithEmptyListSubtask();
    }

    @Override
    @Test
    void deleteValueSubtaskByIdIdTestWithWrongId() {
        super.deleteValueSubtaskByIdIdTestWithWrongId();
    }

    @Override
    @Test
    void startDateTimeEpicTestStandart() {
        super.startDateTimeEpicTestStandart();
    }

    @Override
    @Test
    void getEndTimeTestStandart() {
        super.getEndTimeTestStandart();
    }
}
