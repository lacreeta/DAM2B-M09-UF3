import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Client {
    public static final String DIR_ARRIBADA = System.getProperty("java.io.tmpdir");
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;

    public void connectar() throws Exception {
        socket = new Socket(Servidor.HOST, Servidor.PORT);
        System.out.println("Connectant a -> " + Servidor.HOST + ":" + Servidor.PORT);
        System.out.println("Connexió acceptada: " + socket.getInetAddress());
    }

    public void rebreFitxers() throws IOException, ClassNotFoundException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Nom del fitxer a rebre ('sortir' per sortir): ");
        String nomFitxer = scanner.nextLine();

        if (nomFitxer == null || nomFitxer.trim().isEmpty() || nomFitxer.equalsIgnoreCase("sortir")) {
            System.out.println("Sortint...");
            scanner.close();
            return;
        }

        output = new ObjectOutputStream(socket.getOutputStream());
        output.writeObject(nomFitxer);
        output.flush();

        input = new ObjectInputStream(socket.getInputStream());
        byte[] fitxer = (byte[]) input.readObject();

        String nomFitxerGuardat = DIR_ARRIBADA + File.separator + Paths.get(nomFitxer).getFileName();
        System.out.println("Nom del fitxer a guardar: " + nomFitxerGuardat);

        Files.write(Paths.get(nomFitxerGuardat), fitxer);
        System.out.println("Fitxer rebut i guardat com: " + nomFitxerGuardat);

        scanner.close();
    }

    public void tancarConnexio() throws IOException {
        socket.close();
        System.out.println("Connexió tancada.");
    }

    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.connectar();
            client.rebreFitxers();
            client.tancarConnexio();
        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
