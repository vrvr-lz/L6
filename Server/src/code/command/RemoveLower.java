package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import collectionn.City;
import exceptions.CannotExecuteCommandException;
import fileWork.CityFieldsReader;

import java.io.PrintStream;
import java.util.ArrayList;

public class RemoveLower extends Command {
    private final CollectionManager collectionManager;
    private final CityFieldsReader cityFieldsReader;
    private ArrayList<Object> result = new ArrayList<>();

    public RemoveLower(CollectionManager collectionManager, CityFieldsReader cityFieldsReader) {
        super("remove_lower");
        this.collectionManager = collectionManager;
        this.cityFieldsReader = cityFieldsReader;
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            result.clear();  // Очистим список перед использованием
            if (arguments.length != 0) {
                throw new CannotExecuteCommandException("Эта команда не принимает аргументы.");
            }
            // Создаем новый элемент и читаем его поля
            City cityToRemove = cityFieldsReader.read();
            // Устанавливаем результат для передачи на сервер
            result.add(cityToRemove);
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            if (result.isEmpty()) {
                throw new CannotExecuteCommandException("Ошибка: результат пуст. Команда должна быть выполнена на стороне клиента перед отправкой на сервер.");
            }
            // Получаем элемент, созданный на клиенте
            City cityToRemove = (City) result.get(0);
            collectionManager.removeLower(cityToRemove, printStream);
            printStream.println("Элементы с площадью меньше " + cityToRemove.getArea() + " были удалены.");
        }
    }

    @Override
    public String getDescription() {
        return "удаляет из коллекции все элементы, площадь которых меньше заданной";
    }

    @Override
    public ArrayList<Object> getResult() {
        return result;
    }

    public void setResult(ArrayList<Object> result) {
        this.result = result;
    }
}
