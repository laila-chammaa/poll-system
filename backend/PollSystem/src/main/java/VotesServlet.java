import com.google.gson.Gson;
import model.Choice;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet(name = "VotesServlet", urlPatterns = "/api/votes")
public class VotesServlet extends HttpServlet {

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
        String download = request.getParameter("download");
        String format = request.getParameter("format");
        if (download.equals("true") && format.equals("text")) {
            download(format, response); //TODO: change to PollManager.downloadPollResults?
        } else {
            sendResults(PollManager.getPollResults(), response);
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
            String firstKey = (String) results.keySet().toArray()[i-1];
            String firstValue = results.get(firstKey).toString();
            res[i][0] = firstKey;
            res[i][1] = firstValue;
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

    private void download(String format, HttpServletResponse response) throws IOException {
        String fileExtension = "txt";

        // You must tell the browser the file type you are going to send
        response.setContentType(format);
        String headerKey = "Content-disposition";
        String name = PollManager.getCurrentPoll().getName();
        String fileName = String.format("%s.%s", name, fileExtension);
        String headerVal = String.format("attachment; filename=%s", fileName);
        response.setHeader(headerKey, headerVal);

        //making sure the client does not cache the data
        response.setHeader("Cache-Control",
                "max-age=0, must-revalidate, no-cache, no-store, private, post-check=0, pre-check=0"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // prevents caching at the proxy server

        try {
            PrintWriter out = response.getWriter();
            PollManager.downloadPollDetails(out, fileName);
            out.flush();
            out.close();
        } catch (IOException | PollException.IllegalPollOperation e) {
            throw new IOException(String.format("Failed to download file %s", name), e);
        }
    }
}
