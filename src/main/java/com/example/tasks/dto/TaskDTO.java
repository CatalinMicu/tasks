package com.example.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    private String name;

    @NotNull
    private LocalDate dueDate;

    @NotBlank
    private String statusName;

    private Long userId;

    private String createdBy;

}
