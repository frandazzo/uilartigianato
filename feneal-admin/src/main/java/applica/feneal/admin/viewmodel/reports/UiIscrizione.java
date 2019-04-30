package applica.feneal.admin.viewmodel.reports;

/**
 * Created by fgran on 11/05/2016.
 */
public class UiIscrizione {

    private String settore;
    private String nomeProvincia;
    private String azienda;
    private String piva;
    private String nomeRegione;
    private long companyId;
    private String contratto;
    private String operatoreDelega;
    private String livello;
    private double quota ;
    private int periodo;
    private int  anno;
    private int numComunicazioni = 0;


    public String getOperatoreDelega() {
        return operatoreDelega;
    }

    public void setOperatoreDelega(String operatoreDelega) {
        this.operatoreDelega = operatoreDelega;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }




    public int getNumComunicazioni() {
        return numComunicazioni;
    }

    public void setNumComunicazioni(int numComunicazioni) {
        this.numComunicazioni = numComunicazioni;
    }

    public String getNomeProvincia() {
        return nomeProvincia;
    }

    public void setNomeProvincia(String nomeProvincia) {
        this.nomeProvincia = nomeProvincia;
    }

    public String getNomeRegione() {
        return nomeRegione;
    }

    public void setNomeRegione(String nomeRegione) {
        this.nomeRegione = nomeRegione;
    }

    public String getSettore() {
        return settore;
    }

    public void setSettore(String settore) {
        this.settore = settore;
    }


    public String getAzienda() {
        return azienda;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    public double getQuota() {
        return quota;
    }

    public void setQuota(double quota) {
        this.quota = quota;
    }

    public int getPeriodo() {
        return periodo;
    }

    public void setPeriodo(int periodo) {
        this.periodo = periodo;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public String getContratto() {
        return contratto;
    }

    public void setContratto(String contratto) {
        this.contratto = contratto;
    }


}
