import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Client implements Runnable {

    private static InetSocketAddress socketAddress;
    private static SocketChannel socketChannel;
    private static String name;

    public Client(String name) {
        Client.name = name;
        try {
            socketAddress = new InetSocketAddress("localhost", 8080);
            socketChannel = SocketChannel.open();
            socketChannel.connect(socketAddress);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            String msg;
            while (true) {

                System.out.print("Введите сообщение или exit для выхода\n>> ");
                msg = scanner.nextLine();


                socketChannel.write(ByteBuffer.wrap(
                        (Thread.currentThread().getName() + ": " + msg + "\n").getBytes(StandardCharsets.UTF_8)));
                Thread.sleep(1000);
                int byteCount = socketChannel.read(inputBuffer);
                    System.out.println(new String(inputBuffer.array(), 0, byteCount, StandardCharsets.UTF_8));

                if (msg.equalsIgnoreCase("exit")) {
                    socketChannel.close();
                    break;
                }


                inputBuffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                socketChannel.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
