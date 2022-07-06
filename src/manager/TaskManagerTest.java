package manager;

abstract class TaskManagerTest<T extends TaskManager> {

    abstract void checkAvailableEpicInSubtask();

    abstract void updateEpicStatusWithEmptylistSubtask();

    abstract void addTaskTestStandart();

    abstract void addTaskTestWithEmptyListTask();

    abstract void addEpicTestWithEmptyListEpics();

    abstract void addEpicTestStandart();

    abstract void addSubtaskTestStandart();

    abstract void addSubtaskWithEmptyListSubtask();

    abstract void getTasksTestStandart();

    abstract void getTasksTestWithEmptyListTask();

    abstract void getEpicsTestStandart();

    abstract void getEpicsWithEmptyListEpics();

    abstract void getSubtaskTestStandart();

    abstract void getSubtaskTestWithEmptyListSubtask();

    abstract void updateEpicStatusWithAllSubtaskNew();

    abstract void updateEpicStatusWithAllSubtaskStatusDone();

    abstract void updateEpicStatusWithStatusSubTaskNewAndDone();

    abstract void updateEpicStatusWithAllSubtaskStatusInProgress();

    abstract void deleteAllTheTasksInListTaskTestStandart();

    abstract void deleteAllTheTasksInListTaskEmptyTest();

    abstract void deleteAllTheTasksInListSubtaskTestStandart();

    abstract void deleteAllTheTasksInListSubtaskEmptyTest();

    abstract void deleteAllTheTasksInListEpicTestStandart();

    abstract void deleteAllTheTasksInListEmptyEpicTest();

    abstract void getValueByIdTestStandart();

    abstract void getValueByIdTestWithEmptyListsTasks();

    abstract void getValueByIdTestWithWrongId();

    abstract void updateTaskTestStandart();

    abstract void updateTaskTestWithEmptyListTasks();

    abstract void updateTaskTestWithWrongId();

    abstract void updateEpicTestStandart();

    abstract void updateEpicTestWithEmptyListTasks();

    abstract void updateEpicTestWithWrongId();

    abstract void deleteValueTaskByIdTestStandart();

    abstract void deleteValueTaskByIdTestWithEmptyListTasks();

    abstract void deleteValueTaskByIdTestWithWrongId();

    abstract void deleteValueEpicByIdTestStandart();

    abstract void deleteValueEpicByIdTestWithEmptyListEpics();

    abstract void deleteValueEpicByIdTestWithWrongId();

    abstract void deleteValueSubtaskByIdTestStandart();

    abstract void deleteValueSubtaskByIdTestWithEmptyListSubtask();

    abstract void deleteValueSubtaskByIdIdTestWithWrongId();

    abstract void startDateTimeEpicTestStandart();

    abstract void getEndTimeTestStandart();
}
