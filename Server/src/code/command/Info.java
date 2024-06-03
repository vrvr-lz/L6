package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

public class Info extends Command {
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;
    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на созданный в объекте Application объект CollectionManager.
     */
    public Info(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    /**
     * Конструктор без параметров
     */
    public Info(){
        super("info");
    }

    /**
     *
     * @param arguments аргументы команды.
     * @param invocationEnum режим, с которым следует исполнить команду.
     * @param printStream поток вывода.
     * @throws CannotExecuteCommandException
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)){
            if(arguments.length > 0){
                throw new CannotExecuteCommandException("У данной команды нет аргументов");
            }
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            collectionManager.info(printStream);
        }
    }
    /**
     * @return Возвращает описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "команда получает информацию о коллекции(тип, дата инициализации, кол-во элементов, тип элементов коллекции)";
    }
}