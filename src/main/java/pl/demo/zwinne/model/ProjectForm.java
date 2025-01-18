package pl.demo.zwinne.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProjectForm {
    private String name;
    private String description;
    private LocalDateTime dateCreate;
    private LocalDateTime dateDefense;
}
