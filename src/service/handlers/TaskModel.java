package service.handlers;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;

public class TaskModel {


    private int id;
    private String type;
    private String name;
    private String description;
    private Status status;

    private LocalDateTime startTime;
    private int durationInMinutes;
    private boolean viewed;

    private int epicId;


    public SubTask toSubTask() {
        var task = new SubTask(getName(), getDescription(), getStartTime(), getDurationInMinutes());
        task.setEpicId(getEpicId());
        return task;
    }

    public Epic toEpic() {
        var task = new Epic(getName(), getDescription());
        return task;
    }

    public Task toTask() {
        var task = new Task(getName(), getDescription(), getStartTime(), getDurationInMinutes());
        return task;
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getDurationInMinutes() {
        return durationInMinutes;
    }

    public void setDurationInMinutes(int durationInMinutes) {
        this.durationInMinutes = durationInMinutes;
    }
}
