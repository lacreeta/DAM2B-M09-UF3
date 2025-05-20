import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Hashtable;

public class ServidorXat {
    public static final int PORT = 9999;
    public static final String HOST = "localhost", MSG_SORTIR = "sortir";
    private Hashtable<String, GestorClients> clients = new Hashtable<>();
    private boolean sortir = false;
    private ServerSocket serverSocket;

    public void servidorAEscoltar()throws IOException {
        serverSocket = new ServerSocket(PORT);
        System.out.println("Servidor iniciat a " + HOST + ":" + PORT);
        while (!sortir) {
            Socket clientSocket = serverSocket.accept();
            System.out.println("Client connectat: " + ServidorXat.HOST + ":" + ServidorXat.PORT);
            GestorClients gc = new GestorClients(clientSocket, this);
            gc.start();
        }
    }

    public void pararServidor() {
        try {
            serverSocket.close();
            System.out.println("Servidor aturat.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void finalitzarXat() throws IOException {
        enviarMissatgeGrup(MSG_SORTIR);
        System.out.println("DEBUG: multicast sortir");
        clients.clear();
        sortir = true;
        System.out.println("Xat finalitzat.");
    }
    
    public void afegirClient(GestorClients gestorClients) {
        clients.put(gestorClients.getNom(), gestorClients);
        System.out.println(gestorClients.getNom() + " connectat.");
        try {
            enviarMissatgeGrup("DEBUG: multicast Entra: " + gestorClients.getNom());
            System.out.println("DEBUG: multicast Entra: " + gestorClients.getNom());
        } catch (IOException e) {
            System.out.println("ERROR: No s'ha pogut enviar l'entrada al grup.");
        }
    }

    public void eliminarClient(String nom) {
        if (clients.containsKey(nom)) {
            clients.remove(nom);
            System.out.println("Client eliminat: " + nom);
        }
    }

    public void enviarMissatgeGrup(String msgGrupal) throws IOException {
        for (GestorClients gc : clients.values()) {
            gc.enviarMissatge("grup", msgGrupal);
        }
    }

    public void enviarMissatgePersonal(String destinatario, String remitente, String missatge) throws IOException {
        GestorClients gc = clients.get(destinatario);
        if (gc != null) {
            gc.enviarMissatge(remitente, missatge);
            System.out.println("Missatge personal per (" + destinatario + ") de (" + remitente + "): " + missatge);
        } else {
            System.out.println("Destinatari no trobat: " + destinatario);
        }
    }

    public static void main(String[] args) {
        ServidorXat servidor = new ServidorXat();
        try {
            servidor.servidorAEscoltar();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            servidor.pararServidor();
        }
    }
}
