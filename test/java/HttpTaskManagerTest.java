import model.Epic;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import service.Managers;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskManagerTest {

    protected HttpTaskManager taskManager;
    private static KVServer kvServer;

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
    void setUp() {
        taskManager = (HttpTaskManager) Managers.getDefault();
    }

    @Test
    public void history() {

        Task task1 = new Task("task1", "task1", LocalDateTime.now(), 0);
        taskManager.addTask(task1);
        Task task2 = new Task("task2", "task2", LocalDateTime.now().plusMinutes(10), 0);
        taskManager.addTask(task2);

        assertEquals(0, taskManager.history().size());

        taskManager.getTaskById(task1.getId());
        var history = taskManager.history();
        assertEquals(1, history.size());
        assertEquals(task1, history.get(0));

        taskManager.getTaskById(task2.getId());
        history = taskManager.history();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));

    }

    @Test
    void testAdd() {
        Task task = new Task("task", "task", LocalDateTime.now(), 0);
        Epic epic = new Epic("epic", "epic");
        SubTask subTask = new SubTask("subTask", "subTask", LocalDateTime.now().plusMinutes(10), 0);
        taskManager.addTask(task);
        assertNotEquals(0, task.getId());
        assertNotNull(task, "task not found");
        taskManager.addEpic(epic);
        assertNotEquals(0, epic.getId());
        assertNotNull(epic, "epic not found");
        taskManager.addSubtask(subTask, epic.getId());
        assertNotEquals(0, subTask.getId());
        assertNotNull(subTask, "subTask not found");
    }

    @Test
    void addIntersectedTime() {
        Task task1 = new Task("task", "task", LocalDateTime.now(), 10);
        Task task2 = new Task("task", "task", LocalDateTime.now(), 10);
        taskManager.addTask(task1);
        assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addTask(task2);
        });
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", LocalDateTime.now(), 1);
        taskManager.addTask(task);
        final int taskId = task.getId();

        final Task savedTask = taskManager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final List<Task> tasks = taskManager.getAllTasks();

        assertNotNull(tasks, "Задачи на возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void testDelete() {
        Task task = new Task("task", "task", LocalDateTime.now(), 0);
        Epic epic = new Epic("epic", "epic");
        SubTask subTask = new SubTask("subTask", "subTask", LocalDateTime.now().plusMinutes(10), 0);

        taskManager.addTask(task);
        taskManager.addEpic(epic);
        taskManager.addSubtask(subTask, epic.getId());
        taskManager.deleteTaskById(task.getId());
        assertNull(taskManager.getTaskById(task.getId()));
        taskManager.deleteSubtaskById(subTask.getId());
        assertNull(taskManager.getSubTaskById(subTask.getId()));
        taskManager.deleteEpicById(epic.getId());
        assertNull(taskManager.getEpicById(epic.getId()));
    }

    @Test
    void testUpdate() {
        Task task = new Task("newTask", "newTask", LocalDateTime.now(), 0);
        Epic epic = new Epic("epic", "epic");
        SubTask subTask = new SubTask("subTask", "subTask", LocalDateTime.now(), 0);
        task.setId(1);
        subTask.setId(3);
        epic.setId(2);
    }

    @Test
    void testGet() {

        assertEquals(0, taskManager.getAllSubTasksOfEpic(-1).size());

        Task task = new Task("newTask", "newTask", LocalDateTime.now(), 0);
        Epic epic = new Epic("epic", "epic");
        SubTask subTask = new SubTask("subTask", "subTask", LocalDateTime.now().plusMinutes(10), 0);

        taskManager.addTask(task);
        assertEquals(task, taskManager.getTaskById(task.getId()), "the tasks don't match");
        taskManager.addEpic(epic);
        assertEquals(epic, taskManager.getEpicById(epic.getId()), "the epics don't match");
        assertEquals(1, taskManager.getAllEpics().size());

        assertEquals(0, epic.getSubtasks().size());
        assertEquals(0, taskManager.getAllSubTasksOfEpic(epic.getId()).size());
        taskManager.addSubtask(subTask, epic.getId());
        assertEquals(1, epic.getSubtasks().size());
        assertEquals(1, taskManager.getAllSubTasksOfEpic(epic.getId()).size());
        assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()), "the subTasks don't match");


        assertEquals(1, epic.getSubtasks().size());
        var subTask2 = new SubTask("subTask2", "subTask2", LocalDateTime.now().plusMinutes(20), 0);
        taskManager.addSubtask(subTask2, epic.getId());

        assertEquals(2, epic.getSubtasks().size());


        taskManager.deleteEpicById(epic.getId());
        assertEquals(0, taskManager.getAllEpics().size());
        assertNull(taskManager.getEpicById(epic.getId()), "the epic is deleted");

    }
}
