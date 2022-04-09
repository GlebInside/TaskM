package service;

import exception.ManagerSaveException;

import java.io.IOException;
import java.net.URISyntaxException;

public class HttpTaskManager extends FileBackedTasksManager {
    private final KVTaskClient kvTaskClient;

    public HttpTaskManager(String url, HistoryManager historyManager) throws URISyntaxException, IOException, InterruptedException {

        super(null, historyManager);
        kvTaskClient = new KVTaskClient(url);
    }

    @Override
    protected void saveString(String data) {
        try {
            kvTaskClient.put("all", data);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ManagerSaveException(e.getMessage());
        }
    }


}
