import com.google.gson.Gson;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;

import static util.Constants.TOKENS_FILEPATH;
import static util.Constants.USERS_FILEPATH;

public class UserRepository {

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
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = new FileInputStream(USERS_FILEPATH)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray list = (JSONArray) jsonObject.get("listOfUsers");
            String jsonInString = new Gson().toJson(user);
            JSONObject userJSON = (JSONObject) jsonParser.parse(jsonInString);
            list.add(userJSON);

            FileWriter file = new FileWriter(new File(USERS_FILEPATH));
            jsonObject.put("listOfUsers", list);
            file.write(jsonObject.toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveToken(HashMap<String, String> tokens, String email, String token) {
        JSONParser jsonParser = new JSONParser();

        try (InputStream is = new FileInputStream(TOKENS_FILEPATH)) {
            JSONObject jsonObject = (JSONObject) jsonParser.parse(new InputStreamReader(is, StandardCharsets.UTF_8));
            JSONArray list = (JSONArray) jsonObject.get("tokens");
            if (tokens.get(email) != null) {
                overwriteToken(email, token, list);
            } else {
                JSONObject tokenJSON = new JSONObject();
                tokenJSON.put("email", email);
                tokenJSON.put("token", token);
                list.add(tokenJSON);
            }
            FileWriter file = new FileWriter(new File(TOKENS_FILEPATH));
            jsonObject.put("tokens", list);
            file.write(jsonObject.toJSONString());
            file.flush();
            file.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
