import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public void connectar() throws Exception {
        socket = new Socket(Servidor.HOST, Servidor.PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
        input = new ObjectInputStream(socket.getInputStream());
        System.out.println("Connectant a -> " + Servidor.HOST + ":" + Servidor.PORT);
        System.out.println("Connexió acceptada: " + socket.getInetAddress());
    }

    public void rebreFitxers(String nomFitxer, String ruta) throws IOException, ClassNotFoundException {
        output.writeObject(nomFitxer);
        output.flush();

        byte[] fitxer = (byte[]) input.readObject();
        if (fitxer == null) {
            return;
        }
    
        Path desti = Paths.get(ruta);
        Files.write(desti, fitxer);
        System.out.println("Nom del fitxer a guardar: " + nomFitxer);
        System.out.println("Fitxer rebut i guardat com: " + desti.toAbsolutePath());
    }

    public void tancarConnexio() throws IOException {
        socket.close();
        System.out.println("Connexió tancada.");
    }

    public static void main(String[] args) {
        Client client = new Client();
        Scanner scanner = new Scanner(System.in);
        try {
            client.connectar();
            while (true) {
                System.out.println("Nom del fitxer a rebre ('sortir' per sortir): ");
                String nom = scanner.nextLine().trim();
                if (nom.equalsIgnoreCase("sortir")) {
                    break;
                }
                System.out.print("Ruta local on guardar (ex: C:/Users/Andrés/Desktop/nom.txt): ");
                String rutaLocal = scanner.nextLine().trim();
                client.rebreFitxers(nom, rutaLocal);
            }
            client.tancarConnexio();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }
}
