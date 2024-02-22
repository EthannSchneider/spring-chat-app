package ch.shkermit.tpi.chatapp.utils;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;

import java.io.*;

public class SecretGeneratorUtils {
    private static final String KEY_FILE = System.getProperty("user.home") + "/.chatapp-db/secret.key";

    public static SecretKey generateSecretKey() {
        try {
            if (isSecretKeyAvailable()) {
                return loadSecretKey();
            }

            SecretKey generatedSecretKey = Jwts.SIG.HS256.key().build(); 
        
            saveSecretKey(generatedSecretKey);
            return generatedSecretKey;
        }catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Error while generating secret key", e);
        }
    }

    public static void saveSecretKey(SecretKey secretKey) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(KEY_FILE))) {
            oos.writeObject(secretKey);
        }
    }

    public static SecretKey loadSecretKey() throws IOException, ClassNotFoundException {
        File file = new File(KEY_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(KEY_FILE))) {
                return (SecretKey) ois.readObject();
            }
        }
        return null;
    }

    public static boolean isSecretKeyAvailable() {
        File file = new File(KEY_FILE);
        return file.exists();
    }
}
