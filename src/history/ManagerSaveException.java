package history;

import java.io.IOException;

public class ManagerSaveException extends RuntimeException {

    public ManagerSaveException(String string,Throwable throwable) {
        super(string,throwable);
    }
}
