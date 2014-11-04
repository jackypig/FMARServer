package services;

import models.User;
import play.Logger;
import util.ExceptionUtil;

import java.security.MessageDigest;

/**
 * User: Ling Hung
 * Comp: XappMedia, Inc.
 * Proj: xapp-server
 * Date: 10/29/14
 * Time: 10:58 PM
 */
public class AuthenticateService {
    public boolean authenticate (String email, String password) {
        Logger.info("Authenticating: " + email);
        User user = User.findByEmail(email);
        if (user == null) {
            Logger.warn("Invalid username: " + email);
            return false;
        }

        String encryptedPassword = encrypt(password);
        if (!user.passwordEncrypted.equals(encryptedPassword)) {
            Logger.warn("Invalid password for email: " + email + " input: " + encryptedPassword + " value: " + user.passwordEncrypted);
            return false;
        }

        return true;
    }

    //Create a one-way password hash using this post:
    //  http://stackoverflow.com/questions/304268/getting-a-files-md5-checksum-in-java
    public String encrypt (String password) {
        byte [] hashed = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            hashed = md.digest(password.getBytes("UTF-8"));
            StringBuilder hashedString = new StringBuilder();
            for (int i=0;i<hashed.length;i++) {
                int byteAsInt = hashed[i] & 0xff;
                String hex = Integer.toString(byteAsInt, 16);
                //Logger.info("Byte: " + hashed[i] + " hex: " + hex);
                if (i > 0) {
                    hashedString.append("-");
                }
                hashedString.append(hex);
            }
            return hashedString.toString();
        } catch (Exception e) {
            throw ExceptionUtil.rethrowError(e);
        }
    }
}
