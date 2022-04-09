package service;

import model.Epic;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;


class HttpTaskServerTest {

    private static KVServer kvServer;
    private HttpTaskServer taskServer;
    private HttpTaskManagerClient client = new HttpTaskManagerClient();

    @BeforeAll
    static void beforeAll() throws IOException {
        kvServer = new KVServer();
        kvServer.start();
    }

    @AfterAll
    static void afterAll() {
        kvServer.stop();
    }

    @BeforeEach
    void setUp() throws IOException {
        taskServer = new HttpTaskServer((InMemoryTaskManager) Managers.getDefault());
    }

    @AfterEach
    void afterEach() {
        taskServer.stop();
    }

    @Test
    void epicsGetAllReturnsEmptyList() throws URISyntaxException, IOException, InterruptedException {
        var tasks = client.getAllEpics();
        assertEquals(0, tasks.size());
    }

    @Test
    void epicAdd() throws URISyntaxException, IOException, InterruptedException {
        var tasks = client.getAllEpics();
        assertEquals(0, tasks.size());
        var expected = new Epic("task1", "description 1");
        var id = client.addEpic(expected);
        tasks = client.getAllEpics();
        assertEquals(1, tasks.size());
        assertEquals(expected, tasks.get(0));

        var actual = client.getEpicById(id);
        assertEquals(expected, actual);

    }

    @Test
    void epicDelete() throws URISyntaxException, IOException, InterruptedException {

        var task = new Epic("task1", "description 1");
        var id = client.addEpic(task);
        var tasks = client.getAllEpics();
        assertEquals(1, tasks.size());
        client.deleteEpicById(id);
        tasks = client.getAllEpics();
        assertEquals(0, tasks.size());
    }


    @Test
    void tasksGetAllReturnsEmptyList() throws URISyntaxException, IOException, InterruptedException {
        var tasks = client.getAllTasks();
        assertEquals(0, tasks.size());
    }

    @Test
    void taskAdd() throws URISyntaxException, IOException, InterruptedException {
        var tasks = client.getAllTasks();
        assertEquals(0, tasks.size());
        var expected = new Task("task1", "description 1", LocalDateTime.now(), 1);
        var id = client.addTask(expected);
        tasks = client.getAllTasks();
        assertEquals(1, tasks.size());
        assertEquals(expected, tasks.get(0));

        var actual = client.getTaskById(id);
        assertEquals(expected, actual);

    }

    @Test
    void taskDelete() throws URISyntaxException, IOException, InterruptedException {

        var task = new Task("task1", "description 1", LocalDateTime.now(), 1);
        var id = client.addTask(task);
        var tasks = client.getAllTasks();
        assertEquals(1, tasks.size());
        client.deleteTaskById(id);
        tasks = client.getAllTasks();
        assertEquals(0, tasks.size());
    }


    @Test
    void subtaskGetAllReturnsEmptyList() throws URISyntaxException, IOException, InterruptedException {
        var epicId = client.addEpic(new Epic("", ""));
        var tasks = client.getAllSubTasksOfEpic(epicId);
        assertEquals(0, tasks.size());
    }

    @Test
    void subtaskAdd() {
        var epicId = client.addEpic(new Epic("", ""));
        var tasks = client.getAllSubTasksOfEpic(epicId);
        assertEquals(0, tasks.size());
        var expected = new SubTask("task1", "description 1", LocalDateTime.now(), 1);
        var id = client.addSubtask(expected, epicId);
        tasks = client.getAllSubTasksOfEpic(epicId);
        assertEquals(1, tasks.size());
        assertEquals(expected, tasks.get(id));

        var actual = client.getSubTaskById(id);
        assertEquals(expected, actual);

        var epic = client.getEpicById(epicId);
        var subtasks = epic.getSubtasks();
        assertEquals(1, subtasks.size());
        actual = subtasks.get(id);
        assertEquals(expected, actual);

    }

    @Test
    void subtaskDelete() {

        var epicId = client.addEpic(new Epic("", ""));
        var subTask = new SubTask("task1", "description 1", LocalDateTime.now(), 1);
        var id = client.addSubtask(subTask, epicId);
        var subtasks = client.getAllSubTasksOfEpic(epicId);
        assertEquals(1, subtasks.size());

        client.deleteSubtaskById(id);
        subtasks = client.getAllSubTasksOfEpic(epicId);
        assertEquals(0, subtasks.size());
    }


}