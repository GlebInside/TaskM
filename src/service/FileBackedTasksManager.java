package service;

import exception.ManagerSaveException;
import model.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private static final String COLUMNS = "id,type,name,status,description,epic";

    private final File fileToSave;


    public FileBackedTasksManager(File fileToSave, HistoryManager historyManager) {
        super(historyManager);
        this.fileToSave = fileToSave;
    }

    @Override
    public Task getTaskById(int id) {
        var task = super.getTaskById(id);
        save();
        return task;
    }

    @Override
    public int addEpic(Epic epic) {
        super.addEpic(epic);
        save();
        return epic.getId();
    }

    @Override
    public int addSubtask(SubTask subTask, int epicId) {
        var id = super.addSubtask(subTask, epicId);
        save();
        return id;
    }

    @Override
    public int addTask(Task task) {
        var id = super.addTask(task);
        save();
        return id;
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        super.updateEpic(epic, epicId);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask, int id) {
        super.updateSubTask(subTask, id);
        save();
    }

    @Override
    public void updateTask(Task task, int id) {
        super.updateTask(task, id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteEpicById(int epicId) {
        super.deleteEpicById(epicId);
        save();
    }

    @Override
    public void deleteSubtaskById(int id) {
        super.deleteSubtaskById(id);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    public void save() {
        List<Task> tasks = getAllTasks();
        List<Epic> epics = getAllEpics();
        for (Epic epic : epics) {
            tasks.addAll(new ArrayList<>(getAllSubTasksOfEpic(epic.getId()).values()));
        }
        tasks.addAll(epics);
        var allString = new StringBuilder();
        allString.append(COLUMNS);
        allString.append(System.lineSeparator());
        for (Task task : tasks) {
            allString.append(task.toString() + System.lineSeparator());
        }
        allString.append(System.lineSeparator());
        allString.append(toString(history()));

        saveString(allString.toString());
    }

    protected void saveString(String data) {

        try (var w = new PrintWriter(new FileOutputStream(fileToSave))) {
            w.println(data);
            w.flush();
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    static String toString(List<Task> history) {
        return history.stream().map(i -> String.valueOf(i.getId())).collect(Collectors.joining(","));
    }

    public static List<Task> fromString(String value) {
        List<Task> tasks = new ArrayList<>();
        String[] strings = value.split(System.lineSeparator());
        for (var i = 1; i < strings.length - 2; i++) {
            tasks.add(Task.fromString(strings[i]));
        }
        return tasks;
    }

    public static List<Integer> historyFromString(String value) {
        String[] strings = value.split(System.lineSeparator());
        String[] values = strings[strings.length - 1].split(",");
        return Arrays.stream(values).map(Integer::valueOf).collect(Collectors.toList());
    }

    public static FileBackedTasksManager loadFromFile(File file) {
        var manager = new FileBackedTasksManager(file, Managers.getDefaultHistoryManager());
        try {
            List<Task> tasks = fromString(Files.readString(file.toPath()));
            tasks.sort(Comparator.comparing(Task::getName));
            List<Integer> history = historyFromString(Files.readString(file.toPath()));
            for (Task task : tasks) {
                if (history.contains(task.getId())) {
                }
                if (task instanceof Epic) {
                    manager.addEpic((Epic) task);
                } else if (task instanceof SubTask) {
                    manager.addSubtask((SubTask) task, ((SubTask) task).getEpicId());
                } else {
                    manager.addTask(task);
                }
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return manager;
    }

}
