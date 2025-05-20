import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientXat extends Thread {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private boolean sortir;
    
    public void connecta() throws Exception {
        socket = new Socket(ServidorXat.HOST, ServidorXat.PORT);
        output = new ObjectOutputStream(socket.getOutputStream());
        sortir = false;
    }
    
    public void enviarMissatge(String missatge) {
        try {
            if (output != null) {
                System.out.println("Enviant missatge: " + missatge);
                output.writeObject(missatge);
                output.flush();
            } 
        } catch (IOException e) {
            System.err.println("Error enviant missatge. Sortint...");
            sortir = true;
        }
    }

    public void tancarClient() throws IOException {
        output.close();
        input.close();
        socket.close();
        System.out.println("Flux d'entrada tancat.");
        System.out.println("Flux de sortida tancat.");
    }

    @Override
    public void run() {
        try {
            input = new ObjectInputStream(socket.getInputStream());
            System.out.println("DEBUG: Iniciant rebuda de missatges...");
            while (!sortir) {
                Object rebut = input.readObject();
                if (rebut instanceof String missatgeRaw && missatgeRaw != null && !missatgeRaw.trim().isEmpty()) {
                    String codi = Missatge.getCodiMissatge(missatgeRaw);
                    String[] parts = Missatge.getPartsMissatge(missatgeRaw);
                    if (codi != null && parts != null) {
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
            System.err.println("Error rebent missatges: " + e.getMessage());
            e.printStackTrace(); 
            sortir = true;  
        }
    }

    public void ajuda() {
        System.out.println("---------------------");
        System.out.println("Comandes disponibles:");
        System.out.println("  1.- Conectar al servidor (primer pas obligatori)");
        System.out.println("  2.- Enviar missatge personal");
        System.out.println("  3.- Enviar missatge al grup");
        System.out.println("  4.- (o línia en blanc) -> Sortir del client");
        System.out.println("  5.- Finalitzar tothom");
        System.out.println("---------------------");
    }

    public String getLinea(Scanner scanner, String prompt, boolean obligatorio) {
        if (!prompt.isEmpty()) {
            System.out.print(prompt + " ");
        }
        String linea = scanner.nextLine().trim();
        if (obligatorio) {
            while (linea.isEmpty()) {
                System.out.print(prompt + " ");
                linea = scanner.nextLine().trim();
            }
        }
        return linea;
    }

    public static void main(String[] args) {
        ClientXat client = new ClientXat();
        Scanner scanner = new Scanner(System.in);

        try {
            client.connecta();
            System.out.println("Client connectat a " + ServidorXat.HOST + ":" + ServidorXat.PORT);
            System.out.println("Flux d'entrada i sortida creat.");

            client.start();
            client.ajuda();

            boolean sortir = false;
            while (!sortir) {
                System.out.print("> ");
                String opcio = scanner.nextLine().trim();

                switch (opcio) {
                    case "":
                        sortir = true;
                        break;
                    case "1": {
                        String nom = client.getLinea(scanner, "Introdueix el nom:", true);
                        String msg = Missatge.getMissatgeConectar(nom);
                        client.enviarMissatge(msg);
                        break;
                    }
                    case "2": {
                        String destinatari = client.getLinea(scanner, "Destinatari:", true);
                        String missatge = client.getLinea(scanner, "Missatge a enviar:", true);
                        String msg = Missatge.getMissatgePersonal(destinatari, missatge);
                        client.enviarMissatge(msg);
                        break;
                    }
                    case "3": {
                        String missatge = client.getLinea(scanner, "Missatge al grup:", true);
                        String msg = Missatge.getMissatgeGrup(missatge);
                        client.enviarMissatge(msg);
                        break;
                    }
                    case "4": {
                        String msg = Missatge.getMissatgeSortirClient("Adéu");
                        client.enviarMissatge(msg);
                        sortir = true;
                        break;
                    }
                    case "5": {
                        String msg = Missatge.getMissatgeSortirTots("Adéu");
                        client.enviarMissatge(msg);
                        sortir = true;
                        break;
                    }
                    default:
                        System.out.println("Opció no vàlida.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error al connectar: " + e.getMessage());
        } finally {
            try {
                client.tancarClient();
            } catch (IOException e) {
                e.printStackTrace();
            }
            scanner.close();
        }
    }
}
