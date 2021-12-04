import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;

public class UserManager {
    private ArrayList<User> listOfUsers;

    public UserManager() {
        listOfUsers = new ArrayList<>();
        loadListOfUsers();
    }

    public void loadListOfUsers() {

        String filePath = "authentication/users.json";
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject;

        try (InputStream is = this.getClass().getResourceAsStream(filePath)) {
            Object obj = jsonParser.parse(new InputStreamReader(is, "UTF-8"));
            jsonObject = (JSONObject) obj;
            JSONArray list = (JSONArray) jsonObject.get("listOfUsers");

            for (Object o : list) {
                JSONObject user = (JSONObject) o;
                String userID = (String) user.get("userID");
                String name = (String) user.get("name");
                String email = (String) user.get("email");
                String password = (String) user.get("password");
                listOfUsers.add(new User(userID, name, email, password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean signup(String email, String name, String password) {
        String encryptedPw = encryptPassword(password);

        for (User user : listOfUsers) {
            if (user.getEmail().equals(email)) {
                //user already there
                return false;
            }
        }

        User newUser = new User("", name, email, encryptedPw);
        //TODO:
        // append to JSON
        // generate userID
        // send email for verification
        return verifyEmail(newUser);
    }

    // used for both signups and forgot password processes to send a verification email to user using gateway
    // and set the record as valid
    public boolean verifyEmail(User newUser) {
        //TODO: use gateway to send email
        newUser.setValid(true);
        return true;
    }

    public boolean forgotPassword(String email, String oldPass) {
        //validate that it's the correct user
        Optional<User> user = findUser(email, oldPass);
        if (!user.isPresent()) {
            return false;
        }

        //sending verification token
        if (!verifyEmail(user.get())) {
            return false;
        }
        //TODO: do we get the new password from the user after they verify?
        //user.setPassword(encryptedNewPass);
        return true;
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

    public Optional<User> findUser(String email, String password) {
        String encryptedPw = encryptPassword(password);

        for (User user : listOfUsers) {
            if (user.getEmail().equals(email) && user.getPassword().equals(encryptedPw)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    public boolean authenticateUser(String email, String password) {
        String encryptedPw = encryptPassword(password);

        for (User user : listOfUsers) {
            if (user.getEmail().equals(email) && user.getPassword().equals(encryptedPw)) {
                return true;
            }
        }
        return false;
    }

    public String encryptPassword(String password) {
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
}
