package pl.demo.zwinne.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class TaskDto {
    @NotBlank
    @NotEmpty
    @NotNull
    private String taskName;
    @NotEmpty
    @NotNull
    @NotBlank
    private String taskDescription;
    @PositiveOrZero
    private int taskEstimatedTime;
}
