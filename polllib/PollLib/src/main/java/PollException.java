import javax.servlet.ServletException;

public class PollException extends Exception {
    public PollException(String errorMessage) {
        super(errorMessage);
    }

    public static class IllegalPollOperation extends ServletException
    {
        public IllegalPollOperation(String msg) {
            super(msg);
        }
    }

    public static class InvalidParam extends ServletException
    {
        public InvalidParam(String msg) {
            super(msg);
        }
    }

    public static class TooFewChoices extends Exception
    {
        public TooFewChoices(String msg) {
            super(msg);
        }
    }

    public static class DuplicateChoices extends Exception
    {
        public DuplicateChoices(String msg) {
            super(msg);
        }
    }

    public static class ChoiceNotFound extends ServletException
    {
        public ChoiceNotFound(String msg) {
            super(msg);
        }
    }

    public static class UnauthorizedOperation extends ServletException
    {
        public UnauthorizedOperation(String msg) {
            super(msg);
        }
    }
}