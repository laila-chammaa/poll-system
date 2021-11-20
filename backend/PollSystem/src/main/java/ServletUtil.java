import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ServletUtil {
    public static void handleError(String errorMessage, HttpServletResponse response) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().print(errorMessage);
    }
}
