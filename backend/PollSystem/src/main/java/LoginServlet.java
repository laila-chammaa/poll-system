import com.google.gson.Gson;
import model.IUserManager;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    IUserManager userManager = IUserManager.INSTANCE;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String newPassword = request.getParameter("newPassword");

        HttpSession session = request.getSession();
        OutputStream out = response.getOutputStream();
        String result = "";

        // if only email is not null, user clicked on forgot password
        if (email != null && password == null && name == null && newPassword == null) {
            if(userManager.forgotPassword(email)){
                result = new Gson().toJson(true);
            } else {
                result = new Gson().toJson(false);
            }
        }
        // if email, password, and newPassword is not null, user wishes to change their password
        else if (email != null && password != null && newPassword != null) {
            if(userManager.changePassword(email, password, newPassword)) {
                result = new Gson().toJson(true);
            } else {
                result = new Gson().toJson(false);
            }
        }
        // if email, password, and name is not null, user wishes to sign up
        else if (email != null && password != null && name != null) {
            if (userManager.signup(email, password, name)) {
                result = new Gson().toJson(true);
            } else {
                result = new Gson().toJson(false);
            }
        }
        // if other fields are null, except for email and pw, user wishes to log in
        else if (email != null && password != null) {
            if (userManager.authenticateUser(email, password)) {
                session.setAttribute("email", email);
                result = new Gson().toJson(true);
            } else {
                session.invalidate();
                result = new Gson().toJson(false);
            }
        }
        out.write(result.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();
    }
}
