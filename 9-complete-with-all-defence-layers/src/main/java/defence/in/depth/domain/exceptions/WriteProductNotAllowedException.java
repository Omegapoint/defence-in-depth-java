package defence.in.depth.domain.exceptions;

public class WriteProductNotAllowedException extends BusinessException{
    public WriteProductNotAllowedException(String message) {
        super(message);
    }
}
