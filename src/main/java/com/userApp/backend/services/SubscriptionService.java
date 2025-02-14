package com.userApp.backend.services;

import com.userApp.backend.config.UserValidator;
import com.userApp.backend.email.EmailSender;
import com.userApp.backend.entitites.*;
import com.userApp.backend.exceptions.EntityNotFoundException;
import com.userApp.backend.exceptions.InvalidTokenException;
import com.userApp.backend.repositories.CategoryRepository;
import com.userApp.backend.repositories.FitnessProgramRepository;
import com.userApp.backend.repositories.SpecificAttributeRepository;
import com.userApp.backend.repositories.SubscriptionRepository;
import com.userApp.backend.requests.SubscriptionRequest;
import com.userApp.backend.responses.SubscriptionResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subscriptionRepository;
    private final CategoryRepository categoryRepository;
    private final SpecificAttributeRepository specificAttributeRepository;

    private final ModelMapper modelMapper;
    private final UserValidator userValidator;
    private final FitnessProgramRepository fitnessProgramRepository;
    private final EmailSender emailSender;

    public List<SubscriptionResponse> getAllSubscriptions() throws InvalidTokenException {
        UserEntity loggedUser = userValidator.validateUser();

        List<SubscriptionResponse> result = new ArrayList<>();

        List<CategoryEntity> categories = categoryRepository.findAll();

        List<SubscriptionEntity> subscriptions = subscriptionRepository.findAllByUserId(loggedUser.getId());

        categories.stream().forEach(c -> {
            SubscriptionResponse subscriptionResponse = new SubscriptionResponse();
            subscriptionResponse.setCategoryId(c.getId());
            subscriptionResponse.setCategoryName(c.getName());
            String specificAttributes = specificAttributeRepository.findAllByCategory_Id(c.getId()).stream().map(SpecificAttributeEntity::getName).collect(Collectors.joining(", "));
            subscriptionResponse.setSpecialAttributes(specificAttributes);
            boolean exists = subscriptions.stream().filter(s -> s.getCategory().getId() == c.getId()).filter(s -> s.getIsActive()).findAny().isPresent();
            subscriptionResponse.setExists(exists);
            result.add(subscriptionResponse);
        });
        return result;
    }


    public void changeSubscriptionStatus(SubscriptionRequest request) throws InvalidTokenException, EntityNotFoundException {
        UserEntity loggedUser = userValidator.validateUser();

        Optional<SubscriptionEntity> optional = subscriptionRepository.findAllByUserId(loggedUser.getId()).stream().filter(s -> s.getCategory().getId() == request.id()).findFirst();
        if (optional.isPresent()) {
            SubscriptionEntity entity = optional.get();
            entity.setActive(request.subscribed());
            subscriptionRepository.save(entity);
        } else {
            SubscriptionEntity entity = new SubscriptionEntity();
            Optional<CategoryEntity> categoryEntity = categoryRepository.findById(request.id());
            if (categoryEntity.isPresent() == false)
                throw new EntityNotFoundException("Category not found");
            entity.setCategory(categoryEntity.get());
            entity.setUser(loggedUser);
            entity.setActive(true);
            subscriptionRepository.save(entity);
        }
    }

    public void sendEmails() {
        List<CategoryEntity> categories = categoryRepository.findAll().stream().filter(c -> c.getIsActive()).collect(Collectors.toList());
        for (CategoryEntity category : categories) {
            List<FitnessProgramEntity> fitnessPrograms = fitnessProgramRepository.findAllByCategoryId(category.getId()).stream()
                    .filter(fp -> fp.getIsActive())
                    .filter(fp -> LocalDate.now().isEqual(fp.getCreatedAt().toLocalDateTime().toLocalDate()))
                    .collect(Collectors.toList());

            List<UserEntity> users = subscriptionRepository.findAllByCategoryId(category.getId()).stream()
                    .filter(user -> user.getUser().getAppUser().getLocked() == false && user.getUser().getAppUser().getEnabled() == true)
                    .map(subscription -> subscription.getUser())
                    .collect(Collectors.toList());
            ;

            for (UserEntity user : users)
                sendEmail(user, fitnessPrograms);
        }
    }

    private void sendEmail(UserEntity user, List<FitnessProgramEntity> fitnessPrograms) {
        List<String> links = fitnessPrograms.stream().map(fp -> {
            try {
                return "http://" + SubscriptionService.getIpAddress() + ":9000/api/fitness-programs/" + fp.getId();
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
        }).collect(Collectors.toList());
        List<String> names = fitnessPrograms.stream().map(fp -> fp.getName()).collect(Collectors.toList());
        emailSender.send(user.getAppUser().getMail(), buildEmail(user.getAppUser().getUsername(), links, names));
    }

    private static final String getIpAddress() throws SocketException {
        InetAddress inetAddress = null;
        String ipAddress = "";
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            if (networkInterface.getName().equals("wlan0")) {
                Enumeration<InetAddress> inetAddresses = networkInterface.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    inetAddress = inetAddresses.nextElement();
                    if (inetAddress.getAddress().length == 4) {
                        return inetAddress.getHostAddress();
                    }
                }
            }
        }
        return "";
    }

    private String buildEmail(String name, List<String> links, List<String> names) {
        StringBuilder emailBuilder = new StringBuilder();
        emailBuilder.append("<div style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #1a1a1a;\">\n")
                .append("\n")
                .append("  <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; min-width: 100%; width: 100% !important;\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n")
                .append("    <tbody>\n")
                .append("      <tr>\n")
                .append("        <td width=\"100%\" height=\"53\" bgcolor=\"#3498db\">\n")
                .append("          <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; max-width: 580px;\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n")
                .append("            <tr>\n")
                .append("              <td width=\"70\" bgcolor=\"#3498db\" valign=\"middle\">\n")
                .append("                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse;\">\n")
                .append("                  <tr>\n")
                .append("                    <td style=\"padding-left: 10px;\">\n")
                .append("                    </td>\n")
                .append("                    <td style=\"font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px;\">\n")
                .append("                      <span style=\"font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block;\">Confirm your email</span>\n")
                .append("                    </td>\n")
                .append("                  </tr>\n")
                .append("                </table>\n")
                .append("              </td>\n")
                .append("            </tr>\n")
                .append("          </table>\n")
                .append("        </td>\n")
                .append("      </tr>\n")
                .append("    </tbody>\n")
                .append("  </table>\n")
                .append("\n")
                .append("  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100% !important;\" width=\"100%\">\n")
                .append("    <tbody>\n")
                .append("      <tr>\n")
                .append("        <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n")
                .append("        <td>\n")
                .append("          <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse;\">\n")
                .append("            <tr>\n")
                .append("              <td bgcolor=\"#fbd1a2\" width=\"100%\" height=\"10\"></td>\n")
                .append("            </tr>\n")
                .append("          </table>\n")
                .append("        </td>\n")
                .append("        <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n")
                .append("      </tr>\n")
                .append("    </tbody>\n")
                .append("  </table>\n")
                .append("\n")
                .append("  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100% !important;\" width=\"100%\">\n")
                .append("    <tbody>\n")
                .append("      <tr>\n")
                .append("        <td height=\"30\"><br></td>\n")
                .append("      </tr>\n")
                .append("      <tr>\n")
                .append("        <td width=\"10\" valign=\"middle\"><br></td>\n")
                .append("        <td style=\"font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px;\">\n")
                .append("          <p style=\"margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #1a1a1a;\">Zdravo ").append(name).append(",</p>\n")
                .append("          <p style=\"margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #1a1a1a;\">Hvala Vam na registraciji. Molimo Vas da kliknete na linkove ispod da biste aktivirali Vaš nalog:</p>\n")
                .append("          <ul style=\"list-style-type: none; padding: 0;\">\n");

        for (int j = 0; j < links.size(); j++) {
            emailBuilder.append("            <li style=\"margin: 0 0 20px 0;\">\n")
                    .append("              <blockquote style=\"margin: 0 0 20px 0; border-left: 10px solid #fbd1a2; padding: 15px 0 0.1px 15px; font-size: 19px; line-height: 25px;\">\n")
                    .append("                <p style=\"margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #1a1a1a;\">\n")
                    .append("                  <a href=\"").append(links.get(j)).append("\">").append(names.get(j)).append("</a>\n")
                    .append("                </p>\n")
                    .append("              </blockquote>\n")
                    .append("            </li>\n");
        }

        emailBuilder.append("          </ul>\n")
                .append("          Linkovi će biti aktivni u narednih 15 minuta.\n")
                .append("          <p>Hvala Vam na povjerenju,</p>\n")
                .append("          <p>HelpDesk</p>\n")
                .append("        </td>\n")
                .append("        <td width=\"10\" valign=\"middle\"><br></td>\n")
                .append("      </tr>\n")
                .append("      <tr>\n")
                .append("        <td height=\"30\"><br></td>\n")
                .append("      </tr>\n")
                .append("    </tbody>\n")
                .append("  </table>\n")
                .append("  <div class=\"yj6qo\"></div>\n")
                .append("  <div class=\"adL\"></div>\n")
                .append("</div>");

        return emailBuilder.toString();
    }
}
