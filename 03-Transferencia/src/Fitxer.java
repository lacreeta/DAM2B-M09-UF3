import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Fitxer {
    
    private String nom;
    private byte[] contingut;

    public Fitxer(String nom) {
        this.nom = nom;
    }

    public byte[] getContingut() throws IOException {
        File fitxer = new File(Client.DIR_ARRIBADA + "/" + nom);
        if (!fitxer.exists()) {
            return null;
        }
        Path path = Paths.get(fitxer.getName());
        contingut = Files.readAllBytes(path);
        return contingut;
    }

    public String getNom() {
        return nom;
    }

    public String getRuta() {
        return new File(Client.DIR_ARRIBADA + "/" + nom).getAbsolutePath();
    }
}
