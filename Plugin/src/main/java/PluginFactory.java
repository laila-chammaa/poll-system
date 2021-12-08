import java.io.InputStream;
import java.util.Properties;

public class PluginFactory {
    private static Properties props = new Properties();

    static {
        try {
            InputStream is = PluginFactory.class.getClassLoader().getResourceAsStream("plugins.properties");
            props.load(is);
        } catch (Exception ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Object getPlugin(Class iface) {
        String implName = props.getProperty(iface.getName());
        if (implName == null) {
            throw new RuntimeException("implementation not specified for " +
                    iface.getName() + " in PluginFactory properties.");
        }
        try {
            return Class.forName(implName).getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw new RuntimeException("factory unable to construct instance of " +
                    iface.getName());
        }
    }
}
