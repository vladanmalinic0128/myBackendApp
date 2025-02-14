package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.entitites.AppUserEntity;
import com.userApp.backend.entitites.MessageEntity;
import com.userApp.backend.entitites.UserEntity;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.repositories.AppUserEntityRepository;
import com.userApp.backend.repositories.MessageRepository;
import com.userApp.backend.repositories.UserEntityRepository;
import com.userApp.backend.requests.MessageRequest;
import com.userApp.backend.responses.MessageResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final UserEntityRepository userEntityRepository;
    private final AppUserEntityRepository appUserEntityRepository;

    public void createMessage(MessageRequest request) throws InvalidTokenException, EntityNotFoundException {
        UserEntity loggedUser = userValidator.validateUser();
        MessageEntity entity = new MessageEntity();

        entity.setUser(loggedUser);

        Optional<AppUserEntity> optionalAppUser = appUserEntityRepository.findById(request.id());
        if(optionalAppUser.isEmpty())
            throw new EntityNotFoundException();
        AppUserEntity user = optionalAppUser.get();
        entity.setAppUserId(user.getId());
        entity.setAppUser(user);

        entity.setContent(request.message());
        entity.setAnswered(false);
        entity.setSeen(false);

        entity.setTime(new java.sql.Timestamp(System.currentTimeMillis()));

        messageRepository.save(entity);
    }

    public List<MessageResponse> getAllMessages() throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        return messageRepository.findAllByAppUserId(loggedUser.getId()).stream()
                .sorted((m1, m2) -> {
                    if (m1.getSeen() != m2.getSeen()) {
                        return m1.getSeen() ? 1 : -1;
                    } else if (!m1.getSeen() && !m2.getSeen()) {
                        return m1.getTime().compareTo(m2.getTime());
                    } else {
                        return m2.getTime().compareTo(m1.getTime());
                    }
                })
                .map(message -> {
                    MessageResponse response = new MessageResponse();
                    response.setId(message.getId());
                    response.setFullName(message.getUser().getAppUser().getFirstname() + " " + message.getUser().getAppUser().getLastname());
                    response.setAvatar(message.getAppUser().getAvatar());
                    response.setTime(message.getTime().toString());
                    response.setContent(message.getContent());
                    response.setSeen(message.getSeen());
                    return response;
                })
                .collect(Collectors.toList());
    }


    public void updateMessageState(Long messageId) throws InvalidTokenException, EntityNotFoundException {
        userValidator.validateUser();

        Optional<MessageEntity> optional = messageRepository.findById(messageId);
        if(optional.isEmpty())
            throw new EntityNotFoundException("Message does not exist");

        MessageEntity message = optional.get();
        message.setSeen(!message.getSeen());

        messageRepository.save(message);
    }
}
