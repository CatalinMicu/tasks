package com.example.tasks.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;


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
    private LocalDateTime dueDate;

    @NotBlank
    private String statusTypeId;

    private Long userId;

    private String createdBy;

}
