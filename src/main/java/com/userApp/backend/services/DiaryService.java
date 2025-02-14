package com.userApp.backend.services;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.userApp.backend.config.UserValidator;
import com.userApp.backend.entitites.DiaryEntity;
import com.userApp.backend.entitites.UserEntity;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.repositories.DiaryRepository;
import com.userApp.backend.requests.DiaryRequest;
import com.userApp.backend.responses.DiaryResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DiaryService {
    private final DiaryRepository diaryRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    public void createDiary(DiaryRequest request) throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        DiaryEntity entity = new DiaryEntity();

        entity.setUser(loggedUser);
        entity.setDescription(request.description());
        entity.setCurrentWeight(request.currentWeight());
        entity.setTrainingDuration(request.trainingDuration());
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        diaryRepository.save(entity);
    }

    public List<DiaryResponse> getAllDiaries() throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        return diaryRepository.findAllByUserId(loggedUser.getId()).stream().map(l -> modelMapper.map(l, DiaryResponse.class)).collect(Collectors.toList());
    }

    public ByteArrayOutputStream getPdf() throws InvalidTokenException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document();
        PdfWriter writer;

        try {
            writer = PdfWriter.getInstance(document, outputStream);
            document.open();
            UserEntity user = userValidator.validateUser();

            Font titleFont = new Font(Font.TIMES_ROMAN, 18, Font.BOLD);
            Paragraph title = new Paragraph("Training Diary", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);

            Date currentDate = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Paragraph dateTime = new Paragraph("Generated on: " + dateFormat.format(currentDate));
            dateTime.setAlignment(Element.ALIGN_CENTER);
            document.add(dateTime);

            // Empty row
            document.add(new Paragraph("\n"));

            List<DiaryEntity> diaries = diaryRepository.findAllByUserId(user.getId()).stream().collect(Collectors.toList());
            for (DiaryEntity diary : diaries) {
                document.add(new Paragraph("Training duration: " + diary.getTrainingDuration()));
                document.add(new Paragraph("Current weight: " + diary.getCurrentWeight()));
                document.add(new Paragraph("Description: " + diary.getDescription()));
                document.add(new Paragraph("-----------------------------------------------------"));
            }

            document.close();
            writer.close(); // Close the PdfWriter

        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }

        return outputStream;
    }
}
