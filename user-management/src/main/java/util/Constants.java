package util;

import java.io.InputStream;
import java.util.Properties;

public class Constants {
    private static Properties props = new Properties();
    static {
        try {
            InputStream is = Constants.class.getClassLoader().getResourceAsStream("path.properties");
            props.load(is);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static final String USERS_FILEPATH = props.getProperty("users");
    public static final String TOKENS_FILEPATH = props.getProperty("tokens");
    public static final int NULL_PARAMETER = -1;
    public static final int SUCCESS = 0;
    public static final String FORGOT_PASSWORD = "forgot_pass";
    public static final String SIGNUP = "signup";
}
