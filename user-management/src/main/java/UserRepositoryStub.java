import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

public class UserRepositoryStub extends UserRepository {

    private static final String USERS_FILEPATH = "src/test/users.json";
    private static final String TOKENS_FILEPATH = "src/test/tokens.json";

    public HashMap<String, String> loadTokens() {
        HashMap<String, String> tokens = new HashMap<>();
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
        return tokens;
    }

    public ArrayList<User> loadUsers() {
        ArrayList<User> users = new ArrayList<>();
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
                users.add(new User(name, email, password));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }

    public void saveUser(User user) {
        //Don't want to save during tests
    }

    public void saveToken(HashMap<String, String> tokens, String email, String token) {
        //Don't want to save during tests
    }
}
