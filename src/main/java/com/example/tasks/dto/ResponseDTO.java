package com.example.tasks.dto;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

    private UserDTO user;

    private String response;

}
