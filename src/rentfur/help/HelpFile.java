package rentfur.help;
/**
 * @author fduarte
 */
public class HelpFile {
    
    private String Texto;
    private String archivo;

    public String getTexto() {
        return Texto;
    }

    public void setTexto(String Texto) {
        this.Texto = Texto;
    }

    public String getArchivo() {
        return archivo;
    }

    public void setArchivo(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public String toString() {
        return Texto;
    }
    
}