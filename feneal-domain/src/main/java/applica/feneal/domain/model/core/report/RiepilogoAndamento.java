package applica.feneal.domain.model.core.report;

/**
 * Created by fgran on 01/05/2016.
 */
public class RiepilogoAndamento {

    private String territorio;
    private String contesto;
    private String contestoSecondario;
    private String ente;
    private int anno;
    private int valore;

    public String getContestoSecondario() {
        return contestoSecondario;
    }

    public void setContestoSecondario(String contestoSecondario) {
        this.contestoSecondario = contestoSecondario;
    }

    public String getTerritorio() {
        return territorio;
    }

    public void setTerritorio(String territorio) {
        this.territorio = territorio;
    }

    public String getContesto() {
        return contesto;
    }

    public void setContesto(String contesto) {
        this.contesto = contesto;
    }

    public String getEnte() {
        return ente;
    }

    public void setEnte(String ente) {
        this.ente = ente;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public int getValore() {
        return valore;
    }

    public void setValore(int valore) {
        this.valore = valore;
    }
}
