package model;

import java.time.LocalDateTime;

public class Task {
    private String name;
    private String description;
    private StatusTasks status;
    private int taskId;
    private TypeTask type;
    private long duration;
    private LocalDateTime startTime;

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskId=" + taskId +
                ", duration=" + duration +
                ", startTime=" + startTime +
                '}';
    }

    public Task(String name,TypeTask type, String description, StatusTasks status, int taskId, long duration) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.status = status;
        this.taskId = taskId;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public StatusTasks getStatus() {
        return status;
    }

    public void setStatus(StatusTasks status) {
        this.status = status;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public TypeTask getType() {
        return type;
    }

    public void setType(TypeTask type) {
        this.type = type;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(duration);
    }
}
