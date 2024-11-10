package pl.demo.zwinne.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "PROJECT")
@Data
public class Project {

    @Id
    @GeneratedValue
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, length = 50)
    private String name;

    @CreationTimestamp
    @Column(name = "DATE_CREATE", nullable = false, updatable = false)
    private LocalDateTime dateCreate;


    @UpdateTimestamp
    @Column(name = "DATE_MODIFY", nullable = false)
    private LocalDateTime dateModify;

    @OneToMany(mappedBy = "project")
    private List<Task> tasks;

}
