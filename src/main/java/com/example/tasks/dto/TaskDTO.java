package com.example.tasks.dto;

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


    private String content;

    @NotNull
    private LocalDateTime dueDate;


    private String status;

}
