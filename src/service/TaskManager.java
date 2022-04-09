package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface TaskManager {

    List<Task> history();

    Epic getEpicById(int epicId);

    Map<Integer, SubTask> getAllSubTasksOfEpic(int epicId);

    ArrayList<Epic> getAllEpics();

    int addSubtask(SubTask subTask, int epicId);

    int addEpic(Epic epic);

    void updateEpic(Epic epic, int epicId);

    void updateSubTask(SubTask subTask, int id);

    SubTask getSubTaskById(int id);

    void deleteAllEpics();

    void deleteEpicById(int epicId);

    void deleteSubtaskById(int id);

    ArrayList<Task> getAllTasks();

    Task[] getPrioritizedTasks();

    int addTask(Task task);

    void updateTask(Task task, int id);

    Task getTaskById(int id);

    void deleteTaskById(int id);


    int addAnyTask(Task task);
}
