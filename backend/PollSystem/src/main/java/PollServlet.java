import model.Choice;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@WebServlet(name = "PollServlet", urlPatterns = "/pollSystem")
public class PollServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //get the vote sent by the user
        String choiceName = request.getParameter("choice");
        Optional<Choice> choice = PollManager.getCurrentPoll().getChoices()
                .stream().filter(c -> c.getText().equals(choiceName)).findFirst();
        if (choice.isPresent()) {
            String sessionId = request.getSession().getId();
            PollManager.vote(sessionId, choice.get());
        } else {
            throw new PollException.InvalidParam(String.format("Invalid choice, choice %s is not found", choiceName));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("name");
        String format = request.getParameter("format");
        if (name.equals(PollManager.getCurrentPoll().getName()) && format.equals("text")) {
            download(name, format, response); //TODO: change to PollManager.downloadPollResults?
        } else {
            //TODO: the exception text is printed to the server output stream
            throw new PollException.InvalidParam(String.format("Current poll is not %s, or format is not 'text'", name));
        }
    }

    private void download(String name, String format, HttpServletResponse response) throws IOException {
        String fileExtension = ".txt";
        String msg;

        // You must tell the browser the file type you are going to send
        response.setContentType(format);
        String headerKey = "Content-disposition";
        String headerVal = String.format("attachment; filename=%s.%s", name, fileExtension);
        response.setHeader(headerKey, headerVal);

        //making sure the client does not cache the data
        response.setHeader("Cache-Control",
                "max-age=0, must-revalidate, no-cache, no-store, private, post-check=0, pre-check=0"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // prevents caching at the proxy server

        OutputStream out;
        try {
            out = response.getOutputStream();
            msg = PollManager.getCurrentPoll().toString();

            out.write(msg.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new IOException(String.format("Failed to download file %s", name), e);
        }
    }
}
