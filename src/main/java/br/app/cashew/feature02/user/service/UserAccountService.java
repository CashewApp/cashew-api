package br.app.cashew.feature02.user.service;

import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.user.User;
import br.app.cashew.feature01.authentication.repository.UserRepository;
import br.app.cashew.feature02.user.dto.user.input.UserUpdateInfoDTO;
import br.app.cashew.feature02.user.exception.CpfException;
import br.app.cashew.feature02.user.exception.EmailChangeRequestException;
import br.app.cashew.feature02.user.exception.universityuser.MaxQuantityOfUniversityAndCampusPreferencesReached;
import br.app.cashew.feature02.user.exception.universityuser.UniversityAndCampusNotRelatedException;
import br.app.cashew.feature02.user.exception.universityuser.UniversityAndCampusPreferenceAlreadyExists;
import br.app.cashew.feature02.user.model.EmailChangeRequest;
import br.app.cashew.feature02.user.repository.EmailChangeRequestRepository;
import br.app.cashew.feature02.user.service.email.EmailService;
import br.app.cashew.feature03.cafeteria.exception.campus.CampusDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.UniversityDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.model.Campus;
import br.app.cashew.feature03.cafeteria.model.University;
import br.app.cashew.feature03.cafeteria.model.universityuser.UniversityUser;
import br.app.cashew.feature03.cafeteria.model.universityuser.UniversityUserKey;
import br.app.cashew.feature03.cafeteria.repository.CampusRepository;
import br.app.cashew.feature03.cafeteria.repository.UniversityRepository;
import br.app.cashew.feature03.cafeteria.repository.UniversityUserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class UserAccountService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final EmailChangeRequestRepository emailChangeRequestRepository;
    private final UniversityUserRepository universityUserRepository;
    private final UniversityRepository universityRepository;
    private final CampusRepository campusRepository;

    @Autowired
    public UserAccountService(
            UserRepository userRepository,
            EmailService emailService,
            EmailChangeRequestRepository emailChangeRequestRepository,
            UniversityUserRepository universityUserRepository,
            UniversityRepository universityRepository,
            CampusRepository campusRepository) {

        this.userRepository = userRepository;
        this.emailService = emailService;
        this.emailChangeRequestRepository = emailChangeRequestRepository;
        this.universityUserRepository = universityUserRepository;
        this.universityRepository = universityRepository;
        this.campusRepository = campusRepository;
    }

    @Transactional
    public boolean updateNameAndCpfAndEmail(UserUpdateInfoDTO userUpdateInfoDTO, String userPublicKey) {
        User user = userRepository.findByUserPublicKey(UUID.fromString(userPublicKey))
                .orElseThrow(() -> new UserDoesNotExistsException("Usuario nao pode ser achado"));

        user.setName(userUpdateInfoDTO.getName());

        String newEmail = userUpdateInfoDTO.getEmail();
        String currentCpf = user.getCpf(); // Get the user's current CPF

        if (currentCpf == null) {
            // CPF is null in the user, set it if provided and validate
            String newCpf = userUpdateInfoDTO.getCpf();
            if (newCpf != null) {
                if (!(validateCpf(newCpf)) || !(isCpfAvailable(newCpf))) {
                    throw new CpfException("CPF inválido", "cpf");
                }
                user.setCpf(newCpf);
            }
        }

        // Check if the email is different and not already in use
        boolean isNewEmailAlreadyInUse = userRepository.existsUserByEmail(newEmail);

        if (!newEmail.equals(user.getEmail()) && !(isNewEmailAlreadyInUse)) {
            String pin = createEmailChangeRequest(user);
            emailService.sendEmailConfirmationEmail(newEmail, pin);
            userRepository.save(user);
            return true; // Email is different and not in use, sent confirmation email
        }
        userRepository.save(user);
        return false; // No changes or email is same/already in use
    }


    private boolean validateCpf(String cpf) {

        if (!(cpf.matches("^\\d{11}$"))) {
            return false;
        }

        if (cpf.equals("00000000000") || cpf.equals("11111111111") ||
                cpf.equals("22222222222") || cpf.equals("33333333333") ||
                cpf.equals("44444444444") || cpf.equals("55555555555") ||
                cpf.equals("66666666666") || cpf.equals("77777777777") ||
                cpf.equals("88888888888") || cpf.equals("99999999999") ||
                (cpf.length() != 11)) {
            return false;
        }

        char dig10;
        char dig11;
        int sm;
        int i;
        int r;
        int num;
        int peso;

        try {
            sm = 0;
            peso = 10;

            for (i=0; i<9; i++) {
                num = cpf.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig10 = '0';
            }
            else {
                dig10 = (char) (r + 48); // converte no respectivo caractere numerico
            }

            // Calculo do 2.º. Digito Verificador
            sm = 0;
            peso = 11;

            for(i=0; i<10; i++) {
                num = cpf.charAt(i) - 48;
                sm = sm + (num * peso);
                peso = peso - 1;
            }

            r = 11 - (sm % 11);
            if ((r == 10) || (r == 11)) {
                dig11 = '0';
            }
            else {
                dig11 = (char) (r + 48);
            }

            // Verifica se os digitos calculados conferem com os digitos informados.

            return (dig10 == cpf.charAt(9)) && (dig11 == cpf.charAt(10));
        }
        catch (InputMismatchException erro) {
                return false;
        }
    }

    private boolean isCpfAvailable(String cpf) {
        return !(userRepository.existsByCpf(cpf));
    }

    private String createEmailChangeRequest(User user) {
        SecureRandom secureRandom = new SecureRandom();

        String pin = Integer.toString(secureRandom.nextInt(100000, 999999));

        EmailChangeRequest emailChangeRequest = new EmailChangeRequest();
        emailChangeRequest.setPin(pin);
        emailChangeRequest.setTime(Calendar.getInstance());
        emailChangeRequest.setUser(user);
        emailChangeRequest.setEmail(user.getEmail());
        emailChangeRequestRepository.save(emailChangeRequest);
        return pin;
    }

    public Map<String, String> getUserInfo(String userPublicKey) {

        User user = userRepository.findByUserPublicKey(UUID.fromString(userPublicKey)).
                orElseThrow( () -> new UserDoesNotExistsException("User is invalid"));

        Map<String, String> info = new HashMap<>();

        info.put("name", user.getName());
        info.put("cpf", user.getCpf());
        info.put("email", user.getEmail());

        return info;
    }

    public boolean validateEmailChangeRequest(String pin, UUID userPublicKey) {

        User user = userRepository.findByUserPublicKey(userPublicKey)
                .orElseThrow(() -> new UserDoesNotExistsException("Usuario invalido"));


        EmailChangeRequest emailChangeRequest = emailChangeRequestRepository.findByPinAndUser(pin, user)
                .orElseThrow(() -> new EmailChangeRequestException("Solicitacao de mudança de email invalida", "email"));

        return !(emailChangeRequest.getTime().toInstant().plus(10, ChronoUnit.MINUTES).isBefore(Instant.now()));
    }

    @Transactional
    public void updateEmail(UUID userPublicKey, String email) {
        userRepository.updateEmailByUserPublicKey(userPublicKey, email);
    }

    public List<UniversityUser> getUniversityAndCampusPreferences(String userPublicKey) {

        UUID uuid = UUID.fromString(userPublicKey);

        User user = userRepository.findByUserPublicKey(uuid)
                .orElseThrow(() -> new UserDoesNotExistsException("Usuario nao foi possivel ser achado"));

        return universityUserRepository.findByUniversityUserIDUserID(user.getUserID());
    }

    public void addCpf(String cpf, String userPublicKey) {
        User user = userRepository.findByUserPublicKey(UUID.fromString(userPublicKey))
                .orElseThrow(() -> new UserDoesNotExistsException("Usuario nao existe"));

        if (cpf != null && validateCpf(cpf) && isCpfAvailable(cpf)) {
            user.setCpf(cpf);
        }
        user.setCpf(cpf);
        userRepository.save(user);
    }
    public UniversityUser addUniversityAndCampusPreferences(String universityPublicKey, String campusPublicKey, String userPublicKey) {
        // ver se o usuario nao excedeu o limite de preferencias salvas
        User user = userRepository.findByUserPublicKey(UUID.fromString(userPublicKey))
                .orElseThrow(() -> new UserDoesNotExistsException("Usuario nao existe"));

        if (isMaxQuantityOfUniversityAndCampusPreferencesReached(user.getUserID())) {
            throw new MaxQuantityOfUniversityAndCampusPreferencesReached(
                    "Nao e possivel completar o pedido. Numero maximo de preferencias de universidade e campus excedida");
        }
        University university = universityRepository.findByUniversityPublicKey(UUID.fromString(universityPublicKey))
                .orElseThrow(() -> new UniversityDoesNotExistsException("Universidade invalida", "university"));

        Campus campus = campusRepository.findByPublicKey(UUID.fromString(campusPublicKey))
                .orElseThrow(() -> new CampusDoesNotExistsException("Campus invalido", "campus"));

        // checar se a universidade e campus estao relacionados
        if (!(isUniversityAndCampusRelated(university, campus))) {
            throw new UniversityAndCampusNotRelatedException("Universidade e campus invalidos. Universidade e campus passados nao sao relacionados");
        }
        // verificar se ele nao esta tentando adicionar uma universidade e ‘campus’ que ja esta adicionada
        if (isUniversityAndCampusAlreadySavedByUser(user, university, campus)) {
            throw new UniversityAndCampusPreferenceAlreadyExists("Universidade e campus ja estao adicionados");
        }

        UniversityUser userPreferences = new UniversityUser();
        UniversityUserKey universityUserKey = new UniversityUserKey();
        universityUserKey.setUniversityID(university.getUniversityID());
        universityUserKey.setUserID(user.getUserID());
        userPreferences.setUniversityUserID(universityUserKey);
        userPreferences.setUser(user);
        userPreferences.setUniversity(university);
        userPreferences.setCampus(campus);
        universityUserRepository.save(userPreferences);
        return userPreferences;
    }

    private boolean isMaxQuantityOfUniversityAndCampusPreferencesReached(int userID) {
        int universityAndCampusPreferencesQuantity = universityUserRepository.countByUniversityUserIDUserID(userID);

        return universityAndCampusPreferencesQuantity > 10;
    }

    private boolean isUniversityAndCampusRelated(University university, Campus campus) {
        return campus.getUniversity().getUniversityID() == university.getUniversityID();
    }

    private boolean isUniversityAndCampusAlreadySavedByUser(User user, University university, Campus campus) {
        return universityUserRepository.existsByUserAndUniversityAndCampus(user, university, campus);
    }
}