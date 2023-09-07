package cinema.util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public final class PasswordHashing {

    public static String[] hashPassword(String pass, String salt) {
        SecureRandom random = new SecureRandom();
        byte[] randomSalt;
        if (salt == null) {
            randomSalt = new byte[16];
            random.nextBytes(randomSalt);
        } else {
            randomSalt = Base64.getDecoder().decode(salt);
        }
        KeySpec spec = new PBEKeySpec(pass.toCharArray(), randomSalt, 65536, 128);
        byte[] hash = new byte[0];
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            hash = factory.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            System.out.println(e.getMessage());
        }
        return new String[] {Base64.getEncoder().encodeToString(hash),
                Base64.getEncoder().encodeToString(randomSalt)};
    }
}
