package pl.demo.zwinne.service;
import pl.demo.zwinne.model.Task;

import java.util.List;

public interface TaskService {
    void addTask(Task task);
    void deleteTask(Long id);
    List<Task> getAll();
    Task getTaskById(Long id);
}
