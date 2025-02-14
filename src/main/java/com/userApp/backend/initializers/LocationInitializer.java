package com.userApp.backend.initializers;

import com.userApp.backend.entitites.LocationEntity;
import com.userApp.backend.repositories.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class LocationInitializer /*implements ApplicationRunner*/ {
    private final LocationRepository locationRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> locations = new ArrayList<>(Arrays.asList(
                "Gym", "Park",
                "Home", "Online"
        ));

        locations.stream().map(name -> {
            LocationEntity l = new LocationEntity();
            l.setName(name);
            return l;
        }).forEach(l -> locationRepository.save(l));
    }
}
