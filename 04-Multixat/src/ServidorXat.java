import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {
    public static final int PORT = 9999;
    public static final String HOST = "localhost", MSG_SORTIR = "sortir";
    private Hashtable<String, GestorClients> clients;
    private boolean sortir;
    private ServerSocket serverSocket;

    public void servidorAEscoltar() {
        

    }

    public void pararServidor() throws IOException {
        serverSocket.close();
    }

    public static void finalitzarXat() {
        try {
            ObjectOutputStream output = new ObjectOutputStream(clientSocket.getOutputStream());
            output.writeObject(MSG_SORTIR);
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        clients.clear();
        System.exit(0);
    }
    
    public void afegirClient(GestorClients gestorClients) {
        clients.put(gestorClients.getNom(), gestorClients);
    }
}
