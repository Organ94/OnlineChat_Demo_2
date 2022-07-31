import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    static final ExecutorService executorService = Executors.newFixedThreadPool(2);
    static List<SocketChannel> listSocket = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        final ServerSocketChannel server = ServerSocketChannel.open();
        server.bind(new InetSocketAddress("localhost", 8080));

        while (true) {
//            try (SocketChannel socketChannel = server.accept()) {
//                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);
//
//                while (socketChannel.isConnected()) {
//                    int byteCount = socketChannel.read(inputBuffer);
//
//                    if (byteCount == -1) {
//                        break;
//                    }
//
//                    final String msg = new String(inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8);
//                    inputBuffer.clear();
//                    System.out.println("Получено сообщение от клиента: " + msg);
//
//                    socketChannel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
            SocketChannel socketChannel = server.accept();

            executorService.execute(() -> {
                try (SocketChannel socketChannel1 = socketChannel){
                    listSocket.add(socketChannel1);
                    serve(socketChannel1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
                }
            }

            private static void serve(final SocketChannel socketChannel) throws IOException {
                final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

                if (socketChannel.isConnected()) {
                    socketChannel.write(ByteBuffer.wrap(
                            "Socket connected...".getBytes(StandardCharsets.UTF_8)));
                }
                while (socketChannel.isConnected()) {
                    int byteCount = socketChannel.read(inputBuffer);

                    if (byteCount == -1) {
                        break;
                    }

                    final String msg = new String(inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8);
                    if (msg.equalsIgnoreCase("exit")) {
                        socketChannel.write(ByteBuffer.wrap("socket disconnecting...".getBytes(StandardCharsets.UTF_8)));
                        socketChannel.close();
                        break;
                    }
                    for (SocketChannel channel : listSocket) {
                        channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
                    }
                    inputBuffer.clear();
                    System.out.println("Получено сообщение от клиента: " + msg);
//
//                    for (SocketChannel channel : listSocket) {
//                        channel.write(ByteBuffer.wrap(msg.getBytes(StandardCharsets.UTF_8)));
//                    }
        }
    }
}
