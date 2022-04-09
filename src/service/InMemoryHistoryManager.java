package service;

import model.Node;
import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private DoublyLinkedList<Task> linkedTasks = new DoublyLinkedList<>();

    private HashMap<Integer, Node<Task>> nodes = new HashMap<>();

    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (nodes.containsKey(taskId)) {
            Node<Task> nodeToDelete = nodes.get(taskId);
            linkedTasks.removeNode(nodeToDelete);
            Node<Task> nodeToSave = new Node<>(task);
            nodes.put(taskId, nodeToSave);
            linkedTasks.linkLast(nodeToSave);
        } else {
            Node<Task> nodeToSave = new Node<>(task);
            linkedTasks.linkLast(nodeToSave);
            nodes.put(taskId, nodeToSave);
        }

    }

    @Override
    public void remove(int id) {
        Node<Task> node = nodes.get(id);
        if (node == null) {
            System.out.println("No node found in view history with id: " + id);
            return;
        }
        linkedTasks.removeNode(node);
        System.out.println("Removed node from doubly linked list: " + node);
    }

    @Override
    public List<Task> getHistory() {

        return linkedTasks.getTasks();

    }

    class DoublyLinkedList<T> {
        public Node<T> head;
        public Node<T> tail;

        public void linkLast(Node<T> data) {

            if (head == null) {
                head = tail = data;
                head.prev = null;
                tail.next = null;
            } else {
                tail.next = data;
                data.prev = tail;
                tail = data;
                tail.next = null;
            }
        }

        public void printNodes() {
            Node<T> current = head;
            if (head == null) {
                System.out.println("Doubly linked list is empty");
                return;
            }
            System.out.println("Nodes of doubly linked list: ");
            while (current != null) {
                System.out.print(current.data + " ");
                current = current.next;
            }
        }

        public ArrayList<Task> getTasks() {
            Node<T> current = head;
            ArrayList<Task> history = new ArrayList<>();

            if (head == null) {
                System.out.println("Doubly linked list is empty");
            }

            System.out.println("Tasks of doubly linked list: ");
            while (current != null) {
                history.add((Task) current.data);
                current = current.next;
            }
            return history;
        }

        public void removeNode(Node<T> node) {
            if (node.equals(head)) {
                head = node.next;
                if (head != null) {
                    head.prev = null;
                }
            } else if (node.equals(tail)) {
                tail = node.prev;
                tail.next = null;
            } else {
                Node<T> prevNode = node.prev;
                Node<T> nextNode = node.next;
                prevNode.next = nextNode;
                nextNode.prev = prevNode;
            }
        }
    }
}
