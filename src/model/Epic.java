package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", type='" + getType() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", taskId=" + getTaskId() +
                '}';
    }

    public Epic(String name,TypeTask type, String description, StatusTasks status, int taskId) {
        super(name, type, description, status, taskId);
    }

    @Override
    public TypeTask getType() {
        return super.getType();
    }

    @Override
    public void setType(TypeTask type) {
        super.setType(type);
    }

    public ArrayList<Integer> getSubtaskIds() {
        return subtaskIds;
    }

    public void setSubtaskIds(ArrayList<Integer> subtaskIds) {
        this.subtaskIds = subtaskIds;
    }
}
