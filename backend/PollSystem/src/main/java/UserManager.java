
import model.User;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

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

    public boolean userAuthentication(String email, String password) {
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
