package com.userApp.backend.config;

import com.userApp.backend.entitites.CommentEntity;
import com.userApp.backend.entitites.FitnessProgramEntity;
import com.userApp.backend.entitites.UserEntity;
import com.userApp.backend.repositories.AppUserEntityRepository;
import com.userApp.backend.responses.*;
import lombok.RequiredArgsConstructor;
import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AppUserEntityRepository repository;
    private final UserValidator userValidator;

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setAmbiguityIgnored(true);

        //Non default converters
        mapper.addConverter(ApplicationConfig.fitnessProgramConverter());
        mapper.addConverter(ApplicationConfig.lFitnessProgramConverter());
        mapper.addConverter(ApplicationConfig.commentsConverter());

        return mapper;
    }


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(false);
        config.addAllowedOrigin("http://localhost:4200");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }

    private static Converter<FitnessProgramEntity, FitnessProgramResponse> fitnessProgramConverter()
    {
        return new AbstractConverter<FitnessProgramEntity, FitnessProgramResponse>() {
            @Override
            protected FitnessProgramResponse convert(FitnessProgramEntity entity) {
                    FitnessProgramResponse response = new FitnessProgramResponse();
                    response.setId(entity.getId());
                    response.setName(entity.getName());
                    response.setPrice(entity.getPrice());
                    response.setCategoryName(entity.getCategory().getName());
                    response.setLocationName(entity.getLocation().getName());
                    response.setWeightLevel(entity.getWeightLevel());
                    response.setLink(entity.getLink());
                    if(entity.getImages().size() > 0)
                        response.setImage(entity.getImages().get(0).getImage());
                    return response;
            }
        };
    }

    private static Converter<FitnessProgramEntity, LFitnessProgramResponse> lFitnessProgramConverter()
    {
        return new AbstractConverter<FitnessProgramEntity, LFitnessProgramResponse>() {
            @Override
            protected LFitnessProgramResponse convert(FitnessProgramEntity entity) {
                LFitnessProgramResponse response = new LFitnessProgramResponse();
                response.setId(entity.getId());
                response.setName(entity.getName());
                response.setPrice(entity.getPrice());
                response.setCategoryName(entity.getCategory().getName());
                response.setLocationName(entity.getLocation().getName());
                response.setWeightLevel(entity.getWeightLevel());
                response.setFullName(entity.getCreator().getAppUser().getFirstname() + " " + entity.getCreator().getAppUser().getLastname());
                response.setEmail(entity.getCreator().getAppUser().getMail());
                response.setDescription(entity.getDescription());
                response.setLink(entity.getLink());


                List<LImageResponse> images = new ArrayList<>();
                entity.getImages().stream().forEach(i -> {
                    LImageResponse image = new LImageResponse();
                    image.setName("Image" + i.getId());
                    image.setValue(i.getImage());
                    images.add(image);
                });
                response.setImages(images);

                List<LSpecialAttributesResponse> special_attrs = new ArrayList<>();
                entity.getFitnessProgramSpecificAttributes().stream().forEach(sa -> {
                    LSpecialAttributesResponse specAttr = new LSpecialAttributesResponse();
                    specAttr.setName(sa.getSpecificAttribute().getName());
                    specAttr.setValue(sa.getValue());
                    special_attrs.add(specAttr);
                });
                response.setSpecial_attrs(special_attrs);

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date date = new Date(entity.getCreatedAt().getTime());
                String formattedCreatedAt = dateFormat.format(date);
                response.setCreatedAt(formattedCreatedAt);

                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                String formattedTime = timeFormat.format(entity.getDuration());
                response.setDuration(formattedTime);

                return response;
            }
        };
    }

    private static Converter<CommentEntity, CommentResponse> commentsConverter()
    {
        return new AbstractConverter<CommentEntity, CommentResponse>() {
            @Override
            protected CommentResponse convert(CommentEntity entity) {
                CommentResponse response = new CommentResponse();
                response.setAvatar(entity.getUser().getAppUser().getAvatar());
                response.setFullName(entity.getUser().getAppUser().getFirstname() +  " " + entity.getUser().getAppUser().getLastname());
                response.setContent(entity.getContent());

                SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
                Date date = new Date(entity.getTime().getTime());
                String formattedTime = dateFormat.format(date);
                response.setTime(formattedTime);

                response.setResponse(false);
                return response;
            }
        };
    }

}

