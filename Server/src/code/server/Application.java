package code.server;

import code.collection.CollectionManager;
import code.collection.CommandInvoker;
import collectionn.City;
import fileWork.CityFieldsReader;
import fileWork.CommandContainer;
import fileWork.FileManager;
import fileWork.XmlParser;
import io.UserIO;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Application {
    private CollectionManager collectionManager;
    private FileManager fileManager;
    private XmlParser xmlParser;
    private UserIO userIO;
    private CommandInvoker commandInvoker;
    private ServerConnection serverConnection;
    private boolean isConnected;
    private PrintStream printStream = System.out;

    private static final Logger rootLogger = LogManager.getRootLogger();

    public Application() {
        collectionManager = new CollectionManager();
        fileManager = new FileManager();
        xmlParser = new XmlParser();
        userIO = new UserIO();
        printStream.println("Конструктор класса Application был загружен.");
    }

    public void start(String inputFile) throws IOException, ParserConfigurationException, SAXException {
        try {
            File ioFile = new File(inputFile);
            if (!ioFile.canWrite() || ioFile.isDirectory() || !ioFile.isFile()) throw new IOException();
            String file = fileManager.readCollection(inputFile);

            // Проверка, если файл пустой
            if (file.trim().isEmpty()) {
                printStream.println("Файл пустой, инициализация пустой коллекции.");
            } else {
                City[] cities = xmlParser.parseToCollection(new InputSource(new StringReader(file)));
                for (City city : cities) collectionManager.add(city, System.out);
                rootLogger.printf(Level.INFO, "Элементы коллекций из файла %1$s были загружены.", inputFile);
            }

            this.commandInvoker = new CommandInvoker(collectionManager, inputFile);

            serverConnection = new ServerConnection(); // Здесь хранится datagramSocket сервера.

            Scanner scanner = new Scanner(System.in);

            do {
                System.out.print("Введите порт: ");
                int port = scanner.nextInt();
                if (port <= 0) {
                    printStream.println("Введенный порт невалиден.");
                } else {
                    isConnected = serverConnection.createFromPort(port); // Создание datagramSocket для сервера
                }
            } while (!isConnected);
            printStream.println("Порт установлен.");
        } catch (NoSuchElementException ex) {
            printStream.println("Аварийное завершение работы");
            System.exit(-1);
        } catch (IOException | SAXException | ParserConfigurationException ex) {
            printStream.println("Ошибка при загрузке коллекции: " + ex.getMessage());
            printStream.println("Инициализация пустой коллекции.");
        }

        try {
            cycle(commandInvoker);
        } catch (NoSuchElementException | InterruptedException ex) {
            printStream.println(ex.getMessage());
            printStream.println("Работа сервера завершена.");
        }
    }

    private void cycle(CommandInvoker commandInvoker) throws InterruptedException {
        ServerRequestReader requestReader = new ServerRequestReader(serverConnection.getServerSocket()); // Создание читателя запросов
        ServerResponseSender responseSender = new ServerResponseSender(serverConnection.getServerSocket()); // Создание отправителя запросов
        CommandProcessor commandProcessor = new CommandProcessor(commandInvoker, collectionManager, new CityFieldsReader(userIO)); // Создание объекта, работающего с командами

        while (isConnected) {
            try {
                requestReader.readCommand();
                CommandContainer command = requestReader.getCommandContainer();

                if (command == null) {
                    printStream.println("Ошибка: получена null команда.");
                    continue;
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                PrintStream printStream = new PrintStream(baos);

                commandProcessor.executeCommand(command, printStream);

                Thread.sleep(1000);
                responseSender.send(baos.toString(), requestReader.getSenderAddress(), requestReader.getSenderPort());
                printStream.println("Пакет был отправлен " + requestReader.getSenderAddress().getHostAddress() + " " + requestReader.getSenderPort());
            } catch (IOException ex) {
                printStream.println("Произошла ошибка при чтении: " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                printStream.println("Неизвестная ошибка: " + ex);
            }
        }
    }

    public CollectionManager getCollectionManager() {
        return collectionManager;
    }
}
