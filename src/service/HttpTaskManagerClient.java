package service;

import com.google.gson.Gson;
import model.Epic;
import model.SubTask;
import model.Task;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

import static service.Utils.getGson;


public class HttpTaskManagerClient implements TaskManager {

    private static final String BASE_URL = "http://localhost:8080";
    private static final String SUBTASKS_ENDPOINT = BASE_URL + "/subtasks/subtask";
    private static final String EPICS_ENDPOINT = BASE_URL + "/epics/epic";
    private static final String TASKS_ENDPOINT = BASE_URL + "/tasks/task";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = getGson();


    @Override
    public List<Task> history() {
        return null;
    }

    @Override
    public Epic getEpicById(int epicId) {
        try {
            var request = HttpRequest.newBuilder(new URI(BASE_URL + "/epics/epic?id=" + epicId)).GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return (gson.fromJson(response.body(), Epic.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<Integer, SubTask> getAllSubTasksOfEpic(int epicId) {

        try {
            var request = HttpRequest.newBuilder(new URI(SUBTASKS_ENDPOINT + "?epicId=" + epicId)).GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var body = response.body();
            var subtasks = gson.fromJson(body, SubTask[].class);
            var map = new HashMap<Integer, SubTask>();
            for (var subtask : subtasks) {
                map.put(subtask.getId(), subtask);
            }

            return map;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Epic> getAllEpics() {

        try {
            var request = HttpRequest.newBuilder(new URI(BASE_URL + "/epics/epic")).GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var body = response.body();
            return (new ArrayList(Arrays.asList(gson.fromJson(body, Epic[].class))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int addSubtask(SubTask subTask, int epicId) {
        try {
            subTask.setEpicId(epicId);
            var body = HttpRequest.BodyPublishers.ofString(gson.toJson(subTask));
            var request = HttpRequest.newBuilder(new URI(SUBTASKS_ENDPOINT)).POST(body).build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var result = gson.fromJson(response.body(), Map.class);
            subTask.setId((int) Math.round((Double) result.get("id")));
            return subTask.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int addEpic(Epic epic) {
        try {
            var body = HttpRequest.BodyPublishers.ofString(gson.toJson(epic));
            var request = HttpRequest.newBuilder(new URI(EPICS_ENDPOINT)).POST(body).build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var result = gson.fromJson(response.body(), Map.class);
            epic.setId((int) Math.round((Double) result.get("id")));
            return epic.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateEpic(Epic epic, int epicId) {

    }

    @Override
    public void updateSubTask(SubTask subTask, int id) {

    }

    @Override
    public SubTask getSubTaskById(int id) {
        try {
            var request = HttpRequest.newBuilder(new URI(SUBTASKS_ENDPOINT + "?id=" + id)).GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return (gson.fromJson(response.body(), SubTask.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAllEpics() {

    }

    @Override
    public void deleteEpicById(int epicId) {

        try {
            var request = HttpRequest.newBuilder(new URI(BASE_URL + "/epics/epic?id=" + epicId)).DELETE().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSubtaskById(int id) {

        try {
            var request = HttpRequest.newBuilder(new URI(SUBTASKS_ENDPOINT + "?id=" + id)).DELETE().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Task> getAllTasks() {

        try {
            var request = HttpRequest.newBuilder(new URI(BASE_URL + "/tasks/task")).GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return (new ArrayList(Arrays.asList(gson.fromJson(response.body(), Task[].class))));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public Task[] getPrioritizedTasks() {
        return new Task[0];
    }

    @Override
    public int addTask(Task task) {
        try {
            var body = HttpRequest.BodyPublishers.ofString(gson.toJson(task));
            var request = HttpRequest.newBuilder(new URI(BASE_URL + "/tasks/task")).POST(body).build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            var result = gson.fromJson(response.body(), Map.class);
            task.setId((int) Math.round((Double) result.get("id")));
            return task.getId();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateTask(Task task, int id) {

    }

    @Override
    public Task getTaskById(int id) {

        try {
            var request = HttpRequest.newBuilder(new URI(BASE_URL + "/tasks/task?id=" + id)).GET().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return (gson.fromJson(response.body(), Task.class));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteTaskById(int id) {

        try {
            var request = HttpRequest.newBuilder(new URI(BASE_URL + "/tasks/task?id=" + id)).DELETE().build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public int addAnyTask(Task task) {
        return 0;
    }
}
