import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Optional;

import static util.Constants.USERS_FILEPATH;

public class UserManager implements IUserManager {
    private ArrayList<User> listOfUsers;

    public UserManager() {
        loadListOfUsers();
    }

    public void loadListOfUsers() {
        listOfUsers = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = this.getClass().getResourceAsStream(USERS_FILEPATH)) {
            Object obj = jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray list = (JSONArray) jsonObject.get("listOfUsers");

            for (Object o : list) {
                JSONObject user = (JSONObject) o;
                String name = (String) user.get("name");
                String email = (String) user.get("email");
                String password = (String) user.get("password");
                listOfUsers.add(new User(name, email, password));
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

        User newUser = new User(name, email, encryptedPw);

        saveUserToJson(newUser);
        // send email for verification
        sendVerificationEmail(newUser);
        return true;
    }

    public void saveUserToJson(User user) {
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = this.getClass().getResourceAsStream(USERS_FILEPATH)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray list = (JSONArray) jsonObject.get("listOfUsers");
            String jsonInString = new Gson().toJson(user);
            JSONObject userJSON = (JSONObject) jsonParser.parse(jsonInString);
            list.add(userJSON);

            //TODO: doesn't work, is writing to target file instead of resources file
            FileWriter file = new FileWriter(new File(this.getClass().getResource(USERS_FILEPATH).getPath()));
            jsonObject.put("listOfUsers", list);
            file.write(jsonObject.toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // used for both signups and forgot password processes to send a verification email to user using gateway
    // and set the record as valid
    private void sendVerificationEmail(User newUser) {
        EmailGateway.INSTANCE.send(newUser.getEmail());
    }

    // used when the user clicks on the validation link in the email
    public void validateUser(String email, String password) {
        Optional<User> user = findUser(email, password);
        if (!user.isPresent()) {
            throw new IllegalStateException("User not found.");
        }
        user.get().setValidated(true);
    }

    public boolean forgotPassword(String email, String oldPass) {
        //validate that it's the correct user
        Optional<User> user = findUser(email, oldPass);
        if (!user.isPresent()) {
            return false;
        }

        //sending verification token
        sendVerificationEmail(user.get());

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

    private Optional<User> findUser(String email, String password) {
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
}
