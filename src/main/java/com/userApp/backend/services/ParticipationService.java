package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.entitites.FitnessProgramEntity;
import com.userApp.backend.entitites.ParticipationEntity;
import com.userApp.backend.entitites.PaymentTypeEntity;
import com.userApp.backend.entitites.UserEntity;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.repositories.FitnessProgramRepository;
import com.userApp.backend.repositories.ParticipationRepository;
import com.userApp.backend.repositories.PaymentTypeRepository;
import com.userApp.backend.requests.FitnessProgramFilterRequest;
import com.userApp.backend.requests.ParticipationRequest;
import com.userApp.backend.responses.ParticipationResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ParticipationService {
    private final ParticipationRepository participationRepository;
    private final FitnessProgramRepository fitnessProgramRepository;
    private final PaymentTypeRepository paymentTypeRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;


    public void addNewParticipation(ParticipationRequest request) throws InvalidTokenException, EntityNotFoundException {
        UserEntity loggedUser = userValidator.validateUser();

        Optional<FitnessProgramEntity> optional = fitnessProgramRepository.findById(request.fitnessProgramId());
        if(optional.isPresent() == false)
            throw new EntityNotFoundException("FitnessProgramEntity");
        FitnessProgramEntity fitnessProgramEntity = optional.get();

        Optional<PaymentTypeEntity> optional1 = paymentTypeRepository.findById(request.paymentTypeId());
        if(optional.isPresent() == false)
            throw new EntityNotFoundException("PaymentTypeEntity");
        PaymentTypeEntity paymentTypeEntity = optional1.get();

        ParticipationEntity entity = new ParticipationEntity();
        entity.setActive(true);
        entity.setUser(loggedUser);
        entity.setFitnessProgram(fitnessProgramEntity);
        entity.setCardNumber(request.cardNumber());
        entity.setPaymentType(paymentTypeEntity);
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));

        participationRepository.save(entity);
    }

    public List<ParticipationResponse> getAllParticipations() throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        return participationRepository.findAllByUserId(loggedUser.getId()).stream()
            .sorted(Comparator.comparing(ParticipationEntity::getCreatedAt).reversed()).map(p -> {
            ParticipationResponse response = new ParticipationResponse();
            response.setFitnessProgramName(p.getFitnessProgram().getName());
            response.setPaymentType(p.getPaymentType().getName());
            response.setCardNumber(p.getCardNumber());
            response.setCreatedAt(p.getCreatedAt().toString());
            return response;
        }).collect(Collectors.toList());
    }
}
