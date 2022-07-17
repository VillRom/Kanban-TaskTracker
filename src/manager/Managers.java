package manager;

import java.io.File;

public class Managers {

    public static TaskManager getDefault() {
        return new HTTPTaskManager(new File("autosave.csv"), "http://localhost:8078/");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static FileBackedTasksManager getFileManager() {
        return new FileBackedTasksManager(new File("autosave.csv"));
    }
}
