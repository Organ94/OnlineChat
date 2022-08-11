package logger;

import interfaces.loggerInterface.IStorage;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
    private static Logger logger;
    protected IStorage storage;

    public Logger(IStorage storage) {
        this.storage = storage;
    }

    public String log(String msg) {
        String message = "[" + LocalDateTime.now().format(formatter) + "] " + msg;
        storage.append(message);
        return message;
    }

    protected static Logger getInstance() {
        if (logger == null) logger = new Logger(logger.storage);
        return logger;
    }
}
