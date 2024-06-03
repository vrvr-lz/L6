package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;

import java.io.PrintStream;

public class AverageOfPopulationDensity extends Command {
    private final CollectionManager collectionManager;

    public AverageOfPopulationDensity(CollectionManager collectionManager) {
        super("average_of_population_density");
        this.collectionManager = collectionManager;
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) {
        if (invocationStatus.equals(InvocationStatus.SERVER)) {
            collectionManager.averageOfPopulationDensity(printStream);
        }
    }

    @Override
    public String getDescription() {
        return "выводит среднее значение плотности населения для всех городов в коллекции";
    }
}

