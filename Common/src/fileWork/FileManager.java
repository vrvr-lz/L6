package fileWork;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;

public class FileManager {

    /**
     *
     * @param filePath
     * @return
     */
    public String readCollection(String filePath) {
        FileInputStream fileInputStream = null;
        InputStreamReader inputStreamReader = null;

        StringBuilder sb = new StringBuilder();

        try {
            // Проверяем, является ли файл корректным XML-документом
            if (!isValidXml(filePath)) {
                throw new IOException("Файл поврежден, объект не может быть прочитан");
            }

            fileInputStream = new FileInputStream(filePath);
            inputStreamReader = new InputStreamReader(fileInputStream);

            while (inputStreamReader.ready()) {
                sb.append((char) inputStreamReader.read());
            }
        } catch (IOException ex) {
            System.err.println("Произошла ошибка при добавлении файла во входящий поток " + ex);
            System.exit(-1);
        } catch (NullPointerException ex) {
            System.err.println("Не указан файл, из которого следует читать данные " + ex);
            System.exit(-1);
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (inputStreamReader != null) {
                    inputStreamReader.close();
                }
            } catch (IOException ex) {
                System.err.println("Произошла ошибка при закрытии " + ex);
            }
        }
        return sb.toString();
    }


    //Метод, записывающий данные в указанный файл.

    /**
     *
     * @param str
     * @param filePath
     */
    public void writeCollection(String str, String filePath) {
        PrintWriter printWriter = null;

        try {
            printWriter = new PrintWriter(filePath);
            printWriter.print(str);
        } catch (IOException ex) {
            System.err.println("Произошла ошибка при добавлении файла в исходящий поток\n" + ex);
        } catch (NullPointerException ex) {
            System.err.println("Не указан файл, куда следует записывать данные " + ex);
        } finally {
            if (printWriter != null) {
                printWriter.close();
            }
        }
    }

    /**
     *
     * @param filePath
     * @return
     */

    // Метод для проверки, является ли файл корректным XML-документом
    private boolean isValidXml(String filePath) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filePath));
            return true;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            return false;
        }
    }
}
