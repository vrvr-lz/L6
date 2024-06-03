package code.server;

import fileWork.CommandContainer;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ServerRequestReader {
    private static final int BUFFER_SIZE = 4096;
    PrintStream printStream = System.out;

    private final DatagramSocket serverSocket;
    private final byte[] buffer = new byte[BUFFER_SIZE];

    private InetAddress senderAddress;
    private int senderPort;
    private CommandContainer commandContainer;

    public ServerRequestReader(DatagramSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void readCommand() throws IOException, ClassNotFoundException {
        DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
        serverSocket.receive(dp);

        senderAddress = dp.getAddress();
        senderPort = dp.getPort();

        String str = new String(dp.getData(), 0, dp.getLength(), StandardCharsets.UTF_8);
        byte[] byteArr = Base64.getDecoder().decode(str);

        try (ByteArrayInputStream bais = new ByteArrayInputStream(byteArr);
             ObjectInputStream ois = new ObjectInputStream(bais)) {
            printStream.println("Получен пакет с командой от " + senderAddress.getHostAddress() + " " + senderPort);
            commandContainer = (CommandContainer) ois.readObject();
            printStream.println("Контейнер с командой получен");
        }
    }

    public CommandContainer getCommandContainer() {
        return commandContainer;
    }

    public InetAddress getSenderAddress() {
        return senderAddress;
    }

    public int getSenderPort() {
        return senderPort;
    }
}
