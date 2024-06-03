package code;

import fileWork.CommandContainer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class ClientResponseSender {
    private static final Logger rootLogger = LogManager.getRootLogger();
    private final DatagramChannel clientChannel;

    public ClientResponseSender(DatagramChannel clientChannel) {
        this.clientChannel = clientChannel;
    }

    public void sendContainer(CommandContainer commandContainer, InetSocketAddress inetSocketAddress) throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(4096);
        byteBuffer.clear();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(commandContainer);
        oos.flush();

        String encodedCommand = Base64.getEncoder().encodeToString(baos.toByteArray());
        byteBuffer.put(encodedCommand.getBytes(StandardCharsets.UTF_8));
        byteBuffer.flip();

        rootLogger.info("Отправка данных на сервер: " + inetSocketAddress.toString());
        clientChannel.send(byteBuffer, inetSocketAddress);
    }
}
