import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {
    
    private final int PORT = 7777;
    private final String HOST = "localhost";
    private ServerSocket srvSocket;
    private Socket clientSocket;

    public void connecta() {
        try {
            srvSocket = new ServerSocket(PORT); // obrir conexió
            System.out.println("Servidor en marxa a " + HOST + ":" + PORT);
            clientSocket = srvSocket.accept(); // accepta conexió
        } catch (Exception e) {
            System.err.println("Error al conectar-se: " + e.getMessage());
        }
    }

    public void repDades() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
            String linia;
            while ((linia = reader.readLine()) != null) {
                System.out.println("Rebut: " + linia);
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void tanca() {
        try {
            srvSocket.close();
            clientSocket.close();
            System.out.println("Servidor tancat.");
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        servidor.connecta();
        servidor.repDades();
        servidor.tanca();
    }
}