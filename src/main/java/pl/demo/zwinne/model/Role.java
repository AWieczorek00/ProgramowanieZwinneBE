package pl.demo.zwinne.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import pl.demo.zwinne.Enum.RoleEnum;

import java.util.Date;

@Table(name = "roles")
@Entity
@Data
@NoArgsConstructor
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(nullable = false)
    private Integer id;

    @Column(name = "NAME", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

    @Column(name = "DESCRIPTION", nullable = false)
    private String description;

    @CreationTimestamp
    @Column(updatable = false, name = "CREATED_AT")
    private Date createdAt;

    @UpdateTimestamp
    @Column(name = "UPDATED_AT")
    private Date updatedAt;

}
