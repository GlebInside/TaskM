package service.handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import service.InMemoryTaskManager;
import service.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public abstract class TaskHandlerBase implements HttpHandler {
    protected final InMemoryTaskManager taskManager;
    protected final Gson gson = Utils.getGson();

    public TaskHandlerBase(InMemoryTaskManager taskManager) {
        this.taskManager = taskManager;
    }

    protected abstract Task getById(int id);

    protected abstract ArrayList<Task> getAll(HttpExchange httpExchange);

    protected abstract int add(Task task);

    protected abstract void deleteById(int id);

    protected abstract Task createFromModel(TaskModel model);

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        var method = httpExchange.getRequestMethod();
        if (method.equalsIgnoreCase("get")) {
            handleGet(httpExchange);
        }
        if (method.equalsIgnoreCase("post")) {
            handlePost(httpExchange);
        }
        if (method.equalsIgnoreCase("delete")) {
            handleDelete(httpExchange);
        }

        throw new RuntimeException();
    }

    private String getDeleteResponse(HttpExchange httpExchange) {
        var args = httpExchange.getRequestURI().getQuery();
        if (args == null) {
            return "{'error':'missing url parameter: id'}";
        }
        var parts = args.split("=");
        if (!parts[0].equalsIgnoreCase("id")) {
            return "{'error':'missing url parameter: id'}";
        }
        var id = Integer.parseInt(parts[1]);
        deleteById(id);
        return "'result': 'task with id = " + id + " deleted successfully'";
    }


    private void handleDelete(HttpExchange httpExchange) throws IOException {
        var response = getDeleteResponse(httpExchange);
        httpExchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private void handlePost(HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String body = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        System.out.println("Тело запроса:\n" + body);

        Gson gson = Utils.getGson();

        var model = gson.fromJson(body, TaskModel.class);
        var task = createFromModel(model);
        var id = add(task);
        String response = "{'id':" + id + "}";
        httpExchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }


    private void handleGet(HttpExchange httpExchange) throws IOException {
        var response = getResponse(httpExchange);

        httpExchange.sendResponseHeaders(200, response.length());
        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private String getResponse(HttpExchange httpExchange) {

        var args = httpExchange.getRequestURI().getQuery();
        if (args != null) {
            var parts = args.split("=");
            if (parts[0].equalsIgnoreCase("id")) {
                var id = Integer.parseInt(parts[1]);
                var task = getById(id);
                return gson.toJson(task);
            }
        }
        try {
            return gson.toJson(getAll(httpExchange));
        } catch (Exception x) {
            System.out.println(x);
            return x.getMessage();
        }


    }

}
