import com.google.gson.Gson;
import com.sun.tools.javac.util.List;
import model.Choice;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;

@WebServlet(name = "PollServlet", urlPatterns = "/api/poll")
public class PollServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        String name = request.getParameter("name");
        String question = request.getParameter("question");
        String status = request.getParameter("status");
        String choiceName = request.getParameter("choices");
        Choice c1 = new Choice("Red", "best color");
        Choice c2 = new Choice("Blue", "royal color");

        ArrayList<Choice> choiceList = new ArrayList<>(Arrays.asList(c1, c2));
        if (status == null) {
            try {
                PollManager.createPoll(name, question, choiceList);
            } catch (PollException.TooFewChoices | PollException.DuplicateChoices tooFewChoices) {
                tooFewChoices.printStackTrace(); //TODO: better exceptions
            }
        } else if (status.equals("created") || status.equals("running")) {
            PollManager.updatePoll(name, question, choiceList);
        }

    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //get the status sent by the client
        String status = request.getParameter("status");
        switch (status) {
            case "running":
                PollManager.runPoll();
                break;
            case "released":
                PollManager.releasePoll();
                break;
            case "unreleased":
                PollManager.unreleasePoll();
            case "cleared":
                PollManager.clearPoll();
                break;
            case "closed":
                PollManager.closePoll();
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // You must tell the browser the file type you are going to send
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //making sure the client does not cache the data
        response.setHeader("Cache-Control",
                "max-age=0, must-revalidate, no-cache, no-store, private, post-check=0, pre-check=0"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // prevents caching at the proxy server

        try {
            String json = new Gson().toJson(PollManager.getCurrentPoll());

            OutputStream out = response.getOutputStream();
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new IOException("Failed to send currentPoll as JSON", e);
        }
    }
}
