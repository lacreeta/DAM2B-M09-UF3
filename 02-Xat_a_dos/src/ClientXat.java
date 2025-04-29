import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientXat {

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    public void connecta() {
        try {
            socket = new Socket("localhost", 9999);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
            System.out.println("Client connectat a localhost:9999");
            System.out.println("Flux d'entrada i sortida creat.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public void enviarMissatge(String missatge) {
        try {
            out.writeObject(missatge);
            out.flush();
        } catch (IOException e) {
            System.err.println("Error al enviar mensaje: " + e.getMessage());
        }
    }

    public void tancarClient() {
        try {
            socket.close();
            out.close();
            in.close();
            System.out.println("Tancant client...");
            System.out.println("Client tancat.");
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        client.connecta();
        FilLectorCX lector = new FilLectorCX(client.out);
        lector.start();
        
        System.out.println("Rebut: Escriu el teu nom:");
        try {
            String missatge;
            while (!(missatge = (String) client.in.readObject()).equalsIgnoreCase("sortir")) {
                System.out.println("Missatge ('sortir' per tancar): Rebut: " + missatge);
            }
            lector.join();
            client.tancarClient();
            System.out.println("El servidor ha tancat la connexió.");
        } catch (InterruptedException | IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
