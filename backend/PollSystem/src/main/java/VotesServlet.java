import com.google.gson.Gson;
import model.Choice;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.Optional;

@WebServlet(name = "VotesServlet", urlPatterns = "/api/votes")
public class VotesServlet extends HttpServlet {

    PollService pollService = new PollService();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //get the vote sent by the user
        String choiceName = request.getParameter("choice");
        String pollId = request.getParameter("pollId");
        String pin = request.getParameter("pin");

        Optional<Choice> choice = pollService.accessPoll(pollId).getChoices()
                .stream().filter(c -> c.getText().equals(choiceName)).findFirst();
        try {
            if (choice.isPresent()) {
                // returns pin since it might be generated

                pin = pollService.vote(pin, pollId, choice.get());

                String json = new Gson().toJson(pin);
                OutputStream out = response.getOutputStream();
                out.write(json.getBytes(StandardCharsets.UTF_8));
                out.flush();
                out.close();
            } else {
                throw new PollException.InvalidParam(String.format("Invalid choice, choice %s is not found", choiceName));
            }
        } catch (PollException.IllegalPollOperation | PollException.ChoiceNotFound | PollException.InvalidParam e) {
            ServletUtil.handleError(e.getMessage(), response);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String download = request.getParameter("download");
        String format = request.getParameter("format");
        String pollId = request.getParameter("pollId");
        String email = (String) request.getSession().getAttribute("email");

        if (download.equals("true") && (format.equals("text") || format.equals("json") || format.equals("xml"))) {
            download(pollId, format, email, response);
        } else {
            try {
                sendResults(pollService.getPollResults(pollId, email), response);
            } catch (PollException.IllegalPollOperation e) {
                ServletUtil.handleError(e.getMessage(), response);
            }
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

    private void download(String pollId, String format, String creator, HttpServletResponse response) throws IOException {
        String fileExtension = "";

        switch (format) {
            case "text":
                fileExtension = "txt";
                break;
            case "json":
                fileExtension = "json";
                break;
            case "xml":
                fileExtension = "xml";
                break;
        }

        // You must tell the browser the file type you are going to send
        response.setContentType(format);
        String headerKey = "Content-disposition";
        String name = pollService.accessPoll(pollId).getName();
        String date = LocalDateTime.now().toString();
        String fileName = String.format("%s-%s.%s", name, date, fileExtension);
        String headerVal = String.format("attachment; filename=%s", fileName);
        response.setHeader(headerKey, headerVal);

        //making sure the client does not cache the data
        response.setHeader("Cache-Control",
                "max-age=0, must-revalidate, no-cache, no-store, private, post-check=0, pre-check=0"); // HTTP 1.1
        response.setHeader("Pragma", "no-cache"); // HTTP 1.0
        response.setDateHeader("Expires", 0); // prevents caching at the proxy server

        try {
            PrintWriter out = response.getWriter();
            pollService.downloadPollDetails(pollId, out, creator, format);
            out.flush();
            out.close();
        } catch (IOException | PollException.IllegalPollOperation e) {
            throw new IOException(String.format("Failed to download file %s", name), e);
        }
    }
}
