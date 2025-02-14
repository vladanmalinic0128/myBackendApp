package com.userApp.backend.initializers;

import com.userApp.backend.entitites.PaymentTypeEntity;
import com.userApp.backend.repositories.PaymentTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentTypeInitializer /*implements ApplicationRunner*/ {
    private final PaymentTypeRepository paymentTypeRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> paymentTypes = new ArrayList<>(Arrays.asList(
                "Live", "Visa", "Mastercard", "Paypal"
        ));

        paymentTypes.stream().map(name -> {
            PaymentTypeEntity entity = new PaymentTypeEntity();
            entity.setName(name);
            return entity;
        }).forEach(entity -> paymentTypeRepository.save(entity));
    }
}
