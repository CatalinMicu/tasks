package com.example.tasks.service;

import com.example.tasks.domain.StatusType;
import com.example.tasks.dto.StatusTypeDTO;
import com.example.tasks.mapper.StatusTypeMapper;
import com.example.tasks.repository.StatusTypeRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StatusTypeService {
    private final StatusTypeRepository statusTypeRepository;
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

    public StatusTypeDTO updateStatus(String id, StatusTypeDTO statusTypeDTO) {
        StatusType status = statusTypeRepository.findById(id).orElse(null);
        if (status == null) {
            log.warn("Status type not found with id {}", id);
            return null;
        }
        status.setStatusName(statusTypeDTO.getStatusName());
        status.setCreationDate(statusTypeDTO.getCreationDate());
        status.setCreatedBy(statusTypeDTO.getCreatedBy());
        status.setLastUpdateDate(statusTypeDTO.getLastUpdateDate());
        status.setLastUpdatedBy(statusTypeDTO.getLastUpdatedBy());

        StatusType updatedStatus = statusTypeRepository.save(status);
        return statusMapper.toDto(updatedStatus);
    }

    @Transactional
    public StatusTypeDTO createStatus(@Valid StatusTypeDTO statusTypeDTO) {
        log.info("Status created!");

        StatusType status = statusMapper.toEntity(statusTypeDTO);
        StatusType savedStatus = statusTypeRepository.save(status);

        return statusMapper.toDto(savedStatus);
    }

    @Transactional
    public void deleteStatus(String id) {
        statusTypeRepository.deleteById(id);
    }

    private void ApplyDefaults(StatusType status) {
        if (status.getCreationDate() == null) {
            status.setCreationDate(java.time.LocalDateTime.now());
        }
        if (status.getCreatedBy() == null || status.getCreatedBy().isBlank()) {
            status.setCreatedBy("system");
        }

        if (status.getLastUpdateDate() == null) {
            status.setLastUpdateDate(java.time.LocalDateTime.now());
        }
        if (status.getLastUpdatedBy() == null || status.getLastUpdatedBy().isBlank()) {
            status.setLastUpdatedBy("system");
        }
    }


}
