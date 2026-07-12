package com.example.tasks.dto;

import jakarta.validation.constraints.NotBlank;
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

    @NotNull
    private Long id;

    @NotBlank
    private String content;

    @NotNull
    private LocalDateTime dueDate;

    @NotBlank
    private String status;

    private String createdBy;

}
