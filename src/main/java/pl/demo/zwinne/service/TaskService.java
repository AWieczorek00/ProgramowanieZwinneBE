package pl.demo.zwinne.service;

import pl.demo.zwinne.model.Task;

import java.util.List;

public interface TaskService {
    List<Task> getAll();
    Task getTaskById(Long id);
}
