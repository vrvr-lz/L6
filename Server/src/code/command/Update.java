package code.command;

import code.collection.CollectionManager;
import code.command.abstr.Command;
import code.command.abstr.InvocationStatus;
import collectionn.CityFieldValidation;
import exceptions.CannotExecuteCommandException;
import io.UserIO;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.NoSuchElementException;

public class Update extends Command {
    private final CollectionManager collectionManager;
    private final UserIO userIO;
    private ArrayList<Object> result = new ArrayList<>();

    public Update(CollectionManager collectionManager, UserIO userIO) {
        super("update");
        this.collectionManager = collectionManager;
        this.userIO = userIO;
    }

    @Override
    public void execute(String[] arguments, InvocationStatus invocationStatus, PrintStream printStream) throws CannotExecuteCommandException {
        if (invocationStatus.equals(InvocationStatus.CLIENT)) {
            result = new ArrayList<>();
            try {
                if (arguments.length != 1) {
                    throw new CannotExecuteCommandException("Количество аргументов данной команды должно равняться 1.");
                }
                if (!CityFieldValidation.validate("id", arguments[0])) {
                    throw new CannotExecuteCommandException("Введены невалидные аргументы: id = " + arguments[0]);
                } else {
                    result.add(arguments[0]);
                    printStream.println(CollectionManager.getFieldNames());
                    printStream.println("\nВыберите поля для изменения (оставьте поле пустым для завершения):");
                    String line;

                    boolean isInputEnd = false;

                    do {
                        line = userIO.readLine().trim();
                        if (line.isEmpty()) {
                            isInputEnd = true;
                        } else {
                            String[] parts = line.split("\\s+");
                            if (parts.length == 1) {
                                if (CityFieldValidation.validate(parts[0], "")) {
                                    result.add(parts[0] + ";");
                                } else {
                                    printStream.println("Введены некорректные данные: \"" + parts[0] + "\" + null");
                                }
                            } else if (parts.length == 2) {
                                if (CityFieldValidation.validate(parts[0], parts[1])) {
                                    result.add(parts[0] + ";" + parts[1]);
                                } else {
                                    printStream.println("Введены некорректные данные: " + parts[0] + " + " + parts[1]);
                                }
                            }
                        }
                    } while (!isInputEnd);
                }
            } catch (NoSuchElementException ex) {
                throw new CannotExecuteCommandException("Сканнер достиг конца файла.");
            }
        } else if (invocationStatus.equals(InvocationStatus.SERVER)) {
            String[] spArguments = result.toArray(new String[0]);
            Integer id = Integer.parseInt(spArguments[0]);
            if (collectionManager.containsKey(id)) {
                for (int i = 1; i < spArguments.length; i++) {
                    String[] subStr = spArguments[i].split(";");
                    collectionManager.update(id, subStr[0], subStr[1], printStream);
                }
                printStream.println("Указанные поля были заменены.");
            } else {
                printStream.println("Элемента с указанным id не существует");
            }
        }
    }

    @Override
    public String getDescription() {
        return "изменяет указанное поле выбранного по id элемента коллекции";
    }

    @Override
    public ArrayList<Object> getResult() {
        return result;
    }

    public void setResult(ArrayList<Object> result) {
        this.result = result;
    }
}
