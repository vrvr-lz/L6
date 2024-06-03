package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class Show extends Command {
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;
    /**
     * Конструктор класса без аргументов
     */
    public Show (){
        super("show");
    }
    /**
     * Конструктор класса.
     * @param collectionManager Хранит ссылку на созданный в объекте Application объект CollectionManager.
     */
    public Show (CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов.");
            }
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            printStream.println(collectionManager.show());
        }
    }
    /**
     * Метод, возращающий описание команды
     * @return Метод, возвращающий описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "показывает подробное содержимое всех элементов коллекции";
    }
}
