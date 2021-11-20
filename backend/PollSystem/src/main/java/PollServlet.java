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

        String email = (String) request.getSession().getAttribute("email");
        String pollStr = request.getParameter("poll");
        pollStr = pollStr.replaceAll("\\$", "[");
        pollStr = pollStr.replaceAll("&", "]");
        JSONObject jsonObj = new JSONObject(pollStr);
        String pollId = null;
        try {
            pollId = jsonObj.getString("id");
        } catch (JSONException e) {
            // if it's a create, a pollId is not necessary
        }
        String name = jsonObj.getString("name");
        String question = jsonObj.getString("question");
        String status = request.getParameter("status");
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
                Poll newPoll = pollManager.createPoll(name, question, choiceList, email);
                pollId = newPoll.getId();
            } else if (status.equals("CREATED") || status.equals("RUNNING")) {
                Poll poll = pollManager.accessPoll(pollId);
                if (email.equals(poll.getCreatedBy())) {
                    pollManager.updatePoll(pollId, name, question, choiceList);
                } else {
                    throw new PollException.UnauthorizedOperation("Unauthorized Operation. Cannot edit a poll you did not create.");
                }

            }
            //returning poll ID
            OutputStream out = response.getOutputStream();
            String json = new Gson().toJson(pollId);
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();

        } catch (PollException.TooFewChoices | PollException.DuplicateChoices | IOException tooFewChoices) {
            tooFewChoices.printStackTrace(); //TODO: better exceptions
        }

    }

    // handles update poll status
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the status sent by the client
        String email = (String) request.getSession().getAttribute("email");
        String pollId = request.getParameter("pollId");
        String status = request.getParameter("status");
        Poll poll = pollManager.accessPoll(pollId);
        if (email.equals(poll.getCreatedBy())) {
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
        } else {
            //TODO: how to return errors to frontend?
            OutputStream out = response.getOutputStream();
            String json = new Gson().toJson("Unauthorized Operation. You are not the creator of this poll");
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
            throw new PollException.UnauthorizedOperation("Unauthorized Operation. You are not the creator of this poll");
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

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse resp) throws ServletException, IOException {
        String email = (String) request.getSession().getAttribute("email");
        String pollId = request.getParameter("pollId");
        Poll poll = pollManager.accessPoll(pollId);
        if (email.equals(poll.getCreatedBy())) {
            pollManager.deletePoll(pollId);
        } else {
            throw new PollException.UnauthorizedOperation("Unauthorized Operation. You are not the creator of this poll");
        }
    }
}
