import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class GestorClients extends Thread {
    private Socket client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServidorXat servidorXat;
    private String nom;
    private boolean sortir = false;

    public GestorClients(Socket client, ServidorXat servidorXat) throws IOException {
        this.client = client;
        this.servidorXat = servidorXat;
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }

    public String getNom() {
        return nom;
    }

    public void enviarMissatge(String nom, String missatge) throws IOException {
        if (missatge == null || missatge.trim().isEmpty()) return;
        output.writeObject("Missatge de (" + nom + "): " + missatge);
        output.flush();
    }
    
    public void processaMissatge(String missatgeRaw) throws IOException {
        String codi = Missatge.getCodiMissatge(missatgeRaw);
        String[] parts = Missatge.getPartsMissatge(missatgeRaw);
        switch (codi) {
            case Missatge.CODI_CONECTAR:
                nom = parts[1];
                servidorXat.afegirClient(this);                
                break;
            case Missatge.CODI_SORTIR_CLIENT:
                servidorXat.eliminarClient(nom);
                sortir = true;
                break;
            case Missatge.CODI_SORTIR_TOTS:
                sortir = true;
                servidorXat.finalitzarXat();
                break;
            case Missatge.CODI_MSG_PERSONAL:
                String destinatari = parts[1];
                String msg = parts[2];
                servidorXat.enviarMissatgePersonal(destinatari, nom, msg);
                break;
            case Missatge.CODI_MSG_GRUP:
                String msgGrupal = parts[1];
                servidorXat.enviarMissatgeGrup(msgGrupal);
                break;
            default:
                // lanza error
                break;
        }


    }

    @Override
    public void run() {
        try {
            while (!sortir) {
                String mensajeRaw = (String) input.readObject();
                processaMissatge(mensajeRaw);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error en comunicación con cliente " + nom + ": " + e.getMessage());
        } finally {
            try {
                servidorXat.eliminarClient(nom);
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    
}
