import com.google.gson.Gson;
import model.Choice;
import model.Poll;
import model.PollStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

@WebServlet(name = "VotesServlet", urlPatterns = "/api/votes")
public class VotesServlet extends HttpServlet {

    PollManager pollManager = new PollManager();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //get the vote sent by the user
        String choiceName = request.getParameter("choice");
        String pollId = request.getParameter("pollId");
        String pin = request.getParameter("pin");

        Optional<Choice> choice = pollManager.accessPoll(pollId).getChoices()
                .stream().filter(c -> c.getText().equals(choiceName)).findFirst();
        if (choice.isPresent()) {
            // returns pin since it might be generated
            pin = pollManager.vote(pin, pollId, choice.get());
            String json = new Gson().toJson(pin);
            OutputStream out = response.getOutputStream();
            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } else {
            throw new PollException.InvalidParam(String.format("Invalid choice, choice %s is not found", choiceName));
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String download = request.getParameter("download");
        String format = request.getParameter("format");
        String pollId = request.getParameter("pollId");
        if (download.equals("true") && format.equals("text")) {
            download(pollId, format, response);
        } else {
            sendResults(pollManager.getPollResults(pollId), response);
        }
    }

    private void sendResults(Hashtable<String, Integer> results, HttpServletResponse response) throws IOException {
        // You must tell the browser the file type you are going to send
        response.setContentType("application/json");
        //making sure the client does not cache the data
        response.setHeader("Cache-Control",
                "max-age=0, must-revalidate, no-cache, no-store, private, post-check=0, pre-check=0"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // prevents caching at the proxy server

        int size = results.entrySet().size() + 1;
        String[][] res = new String[size][2];
        res[0][0] = "Choice";
        res[0][1] = "Vote Count";
        for (int i = 1; i < size; i++) {
            String key = (String) results.keySet().toArray()[i - 1];
            String value = results.get(key).toString();
            res[i][0] = key;
            res[i][1] = value;
        }

        OutputStream out;
        try {
            out = response.getOutputStream();
            String json = new Gson().toJson(res);

            out.write(json.getBytes(StandardCharsets.UTF_8));
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new IOException("Failed to send results", e);
        }
    }

    private void download(String pollId, String format, HttpServletResponse response) throws IOException {
        String fileExtension = "json";

        // You must tell the browser the file type you are going to send
        response.setContentType(format);
        String headerKey = "Content-disposition";
        String name = pollManager.accessPoll(pollId).getName();
        String date = LocalDateTime.now().toString();
        String createdBy = pollManager.accessPoll(pollId).getCreatedBy();
        String question = pollManager.accessPoll(pollId).getQuestion();
        String fileName = String.format("%s-%s.%s", name, date, fileExtension);
        String headerVal = String.format("attachment; filename=%s", fileName);
        response.setHeader(headerKey, headerVal);

        //making sure the client does not cache the data
        response.setHeader("Cache-Control",
                "max-age=0, must-revalidate, no-cache, no-store, private, post-check=0, pre-check=0"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // prevents caching at the proxy server

        JSONObject obj = new JSONObject();
        JSONArray list = new JSONArray();

        list.add(pollManager.accessPoll(pollId).getChoices());


        obj.put("Poll", pollId);
        obj.put("Time created", date);
        obj.put("Name", name);
        obj.put("Question ", question);
        obj.put("Created by", createdBy);
        obj.put("Choices", list);

        try {
            PrintWriter out = response.getWriter();
//            pollManager.downloadPollDetails(pollId, out, fileName);
            out.write(obj.toString());
            out.flush();
            out.close();

//        } catch (IOException | PollException.IllegalPollOperation e)
        }catch (IOException e){
            throw new IOException(String.format("Failed to download file %s", name), e);
        }

    }
}
