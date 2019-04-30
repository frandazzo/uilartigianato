package applica.feneal.domain.model.core.quote;

import java.util.Date;

/**
 * Created by fgran on 15/09/2017.
 */
public class UpdatableDettaglioQuota {

    private Double quota;
    private String livello;
    private String note;
    private Date dataInizio;
    private Date dataFine;


    public Double getQuota() {
        return quota;
    }

    public void setQuota(Double quota) {
        this.quota = quota;
    }

    public String getLivello() {
        return livello;
    }

    public void setLivello(String livello) {
        this.livello = livello;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
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
}
