package applica.feneal.domain.model.core.quote;


import applica.feneal.domain.model.utils.SecuredDomainEntity;

import java.util.*;

import static java.util.Calendar.*;

/**
 * Created by fgran on 20/05/2016.
 */
public class DettaglioQuotaAssociativa extends SecuredDomainEntity {

    //uso tabelle fortemente denormalizzate
    //riferimento al padre
    private long idRiepilogoQuotaAssociativa;

    private String provincia;
    private Date dataRegistrazione;
    private Date dataDocumento;
    private String tipoDocumento;
    private String settore;

    private Date dataInizio;
    private Date dataFine;
    private double quota;
    private String livello;
    private String contratto;
    private String note;


    private String delegaMansione;
    private String delegaLuogoLavoro;
    private String delegaNote;
    private String delegaCollaboratore;

    private long idAzienda;
    private long idLavoratore;

    private transient int id_Iscrizione;
    private transient int year;
    private String operatoreDelega;
    private Long operatoreId;

    private String regione;
    private boolean ripresaDati;



    private Long bonificoId;
    private String noteBonifico;
    private Date dataBonifico;


    public Long getBonificoId() {
        return bonificoId;
    }

    public void setBonificoId(Long bonificoId) {
        this.bonificoId = bonificoId;
    }

    public String getNoteBonifico() {
        return noteBonifico;
    }

    public void setNoteBonifico(String noteBonifico) {
        this.noteBonifico = noteBonifico;
    }

    public Long getOperatoreId() {

        return operatoreId;
    }

    public void setOperatoreId(Long operatoreId) {
        this.operatoreId = operatoreId;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public boolean isRipresaDati() {
        return ripresaDati;
    }

    public void setRipresaDati(boolean ripresaDati) {
        this.ripresaDati = ripresaDati;
    }

    public String getDelegaMansione() {
        return delegaMansione;
    }

    public void setDelegaMansione(String delegaMansione) {
        this.delegaMansione = delegaMansione;
    }

    public String getDelegaLuogoLavoro() {
        return delegaLuogoLavoro;
    }

    public void setDelegaLuogoLavoro(String delegaLuogoLavoro) {
        this.delegaLuogoLavoro = delegaLuogoLavoro;
    }

    public String getDelegaNote() {
        return delegaNote;
    }

    public void setDelegaNote(String delegaNote) {
        this.delegaNote = delegaNote;
    }

    public String getDelegaCollaboratore() {
        return delegaCollaboratore;
    }

    public void setDelegaCollaboratore(String delegaCollaboratore) {
        this.delegaCollaboratore = delegaCollaboratore;
    }

    public  List<DettaglioQuotaAssociativa> cloneOnEveryCompetenceYear() {
        //questa funzione ritorna una lista fatta in questo modo:
        //il dettaglio viene ripetuto n volte in base alla differenza tra la data inizio e la data fine


        //questa funzione è necessaria per la corretta conversione di una iscrizione
        //che è definita annualmente  ed una quota che è riferita ad un intervallo date

        //se ad esempio ho una quota con competenza
        //10/12/2005 - 11/11/2007 la risultanta sarà una lista contenete tre dettagli quota:
        //ad indicare che avro una iscrizione per 'anno 2005 + 0' = 2005
        //per l'anno 2005 + 1 = 2006 e l'anno 2005 + 2= 2007

        List<DettaglioQuotaAssociativa> result = new ArrayList<>();

        int diff = getDiffYears(dataInizio, dataFine);
        if (diff == 0){
            result.add(clone(this,retrieveYear(dataInizio)));
            return result;
        }


        for (int i = 0; i <= diff ; i++) {
            result.add(clone(this,retrieveYear(dataInizio) + i));
        }
        return result;
    }

    private DettaglioQuotaAssociativa clone(DettaglioQuotaAssociativa dettaglioQuotaAssociativa, int year) {
        DettaglioQuotaAssociativa c = new DettaglioQuotaAssociativa();

         c.setIdRiepilogoQuotaAssociativa(dettaglioQuotaAssociativa.getIdRiepilogoQuotaAssociativa());

        c.setProvincia(dettaglioQuotaAssociativa.getProvincia());
        c.setDataRegistrazione(dettaglioQuotaAssociativa.getDataRegistrazione());
        c.setDataDocumento(dettaglioQuotaAssociativa.getDataDocumento());
        c.setTipoDocumento(dettaglioQuotaAssociativa.getTipoDocumento());
        c.setSettore(dettaglioQuotaAssociativa.getSettore());

        c.setDataInizio(dettaglioQuotaAssociativa.getDataInizio());
        c.setDataFine(dettaglioQuotaAssociativa.getDataFine());
        c.setQuota(dettaglioQuotaAssociativa.getQuota());
        c.setLivello(dettaglioQuotaAssociativa.getLivello());
        c.setContratto(dettaglioQuotaAssociativa.getContratto());

        c.setNote(dettaglioQuotaAssociativa.getNote());
        c.setYear(year);
        c.setIdAzienda(dettaglioQuotaAssociativa.getIdAzienda());
        c.setIdLavoratore(dettaglioQuotaAssociativa.getIdLavoratore());
        c.setCompanyId(dettaglioQuotaAssociativa.getCompanyId());

        c.setDelegaNote(dettaglioQuotaAssociativa.getDelegaNote());
        c.setDelegaCollaboratore(dettaglioQuotaAssociativa.getDelegaCollaboratore());
        c.setDelegaLuogoLavoro(dettaglioQuotaAssociativa.getDelegaLuogoLavoro());
        c.setDelegaMansione(dettaglioQuotaAssociativa.getDelegaMansione());
        c.setOperatoreDelega(dettaglioQuotaAssociativa.getOperatoreDelega());


        c.setRegione(dettaglioQuotaAssociativa.getRegione());
        c.setRipresaDati(dettaglioQuotaAssociativa.isRipresaDati());

        c.setId_Iscrizione(dettaglioQuotaAssociativa.getId_Iscrizione());



        return c;
    }

    private static int getDiffYears(Date first, Date last) {
        Calendar a = getCalendar(first);
        Calendar b = getCalendar(last);
        int diff = b.get(YEAR) - a.get(YEAR);
        if (diff == 0)
            return 0;
        if (a.get(MONTH) > b.get(MONTH) ||
                (a.get(MONTH) == b.get(MONTH) && a.get(DATE) > b.get(DATE))) {
            diff--;
        }
        return diff;
    }

    private static Calendar getCalendar(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ITALIAN);
        cal.setTime(date);
        return cal;
    }

    private  int retrieveYear(Date date) {
        Calendar cal = Calendar.getInstance(Locale.ITALIAN);
        cal.setTime(date);
        return cal.get(YEAR);
    }

    public boolean checkIfValidForYear(int year){
        int annoInizio = retrieveYear(dataInizio);
        int annoFine = retrieveYear(dataFine);

        return year == annoInizio || year == annoFine;

    }



//    public String calculateCompetenceSpreadOverYears(){
//
//
//        return String.format("%s-%s", getYear(dataInizio), getDiffYears(dataInizio, dataFine));
//
//    }

    public int getYear() {
        return year;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public int getId_Iscrizione() {
        return id_Iscrizione;
    }

    public void setId_Iscrizione(int id_Iscrizione) {
        this.id_Iscrizione = id_Iscrizione;
    }

    public long getIdRiepilogoQuotaAssociativa() {
        return idRiepilogoQuotaAssociativa;
    }

    public void setIdRiepilogoQuotaAssociativa(long idRiepilogoQuotaAssociativa) {
        this.idRiepilogoQuotaAssociativa = idRiepilogoQuotaAssociativa;
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

    public long getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(long idAzienda) {
        this.idAzienda = idAzienda;
    }

    public long getIdLavoratore() {
        return idLavoratore;
    }

    public void setIdLavoratore(long idLavoratore) {
        this.idLavoratore = idLavoratore;
    }

    public void setOperatoreDelega(String operatoreDelega) {
        this.operatoreDelega = operatoreDelega;
    }

    public String getOperatoreDelega() {
        return operatoreDelega;
    }

    public void setDataBonifico(Date dataBonifico) {
        this.dataBonifico = dataBonifico;
    }

    public Date getDataBonifico() {
        return dataBonifico;
    }


//    public static DettaglioQuotaAssociativa createFromIscrizione(Iscrizione iscrizione, Lavoratore lavoratore, Azienda azienda, long companyId) throws Exception {
//        DettaglioQuotaAssociativa q = new DettaglioQuotaAssociativa();
//        q.setCompanyId(companyId);
//        q.setProvincia(iscrizione.getNomeProvincia());
//        q.setTipoDocumento(iscrizione.getSettore().equals(Settore.inps)?RiepilogoQuoteAssociative.IQI:RiepilogoQuoteAssociative.IQA);
//        q.setDataRegistrazione(new Date());
//        q.setDataDocumento(iscrizione.getDataFine());
//        q.setSettore(iscrizione.getSettore());
//        q.setContratto(iscrizione.getContratto());
//        q.setDataFine(iscrizione.getDataFine());
//        q.setDataInizio(iscrizione.getDataInizio());
//        q.setAzienda(iscrizione.getAzienda());
//        if (azienda != null)
//            q.setIdAzienda(azienda.getLid());
//        if (lavoratore == null)
//            throw new Exception("Impossibile inserire l'iscrizione. lavoaratore nullo");
//        q.setIdLavoratore(lavoratore.getLid());
//        q.setLivello(iscrizione.getLivello());
//        q.setQuota(iscrizione.getQuota());
//
//
//        return q;
//    }
}
