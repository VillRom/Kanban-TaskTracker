package model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, String description, String status, int taskId, int epicId) {
        super(name, description, status, taskId);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }
}
