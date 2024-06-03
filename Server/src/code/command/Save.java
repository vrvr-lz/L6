package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;

/**
 * Команда, сохраняющая коллекцию
 */
public class Save extends Command {
    /**
     * Поле, хранящее адрес файла, куда следует сохранить коллекцию.
     */
    private String inputFile;
    /**
     * Поле, хранящее ссылку на объект класса CollectionManager.
     */
    private CollectionManager collectionManager;
    /**
     * Конструктор класса.
     *
     * @param collectionManager Хранит ссылку на созданный в объекте Application объект CollectionManager.
     * @param inputFile         Хранит адрес файла, куда следует сохранить элементы коллекции.
     */
    public Save(CollectionManager collectionManager,String inputFile){
        this.collectionManager=collectionManager;
        this.inputFile=inputFile;
    }
    /**
     * Метод, сохраняющий коллекцию в указанном файле в формате XML. В случае некорректной работы высветится ошибка.
     */

    /**
     * Метод, сохраняющий коллекцию в указанном файле в формате XML. В случае некорректной работы высветится предупреждение.
     * @param invocationEnum режим, с которым должна быть исполнена данная команда.
     * @param printStream поток вывода.
     * @param arguments аргументы команды.
     */

    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)) {
            throw new CannotExecuteCommandException("У данной команды для клиента нет выполнения.");
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            collectionManager.save(inputFile);
            printStream.println("Коллекция " + collectionManager.getClass().getSimpleName() + " была сохранена.");
        }
    }

    /**
     * Метод, возвращающий описание команды.
     *
     * @return Метод, возвращающий описание команды.
     * @see Command
     */
    @Override
    public String getDescription() {
        return "сохраняет коллекцию в указанный файл";
    }
}
