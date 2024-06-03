package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;

import java.io.PrintStream;

public class PrintDescending extends Command {
    private final CollectionManager collectionManager;

    public PrintDescending(CollectionManager collectionManager) {
        super("print_descending");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) {
        if (invocationStatus.equals(InvocationStatus.SERVER)) {
            collectionManager.printDescending(printStream);
        }
    }

    @Override
    public String getDescription() {
        return "выводит элементы коллекции в порядке убывания";
    }
}
