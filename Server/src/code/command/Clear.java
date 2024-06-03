package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

/**
 * Команда, очищающая коллекцию.
 */
public class Clear extends Command {
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;
    /**
     * Конструктор класса без аргументов
     */
    public Clear(){
        super("clear");
    }
    /**
     * Конструктор класса
     *
     * @param collectionManager Хранит ссылку на созданный в объекте Application объект CollectionManager.
     */
    public Clear(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    /**
     * Метод, исполняющий команду. Выводит сообщение когда коллекция очищена.
     * @param arguments      аргументы команды.
     * @param invocationEnum режим работы команды
     * @param printStream поток, куда следует выводит результат команды
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов.");
            }
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            collectionManager.clear();
            printStream.println("Коллекция " + collectionManager.getClass().getSimpleName() + " была очищена.");
        }
    }
    /**
     * @return Описание команды.
     * @see Help
     */
    @Override
    public String getDescription() {
        return "очищает все элементы коллекции";
    }
}