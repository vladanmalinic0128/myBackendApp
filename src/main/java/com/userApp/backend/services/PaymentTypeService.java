package com.userApp.backend.services;

import com.userApp.backend.repositories.PaymentTypeRepository;
import com.userApp.backend.responses.PaymentTypeResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PaymentTypeService {
    private final PaymentTypeRepository paymentTypeRepository;
    private final ModelMapper modelMapper;


    public List<PaymentTypeResponse> getAllPaymentTypes() {
        return paymentTypeRepository.findAll().stream().map(l -> modelMapper.map(l, PaymentTypeResponse.class)).collect(Collectors.toList());
    }
}
