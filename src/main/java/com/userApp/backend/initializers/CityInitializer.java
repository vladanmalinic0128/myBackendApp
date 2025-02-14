package com.userApp.backend.initializers;

import com.userApp.backend.entitites.CityEntity;
import com.userApp.backend.repositories.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CityInitializer /*implements ApplicationRunner*/ {
    private final CityRepository cityRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> cities = new ArrayList<>(Arrays.asList(
                "Beograd", "Novi Sad", "Niš", "Kragujevac", "Subotica",
                "Banja Luka", "Prijedor", "Doboj", "Trebinje", "Foča",
                "Zrenjanin", "Čačak", "Novi Pazar", "Leskovac", "Šabac",
                "Pale", "Višegrad", "Bijeljina", "Zvornik", "Mrkonjić Grad"
        ));

        cities.stream().map(city -> {
            CityEntity entity = new CityEntity();
            entity.setName(city);
            entity.setUsers(null);
            return entity;
        }).forEach(e -> cityRepository.save(e));
    }
}
