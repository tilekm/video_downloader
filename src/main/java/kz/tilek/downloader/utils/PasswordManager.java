package kz.tilek.downloader.utils;


import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;


public class PasswordManager {
    public static String hashPassword(String password) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(32,64,1,15*1024,2);
        return encoder.encode(password);
    }

    public static boolean checkPassword(String password, String hashedPassword) {
        Argon2PasswordEncoder encoder = new Argon2PasswordEncoder(32,64,1,15*1024,2);
        return encoder.matches(password, hashedPassword);
    }
}
