import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;
import java.util.Random;

import static util.Constants.TOKENS_FILEPATH;
import static util.Constants.USERS_FILEPATH;

public class UserManager implements IUserManager {
    private ArrayList<User> listOfUsers;
    HashMap<String, String> tokens;

    protected EmailGateway gateway = EmailGateway.INSTANCE;

    public UserManager() {
        loadListOfUsers();
        loadTokens();
    }

    public void loadListOfUsers() {
        listOfUsers = new ArrayList<>();
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = new FileInputStream(USERS_FILEPATH)) {
            Object obj = jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray list = (JSONArray) jsonObject.get("listOfUsers");

            for (Object o : list) {
                JSONObject user = (JSONObject) o;
                if (user.get("email") == null) continue;
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
        String token = generateToken();
        saveToken(email, token);
        sendVerificationEmail(newUser, token);
        return true;
    }

    private void saveUserToJson(User user) {
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = new FileInputStream(USERS_FILEPATH)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray list = (JSONArray) jsonObject.get("listOfUsers");
            String jsonInString = new Gson().toJson(user);
            JSONObject userJSON = (JSONObject) jsonParser.parse(jsonInString);
            list.add(userJSON);

            //TODO: doesn't work, is writing to target file instead of resources file
            FileWriter file = new FileWriter(new File(USERS_FILEPATH));
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
    private void sendVerificationEmail(User newUser, String token) {
        gateway.send(newUser.getEmail(), newUser.getName(), token);
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
    public void validateUser(String email, String password) {
        Optional<User> user = findUser(email, password);
        if (!user.isPresent()) {
            throw new IllegalStateException("User not found.");
        }
        user.get().setValidated(true);
    }

    public boolean forgotPassword(String email) {
        //validate that the user exists
        Optional<User> user = findUserByEmail(email);
        if (!user.isPresent()) {
            return false;
        }
        // check if there's already a token for this user
        String token = generateToken();
        saveToken(email, token);

        //sending verification token
        sendVerificationEmail(user.get(), token);
        return true;
    }

    //used after forgot password
    private boolean setNewPassword(String email, String password) {
        //validate that the user exists
        String encryptedPw = encryptPassword(password);
        Optional<User> user = findUserByEmail(email);
        if (!user.isPresent()) {
            return false;
        }
        user.get().setPassword(encryptedPw);
        return true;
    }

    private void overwriteToken(String email, String token, JSONArray list) {
        for (Object o : list) {
            JSONObject user = (JSONObject) o;
            if (user.get("email").equals(email)) {
                user.put("token", token);
                return;
            }
        }
    }

    private void saveToken(String email, String token) {
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = new FileInputStream(TOKENS_FILEPATH)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray list = (JSONArray) jsonObject.get("tokens");
            if (tokenExists(email)) {
                overwriteToken(email, token, list);
            } else {
                JSONObject tokenJSON = new JSONObject();
                tokenJSON.put("email", email);
                tokenJSON.put("token", token);
                list.add(tokenJSON);
            }
            //TODO: doesn't work, is writing to target file instead of resources file
            FileWriter file = new FileWriter(new File(TOKENS_FILEPATH));
            jsonObject.put("tokens", list);
            file.write(jsonObject.toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private boolean tokenExists(String email) {
        return tokens.get(email) != null;
    }

    public void loadTokens() {
        tokens = new HashMap<>();
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = new FileInputStream(TOKENS_FILEPATH)) {
            Object obj = jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONObject jsonObject = (JSONObject) obj;
            JSONArray list = (JSONArray) jsonObject.get("tokens");

            for (Object o : list) {
                JSONObject user = (JSONObject) o;
                if (user.get("token") == null) continue;
                String token = (String) user.get("token");
                String email = (String) user.get("email");
                tokens.put(email, token);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        for (User user : listOfUsers) {
            if (user.getEmail().equals(email)) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
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

    // for testing
    protected void setGateway(EmailGateway gateway) {
        this.gateway = gateway;
    }
}
