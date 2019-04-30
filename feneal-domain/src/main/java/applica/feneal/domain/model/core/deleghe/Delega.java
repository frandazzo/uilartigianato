package applica.feneal.domain.model.core.deleghe;


import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.configuration.Contract;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.domain.model.setting.CausaleIscrizioneDelega;
import applica.feneal.domain.model.setting.CausaleRevoca;
import applica.feneal.domain.model.setting.Collaboratore;
import applica.feneal.domain.model.utils.UserDomainEntity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fgran on 05/04/2016.
 */
public class Delega extends UserDomainEntity {


    public static final int state_subscribe = 1;
    public static final int state_sent = 2;
    public static final int state_accepted = 3;
    public static final int state_activated = 4;
    public static final int state_cancelled = 5;
    public static final int state_revoked = 6;


    //Utilizzato per identificare l'azione del "tornare indietro"
    public static final int ACTION_BACK = 0;


    private Date documentDate;
    private Date sendDate;
    private Date acceptDate;
    private Date activationDate;
    private Date cancelDate;
    private Date revokeDate;
    //guid che identifica univocamente l'importazione che ha generato la delega
    private String importGuid;

    //stato della delega
    private Integer state = state_subscribe;


    //settore della delega se impianti fissi o edile
    private Categoria sector;
    //ente bilaterale di riferimento (solo per delega edile)
    private Contract contract;
    //azienda del lavoratore
    private Azienda workerCompany;
    //provincia di riferimento
    private Province province;

    //collaboratore che ha fatto la delega
    private Collaboratore collaborator;

    //utente che hA sottoscritto la delega
    private Lavoratore worker;

    private Boolean breviMano;

    public Boolean getBreviMano() {
        return breviMano;
    }

    public void setBreviMano(Boolean breviMano) {
        this.breviMano = breviMano;
    }

    private CausaleIscrizioneDelega subscribeReason;
    private CausaleRevoca revokeReason;
    private CausaleRevoca cancelReason;
    //flag per un eventuale annullamento da parte di un'altra delega
    //tale delega sara definitivamente annullata e non avrà alcuna possibilità di ritornare ad uno stato precendente
    //l'unica operazione possibile sarà la cancellazione
    private boolean tombstoneDelega;
    //stato precedente;
    private Integer preecedingState;

    //flag che indica se la delega è stata attivata senza la necessaria accettazione
    private Boolean activatedWithoutAcceptance;

    //ad indicale eventuali annotazioni legate ad una particolare annotazione nelle operazioni del sistema sulla delega
    private String notes;
    private String mansione;
    private String luogoLavoro;


    public String getImportGuid() {
        return importGuid;
    }

    public void setImportGuid(String importGuid) {
        this.importGuid = importGuid;
    }

    public String getMansione() {
        return mansione;
    }

    public void setMansione(String mansione) {
        this.mansione = mansione;
    }

    public String getLuogoLavoro() {
        return luogoLavoro;
    }

    public void setLuogoLavoro(String luogoLavoro) {
        this.luogoLavoro = luogoLavoro;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public boolean checkIfActivateOrAccepted(){
        if (state != null && (state == (Delega.state_accepted)|| state == (Delega.state_activated)))
            return true;
        return false;
    }

    public CausaleRevoca getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(CausaleRevoca cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getAcceptDate() {
        return acceptDate;
    }

    public void setAcceptDate(Date acceptDate) {
        this.acceptDate = acceptDate;
    }

    public Date getActivationDate() {
        return activationDate;
    }

    public void setActivationDate(Date activationDate) {
        this.activationDate = activationDate;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Date getRevokeDate() {
        return revokeDate;
    }

    public void setRevokeDate(Date revokeDate) {
        this.revokeDate = revokeDate;
    }

    public Categoria getSector() {
        return sector;
    }

    public void setSector(Categoria sector) {
        this.sector = sector;
    }

    public Contract getContract() {
        return contract;
    }

    public void setContract(Contract contract) {
        this.contract = contract;
    }

    public Azienda getWorkerCompany() {
        return workerCompany;
    }

    public void setWorkerCompany(Azienda workerCompany) {
        this.workerCompany = workerCompany;
    }

    public Collaboratore getCollaborator() {
        return collaborator;
    }

    public void setCollaborator(Collaboratore collaborator) {
        this.collaborator = collaborator;
    }

    public Lavoratore getWorker() {
        return worker;
    }

    public void setWorker(Lavoratore worker) {
        this.worker = worker;
    }

    public CausaleIscrizioneDelega getSubscribeReason() {
        return subscribeReason;
    }

    public void setSubscribeReason(CausaleIscrizioneDelega subscribeReason) {
        this.subscribeReason = subscribeReason;
    }

    public CausaleRevoca getRevokeReason() {
        return revokeReason;
    }

    public void setRevokeReason(CausaleRevoca revokeReason) {
        this.revokeReason = revokeReason;
    }


    public boolean isTombstoneDelega() {
        return tombstoneDelega;
    }

    public void setTombstoneDelega(boolean tombstoneDelega) {
        this.tombstoneDelega = tombstoneDelega;
    }

    public Boolean getActivatedWithoutAcceptance() {
        return activatedWithoutAcceptance;
    }

    public void setActivatedWithoutAcceptance(Boolean activatedWithoutAcceptance) {
        this.activatedWithoutAcceptance = activatedWithoutAcceptance;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPreecedingState() {
        return preecedingState;
    }

    public void setPreecedingState(Integer preecedingState) {
        this.preecedingState = preecedingState;
    }

    public String calculatePrecedingStateString(Integer preecedingState) {

        switch (preecedingState){
            case Delega.state_accepted:
                return String.format("Accettata il %s", new SimpleDateFormat("dd/MM/yyyy").format(this.getAcceptDate()));

            case Delega.state_sent:
                return String.format("Inoltrata il %s", new SimpleDateFormat("dd/MM/yyyy").format(this.getSendDate()));

            case Delega.state_subscribe:
                return String.format("Sotoscritta il %s", new SimpleDateFormat("dd/MM/yyyy").format(this.getDocumentDate()));

            case Delega.state_activated:
                return String.format("Attivata il %s", new SimpleDateFormat("dd/MM/yyyy").format(this.getActivationDate()));

            default:
               return "";
        }



    }
}
