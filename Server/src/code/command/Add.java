package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import collectionn.City;
import collectionn.CityFieldValidation;
import exceptions.CannotExecuteCommandException;
import fileWork.CityFieldsReader;

import java.io.PrintStream;
import java.util.ArrayList;

public class Add extends Command {
    private final CollectionManager collectionManager;
    private final CityFieldsReader cityFieldsReader;
    private ArrayList<Object> result = new ArrayList<>();

    public Add(CollectionManager collectionManager, CityFieldsReader cityFieldsReader) {
        super("add");
        this.collectionManager = collectionManager;
        this.cityFieldsReader = cityFieldsReader;
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            result.clear();  // Очистим список перед использованием
            if (arguments.length > 1) {
                throw new CannotExecuteCommandException("Количество аргументов у данной команды должно быть не более 1.");
            }
            if (arguments.length == 1) {
                if (CityFieldValidation.validate("id", arguments[0])) {
                    printStream.println("Введите значения полей для элемента коллекции:\n");
                    City city = cityFieldsReader.read(Integer.parseInt(arguments[0]));
                    result.add(Integer.parseInt(arguments[0]));
                    result.add(city);
                } else {
                    throw new CannotExecuteCommandException("Введены невалидные аргументы: id = " + arguments[0]);
                }
            } else {
                printStream.println("Введите значения полей для элемента коллекции:\n");
                City city = cityFieldsReader.read();
                result.add(city);
            }
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            if (result.isEmpty()) {
                throw new CannotExecuteCommandException("Ошибка: результат пуст. Команда должна быть выполнена на стороне клиента перед отправкой на сервер.");
            }
            City city = result.size() == 2 ? (City) result.get(1) : (City) result.get(0);
            collectionManager.add(city, printStream);
        }
    }

    @Override
    public String getDescription() {
        return "добавляет элемент коллекции";
    }

    @Override
    public ArrayList<Object> getResult() {
        return result;
    }

    public void setResult(ArrayList<Object> result) {
        this.result = result;
    }
}
