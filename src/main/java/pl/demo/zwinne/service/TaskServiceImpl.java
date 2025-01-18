package pl.demo.zwinne.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.demo.zwinne.model.Project;
import pl.demo.zwinne.model.Task;
import pl.demo.zwinne.respository.TaskRepository;

import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public void addTask(Task task) {
        taskRepository.save(task);
    }

    @Override
    public void deleteTask(Task task) {
        taskRepository.deleteById(task.getId());
    }

    @Override
    public Task save(Task task) {
        return taskRepository.save(task);
    }

    @Override
    public List<Task> getAll() { return taskRepository.findAll(); }

    @Override
    public Task getTaskById(Long id) {
        return taskRepository.findById(id).orElseThrow(() -> new RuntimeException("Project not found"));
    }

    @Override
    public List<Task> getSortedTasks(String sortBy, String order, Long projectId) {
        Sort sort = order.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        return taskRepository.findAll(sort);
    }

    @Override
    public List<Task> searchTasks(String searchText, Long projectId) {
        return taskRepository.findByKeyword(searchText.toLowerCase(), projectId);
    }
}
