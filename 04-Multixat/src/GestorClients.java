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
    private boolean sortir;

    public GestorClients(Socket client, ServidorXat servidorXat) throws IOException {
        this.client = client;
        this.servidorXat = servidorXat;
        output = new ObjectOutputStream(client.getOutputStream());
        input = new ObjectInputStream(client.getInputStream());
    }

    public String getNom() {
        return nom;
    }

    public void enviaMissatge(String nom, Missatge missatge) throws IOException {
        output.writeObject("Missatge de (" + nom + "): " + missatge);
    }
    
    public static void processaMissatge(String missatgeRaw) {
        String codi = Missatge.getCodiMissatge(missatgeRaw);

        switch (codi) {
            case Missatge.CODI_CONECTAR:
                
                break;
            case Missatge.CODI_SORTIR_CLIENT:
               
                break;
            case Missatge.CODI_SORTIR_TOTS:

                break;
            case Missatge.CODI_MSG_PERSONAL:

                break;
            case Missatge.CODI_MSG_GRUP:

                break;
        
            default:
                // lanza error
                break;
        }


    }

    @Override
    public void run() {

  
    }


    
}
