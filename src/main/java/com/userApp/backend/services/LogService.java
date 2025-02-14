package com.userApp.backend.services;

import com.userApp.backend.entitites.LogEntity;
import com.userApp.backend.repositories.LogRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@AllArgsConstructor
public class LogService {
    private final LogRepository logRepository;
    private final ModelMapper modelMapper;

    public void logMessage(String content) {
        LogEntity entity = new LogEntity();
        entity.setContent(content);
        entity.setTime(new Timestamp(System.currentTimeMillis()));
        logRepository.save(entity);
    }
}
