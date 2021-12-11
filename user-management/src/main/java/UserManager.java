import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import static util.Constants.FORGOT_PASSWORD;
import static util.Constants.SIGNUP;

public class UserManager implements IUserManager {
    private ArrayList<User> users;
    HashMap<String, String> tokens;
    protected UserRepository userRepository = UserRepository.INSTANCE;
    protected EmailGateway gateway = EmailGateway.INSTANCE;

    public UserManager() {
        users = userRepository.loadUsers();
        tokens = userRepository.loadTokens();
    }

    public boolean signup(String email, String name, String password) {
        String encryptedPw = encryptPassword(password);

        for (User user : users) {
            if (user.getEmail().equals(email)) {
                //user already there
                return false;
            }
        }

        User newUser = new User(name, email, encryptedPw);

        saveUser(newUser);
        // send email for verification
        String token = generateToken();
        saveToken(email, token);
        sendVerificationEmail(newUser, token, SIGNUP);
        return true;
    }

    private void saveUser(User user) {
        users.add(user);
        userRepository.saveUser(user);
    }

    // used for both signups and forgot password processes to send a verification email to user using gateway
    // and set the record as valid
    private void sendVerificationEmail(User newUser, String token, String type) {
        gateway.send(newUser.getEmail(), newUser.getName(), token, type);
    }

    private String generateToken() {
        String SALTCHARS = "abcdefghijklmnopqrstuvwxyzABCDEFGHJKMNPQRSTVWXYZ0123456789";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 10) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        return salt.toString();
    }

    // used when the user clicks on the validation link in the email
    public boolean validateUser(String email, String token) {
        if (!token.equals(tokens.get(email))) {
            return false;
        }
        Optional<User> user = findUserByEmail(email);
        if (!user.isPresent()) {
            return false;
        }
        user.get().setValidated(true);
        return true;
    }

    public boolean forgotPassword(String email) {
        //validate that the user exists
        Optional<User> user = findUserByEmail(email);
        if (!user.isPresent()) {
            return false;
        }
        String token = generateToken();
        saveToken(email, token);
        //sending verification token
        sendVerificationEmail(user.get(), token, FORGOT_PASSWORD);
        return true;
    }

    //used after forgot password
    public boolean setNewPassword(String email, String password, String token) {
        //validate that the user exists
        if (!tokens.get(email).equals(token)) {
            return false;
        }
        String encryptedPw = encryptPassword(password);
        Optional<User> user = findUserByEmail(email);
        if (!user.isPresent()) {
            return false;
        }
        user.get().setPassword(encryptedPw);
        return true;
    }

    private void saveToken(String email, String token) {
        userRepository.saveToken(tokens, email, token);
        tokens.put(email, token);
    }

    public boolean changePassword(String email, String oldPass, String newPass) {
        //validate that it's the correct user
        Optional<User> user = findUser(email, oldPass);
        if (!user.isPresent()) {
            return false;
        }

        String encryptedNewPass = encryptPassword(newPass);

        user.get().setPassword(encryptedNewPass);
        return true;
    }

    private Optional<User> findUserByEmail(String email) {
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    private Optional<User> findUser(String email, String password) {
        String encryptedPw = encryptPassword(password);

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(encryptedPw)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean authenticateUser(String email, String password) {
        String encryptedPw = encryptPassword(password);

        for (User user : users) {
            if (user.getEmail().equals(email) && user.getPassword().equals(encryptedPw)) {
                return true;
            }
        }
        return false;
    }

    private String encryptPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    // for testing
    protected void setGateway(EmailGateway gateway) {
        this.gateway = gateway;
    }
}
