public interface IUserManager {

    public static final IUserManager INSTANCE = (IUserManager) PluginFactory.getPlugin(IUserManager.class);

    public boolean signup(String email, String name, String password);

    public boolean forgotPassword(String email);

    public boolean changePassword(String email, String oldPass, String newPass);

    public boolean authenticateUser(String email, String password);

    public void validateUser(String email, String password);
}
