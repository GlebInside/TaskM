package service.handlers;

import com.sun.net.httpserver.HttpExchange;
import model.Epic;
import model.Task;
import service.InMemoryTaskManager;

import java.util.ArrayList;
import java.util.stream.Collectors;


public class EpicHandler extends TaskHandlerBase {

    public EpicHandler(InMemoryTaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void deleteById(int id) {
        taskManager.deleteEpicById(id);
    }

    @Override
    protected Task createFromModel(TaskModel model) {
        return model.toEpic();
    }

    @Override
    protected int add(Task task) {
        return taskManager.addEpic((Epic) task);
    }

    @Override
    protected Task getById(int id) {
        return taskManager.getEpicById(id);
    }

    @Override
    protected ArrayList<Task> getAll(HttpExchange httpExchange) {
        var list = taskManager.getAllEpics().stream().map(e -> (Task) e).collect(Collectors.toList());
        return new ArrayList<>(list);
    }
}
