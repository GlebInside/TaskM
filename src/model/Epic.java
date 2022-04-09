package model;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class Epic extends Task {

    private Map<Integer, SubTask> subtasks = new HashMap<>();

    public Epic(String taskName, String taskDescription) {

        super(taskName, taskDescription, LocalDateTime.MAX, -1);
    }

    public Map<Integer, SubTask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(Map<Integer, SubTask> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public Status getStatus() {
        Collection<SubTask> s = subtasks.values();

        if (s.isEmpty()) {
            return Status.NEW;
        }

        int total = s.size();
        int countOfNew = 0;
        int countOfDone = 0;
        Status status = Status.NEW;

        for (SubTask subT : s) {
            if (subT.getStatus() == (Status.IN_PROGRESS)) {
                return Status.IN_PROGRESS;
            }

            if (subT.getStatus() == Status.NEW) {
                countOfNew++;
            }

            if (subT.getStatus() == (Status.DONE)) {
                countOfDone++;
            }

        }
        if (total == countOfNew) status = Status.NEW;
        if (total == countOfDone) status = Status.DONE;
        if (total != countOfDone && total != countOfNew) status = Status.IN_PROGRESS;
        return status;
    }

    @Override
    public void setStatus(Status status) {
        throw new RuntimeException("can't set epic status explicitly");
    }

    @Override
    public void setStartTime(LocalDateTime time) {
        throw new RuntimeException("can't set epic startTime explicitly");
    }

    @Override
    public LocalDateTime getStartTime() {
        LocalDateTime cd = LocalDateTime.now();
        Collection<SubTask> s = subtasks.values();
        if (s.isEmpty()) {
            return null;
        }
        for (SubTask subT : s) {
            if (subT.getStartTime().isBefore(cd)) {
                cd = subT.getStartTime();
            }
        }
        return cd;
    }


    public int getTotalDuration() {
        Collection<SubTask> s = subtasks.values();
        int total = 0;
        for (SubTask subT : s) {
            total += subT.getDurationInMinutes();
        }
        return total;
    }

    @Override
    public LocalDateTime getEndTime() {
        return getStartTime().plusMinutes(getTotalDuration());
    }
}


