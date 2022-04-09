package service;

import java.io.File;

public class Managers {

    private static final String KV_SERVER_URL = "http://localhost:8078";

    public static TaskManager getDefault() {
        try {
            return new HttpTaskManager(KV_SERVER_URL, getDefaultHistoryManager());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedTaskManager(String fname) {
        return new FileBackedTasksManager(new File(fname), getDefaultHistoryManager());
    }
}
