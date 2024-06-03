package code.command;

import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;
import java.util.ArrayList;

public class History extends Command {
    /**
     * Cписок,хранящий исполненные команды
     */
    ArrayList<String> commandsHistoryList = new ArrayList<>();
    public History(){
        super("history");
    }
    /**
     * @param commandsHistoryList Хранит список исполненных команд
     */
    public History(ArrayList<String>  commandsHistoryList) {
        this.commandsHistoryList=commandsHistoryList;
    }
    /**
     * Метод, исполняющий команду. В случае выполнения выведутся  последние исполненные 6 команд(без их аргументов)
     * В случае неудачи высветится прудепреждение
     * @param arguments аргументы команды
     * @param invocationEnum режим работы команды
     * @param printStream  поток, куда следует выводить результаты команды
     * @throws CannotExecuteCommandException
     */
    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов.");
            }
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            StringBuilder sb=new StringBuilder();
            sb.append("History: \n");
            for(String str: commandsHistoryList)
                sb.append(str).append("\n");
            printStream.println(sb);
        }
    }
    /**
     * Метод, выводящий последние исполненные 6 команд(без их аргументов)
     * @return возвращающий описание команды.
     * @see Help
     */
    @Override
    public String getDescription() {
        return "вывести последние 6 команд (без их аргументов)";
    }
}