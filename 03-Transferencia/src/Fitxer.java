import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Fitxer {
    
    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingut() throws IOException {
        String rutaTemp = System.getProperty("java.io.tmpdir");
        File fitxer = new File(rutaTemp, nom);
        if (!fitxer.exists()) {
            return null;
        }
        Path path = fitxer.toPath();
        contingut = Files.readAllBytes(path);
        return contingut;
    }

    public String getNom() {
        return nom;
    }

    public String getRuta() {
        return new File(System.getProperty("java.io.tmpdir"), nom).getAbsolutePath();
    }
}
