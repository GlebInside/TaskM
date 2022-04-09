import model.Epic;
import model.Status;
import model.SubTask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EpicStatusTest {
    private Epic epic;

    @BeforeEach
    private void setUp() {
        epic = new Epic("epic", "descr");
    }

    @Test
    public void noSubtasksStatusNew() {
        assertTrue(Status.NEW == epic.getStatus());
    }

    @Test
    public void allSubtasksDoneStatusDone() {
        var subTask1 = new SubTask("subtask1", "", LocalDateTime.now(), 1);
        epic.getSubtasks().put(1, subTask1);
        assertTrue(Status.NEW == epic.getStatus());
        subTask1.setStatus(Status.DONE);
        assertTrue(Status.DONE == epic.getStatus());
    }

    @Test
    public void newAndDoneCauseInProgress() {
        var subTask1 = new SubTask("subtask1", "", LocalDateTime.now(), 1);
        subTask1.setStatus(Status.DONE);
        epic.getSubtasks().put(1, subTask1);
        var subTask2 = new SubTask("subtask2", "", LocalDateTime.now(), 1);
        epic.getSubtasks().put(2, subTask2);
        assertTrue(Status.IN_PROGRESS == epic.getStatus());

    }

    @Test
    public void newAndInProgressCauseInProgress() {
        var subTask1 = new SubTask("subtask1", "", LocalDateTime.now(), 1);
        subTask1.setStatus(Status.NEW);
        epic.getSubtasks().put(1, subTask1);
        var subTask2 = new SubTask("subtask2", "", LocalDateTime.now(), 1);
        subTask2.setStatus(Status.IN_PROGRESS);
        epic.getSubtasks().put(2, subTask2);
        assertTrue(Status.IN_PROGRESS == epic.getStatus());

    }

    @Test
    public void inProgressAndDoneCauseInProgress() {
        var subTask1 = new SubTask("subtask1", "", LocalDateTime.now(), 1);
        subTask1.setStatus(Status.IN_PROGRESS);
        epic.getSubtasks().put(1, subTask1);
        var subTask2 = new SubTask("subtask2", "", LocalDateTime.now(), 1);
        subTask2.setStatus(Status.DONE);
        epic.getSubtasks().put(2, subTask2);
        assertTrue(Status.IN_PROGRESS == epic.getStatus());
    }

    @Test
    public void inProgressCauseInProgress() {
        var subTask1 = new SubTask("subtask1", "", LocalDateTime.now(), 1);
        subTask1.setStatus(Status.IN_PROGRESS);
        epic.getSubtasks().put(1, subTask1);
        var subTask2 = new SubTask("subtask2", "", LocalDateTime.now(), 1);
        subTask2.setStatus(Status.IN_PROGRESS);
        epic.getSubtasks().put(2, subTask2);
        assertTrue(Status.IN_PROGRESS == epic.getStatus());
    }

    @Test
    public void allNewCauseNew() {
        var subTask1 = new SubTask("subtask1", "", LocalDateTime.now(), 1);
        epic.getSubtasks().put(1, subTask1);
        var subTask2 = new SubTask("subtask2", "", LocalDateTime.now(), 1);
        epic.getSubtasks().put(2, subTask2);
        assertTrue(Status.NEW == epic.getStatus());
    }

    @Test
    public void cantSetStatus() {
        assertThrows(RuntimeException.class, () -> epic.setStatus(Status.DONE));
    }
}
