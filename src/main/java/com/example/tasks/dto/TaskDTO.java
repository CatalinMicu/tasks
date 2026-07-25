package com.example.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {

    private Long taskId;

    @NotBlank
    @Size(max = 255)
    private String name;

    @NotNull
    private LocalDate dueDate;

    @NotBlank
    private String statusName;

    @NotNull
    private Long userId;

    private String assignedTo;

    private String createdBy;

}
