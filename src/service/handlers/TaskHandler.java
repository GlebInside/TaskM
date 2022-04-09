package service.handlers;

import com.sun.net.httpserver.HttpExchange;
import model.Task;
import service.InMemoryTaskManager;

import java.util.ArrayList;


public class TaskHandler extends TaskHandlerBase {

    public TaskHandler(InMemoryTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void deleteById(int id) {
        taskManager.deleteTaskById(id);
    }

    @Override
    protected Task createFromModel(TaskModel model) {
        return model.toTask();
    }

    @Override
    protected int add(Task task) {
        return taskManager.addTask(task);
    }

    @Override
    protected Task getById(int id) {
        return taskManager.getTaskById(id);
    }

    @Override
    protected ArrayList<Task> getAll(HttpExchange httpExchange) {
        return taskManager.getAllTasks();
    }
}
