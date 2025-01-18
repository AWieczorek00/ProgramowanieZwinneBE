package pl.demo.zwinne.service;
import pl.demo.zwinne.model.Task;

import java.util.List;

public interface TaskService {
    void addTask(Task task);
    void deleteTask(Task task);
    Task save(Task task);
    List<Task> getAll();
    Task getTaskById(Long id);
    List<Task> getSortedTasks(String sortBy, String order, Long projectId);
    List<Task> searchTasks(String searchText, Long projectId);
}
