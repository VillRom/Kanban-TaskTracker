package model;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtaskIds = new ArrayList<>();
    private LocalDateTime endTime;

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", type='" + getType() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", status='" + getStatus() + '\'' +
                ", taskId=" + getTaskId() +
                ", duration=" + getDuration() +
                '}';
    }

    public Epic(String name,TypeTask type, String description, StatusTasks status, int taskId, long duration) {
        super(name, type, description, status, taskId, duration);
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

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}
