package model;

public interface IUserManager {

    public void loadListOfUsers();

    public boolean signup(String email, String name, String password);

    public boolean forgotPassword(String email, String oldPass);

    public boolean changePassword(String email, String oldPass, String newPass);

    public boolean authenticateUser(String email, String password);

    public String encryptPassword(String password);

}
