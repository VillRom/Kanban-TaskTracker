package model;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name,TypeTask type, String description, StatusTasks status, int taskId, int epicId) {
        super(name, type, description, status, taskId);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", type='" + getType() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", taskId=" + getTaskId() +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public TypeTask getType() {
        return super.getType();
    }

    @Override
    public void setType(TypeTask type) {
        super.setType(type);
    }
}
