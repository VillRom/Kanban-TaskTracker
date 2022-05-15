package model;

public class Task {
    private String name;
    private String description;
    private StatusTasks status;
    private int taskId;

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskId=" + taskId +
                '}';
    }

    public Task(String name, String description, StatusTasks status, int taskId){
        this.name = name;
        this.description = description;
        this.status = status;
        this.taskId = taskId;
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
}
