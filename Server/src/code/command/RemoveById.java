package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class RemoveById extends Command {
    private final CollectionManager collectionManager;

    public RemoveById(CollectionManager collectionManager) {
        super("remove_by_id");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            if (arguments.length != 1) {
                throw new CannotExecuteCommandException("Эта команда принимает только один аргумент.");
            }
            int id;
            try {
                id = Integer.parseInt(arguments[0]);
            } catch (NumberFormatException e) {
                throw new CannotExecuteCommandException("Неверный формат аргумента: " + arguments[0]);
            }
            super.result.add(id);
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            int id = (int) this.getResult().get(0);
            collectionManager.removeById(id, printStream);
        }
    }

    @Override
    public String getDescription() {
        return "удаляет элемент из коллекции по его ID";
    }
}
