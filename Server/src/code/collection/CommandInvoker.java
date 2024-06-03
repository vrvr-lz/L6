package code.collection;

import code.command.*;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import exceptions.CannotExecuteCommandException;
import fileWork.CityFieldsReader;
import fileWork.CommandContainer;
import io.UserIO;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;

public class CommandInvoker {
    private HashMap<String, Command> serverCommands;
    private CollectionManager collectionManager;
    private String inputFile;
    private CommandContainer lastCommandContainer;
    private CityFieldsReader cityFieldsReader;
    private UserIO io;
    ExecuteScriptCommand.Script script;
    ArrayList<String> HistoryList = new ArrayList<>();

    public CommandInvoker(CollectionManager collectionManager, String inputFile) {
        this.serverCommands = new HashMap<>();
        this.collectionManager = collectionManager;
        this.inputFile = inputFile;
        this.cityFieldsReader = new CityFieldsReader(new UserIO());
        this.script = new ExecuteScriptCommand.Script();
        this.putServerCommands();
        System.out.println("Элементы коллекции для сервера были загружены");
    }

    public CommandInvoker(CollectionManager collectionManager) {
        this.serverCommands = new HashMap<>();
        this.collectionManager = collectionManager;
        this.cityFieldsReader = new CityFieldsReader(new UserIO());
        this.putServerCommands();
    }

    private void putServerCommands() {
        serverCommands.put("info", new Info(collectionManager));
        serverCommands.put("show", new Show(collectionManager));
        serverCommands.put("clear", new Clear(collectionManager));
        serverCommands.put("save", new Save(collectionManager, inputFile));
        serverCommands.put("help", new Help(serverCommands));
        serverCommands.put("add", new Add(collectionManager, cityFieldsReader));
        serverCommands.put("add_if_max", new AddIfMax(collectionManager, cityFieldsReader));
        serverCommands.put("add_if_min", new AddIfMin(collectionManager, cityFieldsReader));
        serverCommands.put("history", new History(HistoryList));
        serverCommands.put("execute_script", new ExecuteScriptCommand(collectionManager));
        serverCommands.put("group_counting_by_area", new GroupCountingByArea(collectionManager));
        serverCommands.put("remove_by_id", new RemoveById(collectionManager));
        serverCommands.put("remove_lower", new RemoveLower(collectionManager, cityFieldsReader));
        serverCommands.put("update", new Update(collectionManager, io));
        serverCommands.put("exit", new Exit());
        serverCommands.put("print_descending", new PrintDescending(collectionManager));
        serverCommands.put("average_of_population_density", new AverageOfPopulationDensity(collectionManager));
    }

    public boolean executeServer(String firstCommandLine, ArrayList<Object> result, PrintStream printStream) {
        String[] words = firstCommandLine.trim().split("\\s+");
        String[] arguments = Arrays.copyOfRange(words, 1, words.length);

        try {
            if (serverCommands.containsKey(words[0].toLowerCase(Locale.ROOT))) {
                Command command = serverCommands.get(words[0].toLowerCase(Locale.ROOT));
                command.setResult(result);
                command.execute(arguments, InvocationStatus.SERVER, printStream);
                lastCommandContainer = new CommandContainer(command.getName(), command.getResult());
                return true;
            }
        } catch (NullPointerException ex) {
            System.out.println("Команда " + words[0] + " не распознана, для получения справки введите команду help");
            ex.printStackTrace();
        } catch (CannotExecuteCommandException ex) {
            System.out.println(ex.getMessage());
        }
        return false;
    }

    public CommandContainer getLastCommandContainer() {
        return lastCommandContainer;
    }

    public void addToCommandsHistory(String string) {
        if (HistoryList.size() == 6) {
            HistoryList.remove(0);
            HistoryList.add(string);
        } else {
            HistoryList.add(string);
        }
    }
}
