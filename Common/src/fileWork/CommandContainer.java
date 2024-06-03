package fileWork;

import java.io.Serializable;
import java.util.ArrayList;

public class CommandContainer implements Serializable {
    private final String name;
    private final ArrayList<Object> arguments; // Поля для хранения аргументов

    public CommandContainer(String name, ArrayList<Object> arguments) {
        this.name = name;
        this.arguments = arguments;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Object> getArguments() { // Метод для получения аргументов
        return arguments;
    }
}
