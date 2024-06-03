package code.command;

import code.collection.CollectionManager;
import code.collection.CommandInvoker;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;
import exceptions.RecursiveCallException;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class ExecuteScriptCommand extends Command {

    private CollectionManager collectionManager;
    private String scriptPath;
    private Script script;

    public ExecuteScriptCommand(CollectionManager collectionManager) {
        super("execute_script");
        this.collectionManager = collectionManager;
        this.script = new Script();
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationEnum, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationEnum.equals(InvocationStatus.SERVER)) {
            try {
                if (arguments.length != 1) {
                    throw new CannotExecuteCommandException("Количество аргументов команды должно быть 1.");
                }
                scriptPath = arguments[0].trim();
                if (scriptPath.isEmpty()) {
                    throw new CannotExecuteCommandException("Путь к файлу скрипта не может быть пустым.");
                }

                if (script.scriptPaths.contains(scriptPath)) throw new RecursiveCallException(scriptPath);
                else script.putScript(scriptPath);

                File ioFile = new File(scriptPath);
                if (!ioFile.canWrite() || ioFile.isDirectory() || !ioFile.isFile()) throw new IOException();

                FileInputStream fileInputStream = new FileInputStream(scriptPath);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream);
                Scanner scanner = new Scanner(inputStreamReader);

                CommandInvoker commandInvoker = new CommandInvoker(collectionManager);

                while (scanner.hasNext()) {
                    String commandLine = scanner.nextLine().trim();
                    if (!commandLine.isEmpty()) {
                        String[] parts = commandLine.split("\\s+");
                        String commandName = parts[0];
                        String[] commandArguments = Arrays.copyOfRange(parts, 1, parts.length);

                        if (commandInvoker.executeServer(commandName, new ArrayList<>(Arrays.asList(commandArguments)), printStream)) {
                            // Add executed command to the result list
                            super.result.add(commandInvoker.getLastCommandContainer());
                        }
                    }
                }
                script.removeScript(scriptPath);
                return;
            } catch (FileNotFoundException ex) {
                printStream.println("Файл скрипта не найден");
            } catch (IOException ex) {
                printStream.println("Доступ к файлу невозможен");
            } catch (IllegalArgumentException ex) {
                printStream.println("Скрипт не передан в качестве аргумента команды, либо кол-во аргументов больше 1");
            } catch (RecursiveCallException ex) {
                printStream.println("Скрипт " + scriptPath + " уже существует (Рекурсивный вызов)");
            }
            script.removeScript(scriptPath);
            throw new CannotExecuteCommandException("Принудительное завершение работы команды execute_script");
        }
    }

    @Override
    public String getDescription() {
        return "выполняет команды, описанные в скрипте";
    }

    public static class Script {
        private final ArrayList<String> scriptPaths = new ArrayList<>();

        public void putScript(String scriptPath) {
            scriptPaths.add(scriptPath);
        }

        public void removeScript(String scriptPath) {
            scriptPaths.remove(scriptPath);
        }
    }
}
