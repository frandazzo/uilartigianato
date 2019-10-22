package applica.feneal.domain.model.core;

import applica.framework.AEntity;

import java.util.Date;

public class CommunicationStructure extends AEntity {
    private String briefDescription;
    private Date date;
    private String description;
    private String attached1;
    private String attached2;
    private String attached3;
    private String nomeattached1;
    private String nomeattached2;
    private String nomeattached3;

    public String getNomeattached1() {
        return nomeattached1;
    }

    public void setNomeattached1(String nomeattached1) {
        this.nomeattached1 = nomeattached1;
    }

    public String getNomeattached2() {
        return nomeattached2;
    }

    public void setNomeattached2(String nomeattached2) {
        this.nomeattached2 = nomeattached2;
    }

    public String getNomeattached3() {
        return nomeattached3;
    }

    public void setNomeattached3(String nomeattached3) {
        this.nomeattached3 = nomeattached3;
    }

    public String getBriefDescription() {
        return briefDescription;
    }

    public void setBriefDescription(String briefDescription) {
        this.briefDescription = briefDescription;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAttached1() {
        return attached1;
    }

    public void setAttached1(String attached1) {
        this.attached1 = attached1;
    }

    public String getAttached2() {
        return attached2;
    }

    public void setAttached2(String attached2) {
        this.attached2 = attached2;
    }

    public String getAttached3() {
        return attached3;
    }

    public void setAttached3(String attached3) {
        this.attached3 = attached3;
    }
}
