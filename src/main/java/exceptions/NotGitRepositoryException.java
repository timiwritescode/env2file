package exceptions;

public class NotGitRepositoryException extends Exception{
    public NotGitRepositoryException(String message) {
        super(message);
    }
}
