package applica.feneal.domain.model.dbnazionale;

import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;

import java.util.Date;

/**
 * Created by fgran on 30/04/2016.
 */
public class Iscrizione  {


    private int id_Lavoratore;
    private String nomeProvincia;
    private String nomeRegione;
    private String settore;
    private String piva;
    private String azienda;
    private String livello;
    private double quota ;
    private int  anno;
    private Date dataInizio;
    private Date dataFine;
    private String contratto;
    private String nomeCompleto;
    private long companyId;
    private String operatoreDelega;

    public String getOperatoreDelega() {
        return operatoreDelega;
    }

    public void setOperatoreDelega(String operatoreDelega) {
        this.operatoreDelega = operatoreDelega;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public int getId_Lavoratore() {
        return id_Lavoratore;
    }

    public void setId_Lavoratore(int id_Lavoratore) {
        this.id_Lavoratore = id_Lavoratore;
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

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    public String getAzienda() {
        return azienda;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
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

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public Date getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(Date dataInizio) {
        this.dataInizio = dataInizio;
    }

    public Date getDataFine() {
        return dataFine;
    }

    public void setDataFine(Date dataFine) {
        this.dataFine = dataFine;
    }

    public String getContratto() {
        return contratto;
    }

    public void setContratto(String contratto) {
        this.contratto = contratto;
    }

    public static Iscrizione constructIscrizione(DettaglioQuotaAssociativa dettaglioQuotaAssociativa, Azienda azienda, Lavoratore lavoratore) {

        Iscrizione result = new Iscrizione();

        result.setId_Lavoratore(Integer.parseInt(String.valueOf(dettaglioQuotaAssociativa.getIdLavoratore())));
        result.setNomeProvincia(dettaglioQuotaAssociativa.getProvincia());
        result.setAzienda(azienda.getDescription());
        result.setPiva(azienda.getPiva());
        result.setSettore(dettaglioQuotaAssociativa.getSettore());
        result.setContratto(dettaglioQuotaAssociativa.getContratto());
        result.setCompanyId(dettaglioQuotaAssociativa.getCompanyId());
        result.setAnno(dettaglioQuotaAssociativa.getYear());
        result.setOperatoreDelega(dettaglioQuotaAssociativa.getOperatoreDelega());

        result.setLivello(dettaglioQuotaAssociativa.getLivello());
        result.setQuota(dettaglioQuotaAssociativa.getQuota());
        result.setNomeCompleto(lavoratore.getSurnamename());
        result.setDataInizio(dettaglioQuotaAssociativa.getDataInizio());
        result.setDataFine(dettaglioQuotaAssociativa.getDataFine());

        return result;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getCompanyId() {
        return companyId;
    }
}
