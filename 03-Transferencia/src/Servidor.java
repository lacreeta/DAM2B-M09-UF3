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
        ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
        System.out.println("Esperant el nom del fitxer del client...");
        ObjectInputStream input = new ObjectInputStream(clientSocket.getInputStream());
        Fitxer fitxer = new Fitxer((String) input.readObject());
        System.out.println("Nomfitxer rebut: " + fitxer.getNom());
        byte[] contingut = fitxer.getContingut();
        System.out.println("Contingut del fitxer a enviar: " + contingut.length + " bytes");
        System.out.println("Fitxer enviat al client: " + fitxer.getRuta());
        output.writeObject(contingut);
        output.flush();
    }
    
    public static void main(String[] args) {
        Servidor servidor = new Servidor();
        try {
            servidor.connectar();
            
            servidor.enviarFitxers();
            
            servidor.tancarConnexio(servidor.clientSocket);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
