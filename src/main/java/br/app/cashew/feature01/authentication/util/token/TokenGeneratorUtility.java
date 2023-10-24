package br.app.cashew.feature01.authentication.util.token;

import br.app.cashew.feature01.authentication.exception.fingerprint.FingerprintHashingException;
import jakarta.xml.bind.DatatypeConverter;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;

public class TokenGeneratorUtility {


    private TokenGeneratorUtility() {
        throw new IllegalStateException("Utility class can't be instantiated");
    }

    public static String generateToken() {

        // Random data generator
        SecureRandom secureRandom = new SecureRandom();

        //Generate a random string that will constitute the fingerprint for this user
        byte[] randomFgp = new byte[50];
        secureRandom.nextBytes(randomFgp);

        return DatatypeConverter.printHexBinary(randomFgp);
    }

    public static String hashToken(String userFingerprint) {

        //Compute a SHA256 hash of the fingerprint in order to store the
        //fingerprint hash (instead of the raw value) in the token
        //to prevent an XSS to be able to read the fingerprint and
        //set the expected cookie itself

        byte[] userFingerprintDigest;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            userFingerprintDigest = digest.digest(userFingerprint.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new FingerprintHashingException("Could not hash the user fingerprint");
        }

        return DatatypeConverter.printHexBinary(userFingerprintDigest);
    }

    public static boolean validateToken(String userFingerprint, String jwtUserFingerprint) {

        byte[] userFingerprintDigest;
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            userFingerprintDigest = digest.digest(userFingerprint.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            throw new FingerprintHashingException("Could not hash the user fingerprint");
        }
        String userFingerprintHashed = DatatypeConverter.printHexBinary(userFingerprintDigest);

        return userFingerprintHashed.equals(jwtUserFingerprint);
    }
}
