package model;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.Objects;

public class Task implements Serializable {

    private int id;
    private String name;
    private String description;
    private Status status;
    private boolean viewed;
    private LocalDateTime startTime;
    private int durationInMinutes;

    public boolean isViewed() {
        return viewed;
    }

    public void setViewed(boolean viewed) {
        this.viewed = viewed;
    }

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");


    public Task(String taskName, String taskDescription, LocalDateTime startTime, int durationInMinutes) {
        this.name = taskName;
        this.description = taskDescription;
        this.status = Status.NEW;
        this.startTime = startTime.truncatedTo(ChronoUnit.MINUTES);
        this.durationInMinutes = durationInMinutes;


        System.out.println("Task " + taskName + " has been created.");
        System.out.println("Task description is: " + taskDescription);
        System.out.println("Start time is: " + startTime.format(formatter));
        System.out.println("Duration is: " + durationInMinutes);
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

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getEndTime() {
        return startTime.plusMinutes(durationInMinutes);
    }

    @Override
    public String toString() {
        return id + "," + this.getClass().getName().substring(6) + "," + name + "," + getStatus() + "," + description + "," + formatter.format(startTime) + "," + durationInMinutes;
    }

    public static Task fromString(String value) {
        String[] array = value.split(",");
        var id = Integer.parseInt(array[0]);
        String clazz = array[1];
        var name = array[2];
        String statusStr = array[3];
        Status status;
        if ("NEW".equals(statusStr)) {
            status = Status.NEW;
        } else if ("IN_PROGRESS".equals(statusStr)) {
            status = (Status.IN_PROGRESS);
        } else {
            status = (Status.DONE);
        }

        var description = array[4];
        var startTime = LocalDateTime.parse(array[5], formatter );
        var duration = Integer.parseInt(array[6]);


        if ("Task".equals(clazz)) {
            Task task = new Task(name, description, startTime, duration);
            task.setId(id);
            task.setStatus(status);
            return task;
        } else if ("Epic".equals(clazz)) {
            Task task = new Epic(name, description);
            task.setId(id);
            return task;
        } else {
            Task task = new SubTask(name, description, startTime, duration);
            task.setId(id);
            task.setStatus(status);
            return task;
        }
    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        PrintWriter w = new PrintWriter(new FileOutputStream("1.csv"));
        Task task = new Task("A", "AA", LocalDateTime.now(), 0);
        System.out.println(task);
        w.println(task);
        w.flush();
        w.close();
        BufferedReader r = new BufferedReader(new FileReader("1.csv"));
        String s = r.readLine();
        System.out.println(fromString(s));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && viewed == task.viewed && durationInMinutes == task.durationInMinutes && name.equals(task.name) && description.equals(task.description) && status == task.status && startTime.equals(task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, status, viewed, startTime, durationInMinutes);
    }
}
