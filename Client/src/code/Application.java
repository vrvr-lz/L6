package code;

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
import io.UserIO;

import java.io.IOException;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Locale;
import java.util.NoSuchElementException;

public class Application {
    private final PrintStream printStream = System.out;
    private final UserIO userIO;
    private final int port;

    public Application(Integer port) {
        this.port = port;
        this.userIO = new UserIO();
        printStream.println("Конструктор класса Application был загружен");
    }

    public void start() {
        try {
            InetSocketAddress inetSocketAddress = new InetSocketAddress("localhost", port);

            ClientConnection clientConnection = new ClientConnection();
            clientConnection.connect(inetSocketAddress);

            ClientResponseSender responseSender = new ClientResponseSender(clientConnection.getClientChannel());
            ClientRequestReader requestReader = new ClientRequestReader(clientConnection.getClientChannel());

            printStream.println("Клиент готов к чтению команд.");
            boolean isConnected = true;
            boolean isNeedInput = true;
            boolean isCommandAcceptable = false;

            String line = "";

            while (isConnected) {
                if (isNeedInput) {
                    System.out.println("Введите название команды:");
                    userIO.printPreamble();
                    line = userIO.readLine();
                    isCommandAcceptable = true; // Клиент теперь всегда принимает команду для отправки на сервер
                }
                try {
                    if (isCommandAcceptable) {
                        if (line.trim().startsWith("add")) {
                            String[] parts = line.trim().split("\\s+");
                            String[] arguments = parts.length > 1 ? new String[]{parts[1]} : new String[]{};

                            Add addCommand = new Add(null, new CityFieldsReader(userIO));
                            addCommand.execute(arguments, InvocationStatus.CLIENT, printStream);

                            CommandContainer commandContainer = new CommandContainer(addCommand.getName(), (ArrayList<Object>) addCommand.getResult());
                            responseSender.sendContainer(commandContainer, inetSocketAddress);
                        } else if (line.trim().startsWith("add_if_max")) {
                            AddIfMax addIfMaxCommand = new AddIfMax(null, new CityFieldsReader(userIO));
                            addIfMaxCommand.execute(new String[]{}, InvocationStatus.CLIENT, printStream);

                            CommandContainer commandContainer = new CommandContainer(addIfMaxCommand.getName(), (ArrayList<Object>) addIfMaxCommand.getResult());
                            responseSender.sendContainer(commandContainer, inetSocketAddress);
                        } else if (line.trim().startsWith("add_if_min")) {
                            AddIfMin addIfMinCommand = new AddIfMin(null, new CityFieldsReader(userIO));
                            addIfMinCommand.execute(new String[]{}, InvocationStatus.CLIENT, printStream);

                            CommandContainer commandContainer = new CommandContainer(addIfMinCommand.getName(), (ArrayList<Object>) addIfMinCommand.getResult());
                            responseSender.sendContainer(commandContainer, inetSocketAddress);
                        } else if (line.trim().startsWith("remove_lower")) {
                            RemoveLower removeLowerCommand = new RemoveLower(null, new CityFieldsReader(userIO));
                            removeLowerCommand.execute(new String[]{}, InvocationStatus.CLIENT, printStream);

                            CommandContainer commandContainer = new CommandContainer(removeLowerCommand.getName(), (ArrayList<Object>) removeLowerCommand.getResult());
                            responseSender.sendContainer(commandContainer, inetSocketAddress);
                        } else if (line.trim().startsWith("update")) {
                            String[] parts = line.trim().split("\\s+");
                            String[] arguments = parts.length > 1 ? new String[]{parts[1]} : new String[]{};

                            Update updateCommand = new Update(null, userIO);
                            updateCommand.execute(arguments, InvocationStatus.CLIENT, printStream);

                            CommandContainer commandContainer = new CommandContainer(updateCommand.getName(), (ArrayList<Object>) updateCommand.getResult());
                            responseSender.sendContainer(commandContainer, inetSocketAddress);
                        } else if (line.trim().startsWith("execute_script")) {
                            String[] parts = line.trim().split("\\s+");
                            if (parts.length < 2) {
                                System.out.println("Ошибка: необходимо указать путь к файлу скрипта.");
                                continue;
                            }
                            String scriptPath = parts[1];

                            ExecuteScriptCommand executeScriptCommand = new ExecuteScriptCommand(null);
                            executeScriptCommand.execute(new String[]{scriptPath}, InvocationStatus.CLIENT, printStream);

                            CommandContainer commandContainer = new CommandContainer(executeScriptCommand.getName(), (ArrayList<Object>) executeScriptCommand.getResult());
                            responseSender.sendContainer(commandContainer, inetSocketAddress);
                        } else {
                            // Отправка других команд
                            CommandContainer commandContainer = new CommandContainer(line, new ArrayList<>());
                            responseSender.sendContainer(commandContainer, inetSocketAddress);
                        }

                        // Получение ответа от сервера
                        ByteBuffer buffer = requestReader.receiveBuffer();
                        String response = new String(buffer.array(), 0, buffer.limit(), StandardCharsets.UTF_8);
                        System.out.println("Ответ от сервера: " + response);

                        isNeedInput = true;
                    }
                } catch (PortUnreachableException | SocketTimeoutException ex) {
                    if (ex instanceof PortUnreachableException) {
                        printStream.println("Порт " + port + " не доступен. Повторить отправку команды? y/n");
                    } else {
                        printStream.println("Сервер не отвечает. Повторить отправку команды? y/n");
                    }
                    String result = userIO.readLine().trim().toLowerCase(Locale.ROOT).split("\\s+")[0];
                    if (result.equals("n")) {
                        printStream.println("Завершение работы клиента");
                        isConnected = false;
                    } else {
                        isNeedInput = false;
                    }
                } catch (CannotExecuteCommandException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (NoSuchElementException ex) {
            printStream.println("\nАварийное завершение работы.");
        } catch (SocketException ex) {
            printStream.println("Ошибка подключения сокета к порту, или сокет не может быть открыт."
                    + ex.getMessage() + "\n" + "localhost" + " ; " + port);
        } catch (IllegalArgumentException ex) {
            printStream.println("Порт не принадлежит ОДЗ: " + port);
        } catch (IOException ex) {
            printStream.println("Ошибка IOE");
        } catch (InterruptedException ex) {
            printStream.println("Ошибка InterruptedException");
        }
    }
}
