import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    private final int PORT = 7777;
    private final String HOST = "localhost";
    private Socket socket;
    private PrintWriter out;

    public void connecta() {
        try {
            socket = new Socket(HOST, PORT);
            out = new PrintWriter(socket.getOutputStream(), true);
            System.out.println("Connectat a servidor en: " + HOST + ":" + PORT);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void tanca() {
        try {
            socket.close();
            out.close();
            System.out.println("Client tancat");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void envia(String message) {
        out.println("Enviat al servidor: " + message);
    }
    public static void main(String[] args) {
        Client client = new Client();
        client.connecta();
        client.envia("Prova d'enviament 1");
        client.envia("Prova d'enviament 2");
        client.envia("Adéu!");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Prem Enter per tancar el client...");
        try {
            reader.readLine();
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
        client.tanca();
    }
}
