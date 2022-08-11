package logger;

import interfaces.loggerInterface.IStorage;

import java.util.List;

public class ListStorageAdapter implements IStorage {

    protected List<String> listWithMessage;

    public ListStorageAdapter(List<String> listWithMessage) {
        this.listWithMessage = listWithMessage;
    }

    @Override
    public void append(String msg) {
        listWithMessage.add(msg);
    }
}
