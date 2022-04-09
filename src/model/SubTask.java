package model;

import java.time.LocalDateTime;

public class SubTask extends Task {

    private int epicId;

    public SubTask(String taskName, String taskDescription, LocalDateTime startTime, int durationInMinutes) {
        super(taskName, taskDescription, startTime, durationInMinutes);
    }

    public int getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return getId() + "," + this.getClass().getName().substring(6) + "," + getName() + "," + getStatus() + "," + getDescription() + "," + this.epicId;
    }
}

