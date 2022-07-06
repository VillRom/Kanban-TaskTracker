package manager;

import model.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



public abstract class InMemoryTaskManagerTest extends TaskManagerTest {

    private TaskManager taskManager = createTaskManagerWithTasks();

    @AfterEach
    public void reloadTaskManager() {
        taskManager = createTaskManagerWithTasks();
    }

    public TaskManager createTaskManagerWithTasks() {
        TaskManager tasksManager = new InMemoryTaskManager();
        tasksManager.addEpic(new Epic("Сварить россольник", TypeTask.EPIC, "по рецепту бабушки"
                , StatusTasks.IN_PROGRESS, 3, 10));
        tasksManager.addSubtask(new Subtask("Сварить бульон", TypeTask.SUBTASK, "Варить 40 минут"
                , StatusTasks.NEW,4, 3, 15));
        return tasksManager;
    }

    @Test
    protected abstract void saveTestWithEmptyListTasks();

    @Test
    protected abstract void saveTestWithEpicWithoutSubtask();

    @Test
    protected abstract void saveTestWithEpicWithEmptyHistory();

    @Test
    protected abstract void loadFromFileTestWithEmptyListTasks();

    @Test
    protected abstract void loadFromFileTestWithEpicWithoutSubtask();

    @Test
    protected abstract void loadFromFileTestWithEpicWithEmptyHistory();

    @Override
    @Test
    void checkAvailableEpicInSubtask() {
        Assertions.assertNotNull(taskManager.getValueById(((Subtask) taskManager.getValueById(4)).getEpicId()));
        Assertions.assertEquals(3, taskManager.getValueById(((Subtask) taskManager.getValueById(4))
                .getEpicId()).getTaskId());
    }

    @Override
    @Test
    public void updateEpicStatusWithEmptylistSubtask() {
        taskManager.addEpic(new Epic("Сварить россольник", TypeTask.EPIC, "по рецепту бабушки"
                , StatusTasks.NEW, 3, 10));
        assertEquals(StatusTasks.NEW, taskManager.getValueById(3).getStatus(), "Статус отличается");
    }

    @Override
    @Test
    public void updateEpicStatusWithAllSubtaskNew() {
        assertEquals(StatusTasks.NEW, taskManager.getValueById(3).getStatus());
    }

    @Override
    @Test
    public void updateEpicStatusWithAllSubtaskStatusDone() {
        taskManager.updateSubtask(4, new Subtask("Сварить бульон", TypeTask.SUBTASK, "Варить 40 минут"
                , StatusTasks.DONE,4, 3, 15));
        taskManager.updateSubtask(5, new Subtask("Нарезать и добавить соленые огурцы", TypeTask.SUBTASK
                , "Резать кубиками", StatusTasks.DONE,5, 3, 10));
        assertEquals(StatusTasks.DONE, taskManager.getValueById(3).getStatus());
    }

    @Override
    @Test
    public void updateEpicStatusWithStatusSubTaskNewAndDone() {
        taskManager.updateSubtask(4, new Subtask("Сварить бульон", TypeTask.SUBTASK, "Варить 40 минут"
                , StatusTasks.DONE,4, 3,15));
        taskManager.addSubtask(new Subtask("Проверка", TypeTask.SUBTASK,
                "Проверка на добавление задачи в конец списка", StatusTasks.NEW, 5, 3, 10));
        assertEquals(StatusTasks.IN_PROGRESS, taskManager.getValueById(3).getStatus());
    }

    @Override
    @Test
    public void updateEpicStatusWithAllSubtaskStatusInProgress() {
        taskManager.updateSubtask(4, new Subtask("Сварить бульон", TypeTask.SUBTASK,
                "Варить 40 минут", StatusTasks.DONE,4, 3, 15));
        assertEquals(StatusTasks.DONE, taskManager.getValueById(3).getStatus());
        taskManager = createTaskManagerWithTasks();
    }

    @Override
    @Test
    void addTaskTestStandart() {
        taskManager.addTask(new Task("Проверка 1", TypeTask.TASK,
                "Проверка на добавление в конец списка Tasks", StatusTasks.NEW, 7, 10));
        final Task taskTest = new Task("Проверка 2", TypeTask.TASK,
                "Проверка на добавление в конец списка Tasks 2", StatusTasks.NEW, 8, 15);
        taskManager.addTask(taskTest);
        Assertions.assertNotNull(taskManager.getTasks().get(taskManager.getTasks().size() - 1));
        Assertions.assertEquals(taskTest, taskManager.getTasks().get(taskManager.getTasks().size() - 1));
    }


    @Test
    @Override
    public void addTaskTestWithEmptyListTask() {
        taskManager.deleteAllTheTasksInListTask();
        final Task taskTest = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на добавление в пустой список Tasks", StatusTasks.NEW, 7, 10);
        taskManager.addTask(taskTest);
        Assertions.assertEquals(1, taskManager.getTasks().size(), "В списке больше одной задачи");
        Assertions.assertEquals(taskTest, taskManager.getTasks().get(taskManager.getTasks().size() - 1));
    }

    @Override
    @Test
    void addEpicTestStandart() {
        final Epic testEpicTask = new Epic("Проверка 1", TypeTask.EPIC,
                "Проверка на добавление в конец списка epics", StatusTasks.NEW, 9, 15);
        final int sizeListEpicsBefore = taskManager.getEpics().size();
        taskManager.addEpic(testEpicTask);
        Assertions.assertEquals(sizeListEpicsBefore + 1,
                taskManager.getEpics().size(), "Длина списка не увеличилась");
        Assertions.assertEquals(testEpicTask, taskManager.getEpics().get(taskManager.getEpics().size() - 1),
                "Задачи не равны");
    }

    @Override
    @Test
    void addEpicTestWithEmptyListEpics() {
        taskManager.deleteAllTheTasksInListEpic();
        final Epic testEpicTask = new Epic("Проверка 1", TypeTask.EPIC,
                "Проверка на добавление в пустой список epics", StatusTasks.NEW, 9, 15);
        taskManager.addEpic(testEpicTask);
        Assertions.assertFalse(taskManager.getEpics().isEmpty(), "Список пустой");
        Assertions.assertEquals(1, taskManager.getEpics().size(), "В списке больше 1 задачи");
        Assertions.assertEquals(testEpicTask, taskManager.getEpics().get(0), "Задачи не совпадают");
    }

    @Override
    @Test
    void addSubtaskTestStandart() {
        Subtask testSubtask = new Subtask("Проверка", TypeTask.SUBTASK,
                "Проверка на добавление задачи в конец списка", StatusTasks.NEW, 10, 3, 15);
        final int sizeListSubtaskBefore = taskManager.getSubtasks().size();
        final long durationAfter = ((Epic)taskManager.getEpics().get(0)).getDuration();
        taskManager.addSubtask(testSubtask);
        Assertions.assertEquals(sizeListSubtaskBefore + 1, taskManager.getSubtasks().size(),
                "Длина списка не увеличилась");
        Assertions.assertEquals(testSubtask, taskManager.getSubtasks().get(taskManager.getSubtasks().size() - 1),
                "Задачи не равны");
        Assertions.assertNotEquals(durationAfter, ((Epic)taskManager.getEpics().get(0)).getDuration(),
                "Продолжительность не изменилась");
        Assertions.assertEquals(30, ((Epic)taskManager.getEpics().get(0)).getDuration(),
                "Продолжительность не совпадает");
    }

    @Override
    @Test
    void addSubtaskWithEmptyListSubtask() {
        taskManager.deleteAllTheTasksInListSubtask();
        final Subtask testSubtask = new Subtask("Проверка", TypeTask.SUBTASK,
                "Проверка на добавление задачи в пустой список subtask", StatusTasks.NEW, 10, 3,
                15);
        taskManager.addSubtask(testSubtask);
        Assertions.assertFalse(taskManager.getSubtasks().isEmpty(), "Список пустой");
        Assertions.assertEquals(1, taskManager.getSubtasks().size(), "В списке больше 1 задачи");
        Assertions.assertEquals(testSubtask, taskManager.getSubtasks().get(0), "Задачи не совпадают");
    }

    @Override
    @Test
    void getTasksTestStandart() {
        final Task testTaskFirst = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на возврат списка Тасков 1", StatusTasks.NEW, 7, 15);
        final Task testTaskSecond = new Task("Проверка 2", TypeTask.TASK,
                "Проверка на возврат списка Тасков 2", StatusTasks.NEW, 8,10);
        final List<Task> taskListEmpty = new ArrayList<>();
        final List<Task> taskList = new ArrayList<>();
        taskManager.addTask(testTaskFirst);
        taskManager.addTask(testTaskSecond);
        taskList.add(testTaskFirst);
        taskList.add(testTaskSecond);
        Assertions.assertNotEquals(taskListEmpty, taskManager.getTasks(), "Список пустой");
        Assertions.assertEquals(taskList, taskManager.getTasks(), "Списки не равны");
    }

    @Override
    @Test
    void getTasksTestWithEmptyListTask() {
        final List<Task> testListTask = new ArrayList<>();
        Assertions.assertEquals(0, taskManager.getTasks().size(), "Список не пустой");
        Assertions.assertEquals(testListTask, taskManager.getTasks(), "Списки не равны");
    }

    @Override
    @Test
    void getEpicsTestStandart() {
        taskManager.deleteAllTheTasksInListEpic();
        final Epic testEpicFirst = new Epic("Проверка 1", TypeTask.EPIC,
                "Проверка на возврат списка Эпиков 1", StatusTasks.NEW, 7, 10);
        final List<Epic> epicListEmpty = new ArrayList<>();
        final List<Epic> epicList = new ArrayList<>();
        taskManager.addEpic(testEpicFirst);
        epicList.add(testEpicFirst);
        Assertions.assertNotEquals(epicListEmpty, taskManager.getEpics(), "Список пустой");
        Assertions.assertEquals(epicList, taskManager.getEpics(), "Списки не равны");
    }

    @Override
    @Test
    void getEpicsWithEmptyListEpics() {
        taskManager.deleteAllTheTasksInListEpic();
        final List<Epic> testListEpics = new ArrayList<>();
        Assertions.assertEquals(0, taskManager.getEpics().size(), "Список не пустой");
        Assertions.assertEquals(testListEpics, taskManager.getTasks(), "Списки не равны");
    }

    @Override
    @Test
    void getSubtaskTestStandart() {
        final List<Subtask> testListSubtasks = new ArrayList<>();
        final List<Subtask> testListSubtasksEmpty = new ArrayList<>();
        testListSubtasks.add((Subtask) taskManager.getSubtasks().get(0));
        Assertions.assertNotEquals(testListSubtasksEmpty, taskManager.getSubtasks(), "Список пустой");
        Assertions.assertEquals(testListSubtasks, taskManager.getSubtasks(), "Списки не равны");
    }

    @Override
    @Test
    void deleteAllTheTasksInListTaskTestStandart() {
        final Task testTaskFirst = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на удаление списка Тасков", StatusTasks.NEW, 7, 15);
        final Task testTaskSecond = new Task("Проверка 2", TypeTask.TASK,
                "Проверка на удаление списка Тасков", StatusTasks.NEW, 8, 10);
        taskManager.addTask(testTaskFirst);
        taskManager.addTask(testTaskSecond);
        final List<Object> testListHistory = List.of(taskManager.getEpics().get(0));
        taskManager.getValueById(3);
        taskManager.getValueById(8);
        taskManager.deleteAllTheTasksInListTask();
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "Список не пустой");
    }

    @Override
    @Test
    void deleteAllTheTasksInListTaskEmptyTest() {
        taskManager.getValueById(3);
        taskManager.getValueById(4);
        final  List<Object> testListHistory = List.of(taskManager.getEpics().get(0), taskManager.getSubtasks().get(0));
        taskManager.deleteAllTheTasksInListTask();
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "Список не пустой");
    }

    @Override
    @Test
    void getSubtaskTestWithEmptyListSubtask() {
        taskManager.deleteAllTheTasksInListSubtask();
        final List<Subtask> testListSubtasksEmpty = new ArrayList<>();
        Assertions.assertEquals(0, taskManager.getSubtasks().size(), "Список не пустой");
        Assertions.assertEquals(testListSubtasksEmpty, taskManager.getSubtasks(), "Списки не равны");
    }

    @Override
    @Test
    void deleteAllTheTasksInListSubtaskTestStandart() {
        taskManager.getValueById(4);
        taskManager.getValueById(3);
        final List<Object> testListHistory = List.of(taskManager.getEpics().get(0));
        taskManager.deleteAllTheTasksInListSubtask();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "Список не пустой");
        Assertions.assertTrue(((Epic) taskManager.getEpics().get(taskManager.getEpics().size() - 1)).
                getSubtaskIds().isEmpty(), "Список подзадач в эпике не пустой");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
        Assertions.assertEquals(StatusTasks.NEW, ((Epic) taskManager.getEpics().
                get(taskManager.getEpics().size() - 1)).getStatus(), "Cтатус не поменялся");
    }

    @Override
    @Test
    void deleteAllTheTasksInListSubtaskEmptyTest() {
        TaskManager taskManagerTest = new InMemoryTaskManager();
        taskManagerTest.addTask(new Task("Проверка 1", TypeTask.TASK,
                "Проверка на удаление списка Тасков", StatusTasks.NEW, 7, 15));
        taskManagerTest.getValueById(7);
        final List<Object> testListHistory = List.of(taskManagerTest.getTasks().get(0));
        taskManagerTest.deleteAllTheTasksInListSubtask();
        Assertions.assertTrue(taskManagerTest.getSubtasks().isEmpty(), "Список не пустой");
        Assertions.assertEquals(testListHistory, taskManagerTest.getHistory(), "Списки не равны");
    }

    @Override
    @Test
    void deleteAllTheTasksInListEpicTestStandart() {
        taskManager.getValueById(4);
        taskManager.getValueById(3);
        final List<Object> testListHistory = new ArrayList<>();
        taskManager.deleteAllTheTasksInListEpic();
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "Список подзадач не пустой");
        Assertions.assertTrue(taskManager.getEpics().isEmpty(), "Список Эпиков не пустой");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
    }

    @Override
    @Test
    void deleteAllTheTasksInListEmptyEpicTest() {
        TaskManager taskManagerTest = new InMemoryTaskManager();
        taskManagerTest.addTask(new Task("Проверка 1", TypeTask.TASK,
                "Проверка на удаление списка Тасков", StatusTasks.NEW, 7, 15));
        taskManagerTest.getValueById(7);
        final List<Object> testListHistory = List.of(taskManagerTest.getTasks().get(0));
        taskManagerTest.deleteAllTheTasksInListEpic();
        Assertions.assertTrue(taskManagerTest.getSubtasks().isEmpty(), "Список не пустой");
        Assertions.assertTrue(taskManagerTest.getEpics().isEmpty(), "Список Эпиков не пустой");
        Assertions.assertEquals(testListHistory, taskManagerTest.getHistory(), "Списки не равны");
    }

    @Override
    @Test
    void getValueByIdTestStandart() {
        final List<Object> testListHistory = List.of(taskManager.getEpics().get(0));
        final Task epicTest = (Task) taskManager.getEpics().get(0);
        final Task epicTaskTet = taskManager.getValueById(3);
        Assertions.assertEquals(TypeTask.EPIC, taskManager.getValueById(3).getType(), "Тип задач не совпадает");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "Списки не равны");
        Assertions.assertEquals(epicTest, epicTaskTet, "Задачи не равны");
    }

    @Override
    @Test
    void getValueByIdTestWithEmptyListsTasks() {
        TaskManager taskManagerTest = new InMemoryTaskManager();
        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                taskManagerTest.getValueById(1);
            }
        });
        Assertions.assertEquals(null, exception.getMessage());
    }

    @Override
    @Test
    void getValueByIdTestWithWrongId() {
        final NullPointerException exception = Assertions.assertThrows(NullPointerException.class, new Executable() {
            @Override
            public void execute() throws Throwable {
                taskManager.getValueById(1);
            }
        });
        Assertions.assertEquals(null, exception.getMessage());
    }

    @Override
    @Test
    void updateTaskTestStandart() {
        taskManager.addTask(new Task("Проверка 1", TypeTask.TASK,
                "Проверка на изменение задачи в списке Тасков", StatusTasks.NEW, 7, 15));
        final Task testTaskBefore = (Task) taskManager.getTasks().get(0);
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на изменение задачи в списке Тасков", StatusTasks.IN_PROGRESS, 7, 15);
        taskManager.updateTask(testTask.getTaskId(), testTask);
        Assertions.assertNotEquals(testTask, testTaskBefore, "Задачи равны");
        Assertions.assertEquals(StatusTasks.IN_PROGRESS, ((Task) taskManager.getTasks().get(0)).getStatus(),
                "Изменения в статусе не сохранились");
    }

    @Override
    @Test
    void updateTaskTestWithEmptyListTasks() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на изменение задачи в списке Тасков", StatusTasks.IN_PROGRESS, 7, 15);
        taskManager.updateTask(testTask.getTaskId(), testTask);
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "Список не пустой");
    }

    @Override
    @Test
    void updateTaskTestWithWrongId() {
        final Task testTask = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на изменение задачи в списке Тасков", StatusTasks.IN_PROGRESS, 7, 15);
        final Task testTaskAfter = new Task("Проверка 1", TypeTask.TASK,
                "Проверка на изменение задачи в списке Тасков", StatusTasks.IN_PROGRESS, 7, 15);
        taskManager.addTask(testTask);
        taskManager.updateTask(8, testTaskAfter);
        Assertions.assertEquals(1, taskManager.getTasks().size(), "размер списка изменился");
        Assertions.assertNotEquals(testTaskAfter, taskManager.getTasks().get(0), "задача в списке изменилась");
    }

    @Override
    @Test
    void updateEpicTestStandart() {
        final Epic testEpicBefore = (Epic) taskManager.getEpics().get(0);
        final Epic testEpicAfter = new Epic("Сварить борщ", TypeTask.EPIC, "по рецепту бабушки"
                , StatusTasks.IN_PROGRESS, 3, 15);
        taskManager.updateEpic(3, testEpicAfter);
        Assertions.assertNotEquals(testEpicBefore, taskManager.getEpics().get(0), "Задача не изменилась");
        Assertions.assertEquals(testEpicAfter.getTaskId(), ((Epic) taskManager.getEpics().get(0)).getTaskId(),
                "идентификатор поменялся");
        Assertions.assertEquals(1, taskManager.getEpics().size(), "изменился размер списка");
        Assertions.assertEquals(testEpicAfter.getSubtaskIds(), ((Epic) taskManager.getEpics().get(0)).getSubtaskIds(),
                "список id сабтасков в эпике изменился");
    }

    @Override
    @Test
    void updateEpicTestWithEmptyListTasks() {
        taskManager.deleteAllTheTasksInListEpic();
        final Epic testEpic = new Epic("Сварить борщ", TypeTask.EPIC, "по рецепту бабушки"
                , StatusTasks.IN_PROGRESS, 3, 15);
        taskManager.updateEpic(3, testEpic);
        Assertions.assertEquals(0, taskManager.getEpics().size(), "размер списка изменился");
        Assertions.assertTrue(taskManager.getEpics().isEmpty(), "список не пустой");
    }

    @Override
    @Test
    void updateEpicTestWithWrongId() {
        final Epic testEpic = new Epic("Сварить борщ", TypeTask.EPIC, "по рецепту бабушки"
                , StatusTasks.IN_PROGRESS, 3, 15);
        final ArrayList<Integer> subtasks = ((Epic) taskManager.getEpics().get(0)).getSubtaskIds();
        taskManager.updateEpic(4, testEpic);
        Assertions.assertEquals(1, taskManager.getEpics().size(), "размер списка изменился");
        Assertions.assertEquals(subtasks, ((Epic) taskManager.getEpics().get(0)).getSubtaskIds(),
                "список id сабтасков в эпике изменился");
    }

    @Override
    @Test
    void deleteValueTaskByIdTestStandart() {
        taskManager.addTask(new Task("Проверка 1", TypeTask.TASK,
                "Проверка на удаление задачи из списка Тасков", StatusTasks.NEW, 7, 15));
        taskManager.getValueById(7);
        taskManager.getValueById(3);
        final List<Task> testListHistory = taskManager.getHistory();
        final int sizeLitTasks = taskManager.getTasks().size();
        taskManager.deleteValueTaskById(7);
        Assertions.assertNotEquals(testListHistory, taskManager.getHistory(),
                "задача не удалилась из истории просмотра");
        Assertions.assertEquals(1, taskManager.getHistory().size(), "список истории не равен 1");
        Assertions.assertNotEquals(sizeLitTasks, taskManager.getTasks().size(), "задача не удалилась");
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "список задач не пустой");
    }

    @Override
    @Test
    void deleteValueTaskByIdTestWithEmptyListTasks() {
        taskManager.getValueById(3);
        final List<Task> testListHistory = taskManager.getHistory();
        taskManager.deleteValueTaskById(7);
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "список не пустой");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "история просмотров изменилась");
    }

    @Override
    @Test
    void deleteValueTaskByIdTestWithWrongId() {
        taskManager.getValueById(3);
        final List<Task> testListHistory = taskManager.getHistory();
        taskManager.deleteValueTaskById(5);
        Assertions.assertTrue(taskManager.getTasks().isEmpty(), "список не пустой");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "история просмотров изменилась");
    }

    @Override
    @Test
    void deleteValueEpicByIdTestStandart() {
        taskManager.addTask(new Task("Проверка 1", TypeTask.TASK,
                "Проверка на удаление задачи в списке Тасков", StatusTasks.IN_PROGRESS, 7, 15));
        taskManager.getValueById(7);
        taskManager.getValueById(3);
        taskManager.getValueById(4);
        final List<Task> testListHistoryBefore = taskManager.getHistory();
        final ArrayList testListHistoryAfter = taskManager.getTasks();
        taskManager.deleteValueEpicById(3);
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "список сабтасков не удалился");
        Assertions.assertNotEquals(testListHistoryBefore, taskManager.getHistory(),
                "просмотр задач не изменился");
        Assertions.assertEquals(testListHistoryAfter, taskManager.getHistory(),
                "список просмотра не совпадает");
        Assertions.assertTrue(taskManager.getEpics().isEmpty(), "список эпиков не удалился");
    }

    @Override
    @Test
    void deleteValueEpicByIdTestWithEmptyListEpics() {
        taskManager.deleteAllTheTasksInListEpic();
        taskManager.deleteValueEpicById(7);
        Assertions.assertTrue(taskManager.getEpics().isEmpty(), "список эпиков не пустой");
    }

    @Override
    @Test
    void deleteValueEpicByIdTestWithWrongId() {
        taskManager.getValueById(3);
        taskManager.getValueById(4);
        final List<Task> testListHistory = taskManager.getHistory();
        final ArrayList subtasks = taskManager.getSubtasks();
        final ArrayList epics = taskManager.getEpics();
        taskManager.deleteValueEpicById(4);
        Assertions.assertEquals(1, taskManager.getSubtasks().size(),
                "размер списка сабтасков не совпадает");
        Assertions.assertEquals(1, taskManager.getEpics().size(), "размер списка эпиков не совпадает");
        Assertions.assertEquals(testListHistory, taskManager.getHistory(), "список просмотров изменился");
        Assertions.assertEquals(subtasks, taskManager.getSubtasks(), "список сабтасков не совпадает");
        Assertions.assertEquals(epics, taskManager.getEpics(), "список эпиков не совпадает");
    }

    @Override
    @Test
    void deleteValueSubtaskByIdTestStandart() {
        taskManager.getValueById(3);
        taskManager.getValueById(4);
        final List<Task> testListHistoryBefore = taskManager.getHistory();
        taskManager.deleteValueSubtaskById(4);
        Assertions.assertNotEquals(testListHistoryBefore, taskManager.getHistory(),
                "просмотр задач не изменился");
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "подзадача не удалилась");
        Assertions.assertEquals(StatusTasks.NEW, ((Epic)taskManager.getEpics().get(0)).getStatus(),
                "статус эпика изменился");
    }

    @Override
    @Test
    void deleteValueSubtaskByIdTestWithEmptyListSubtask() {
        taskManager.deleteAllTheTasksInListSubtask();
        taskManager.getValueById(3);
        final List<Task> testListHistoryBefore = taskManager.getHistory();
        final ArrayList epics = taskManager.getEpics();
        taskManager.deleteValueSubtaskById(4);
        Assertions.assertEquals(testListHistoryBefore, taskManager.getHistory(),
                "история просмотров изменилась");
        Assertions.assertTrue(taskManager.getSubtasks().isEmpty(), "список подзадач не пустой");
        Assertions.assertEquals(epics, taskManager.getEpics(), "список эпиков изменился");
    }

    @Override
    @Test
    void deleteValueSubtaskByIdIdTestWithWrongId() {
        taskManager.getValueById(3);
        taskManager.getValueById(4);
        final List<Task> testListHistoryBefore = taskManager.getHistory();
        final ArrayList epics = taskManager.getEpics();
        final ArrayList subtasks = taskManager.getSubtasks();
        taskManager.deleteValueSubtaskById(5);
        Assertions.assertEquals(testListHistoryBefore, taskManager.getHistory(),
                "история просмотров изменилась");
        Assertions.assertEquals(subtasks, taskManager.getSubtasks(), "список подзадач изменился");
        Assertions.assertEquals(epics, taskManager.getEpics(), "список эпиков изменился");
    }

    @Override
    @Test
    void startDateTimeEpicTestStandart() {
        LocalDateTime startTime = LocalDateTime.of(2022,8,10,12,00);
        ((Subtask)taskManager.getSubtasks().get(0)).setStartTime(startTime);
        taskManager.startDateTimeEpic(((Epic)taskManager.getEpics().get(0)));
        Assertions.assertNotNull(((Epic)taskManager.getEpics().get(0)).getStartTime(), "в startTime null");
        Assertions.assertEquals(startTime, ((Epic)taskManager.getEpics().get(0)).getStartTime(),
                "время не совпадает");
    }

    @Override
    @Test
    void getEndTimeTestStandart() {
        LocalDateTime startTime = LocalDateTime.of(2022,8,10,12,00);
        ((Subtask)taskManager.getSubtasks().get(0)).setStartTime(startTime);
        taskManager.getEndTime(((Subtask)taskManager.getSubtasks().get(0)));
        Assertions.assertEquals(startTime.plusMinutes(15), taskManager.getEndTime(((Subtask)taskManager.getSubtasks().
                get(0))), "время не совпало");
    }
}
