public class Main {

    public static void main(String[] args) {

        Client client = new Client("Client-1");
        new Thread(client).start();
    }
}
