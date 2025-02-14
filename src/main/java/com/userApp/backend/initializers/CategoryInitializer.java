package com.userApp.backend.initializers;

import com.userApp.backend.entitites.CategoryEntity;
import com.userApp.backend.entitites.SpecificAttributeEntity;
import com.userApp.backend.repositories.CategoryRepository;
import com.userApp.backend.repositories.SpecificAttributeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CategoryInitializer /*implements ApplicationRunner*/ {
    private final CategoryRepository categoryRepository;
    private final SpecificAttributeRepository specificAttributeRepository;

    //@Override
    public void run(ApplicationArguments args) throws Exception {
        List<String> categories = new ArrayList<>(Arrays.asList(
                "Kardio trening", "Trening snage",
                "Trening kondicije", "Trening boksa"
        ));

        categories.stream().map(name -> {
            CategoryEntity c = new CategoryEntity();
            c.setActive(true);
            c.setName(name);
            return c;
        }).forEach(c -> categoryRepository.save(c));

        List<String> specificAttr1 = new ArrayList<>(Arrays.asList(
                "Trajanje", "Tip", "Brzina hoda"
        ));
        List<String> specificAttr2 = new ArrayList<>(Arrays.asList(
                "Trajanje", "Split", "Broj vjezbi"
        ));
        List<String> specificAttr3 = new ArrayList<>(Arrays.asList(
                "Trajanje", "Broj kilometara", "Brzina hoda"
        ));
        List<String> specificAttr4 = new ArrayList<>(Arrays.asList(
                "Udaranje vrece", "Kardio ukljucen", "Sparing ukljucen"
        ));

        this.saveAttributes("Kardio trening", specificAttr1);
        this.saveAttributes("Trening snage", specificAttr2);
        this.saveAttributes("Trening kondicije", specificAttr3);
        this.saveAttributes("Trening boksa", specificAttr4);
    }

    private final void saveAttributes(String categoryName, List<String> specificAttributes) {
        CategoryEntity categoryEntity = categoryRepository.findAll().stream().filter(c -> categoryName.equals(c.getName())).collect(Collectors.toList()).get(0);
        specificAttributes.stream().map(name -> {
            SpecificAttributeEntity sa = new SpecificAttributeEntity();
            sa.setActive(true);
            sa.setName(name);
            sa.setCategory(categoryEntity);
            return sa;
        }).forEach(sa -> specificAttributeRepository.save(sa));
    }
}
