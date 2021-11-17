import com.google.gson.Gson;
import model.Choice;
import model.Poll;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


@WebServlet(name = "PollServlet", urlPatterns = "/api/poll")
public class PollServlet extends HttpServlet {

    PollManager pollManager = new PollManager();

    // handles create/update poll
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");

        HttpSession session = request.getSession();
        String email = (String) session.getAttribute("email");
        String password = (String) session.getAttribute("password");
        String token = (String) session.getAttribute("token");

        String pollStr = request.getParameter("poll");
        pollStr = pollStr.replaceAll("\\$", "[");
        pollStr = pollStr.replaceAll("&", "]");
        JSONObject jsonObj = new JSONObject(pollStr);
        String pollId = null;
        try {
            pollId = jsonObj.getString("pollId");
        } catch (JSONException e) {

        }
        String name = jsonObj.getString("name");
        String question = jsonObj.getString("question");
        String status = request.getParameter("status");
        //TODO: update the frontend, do we want User class? get username/pass? validate?
        String creator = request.getParameter("user");
        JSONArray choicesArray = jsonObj.getJSONArray("choices");
        ArrayList<Choice> choiceList = new ArrayList<>();

        for (int i = 0; i < choicesArray.length(); ++i) {
            JSONObject choice = choicesArray.getJSONObject(i);
            String text = choice.getString("text");
            String description = choice.getString("description");
            choiceList.add(new Choice(text, description));
        }

        try {
            if (status == null || pollId == null) {
                pollManager.createPoll(name, question, choiceList, creator);
            } else if (status.equals("CREATED") || status.equals("RUNNING")) {
                pollManager.updatePoll(pollId, name, question, choiceList);
            }
        } catch (PollException.TooFewChoices | PollException.DuplicateChoices tooFewChoices) {
            tooFewChoices.printStackTrace(); //TODO: better exceptions
        }

    }

    // handles update poll status
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //get the status sent by the client
        String status = request.getParameter("status");
        String pollId = request.getParameter("pollId");
        switch (status) {
            case "running":
                pollManager.runPoll(pollId);
                break;
            case "released":
                pollManager.releasePoll(pollId);
                break;
            case "unreleased":
                pollManager.unreleasePoll(pollId);
            case "cleared":
                pollManager.clearPoll(pollId);
                break;
            case "closed":
                pollManager.closePoll(pollId);
                break;
        }
    }

    // handles access poll by id, and returning a list of polls by user
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pollId = request.getParameter("pollId");
        String creator = request.getParameter("creator");
        // You must tell the browser the file type you are going to send
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        //making sure the client does not cache the data
        response.setHeader("Cache-Control",
                "max-age=0, must-revalidate, no-cache, no-store, private, post-check=0, pre-check=0"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // prevents caching at the proxy server

        try {
            String json;
            if (creator != null) {
                List<Poll> pollList = pollManager.getAllPollsByUser(creator);
                json = new Gson().toJson(pollList);

            } else {
                Poll poll = pollManager.accessPoll(pollId);
                json = new Gson().toJson(poll);

            }
            OutputStream out = response.getOutputStream();
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new IOException("Failed to send poll as JSON", e);
        }
    }
}
