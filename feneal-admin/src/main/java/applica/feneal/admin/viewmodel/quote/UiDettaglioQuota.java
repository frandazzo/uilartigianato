package applica.feneal.admin.viewmodel.quote;

import java.util.Date;

public class UiDettaglioQuota {

    private long idQuota;

    private String regione;
    private String provincia;
    private String settore;
    private String operatoreDelega;
    private boolean ripresaDati;


    private Date dataRegistrazione;
    private Date dataDocumento;
    private String tipoDocumento;


    private Date dataInizio;
    private Date dataFine;
    private double quota;
    private String livello;
    private String note;
    private String contratto;
    private long companyId;
    private Date dataBonifico;
    private String noteBonifico;
    private Long bonificoId;

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getOperatoreDelega() {
        return operatoreDelega;
    }

    public void setOperatoreDelega(String operatoreDelega) {
        this.operatoreDelega = operatoreDelega;
    }

    public boolean isRipresaDati() {
        return ripresaDati;
    }

    public void setRipresaDati(boolean ripresaDati) {
        this.ripresaDati = ripresaDati;
    }

    //dati lavoratore
    private String lavoratoreNomeCompleto;
    private String lavoratoreCodiceFiscale;

    private String lavoratoreNome;
    private String lavoratoreCognome;

    private long lavoratoreId;
    //Dati azienda
    private String aziendaRagioneSociale;
    private long aziendaId;

    private long id;

    public String getLavoratoreNome() {
        return lavoratoreNome;
    }

    public void setLavoratoreNome(String lavoratoreNome) {
        this.lavoratoreNome = lavoratoreNome;
    }

    public String getLavoratoreCognome() {
        return lavoratoreCognome;
    }

    public void setLavoratoreCognome(String lavoratoreCognome) {
        this.lavoratoreCognome = lavoratoreCognome;
    }

    public long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }




    public long getIdQuota() {
        return idQuota;
    }

    public void setIdQuota(long idQuota) {
        this.idQuota = idQuota;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public Date getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getSettore() {
        return settore;
    }

    public void setSettore(String settore) {
        this.settore = settore;
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

    public double getQuota() {
        return quota;
    }

    public void setQuota(double quota) {
        this.quota = quota;
    }

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    public String getContratto() {
        return contratto;
    }

    public void setContratto(String contratto) {
        this.contratto = contratto;
    }

    public String getLavoratoreNomeCompleto() {
        return lavoratoreNomeCompleto;
    }

    public void setLavoratoreNomeCompleto(String lavoratoreNomeCompleto) {
        this.lavoratoreNomeCompleto = lavoratoreNomeCompleto;
    }

    public String getAziendaRagioneSociale() {
        return aziendaRagioneSociale;
    }

    public void setAziendaRagioneSociale(String aziendaRagioneSociale) {
        this.aziendaRagioneSociale = aziendaRagioneSociale;
    }

    public String getLavoratoreCodiceFiscale() {
        return lavoratoreCodiceFiscale;
    }

    public void setLavoratoreCodiceFiscale(String lavoratoreCodiceFiscale) {
        this.lavoratoreCodiceFiscale = lavoratoreCodiceFiscale;
    }

    public long getAziendaId() {
        return aziendaId;
    }

    public void setAziendaId(long aziendaId) {
        this.aziendaId = aziendaId;
    }

    public long getLavoratoreId() {
        return lavoratoreId;
    }

    public void setLavoratoreId(long lavoratoreId) {
        this.lavoratoreId = lavoratoreId;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setDataBonifico(Date dataBonifico) {
        this.dataBonifico = dataBonifico;
    }

    public Date getDataBonifico() {
        return dataBonifico;
    }

    public void setNoteBonifico(String noteBonifico) {
        this.noteBonifico = noteBonifico;
    }

    public String getNoteBonifico() {
        return noteBonifico;
    }

    public void setBonificoId(Long bonificoId) {
        this.bonificoId = bonificoId;
    }

    public Long getBonificoId() {
        return bonificoId;
    }
}
