package sec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class SecurityService {
    public static String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] hashedBytes = messageDigest.digest(password.getBytes());
        StringBuilder hexString = new StringBuilder();
        for (byte b : hashedBytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public static boolean performMFA() {
        Random random = new Random();
        int mfaCode = 100000 + random.nextInt(900000);
        System.out.println("[DEBUG] MFA code generated: " + mfaCode);

        System.out.println("\nA Multi-Factor Authentication code has been sent to your registered device.");
        System.out.print("Enter the MFA code: ");
        int userCode = new java.util.Scanner(System.in).nextInt();

        return userCode == mfaCode;
    }
}