package br.app.cashew.global.exception;

import br.app.cashew.feature01.authentication.exception.PasswordChangeRequestException;
import br.app.cashew.feature01.authentication.exception.RefreshTokenIsInvalidException;
import br.app.cashew.feature01.authentication.exception.email.EmailAlreadyExistsException;
import br.app.cashew.feature01.authentication.exception.email.EmailNotFoundException;
import br.app.cashew.feature01.authentication.exception.fingerprint.FingerprintDoesNotExists;
import br.app.cashew.feature01.authentication.exception.user.UserCouldNotBeFoundException;
import br.app.cashew.feature01.authentication.exception.user.UserDoesNotExistsException;
import br.app.cashew.feature02.user.exception.CpfException;
import br.app.cashew.feature02.user.exception.EmailChangeRequestException;
import br.app.cashew.feature02.user.exception.universityuser.MaxQuantityOfUniversityAndCampusPreferencesReached;
import br.app.cashew.feature02.user.exception.universityuser.UniversityAndCampusNotRelatedException;
import br.app.cashew.feature02.user.exception.universityuser.UniversityAndCampusPreferenceAlreadyExists;
import br.app.cashew.feature03.cafeteria.exception.CafeteriaDoesNotExistException;
import br.app.cashew.feature03.cafeteria.exception.campus.CampusAlreadyExistsException;
import br.app.cashew.feature03.cafeteria.exception.campus.CampusDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.UniversityAlreadyExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.UniversityDoesNotExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.cnpj.CnpjAlreadyExistsException;
import br.app.cashew.feature03.cafeteria.exception.university.cnpj.CnpjInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY = "errors";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<Map<String, Map<String, String>>> handleConstraintViolationException(MethodArgumentNotValidException ex) {
        Map<String, Map<String, String>> response = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        response.put(ERROR_KEY, errors);

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleUserAlreadyExistsException(EmailAlreadyExistsException ex) {

        Map<String, Map<String, String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonMap(ex.getField(), ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(EmailNotFoundException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleEmailNotFoundException(EmailNotFoundException ex) {

        Map<String, Map<String, String>> errors = Collections.singletonMap(ERROR_KEY, Collections.singletonMap(ex.getField(), ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.OK);
    }

    @ExceptionHandler(FingerprintDoesNotExists.class)
    protected ResponseEntity<Map<String, List<String>>> handleFingerPrintDoesNotExists(FingerprintDoesNotExists ex) {

        Map<String, List<String>> errors = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(BadCredentialsException.class)
    protected ResponseEntity<Map<String, Map<String, String>>> handleBadCredentialsException(BadCredentialsException ex) {

        Map<String, Map<String, String>> errors = Collections.singletonMap(ERROR_KEY, Collections.singletonMap("password", ex.getMessage()));
        return new ResponseEntity<>(errors, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RefreshTokenIsInvalidException.class)
    protected ResponseEntity<Map<String, Map<String, String>>> handleRefreshTokenInsInvalidException(RefreshTokenIsInvalidException ex) {

        Map<String, Map<String, String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonMap(ex.getField(), ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(UserDoesNotExistsException.class)
    protected ResponseEntity<Map<String, List<String>>> handleUserDoesNotExists(UserDoesNotExistsException ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CpfException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleCpfException(CpfException ex) {

        Map<String, Map<String, String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonMap(ex.getField(), ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailChangeRequestException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleEmaiLChangeRequestException(EmailChangeRequestException ex) {

        Map<String, Map<String, String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonMap(ex.getField(), ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({UserCouldNotBeFoundException.class, MaxQuantityOfUniversityAndCampusPreferencesReached.class, UniversityAndCampusNotRelatedException.class})
    public ResponseEntity<Map<String, List<String>>> handleBadRequestExceptions(RuntimeException ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PasswordChangeRequestException.class)
    public ResponseEntity<Map<String, List<String>>> handlePasswordChangeRequestException(PasswordChangeRequestException ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.GONE);
    }

    @ExceptionHandler(UniversityDoesNotExistsException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleInstitutionDoesNotExistsException(UniversityDoesNotExistsException ex) {

        Map<String, Map<String, String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonMap(ex.getField(), ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Void> handleMissingServletRequestParameterException() {

        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CnpjInvalidException.class})
    public ResponseEntity<Map<String, List<String>>> handleInstitutionDoesNotExistsException(CnpjInvalidException ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({CnpjAlreadyExistsException.class})
    public ResponseEntity<Map<String, List<String>>> handleCnpjAlreadyExistsException(CnpjAlreadyExistsException ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler(CafeteriaDoesNotExistException.class)
    public ResponseEntity<Map<String, List<String>>> handleCafeteriaDoesNotExistException(CafeteriaDoesNotExistException ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CampusAlreadyExistsException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleCampusAlreadyExists(CampusAlreadyExistsException ex) {

        Map<String, Map<String, String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonMap(ex.getMessage(), ex.getField()));

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(CampusDoesNotExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleCampusDoesNotExists(CampusDoesNotExistsException ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UniversityAlreadyExistsException.class)
    public ResponseEntity<Map<String, List<String>>> handleUniversityAlreadyExistsException(UniversityAlreadyExistsException ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Map<String, String>>> handleIllegalArgumentException(IllegalArgumentException ex) {

        Map<String, Map<String, String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonMap("publicKey",ex.getMessage()));

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UniversityAndCampusPreferenceAlreadyExists.class)
    public ResponseEntity<Map<String, List<String>>> handleUniversityAndCampusPreferenceAlreadyExists(UniversityAndCampusPreferenceAlreadyExists ex) {

        Map<String, List<String>> response = Collections.singletonMap(ERROR_KEY, Collections.singletonList(ex.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
        // TODO refatorar nome de metodos @ExceptionHandlers
    }
}
// TODO refatorar e ver se cada excecao esta sendo retornada com um HttpStatus semantico
