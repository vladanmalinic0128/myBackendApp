package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.entitites.*;
import com.userApp.backend.enums.FitnessProgramsRole;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.repositories.*;
import com.userApp.backend.requests.*;
import com.userApp.backend.responses.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class FitnessProgramService {
    private final FitnessProgramRepository fitnessProgramRepository;
    private final CommentRepository commentRepository;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final CategoryService categoryService;
    private final LocationService locationService;
    private final SpecificAttributeRepository specificAttributeRepository;
    private final FitnessProgramSpecialAttributeRepository fitnessProgramSpecialAttributeRepository;
    private final ImageRepository imageRepository;


    public List<FitnessProgramResponse> getFitnessPrograms(FitnessProgramsRole role) throws InvalidTokenException {
        List<FitnessProgramEntity> result;
        if(role == FitnessProgramsRole.MY_PROGRAMS)
            result = getMyFitnessPrograms();
        else if(role == FitnessProgramsRole.FINISHED_PROGRAMS)
            result = getFinishedFitnessPrograms();
        else if(role == FitnessProgramsRole.BOUGHT_PROGRAMS)
            result = getBoughtFitnessPrograms();
        else if(role == FitnessProgramsRole.OTHER_PROGRAMS)
            result = getOtherFitnessPrograms();
        else
            result = getAllFitnessPrograms();
        if(result == null)
            return null;
        return result.stream().filter(f -> f.getIsActive())
                .map(f -> modelMapper.map(f, FitnessProgramResponse.class)).collect(Collectors.toList());
    }

    public List<FitnessProgramResponse> getFilteredFitnessPrograms(FitnessProgramFilterRequest request, FitnessProgramsRole role) throws InvalidTokenException {
        List<FitnessProgramEntity> result;
        if(role == FitnessProgramsRole.MY_PROGRAMS)
            result = getMyFitnessPrograms();
        else if(role == FitnessProgramsRole.FINISHED_PROGRAMS)
            result = getFinishedFitnessPrograms();
        else if(role == FitnessProgramsRole.BOUGHT_PROGRAMS)
            result = getBoughtFitnessPrograms();
        else if(role == FitnessProgramsRole.OTHER_PROGRAMS)
            result = getOtherFitnessPrograms();
        else
            result = getAllFitnessPrograms();
        if(result == null)
            return null;
        Stream<FitnessProgramEntity> filteredFitnessPrograms = result.stream().filter(f -> f.getIsActive());
        if(request.name() != null && request.name().length() > 0)
            filteredFitnessPrograms = filteredFitnessPrograms.filter(f -> f.getName().contains(request.name()));
        if(request.categoryId() != null)
            filteredFitnessPrograms = filteredFitnessPrograms.filter(f -> f.getCategoryId() == request.categoryId());
        if(request.locationId() != null)
            filteredFitnessPrograms = filteredFitnessPrograms.filter(f -> f.getLocationId() == request.locationId());
        if(request.startWeightLevel() != null)
            filteredFitnessPrograms = filteredFitnessPrograms.filter(f -> f.getWeightLevel() >= request.startWeightLevel());
        if(request.endWeightLevel() != null)
            filteredFitnessPrograms = filteredFitnessPrograms.filter(f -> f.getWeightLevel() <= request.endWeightLevel());
        if(request.startPrice() != null)
            filteredFitnessPrograms = filteredFitnessPrograms.filter(f -> f.getPrice() >= request.startPrice());
        if(request.endPrice() != null)
            filteredFitnessPrograms = filteredFitnessPrograms.filter(f -> f.getPrice() <= request.endPrice());
        return filteredFitnessPrograms.map(f -> modelMapper.map(f, FitnessProgramResponse.class)).collect(Collectors.toList());
    }

    public LFitnessProgramResponse getLFitnessProgram(String stringId) throws EntityNotFoundException {
        Long id = 0L;
        try {
            id = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Id not convertable");
        }

        Optional<FitnessProgramEntity> optional = fitnessProgramRepository.findById(id);
        if(optional.isPresent() == false)
            throw new EntityNotFoundException("FitnessProgramEntity");
        FitnessProgramEntity entity = optional.get();

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

        return modelMapper.map(entity, LFitnessProgramResponse.class);
    }


    public List<CommentResponse> getComments(String stringId) throws EntityNotFoundException {
        Long id = 0L;
        try {
            id = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Id not convertable");
        }

        Optional<FitnessProgramEntity> optional = fitnessProgramRepository.findById(id);
        if(optional.isPresent() == false)
            throw new EntityNotFoundException("FitnessProgramEntity");

        return commentRepository.findAllByFitnessProgram_Id(id).stream().sorted(Comparator.comparing(CommentEntity::getTime))
                .map(l -> modelMapper.map(l, CommentResponse.class)).collect(Collectors.toList());
    }

    public void createComment(CommentRequest request) throws InvalidTokenException, EntityNotFoundException {
        UserEntity loggedUser = userValidator.validateUser();
        Long id = 0L;

        Optional<FitnessProgramEntity> optional = fitnessProgramRepository.findById(request.fitnessProgramId());
        if(optional.isPresent() == false)
            throw new EntityNotFoundException("FitnessProgramEntity");
        FitnessProgramEntity fitnessProgram = optional.get();

        CommentEntity commentEntity = new CommentEntity();
        commentEntity.setUser(loggedUser);
        commentEntity.setTime(new Timestamp((System.currentTimeMillis())));
        commentEntity.setFitnessProgram(fitnessProgram);
        commentEntity.setContent(request.content());

        commentRepository.save(commentEntity);
    }

    public void createFitnessProgram(PostFitnessProgramRequest request) throws InvalidTokenException, EntityNotFoundException {
        UserEntity loggedUser = userValidator.validateUser();
        FitnessProgramEntity entity = new FitnessProgramEntity();

        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setLink(request.link());
        entity.setWeightLevel(request.weightLevel());
        entity.setPrice(request.price());
        entity.setCreator(loggedUser);
        entity.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        entity.setActive(true);
        entity.setFinished(false);

        entity.setCategory(categoryService.findById(request.categoryId()));
        entity.setLocation(locationService.findById(request.locationId()));

        entity.setDuration(FitnessProgramService.convertMinutesToTime(request.duration()));

        FitnessProgramEntity result = fitnessProgramRepository.save(entity);

        for(SpecialAttributeRequest specAttr: request.specialAttributes())
        {
            Optional<SpecificAttributeEntity> optional = specificAttributeRepository.findById(specAttr.id());
            if(optional.isPresent() == false)
                throw new EntityNotFoundException("SpecificAttributeEntity with id:" + specAttr.id());
            SpecificAttributeEntity specificAttributeEntity = optional.get();
            FitnessProgramSpecificAttributeEntity fitnessProgramSpecificAttributeEntity = new FitnessProgramSpecificAttributeEntity();
            fitnessProgramSpecificAttributeEntity.setFitnessProgram(result);
            fitnessProgramSpecificAttributeEntity.setSpecificAttribute(specificAttributeEntity);
            fitnessProgramSpecificAttributeEntity.setSpecificAttributeId(specificAttributeEntity.getId());
            fitnessProgramSpecificAttributeEntity.setValue(specAttr.value());
            fitnessProgramSpecialAttributeRepository.save(fitnessProgramSpecificAttributeEntity);
        }
        
        for(ImageRequest image: request.images())
        {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setFitnessProgram(result);
            imageEntity.setImage(image.image());
            imageRepository.save(imageEntity);
        }
    }

    @Transactional
    public void updateFitnessProgram(PostFitnessProgramRequest request) throws InvalidTokenException, EntityNotFoundException {
        UserEntity loggedUser = userValidator.validateUser();
        Optional<FitnessProgramEntity> optionalEntity = fitnessProgramRepository.findById(request.id());
        if(optionalEntity.isPresent() == false)
            throw new EntityNotFoundException("FitnessProgramEntity");
        FitnessProgramEntity entity = optionalEntity.get();

        entity.setName(request.name());
        entity.setDescription(request.description());
        entity.setLink(request.link());
        entity.setWeightLevel(request.weightLevel());
        entity.setPrice(request.price());
        entity.setCreator(loggedUser);

        entity.setCategory(categoryService.findById(request.categoryId()));
        entity.setLocation(locationService.findById(request.locationId()));

        entity.setDuration(FitnessProgramService.convertMinutesToTime(request.duration()));

        FitnessProgramEntity result = fitnessProgramRepository.save(entity);

        fitnessProgramSpecialAttributeRepository.deleteAllByFitnessProgramId(request.id());
        for(SpecialAttributeRequest specAttr: request.specialAttributes())
        {
            Optional<SpecificAttributeEntity> optional = specificAttributeRepository.findById(specAttr.id());
            if(optional.isPresent() == false)
                throw new EntityNotFoundException("SpecificAttributeEntity with id:" + specAttr.id());
            SpecificAttributeEntity specificAttributeEntity = optional.get();
            FitnessProgramSpecificAttributeEntity fitnessProgramSpecificAttributeEntity = new FitnessProgramSpecificAttributeEntity();
            fitnessProgramSpecificAttributeEntity.setFitnessProgram(result);
            fitnessProgramSpecificAttributeEntity.setSpecificAttribute(specificAttributeEntity);
            fitnessProgramSpecificAttributeEntity.setSpecificAttributeId(specificAttributeEntity.getId());
            fitnessProgramSpecificAttributeEntity.setValue(specAttr.value());
            fitnessProgramSpecialAttributeRepository.save(fitnessProgramSpecificAttributeEntity);
        }

        imageRepository.deleteAllByFitnessProgramId(request.id());
        for(ImageRequest image: request.images())
        {
            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setFitnessProgram(result);
            imageEntity.setImage(image.image());
            imageRepository.save(imageEntity);
        }
    }

    public void deleteFitnessProgram(String stringId) throws EntityNotFoundException, InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        Long id = 0L;
        try {
            id = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Id not convertable");
        }

        Optional<FitnessProgramEntity> optionalEntity = fitnessProgramRepository.findById(id);
        if(optionalEntity.isPresent() == false)
            throw new EntityNotFoundException("FitnessProgramEntity");
        FitnessProgramEntity entity = optionalEntity.get();
        entity.setActive(false);
        fitnessProgramRepository.save(entity);
    }

    public void markFitnessProgramAsFinished(String stringId) throws EntityNotFoundException, InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        Long id = 0L;
        try {
            id = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Id not convertable");
        }

        Optional<FitnessProgramEntity> optionalEntity = fitnessProgramRepository.findById(id);
        if(optionalEntity.isPresent() == false)
            throw new EntityNotFoundException("FitnessProgramEntity");
        FitnessProgramEntity entity = optionalEntity.get();
        entity.setFinished(true);
        fitnessProgramRepository.save(entity);
    }

    public boolean getStatus(String stringId) throws InvalidTokenException, EntityNotFoundException {
        UserEntity loggedUser = userValidator.validateUser();
        Long id = 0L;
        try {
            id = Long.parseLong(stringId);
        } catch (NumberFormatException e) {
            throw new EntityNotFoundException("Id not convertable");
        }

        Optional<FitnessProgramEntity> optionalEntity = fitnessProgramRepository.findById(id);
        if(optionalEntity.isPresent() == false)
            throw new EntityNotFoundException("FitnessProgramEntity");
        FitnessProgramEntity entity = optionalEntity.get();
        if(entity.getCreatorId() == loggedUser.getId())
            return false;
        if(entity.getIsActive() == false)
            return false;
        if(entity.getIsFinished() == true)
            return false;
        if(entity.getParticipations().stream().filter(p -> p.getIsActive() && p.getFitnessProgramId() == entity.getId() && p.getUserId() == loggedUser.getId()).findAny().isPresent())
            return false;
        return true;
    }

    private static java.sql.Time convertMinutesToTime(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return new java.sql.Time((long) hours * 60 * 60 * 1000 + minutes * 60 * 1000);
    }

    private List<FitnessProgramEntity> getAllFitnessPrograms() {
        return fitnessProgramRepository.findAll().stream()
                .filter(fp->fp.getIsActive())
                .filter(fp->!fp.getIsFinished())
                .collect(Collectors.toList());
    }

    private List<FitnessProgramEntity> getOtherFitnessPrograms() throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        return fitnessProgramRepository.findAll().stream().filter(fp->fp.getCreatorId() != loggedUser.getId())
                .filter(fp->fp.getIsActive())
                .filter(fp->!fp.getIsFinished())
                .filter(fp -> fp.getParticipations().stream().filter(p -> p.getIsActive() && p.getFitnessProgramId() == fp.getId() && p.getUserId() == loggedUser.getId()).findAny().isEmpty())
                .collect(Collectors.toList());
    }

    private List<FitnessProgramEntity> getBoughtFitnessPrograms() throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        return fitnessProgramRepository.findAll().stream().filter(fp->fp.getCreatorId() != loggedUser.getId())
                .filter(fp->fp.getIsActive())
                .filter(fp->!fp.getIsFinished())
                .filter(fp -> fp.getParticipations().stream().filter(p -> p.getIsActive() && p.getFitnessProgramId() == fp.getId() && p.getUserId() == loggedUser.getId()).findAny().isPresent())
                .collect(Collectors.toList());
    }

    private List<FitnessProgramEntity> getFinishedFitnessPrograms() throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        return fitnessProgramRepository.findAll().stream().filter(fp->fp.getCreatorId() == loggedUser.getId())
                .filter(fp->fp.getIsActive())
                .filter(fp->fp.getIsFinished())
                .collect(Collectors.toList());
    }

    private List<FitnessProgramEntity> getMyFitnessPrograms() throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();
        return fitnessProgramRepository.findAll().stream().filter(fp->fp.getCreatorId() == loggedUser.getId())
                .filter(fp->fp.getIsActive())
                .filter(fp->!fp.getIsFinished())
                .collect(Collectors.toList());
    }
}
