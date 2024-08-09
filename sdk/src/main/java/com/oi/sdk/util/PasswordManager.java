package com.oi.sdk.util;

import org.bouncycastle.crypto.generators.SCrypt;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

import java.security.Security;
import java.util.Arrays;

/**
 * @author Vvek
 */
public class PasswordManager {

    private static final byte[] SALT = "2018-OmegaIntelligence!2024".getBytes();

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    /**
     * Encrypt the password using SCrypt
     */
    public static String encrypt(String password) {
        // Generate the hashed password
        byte[] hashedPassword = SCrypt.generate(
                password.getBytes(),
                SALT,
                16384, // CPU/memory cost factor
                8,     // Block size
                1,     // Parallelization factor
                32     // Desired key length in bytes
        );

        // Convert the hashed password to hex format for storage
        return Hex.toHexString(hashedPassword);
    }


    /**
     * Authenticating a password
     */
    public static boolean authenticate(String password, String encryptedPassword) {

        // Convert the stored hashed password from hex format to bytes (optional)
        byte[] storedHashedPassword = Hex.decode(encryptedPassword);

        // Generate the hashed password using the provided password and stored salt
        byte[] hashedPassword = SCrypt.generate(
                password.getBytes(),
                SALT,
                16384, // CPU/memory cost factor (must match the value used during storage)
                8,     // Block size (must match the value used during storage)
                1,     // Parallelization factor (must match the value used during storage)
                32     // Desired key length in bytes (must match the value used during storage)
        );

        // Compare the generated hashed password with the stored hashed password
        return Arrays.equals(hashedPassword, storedHashedPassword);
    }
}
