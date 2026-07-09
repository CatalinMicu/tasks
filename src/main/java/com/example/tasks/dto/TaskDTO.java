package com.example.tasks.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDTO {

    @NotEmpty
    private Long id;

    @NotEmpty
    private String content;

    @NotEmpty
    private LocalDateTime dueDate;

    @NotEmpty
    private String status;

}
