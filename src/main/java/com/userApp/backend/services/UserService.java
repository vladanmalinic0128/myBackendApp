package com.userApp.backend.services;

import com.userApp.backend.config.JwtService;
import com.userApp.backend.config.UserValidator;
import com.userApp.backend.email.EmailSender;
import com.userApp.backend.entitites.AppUserEntity;
import com.userApp.backend.entitites.CityEntity;
import com.userApp.backend.entitites.ConfirmationTokenEntity;
import com.userApp.backend.entitites.UserEntity;
import com.userApp.backend.exceptions.*;
import com.userApp.backend.repositories.AdvisorEntityRepository;
import com.userApp.backend.repositories.AppUserEntityRepository;
import com.userApp.backend.repositories.CityRepository;
import com.userApp.backend.repositories.UserEntityRepository;
import com.userApp.backend.requests.*;
import com.userApp.backend.responses.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.login.AccountLockedException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.time.LocalDateTime;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final AppUserEntityRepository appUserEntityRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final EmailSender emailSender;
    private final ConfirmationTokenService confirmationTokenService;
    private final CityRepository cityRepository;
    private final AppUserService appUserService;
    private final AuthenticationManager authenticationManager;
    private final UserValidator userValidator;
    private final AdvisorEntityRepository advisorEntityRepository;

    @Transactional
    public RegistrationResponse register(RegistrationRequest request) throws RegistrationException {
        String emailRegex = "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$";
        if (request.email().matches(emailRegex) == false)
            throw new RegistrationException("Invalid email");

        String usernameRegex = "^[a-zA-Z0-9_-]{5,30}$";
        if (request.username().matches(usernameRegex) == false)
            throw new RegistrationException("Invalid username");

        //Password contains at least one lowercase letter, one uppercase letter, one digit and one special character.
        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+])[0-9a-zA-Z!@#$%^&*()_+]{8,}$";
        if (request.password().matches(regex) == false) {
            throw new RegistrationException("Invalid password");
        }

        boolean usernameExists = appUserEntityRepository.findByUsername(request.username()).isPresent();
        if (usernameExists)
            throw new RegistrationException("Username already exists");

        boolean emailExists = appUserEntityRepository.findByMail(request.email()).isPresent();
        if (emailExists) {
            throw new RegistrationException("Email already exists");
        }

        AppUserEntity appUser = AppUserEntity.builder()
                .firstname(request.firstname())
                .lastname(request.lastname())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .mail(request.email())
                .avatar(request.avatar())
                .locked(false)
                .enabled(false)
                .isActive(true)
                .build();

        Optional<CityEntity> optionalCity = cityRepository.findById(request.cityId());
        if (optionalCity.isPresent() == false)
            throw new RegistrationException("Registration failed: City does not exist");

        UserEntity user = UserEntity.builder()
                .appUser(appUser)
                .city(optionalCity.get())
                .build();

        appUserEntityRepository.save(appUser);
        userEntityRepository.save(user);

        var jwtToken = jwtService.generateToken(appUser);

        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(
                jwtToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String confirmationLink = null;
        try {
            confirmationLink = "http://" + getIpAddress() + ":9000/api/register/confirm?token=" + jwtToken;
            emailSender.send(request.email(), buildEmail(request.username(), confirmationLink));
            RegistrationResponse response = new RegistrationResponse(jwtToken);
            return response;
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
    }

    private final String getIpAddress() throws SocketException {
        InetAddress inetAddress = null;
        String ipAddress = "";
        Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
        while (networkInterfaces.hasMoreElements()) {
            NetworkInterface networkInterface = networkInterfaces.nextElement();
            System.out.println(networkInterface.getName() + " " + networkInterface.getInetAddresses());
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

    @Transactional
    public String confirmToken(String token) {
        ConfirmationTokenEntity confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() ->
                        new IllegalStateException("token not found"));

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("email already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("token expired");
        }

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(
                confirmationToken.getAppUser().getMail());
        return "Confirmed successfully";
    }

    private String buildEmail(String name, String link) {
        return "<div style=\"font-family: Helvetica, Arial, sans-serif; font-size: 16px; margin: 0; color: #1a1a1a;\">\n" +
                "\n" +
                "  <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; min-width: 100%; width: 100% !important;\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"100%\" height=\"53\" bgcolor=\"#3498db\">\n" +
                "          <table role=\"presentation\" width=\"100%\" style=\"border-collapse: collapse; max-width: 580px;\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "            <tr>\n" +
                "              <td width=\"70\" bgcolor=\"#3498db\" valign=\"middle\">\n" +
                "                <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse;\">\n" +
                "                  <tr>\n" +
                "                    <td style=\"padding-left: 10px;\">\n" +
                "                    </td>\n" +
                "                    <td style=\"font-size: 28px; line-height: 1.315789474; Margin-top: 4px; padding-left: 10px;\">\n" +
                "                      <span style=\"font-family: Helvetica, Arial, sans-serif; font-weight: 700; color: #ffffff; text-decoration: none; vertical-align: top; display: inline-block;\">Confirm your email</span>\n" +
                "                    </td>\n" +
                "                  </tr>\n" +
                "                </table>\n" +
                "              </td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100% !important;\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td width=\"10\" height=\"10\" valign=\"middle\"></td>\n" +
                "        <td>\n" +
                "          <table role=\"presentation\" width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse;\">\n" +
                "            <tr>\n" +
                "              <td bgcolor=\"#fbd1a2\" width=\"100%\" height=\"10\"></td>\n" +
                "            </tr>\n" +
                "          </table>\n" +
                "        </td>\n" +
                "        <td width=\"10\" valign=\"middle\" height=\"10\"></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "\n" +
                "  <table role=\"presentation\" class=\"m_-6186904992287805515content\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse: collapse; max-width: 580px; width: 100% !important;\" width=\"100%\">\n" +
                "    <tbody>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "        <td style=\"font-family: Helvetica, Arial, sans-serif; font-size: 19px; line-height: 1.315789474; max-width: 560px;\">\n" +
                "          <p style=\"margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #1a1a1a;\">Zdravo " + name + ",</p>\n" +
                "          <p style=\"margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #1a1a1a;\">Hvala Vam na registraciji. Molimo Vas da kliknete na link ispod da biste aktivirali Vaš nalog:</p>\n" +
                "          <blockquote style=\"margin: 0 0 20px 0; border-left: 10px solid #fbd1a2; padding: 15px 0 0.1px 15px; font-size: 19px; line-height: 25px;\">\n" +
                "            <p style=\"margin: 0 0 20px 0; font-size: 19px; line-height: 25px; color: #1a1a1a;\">\n" +
                "              <a href=\"" + link + "\">Aktivirajte odmah</a>\n" +
                "            </p>\n" +
                "          </blockquote>\n" +
                "          Link će biti aktivan u narednih 15 minuta.\n" +
                "          <p>Hvala Vam na povjerenju,</p>\n" +
                "          <p>HelpDesk</p>\n" +
                "        </td>\n" +
                "        <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "      </tr>\n" +
                "      <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "      </tr>\n" +
                "    </tbody>\n" +
                "  </table>\n" +
                "  <div class=\"yj6qo\"></div>\n" +
                "  <div class=\"adL\"></div>\n" +
                "</div>";
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) throws EmailNotVerifiedException, UsernameNotFoundException {

        AppUserEntity user = appUserEntityRepository.findByUsername(request.username())
                .orElseThrow(com.userApp.backend.exceptions.UsernameNotFoundException::new);

        if (user.getLocked() == true || user.getEnabled() == false)
            throw new EmailNotVerifiedException("Email not verified");

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        var jwtToken = jwtService.generateToken(user);

        return new AuthenticationResponse(
                jwtToken,
                user.getUsername(),
                user.getMail(),
                user.getFirstname(),
                user.getLastname(),
                user.getAvatar(),
                user.getUser().getCity().getName()
        );
    }

    public AuthenticationResponse sendConfirmationMail(AuthenticationRequest request) throws UsernameNotFoundException, EmailAlreadyVerifiedException, AccountLockedException, WrongCredentialsException {
        AppUserEntity appUser = appUserEntityRepository.findByUsername(request.username())
                .orElseThrow(com.userApp.backend.exceptions.UsernameNotFoundException::new);

        if (passwordEncoder.matches(request.password(), appUser.getPassword()) == false)
            throw new WrongCredentialsException("Wrong credentials");

        if (appUser.getLocked() == true)
            throw new AccountLockedException("Account locked exception");

        if (appUser.getLocked() == false && appUser.getEnabled() == true)
            throw new EmailAlreadyVerifiedException("Email already verified");

        var jwtToken = jwtService.generateToken(appUser);

        ConfirmationTokenEntity confirmationToken = new ConfirmationTokenEntity(
                jwtToken,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        String confirmationLink = null;
        try {
            confirmationLink = "http://" + getIpAddress() + ":9000/api/register/confirm?token=" + jwtToken;
            emailSender.send(appUser.getMail(), buildEmail(request.username(), confirmationLink));
            RegistrationResponse response = new RegistrationResponse(jwtToken);
            return new AuthenticationResponse(
                    jwtToken,
                    appUser.getUsername(),
                    appUser.getMail(),
                    appUser.getFirstname(),
                    appUser.getLastname(),
                    appUser.getAvatar(),
                    appUser.getUser().getCity().getName()
            );
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }

    }

    public UsernameExistsResponse usernameExists(UsernameExistsRequest request) {
        boolean exists = appUserEntityRepository.existsByUsername(request.username());
        UsernameExistsResponse result = new UsernameExistsResponse();
        result.setExists(exists);
        return result;
    }

    public EmailExistsResponse emailExists(EmailExistsRequest request) {
        boolean exists = appUserEntityRepository.existsByMail(request.email());
        EmailExistsResponse result = new EmailExistsResponse();
        result.setExists(exists);
        return result;
    }

    public ChatUsersResponse getUsersAndAdvisors() throws InvalidTokenException {
        UserEntity user = userValidator.validateUser();
        ChatUsersResponse response = new ChatUsersResponse();

        List<UserChatResponse> advisors = advisorEntityRepository.findAll().stream().filter(a -> a.getAppUser().getIsActive())
                .map(a -> new UserChatResponse(a.getId(), a.getAppUser().getUsername()))
                .collect(Collectors.toList());
        List<UserChatResponse> users = userEntityRepository.findAll().stream().filter(u -> u.getAppUser().getIsActive())
                .filter(u -> u.getId() != user.getId())
                .map(a -> new UserChatResponse(a.getId(), a.getAppUser().getUsername()))
                .collect(Collectors.toList());

        response.setAdvisors(advisors);
        response.setUsers(users);

        return response;
    }


    public void editData(EditDataRequest request) throws InvalidTokenException, EntityNotFoundException {
        UserEntity user = userValidator.validateUser();

        user.getAppUser().setFirstname(request.firstname());
        user.getAppUser().setLastname(request.lastname());
        user.setCityId(request.cityId());
        user.getAppUser().setAvatar(request.image());

        Optional<CityEntity> optionalCity = cityRepository.findById(request.cityId());
        if (optionalCity.isPresent() == false)
            throw new EntityNotFoundException("Settings failed: City does not exist");
        user.setCity(optionalCity.get());

        appUserEntityRepository.save(user.getAppUser());
        userEntityRepository.save(user);

    }

    public void editPassword(EditPasswordRequest request) throws InvalidTokenException, EmailNotVerifiedException, RegistrationException {
        AppUserEntity user = userValidator.validateUser().getAppUser();

        if (user.getLocked() == true || user.getEnabled() == false)
            throw new EmailNotVerifiedException("Email not verified");

        String regex = "^(?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+])[0-9a-zA-Z!@#$%^&*()_+]{8,}$";
        if (request.newPassword().matches(regex) == false) {
            throw new RegistrationException("Invalid password");
        }

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        request.oldPassword()
                )
        );

        user.setPassword(passwordEncoder.encode(request.newPassword()));
        appUserEntityRepository.save(user);
    }
}
