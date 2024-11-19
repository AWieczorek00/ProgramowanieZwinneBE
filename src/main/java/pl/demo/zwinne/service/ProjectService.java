package pl.demo.zwinne.service;


import pl.demo.zwinne.model.Project;

import java.util.List;

public interface ProjectService {
    List<Project> getAll();
    Project getProjectById(Long id);
}
