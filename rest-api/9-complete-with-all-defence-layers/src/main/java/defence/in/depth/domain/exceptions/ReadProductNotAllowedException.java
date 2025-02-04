package defence.in.depth.domain.exceptions;

public class ReadProductNotAllowedException extends BusinessException {
    public ReadProductNotAllowedException(String message) {
        super(message);
    }
}
