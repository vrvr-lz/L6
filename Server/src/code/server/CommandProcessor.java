package code.server;

import code.collection.CollectionManager;
import code.collection.CommandInvoker;
import code.command.Add;
import code.command.AddIfMax;
import code.command.AddIfMin;
import code.command.RemoveLower;
import code.command.Update;
import code.command.ExecuteScriptCommand;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;
import fileWork.CityFieldsReader;
import fileWork.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.ArrayList;

public class CommandProcessor {
    private static final Logger rootLogger = LogManager.getRootLogger();
    private final CommandInvoker commandInvoker;
    private final CollectionManager collectionManager;
    private final CityFieldsReader cityFieldsReader;

    public CommandProcessor(CommandInvoker commandInvoker, CollectionManager collectionManager, CityFieldsReader cityFieldsReader) {
        this.commandInvoker = commandInvoker;
        this.collectionManager = collectionManager;
        this.cityFieldsReader = cityFieldsReader;
    }

    public void executeCommand(CommandContainer commandContainer, PrintStream printStream) {
        String commandName = commandContainer.getName();
        ArrayList<Object> commandArgs = commandContainer.getArguments();

        rootLogger.info("Выполнение команды: " + commandName + " с аргументами: " + commandArgs);

        if (commandArgs == null) {
            rootLogger.error("Аргументы команды null для команды: " + commandName);
            printStream.println("Ошибка: аргументы команды не могут быть null.");
            return;
        }

        for (Object arg : commandArgs) {
            if (arg == null) {
                rootLogger.error("Один из аргументов команды null для команды: " + commandName);
                printStream.println("Ошибка: один из аргументов команды не может быть null.");
                return;
            }
        }

        switch (commandName) {
            case "add":
                Add addCommand = new Add(collectionManager, cityFieldsReader);
                addCommand.setResult(commandArgs);
                try {
                    addCommand.execute(new String[]{}, InvocationStatus.SERVER, printStream);
                } catch (CannotExecuteCommandException e) {
                    e.printStackTrace();
                }
                break;

            case "add_if_max":
                AddIfMax addIfMaxCommand = new AddIfMax(collectionManager, cityFieldsReader);
                addIfMaxCommand.setResult(commandArgs);
                try {
                    addIfMaxCommand.execute(new String[]{}, InvocationStatus.SERVER, printStream);
                } catch (CannotExecuteCommandException e) {
                    e.printStackTrace();
                }
                break;

            case "add_if_min":
                AddIfMin addIfMinCommand = new AddIfMin(collectionManager, cityFieldsReader);
                addIfMinCommand.setResult(commandArgs);
                try {
                    addIfMinCommand.execute(new String[]{}, InvocationStatus.SERVER, printStream);
                } catch (CannotExecuteCommandException e) {
                    e.printStackTrace();
                }
                break;

            case "remove_lower":
                RemoveLower removeLowerCommand = new RemoveLower(collectionManager, cityFieldsReader);
                removeLowerCommand.setResult(commandArgs);
                try {
                    removeLowerCommand.execute(new String[]{}, InvocationStatus.SERVER, printStream);
                } catch (CannotExecuteCommandException e) {
                    e.printStackTrace();
                }
                break;

            case "update":
                Update updateCommand = new Update(collectionManager, cityFieldsReader.getUserIO());
                updateCommand.setResult(commandArgs);
                try {
                    updateCommand.execute(new String[]{}, InvocationStatus.SERVER, printStream);
                } catch (CannotExecuteCommandException e) {
                    e.printStackTrace();
                }
                break;

            case "execute_script":
                ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand(collectionManager);
                executeScriptCommand.setResult(commandArgs);
                try {
                    if (commandArgs.isEmpty()) {
                        throw new CannotExecuteCommandException("Нет аргументов для команды execute_script");
                    }
                    String[] arguments = commandArgs.stream().map(Object::toString).toArray(String[]::new);
                    executeScriptCommand.execute(arguments, InvocationStatus.SERVER, printStream);
                } catch (CannotExecuteCommandException e) {
                    e.printStackTrace();
                }
                break;

            default:
                boolean result = commandInvoker.executeServer(commandName, commandArgs, printStream);
                if (!result) {
                    printStream.println("Команда " + commandName + " не распознана или не может быть выполнена.");
                }
                break;
        }
    }
}
