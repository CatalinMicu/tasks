package com.example.tasks.controller;

import com.example.tasks.dto.StatusTypeDTO;
import com.example.tasks.service.StatusTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/statuses")
public class StatusTypeController {
    private final StatusTypeService statusTypeService;

    public StatusTypeController(StatusTypeService statusTypeService) {
        this.statusTypeService = statusTypeService;
    }


    @GetMapping
    public List<StatusTypeDTO> getAllStatuses() {
        return statusTypeService.getAllStatuses();
    }

    @PostMapping
    public StatusTypeDTO createStatus(@RequestBody StatusTypeDTO statusTypeDTO) {
        return statusTypeService.createStatus(statusTypeDTO);
    }

    @PatchMapping("/{id}")
    public StatusTypeDTO updateStatus(@PathVariable String id, @RequestBody StatusTypeDTO statusTypeDTO) {
        return statusTypeService.updateStatus(id, statusTypeDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteStatus(@PathVariable String id) {
        statusTypeService.deleteStatus(id);
    }
}
