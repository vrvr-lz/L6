package code.server;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.PrintStream;

public class MainServer {
    PrintStream printStream = System.out;
    private static final Logger rootLogger = LogManager.getRootLogger();
    public static void main(String[] args){
        PrintStream printStream = System.out;
        Application application = new Application();

        if (args.length > 0) {
            if (!args[0].equals("")) {
                System.out.println("Старт с аргументом");
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    printStream.println("Сохранение коллекции в файле.");
                    application.getCollectionManager().save(args[0]);
                    printStream.println("Коллекция была сохранена "+ args[0]);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                        printStream.println("Ошибка с потоками: "+ ex);
                    }
                    printStream.println("Завершение работы сервера.");
                }));
                try {
                    application.start(args[0]);
                } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException ex) {
                    rootLogger.warn("По указанному адресу нет подходящего файла "+ args[0]);
                }
            }
        }
        else {
            System.out.println("Старт без аргумента");
            String file = "C:\\Users\\Kat\\IdeaProjects\\lb6\\filess\\MyFile.xml";
            try {
                application.start(file);
            } catch (ParserConfigurationException | IOException | org.xml.sax.SAXException ex ) {
                rootLogger.warn("По указанному адресу нет подходящего файла " + file);
            }
        }
    }
}