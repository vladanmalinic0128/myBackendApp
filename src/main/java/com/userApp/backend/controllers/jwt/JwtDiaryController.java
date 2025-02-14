package com.userApp.backend.controllers.jwt;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.entitites.UserEntity;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.requests.DiaryRequest;
import com.userApp.backend.responses.DiaryResponse;
import com.userApp.backend.services.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

@RestController
@RequestMapping( "/diaries")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class JwtDiaryController {
    private final DiaryService diaryService;
    private final UserValidator userValidator;

    @PostMapping
    public ResponseEntity<?> addNewDiary(
            @RequestBody DiaryRequest request
    ) throws InvalidTokenException, EntityNotFoundException {
        diaryService.createDiary(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<DiaryResponse>> getAllDiaries() throws InvalidTokenException {
        userValidator.validateUser();
        List<DiaryResponse> diaries = diaryService.getAllDiaries();
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/pdf")
    public ResponseEntity<byte[]> getPdfForDiary() throws InvalidTokenException {
        UserEntity user = userValidator.validateUser();
        ByteArrayOutputStream outputStream = diaryService.getPdf();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("filename", user.getAppUser().getUsername() + "_diary.pdf");

        return ResponseEntity.ok()
                .headers(headers)
                .body(outputStream.toByteArray());
    }

}
