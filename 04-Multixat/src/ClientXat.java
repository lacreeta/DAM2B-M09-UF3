import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientXat extends Thread {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean sortir;
    
    public void connecta() throws Exception {
        socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
    }
    
    public void enviarMissatge(String missatge) throws IOException {
        output.writeObject(missatge);
        output.flush();
    }

    public void tancarClient() throws IOException {
        output.close();
        input.close();
        socket.close();
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            while (!sortir) {
                Object rebut = input.readObject();
                if (rebut instanceof String missatgeRaw) {
                    String codi = Missatge.getCodiMissatge(missatgeRaw);
                    String[] parts = Missatge.getPartsMissatge(missatgeRaw);
                    if (codi != null) {
                        switch (codi) {
                            case Missatge.CODI_MSG_GRUP:
                                System.out.println("Missatge de grup: " + parts[1]);
                                break;
                            case Missatge.CODI_MSG_PERSONAL:
                                System.out.println("Missatge personal de " + parts[1] + ": " + parts[2]);
                                break;
                            case Missatge.CODI_SORTIR_CLIENT:
                                System.out.println("T'han desconnectat del servidor.");
                                sortir = true;
                                break;
                            case Missatge.CODI_SORTIR_TOTS:
                                System.out.println("Servidor tancat per a tots.");
                                sortir = true;
                                break;
                            default:
                                System.out.println("Missatge desconegut: " + missatgeRaw);
                        }
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
