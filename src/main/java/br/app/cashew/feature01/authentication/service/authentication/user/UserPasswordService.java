package br.app.cashew.feature01.authentication.service.authentication.user;

import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature01.authentication.model.PasswordChangeRequest;
import br.app.cashew.feature01.authentication.repository.PasswordChangeRequestRepository;
import br.app.cashew.feature01.authentication.repository.RefreshTokenRepository;
import br.app.cashew.feature01.authentication.util.token.TokenGeneratorUtility;
import br.app.cashew.feature01.authentication.exception.user.UserCouldNotBeFoundException;
import br.app.cashew.feature01.authentication.model.user.User;
import br.app.cashew.feature01.authentication.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserPasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordChangeRequestRepository passwordChangeRequestsRepository;

    @Autowired
    public UserPasswordService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            RefreshTokenRepository refreshTokenRepository,
            PasswordChangeRequestRepository passwordChangeRequestsRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordChangeRequestsRepository = passwordChangeRequestsRepository;
    }

    @Transactional
    public void changePassword(String newPassword, UUID userPublicKey) {

        Optional<User> user = userRepository.findByUserPublicKey(userPublicKey);

        if (user.isEmpty()) {
            throw new UserDoesNotExistsException("User is invalid");
        }

        userRepository.updatePasswordByUserPublicKey(userPublicKey, passwordEncoder.encode(newPassword));

        // remove todos os registros da tab refresh token que estao vinculados ao usuario autenticado atual
        refreshTokenRepository.deleteAllByUser(user.get());
    }

    public boolean validatePasswordChangeRequest(String hashedPasswordChangeRequestToken) {

        Optional<PasswordChangeRequest> passwordChangeRequest = passwordChangeRequestsRepository.findByToken(hashedPasswordChangeRequestToken);

        return
                passwordChangeRequest.isPresent() &&
                        passwordChangeRequest.get().getTime().toInstant().plus(24, ChronoUnit.HOURS).isAfter(Instant.now());
    }

    public boolean validatePasswordChangeRequestCreationAttempt(User user) {

        Optional<PasswordChangeRequest> passwordChangeRequest = passwordChangeRequestsRepository.findFirstByUserOrderByTimeDesc(user);

        return passwordChangeRequest.isEmpty() || passwordChangeRequest.get().getTime().toInstant().plus(24, ChronoUnit.HOURS).isBefore(Instant.now());
    }

    public String generatePasswordChangeRequest(User user) {
        PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest();

        String token = TokenGeneratorUtility.generateToken();
        passwordChangeRequest.setToken(TokenGeneratorUtility.hashToken(token));

        passwordChangeRequest.setTime(Calendar.getInstance());

        passwordChangeRequest.setUser(user);

        passwordChangeRequestsRepository.save(passwordChangeRequest);

        return token;
    }

    public boolean validateOldPassword(String oldPassword, UUID userPublicKey) {

        return passwordEncoder.matches(oldPassword, userRepository.findPasswordByUserPublicKey(userPublicKey));
    }

    public User getUserByPasswordChangeRequest(String passwordChangeRequestToken) {

        Optional<User> user = passwordChangeRequestsRepository.findUserByToken(passwordChangeRequestToken);

        if (passwordChangeRequestsRepository.findUserByToken(passwordChangeRequestToken).isPresent()) {

            return user.get();
        }

        throw new UserCouldNotBeFoundException("Could not find user by \"passwordChangeRequestToken\"");
    }

    public void invalidatePasswordChangeRequest(String passwordChangeRequestToken) {
        passwordChangeRequestsRepository.updateTokenByToken(passwordChangeRequestToken);
    }
}
// TODO refatorar classe de maneira a reutilizar logica para implementar metodos voltado para entidade "Partner"