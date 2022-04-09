package service;

import com.sun.net.httpserver.HttpServer;
import service.handlers.EpicHandler;
import service.handlers.SubTaskHandler;
import service.handlers.TaskHandler;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    private final HttpServer httpServer;
    private final InMemoryTaskManager taskManager;

    public HttpTaskServer(InMemoryTaskManager taskManager) throws IOException {
        this.taskManager = taskManager;

        final int PORT = 8080;
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);

        httpServer.createContext("/tasks/task", new TaskHandler(taskManager));
        httpServer.createContext("/epics/epic", new EpicHandler(taskManager));
        httpServer.createContext("/subtasks/subtask", new SubTaskHandler(taskManager));
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stop() {
        httpServer.stop(1);
    }
}