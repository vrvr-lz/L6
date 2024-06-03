package code.command;

import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Класс команды, которая выводит описания всех команд, реализованных в программе.
 */
public class Help extends Command {
    /**
     * Коллекция, содержащая объекты всех доступных в программе команд без дополнительных аргументов.
     */
    private final HashMap<String, Command> commands;
    PrintStream printStream = System.out;
    /**
     * Конструктор класса.
     *
     * @param commands Коллекция, содержащая объекты всех доступных в программе команд с дополнительными аргументами.(либо клиентские, либо сервера)
     */
    public Help(HashMap<String, Command> commands) {
        super("help");
        this.commands = commands;
    }
    /**
     * Метод, выводящий справку по всем доступным командам.
     *
     * @param invocationEnum режим, с которым должна быть исполнена данная команда.
     * @param printStream поток вывода.
     * @param arguments аргументы команды.
     */
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.CLIENT)) {
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("У данной команды нет аргументов.");
            } else {
                commands.forEach((key, value) -> System.out.println(key + ": " + value.getDescription()));
            }
        } else if (invocationEnum.equals(InvocationStatus.SERVER)) {
            commands.forEach((key, value) -> printStream.println(key + ": " + value.getDescription()));
        }
    }
    /**
     * @return Описание данной команды.
     * @see Help
     */
    @Override
    public String getDescription() {
        return "выводит справку по всем командам";
    }
}