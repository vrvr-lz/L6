package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import collectionn.City;
import exceptions.CannotExecuteCommandException;
import fileWork.CityFieldsReader;

import java.io.PrintStream;
import java.util.ArrayList;

public class AddIfMax extends Command {
    private final CollectionManager collectionManager;
    private final CityFieldsReader cityFieldsReader;
    private ArrayList<Object> result = new ArrayList<>();

    public AddIfMax(CollectionManager collectionManager, CityFieldsReader cityFieldsReader) {
        super("add_if_max");
        this.collectionManager = collectionManager;
        this.cityFieldsReader = cityFieldsReader;
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            result.clear();  // Очистим список перед использованием
            if (arguments.length > 0) {
                throw new CannotExecuteCommandException("Эта команда не принимает аргументы.");
            }
            printStream.println("Введите значения полей для элемента коллекции:\n");
            City city = cityFieldsReader.read();
            result.add(city);
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            if (result.isEmpty()) {
                throw new CannotExecuteCommandException("Ошибка: результат пуст. Команда должна быть выполнена на стороне клиента перед отправкой на сервер.");
            }
            City city = (City) result.get(0);
            collectionManager.addIfMax(city, printStream);
        }
    }

    @Override
    public String getDescription() {
        return "добавляет элемент в коллекцию, если его значение area больше, чем у всех других элементов";
    }

    @Override
    public ArrayList<Object> getResult() {
        return result;
    }

    public void setResult(ArrayList<Object> result) {
        this.result = result;
    }
}
