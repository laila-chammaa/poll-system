import com.google.gson.Gson;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@WebServlet(name = "LoginServlet", urlPatterns = "/api/login")
public class LoginServlet extends HttpServlet {
    UserManager userManager = new UserManager();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String email = request.getParameter("email");
        String password = request.getParameter("password");

        OutputStream out = response.getOutputStream();
        String result;

        if (userManager.userAuthentication(email, password)) {
            String encryptedPass = userManager.encryptPassword(password);
            request.getSession().setAttribute("email", email);
            request.getSession().setAttribute("password", encryptedPass);
            response.setHeader("email", email);
            response.setHeader("password", encryptedPass);

            result = new Gson().toJson(true);

        } else {
            result = new Gson().toJson(false);
        }

        out.write(result.getBytes(StandardCharsets.UTF_8));
        out.flush();
        out.close();

    }
}
