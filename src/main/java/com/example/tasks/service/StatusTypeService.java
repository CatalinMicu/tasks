package com.example.tasks.service;

import com.example.tasks.domain.StatusType;
import com.example.tasks.dto.StatusTypeDTO;
import com.example.tasks.mapper.StatusTypeMapper;
import com.example.tasks.repository.StatusTypeRepository;
import com.example.tasks.repository.TaskRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusTypeService {
    private final StatusTypeRepository statusTypeRepository;
    private final TaskRepository taskRepository;
    private final StatusTypeMapper statusMapper;

    public List<StatusTypeDTO> getAllStatuses() {
        log.info("Statuses retrieved!");
        return statusTypeRepository.findAll()
                .stream()
                .map(statusMapper::toDto)
                .toList();
    }


    public StatusTypeDTO getStatusById(String id) {
        StatusType status = statusTypeRepository.findById(id).orElse(null);
        if (status == null) {
            log.warn("Status type not found with id {}", id);
            return null;
        }
        return statusMapper.toDto(status);
    }

    @Transactional
    public StatusTypeDTO updateStatus(String id, StatusTypeDTO statusTypeDTO) {
        StatusType status = statusTypeRepository.findById(id).orElse(null);
        if (status == null) {
            log.warn("Status type not found with id {}", id);
            return null;
        }
        status.setStatusName(statusTypeDTO.getStatusName());
        status.setLastUpdateDate(LocalDateTime.now());
        status.setLastUpdatedBy("system");

        StatusType updatedStatus = statusTypeRepository.save(status);
        return statusMapper.toDto(updatedStatus);
    }

    @Transactional
    public StatusTypeDTO createStatus(@Valid StatusTypeDTO statusTypeDTO) {
        log.info("Status created!");

        StatusType status = statusMapper.toEntity(statusTypeDTO);
        applyDefaults(status);
        StatusType savedStatus = statusTypeRepository.save(status);


        return statusMapper.toDto(savedStatus);
    }

    @Transactional
    public void deleteStatus(String id) {
        if (taskRepository.existsByStatusType_StatusTypeId(id)) {
            log.warn("Status type {} is used by tasks and cannot be deleted", id);
            return;
        }
        statusTypeRepository.deleteById(id);
    }

    private void applyDefaults(StatusType status) {
        if (status.getCreationDate() == null) {
            status.setCreationDate(LocalDateTime.now());
        }
        if (status.getCreatedBy() == null || status.getCreatedBy().isBlank()) {
            status.setCreatedBy("system");
        }

        if (status.getLastUpdateDate() == null) {
            status.setLastUpdateDate(LocalDateTime.now());
        }
        if (status.getLastUpdatedBy() == null || status.getLastUpdatedBy().isBlank()) {
            status.setLastUpdatedBy("system");
        }
    }


}
