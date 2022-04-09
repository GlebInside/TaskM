package service.handlers;

import com.sun.net.httpserver.HttpExchange;
import model.SubTask;
import model.Task;
import service.InMemoryTaskManager;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;


public class SubTaskHandler extends TaskHandlerBase {

    public SubTaskHandler(InMemoryTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void deleteById(int id) {
        taskManager.deleteSubtaskById(id);
    }

    @Override
    protected Task createFromModel(TaskModel model) {
        return model.toSubTask();
    }

    @Override
    protected int add(Task task) {
        var subtask = (SubTask) task;
        return taskManager.addSubtask(subtask, subtask.getEpicId());
    }

    @Override
    protected Task getById(int id) {
        return taskManager.getSubTaskById(id);
    }

    @Override
    protected ArrayList<Task> getAll(HttpExchange httpExchange) {
        var args = httpExchange.getRequestURI().getQuery();
        if (args == null) {
            throw new RuntimeException("missing required argument: 'epicId'");
        }
        var parts = args.split("=");
        if (parts.length != 2) {
            throw new RuntimeException("missing required argument: 'epicId'");
        }
        if (!parts[0].equalsIgnoreCase("epicId")) {
            throw new RuntimeException("missing required argument: 'epicId'");
        }
        var epicId = Integer.parseInt(parts[1]);
        return new ArrayList<>(getAllSubTasksOfEpic(epicId).values().stream().map(e -> (Task) e).collect(Collectors.toList()));
    }

    public Map<Integer, SubTask> getAllSubTasksOfEpic(int epicId) {
        return taskManager.getAllSubTasksOfEpic(epicId);
    }
}
