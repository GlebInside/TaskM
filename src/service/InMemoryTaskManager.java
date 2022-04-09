package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, SubTask> subtasks = new HashMap<>();

    protected TreeSet<Task> tasksAndSubtasksByStartTime = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public TreeSet<Task> getTasksAndSubtasksByStartTime() {
        return tasksAndSubtasksByStartTime;
    }

    public void setTasksAndSubtasksByStartTime(TreeSet<Task> tasksAndSubtasksByStartTime) {
        this.tasksAndSubtasksByStartTime = tasksAndSubtasksByStartTime;
    }

    protected final HistoryManager historyManager;

    private int nextId = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.historyManager = historyManager;
    }

    @Override
    public List<Task> history() {
        return historyManager.getHistory();
    }

    @Override
    public Epic getEpicById(int epicId) {
        Epic epic = epics.get(epicId);
        if (!epics.isEmpty()) {
            historyManager.add(epic);
            System.out.println(epic);
            return epic;
        } else {
            System.out.println("There are no epics");
        }
        return null;
    }

    @Override
    public SubTask getSubTaskById(int id) {
        SubTask subTask = subtasks.get(id);
        if (!(subTask == null)) {
            historyManager.add(subTask);
            System.out.println(subTask);
        } else {
            System.out.println("There is no subTask with id: " + id);
        }
        return subTask;
    }

    @Override
    public Task getTaskById(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return null;
        }
        historyManager.add(task);
        System.out.println(task);
        return task;
    }

    @Override
    public ArrayList<Epic> getAllEpics() {
        System.out.println(this.epics.values());
        return new ArrayList<>(this.epics.values());
    }

    @Override
    public Map<Integer, SubTask> getAllSubTasksOfEpic(int epicId) {
        Epic epic = epics.get(epicId);
        if (epics.containsKey(epicId)) {
            System.out.println(epic.getSubtasks());
            return epic.getSubtasks();
        }

        System.out.println("There are no epics");
        return Collections.emptyMap();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        System.out.println(this.tasks.values());
        return new ArrayList<>(this.tasks.values());
    }

    public Task[] getPrioritizedTasks() {
        return (Task[]) tasksAndSubtasksByStartTime.toArray();
    }

    @Override
    public int addEpic(Epic epic) {

        var id = nextId++;
        epic.setId(id);

        epics.put(epic.getId(), epic);
        return id;
    }

    @Override
    public int addSubtask(SubTask subTask, int epicId) {
        if (!validateTask(subTask)) {
            throw new IllegalArgumentException("subtask is not valid");
        }
        Epic epic = epics.get(epicId);

        var id = nextId++;
        subTask.setId(id);
        subTask.setEpicId(epicId);

        Map<Integer, SubTask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.put(subTask.getId(), subTask);
        subtasks.put(subTask.getId(), subTask);
        tasksAndSubtasksByStartTime.add(subTask);

        return id;
    }

    @Override
    public int addTask(Task task) {
        if (!validateTask(task)) {
            throw new IllegalArgumentException("task is not valid");
        }
        var id = nextId++;

        task.setId(id);
        tasks.put(task.getId(), task);
        tasksAndSubtasksByStartTime.add(task);

        return id;

    }

    @Override
    public void updateEpic(Epic epic, int epicId) {
        epics.put(epicId, epic);
    }

    @Override
    public void updateSubTask(SubTask subTask, int id) {
        if (!validateTask(subTask)) {
            throw new IllegalArgumentException("subtask is not valid");
        }
        Epic epic = epics.get(subTask.getEpicId());
        Map<Integer, SubTask> subtasks = epic.getSubtasks();
        subtasks.replace(id, subTask);
    }

    @Override
    public void updateTask(Task task, int id) {
        tasks.put(id, task);
    }

    @Override
    public void deleteAllEpics() {
        epics.values().clear();
    }

    @Override
    public void deleteEpicById(int epicId) {

        epics.get(epicId).getSubtasks().clear();
        epics.remove(epicId);
        historyManager.remove(epicId);
    }

    @Override
    public void deleteSubtaskById(int id) {
        Collection<Epic> e = epics.values();
        for (Epic ep : e) {
            Map<Integer, SubTask> a = ep.getSubtasks();
            Collection<SubTask> e1 = a.values();
            for (SubTask sb : e1) {
                if (sb.getId() == id) {
                    e1.remove(sb);
                    historyManager.remove(id);
                    break;
                }
            }
        }
        subtasks.remove(id);
    }

    @Override
    public void deleteTaskById(int id) {
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public int addAnyTask(Task task) {
        if (task instanceof Epic) {
            return addEpic((Epic) task);
        }
        if (task instanceof SubTask) {
            var subtask = (SubTask) task;
            return addSubtask(subtask, subtask.getEpicId());
        }
        return addTask(task);
    }

    public boolean validateTask(Task task) {
        for (Task currentTask : tasksAndSubtasksByStartTime) {
            if (currentTask.getName().equalsIgnoreCase(task.getName())) {
                System.out.println("task validation failed: duplicate names: " + currentTask.getName());
                return false;
            }
            if (currentTask.getId() != task.getId() && intersected(task.getStartTime(), task.getEndTime(),
                    currentTask.getStartTime(), currentTask.getEndTime())) {
                System.out.println("task validation failed: intersected intervals");
                return false;
            }

        }
        return true;
    }

    private boolean intersected(LocalDateTime a, LocalDateTime b, LocalDateTime c, LocalDateTime d) {
        if ((a.isAfter(c) || a.isEqual(c)) && (a.isBefore(d) || a.isEqual(d))) {
            return true;
        }
        if ((c.isAfter(a) || c.isEqual(a)) && (c.isBefore(b) || c.isEqual(b))) {
            return true;
        }
        return false;
    }


}
