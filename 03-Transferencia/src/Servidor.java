import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor {

    public static final int PORT = 9999;
    public static final String HOST = "localhost";
    private ServerSocket serverSocket;
    private Socket clientSocket;


    public void connectar() {
        try {
            System.out.println("Acceptant connexions en -> " + HOST + ":" + PORT);
            serverSocket = new ServerSocket(PORT);
            System.out.println("Esperant connexio...");
            clientSocket = serverSocket.accept();
            System.out.println("Connexió acceptada: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            System.err.println("Error al conectar-se: " + e.getMessage());
        }
    }

    public void tancarConnexio(Socket socket) {
        try {
            socket.close();
            serverSocket.close();
            System.out.println("Tancat connexió amb el client: " + clientSocket.getInetAddress());
        } catch (Exception e) {
            System.err.println("Error al desconectar-se: " + e.getMessage());
        }
    }

    public void enviarFitxers() throws IOException, ClassNotFoundException {
        try (ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream())) {
            while (true) {
                String nomFitxer = (String) input.readObject();
                Fitxer fitxer = new Fitxer(nomFitxer);

                if (fitxer.getNom().equalsIgnoreCase("sortir")) {
                    System.out.println("Client ha demanat sortir.");
                    break;
                }
                System.out.println("Nom fitxer rebut: " + fitxer.getNom());
                byte[] contingut = fitxer.getContingut();
                if (contingut == null) {
                    System.out.println("Error llegint el fitxer del client: null");
                    output.writeObject(null);
                    System.out.println("Nom del fitxer buit o nul. Sortint...");
                    return;
                } else {
                    System.out.println("Contingut del fitxer del client: " + contingut.length + " bytes");
                    System.out.println("Fitxer enviat al client: " + fitxer.getRuta());
                    output.writeObject(contingut);
                }
                output.flush();
            }
        }
    }
    
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        try {
            servidor.connectar();
            System.out.println("Esperant el nom del fitxer del client...");
            servidor.enviarFitxers();
            servidor.tancarConnexio(servidor.clientSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
