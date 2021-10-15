import model.Choice;
import model.Poll;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@WebServlet(name = "PollServlet", urlPatterns = "/api/poll")
public class PollServlet extends HttpServlet {
    //TODO: add updatePoll servlet endpoint

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //String name, String question, ArrayList<Choice> choices
        String name = request.getParameter("name");
        String question = request.getParameter("question");
        String choiceName = request.getParameter("choices");
        ArrayList<Choice> choiceList = new ArrayList<>();
        try {
            PollManager.createPoll(name, question, choiceList);
        } catch (PollException.TooFewChoices | PollException.DuplicateChoices tooFewChoices) {
            tooFewChoices.printStackTrace(); //TODO: better exceptions
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException {
        //get the status sent by the client
        String status = request.getParameter("status");
        switch (status) {
            case "run":
                PollManager.runPoll();
                break;
            case "release":
                PollManager.releasePoll();
                break;
            case "unrelease":
                PollManager.unreleasePoll();
            case "clear":
                PollManager.clearPoll();
                break;
            case "close":
                PollManager.closePoll();
                break;
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
