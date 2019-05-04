package applica.feneal.domain.model.core.rappresentanza;

import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.domain.model.utils.SecuredDomainEntity;

import java.util.Date;

public class DelegaUnc  extends SecuredDomainEntity {

    private Date documentDate;
    private Date cancelDate;
    //categoria della delega
    private Categoria sector;
    //provincia di riferimento
    private Province province;
    //utente che hA sottoscritto la delega
    private Lavoratore worker;
    private String notes;

    public Date getDocumentDate() {
        return documentDate;
    }

    public void setDocumentDate(Date documentDate) {
        this.documentDate = documentDate;
    }

    public Date getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(Date cancelDate) {
        this.cancelDate = cancelDate;
    }

    public Categoria getSector() {
        return sector;
    }

    public void setSector(Categoria sector) {
        this.sector = sector;
    }

    public Province getProvince() {
        return province;
    }

    public void setProvince(Province province) {
        this.province = province;
    }

    public Lavoratore getWorker() {
        return worker;
    }

    public void setWorker(Lavoratore worker) {
        this.worker = worker;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
