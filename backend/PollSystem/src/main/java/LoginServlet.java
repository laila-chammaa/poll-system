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
    IUserManager userManager = new UserManager();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        HttpSession session = request.getSession();
        OutputStream out = response.getOutputStream();
        String result;

        if (userManager.authenticateUser(email, password)) {
            session.setAttribute("email", email);
            result = new Gson().toJson(true);
        } else {
            session.invalidate();
            result = new Gson().toJson(false);
        }
        out.write(result.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();

    }
}
