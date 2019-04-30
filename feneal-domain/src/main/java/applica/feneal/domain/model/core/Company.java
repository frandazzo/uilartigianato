package applica.feneal.domain.model.core;

import applica.feneal.domain.model.core.configuration.TipoConfederazioneEnum;
import applica.feneal.domain.model.geo.Province;
import applica.framework.AEntity;
import applica.framework.annotations.ManyToMany;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

/**
 * Applica
 * User: Ciccio Randazzo
 * Date: 10/12/15
 * Time: 9:30 AM
 * To change this template use File | Settings | File Templates.
 */
@JsonIgnoreProperties({ "iid", "sid", "lid" })
public class Company extends AEntity {

    private String description;
    @ManyToMany
    private List<Province> provinces;


    //impostazioni SMS
    //opzioni per l'invio degli sms
    private String smsUsername;
    private String smsPassword;
    private String smsSenderNumber;
    private String smsSenderAlias;

    //impostazioni mail
    private String mailSender;
    private String mailSenderAsString;
    private int tipoConfederazione = TipoConfederazioneEnum.Regionale;


    public String getSmsUsername() {
        return smsUsername;
    }

    public void setSmsUsername(String smsUsername) {
        this.smsUsername = smsUsername;
    }

    public String getSmsPassword() {
        return smsPassword;
    }

    public void setSmsPassword(String smsPassword) {
        this.smsPassword = smsPassword;
    }

    public String getSmsSenderNumber() {
        return smsSenderNumber;
    }

    public void setSmsSenderNumber(String smsSenderNumber) {
        this.smsSenderNumber = smsSenderNumber;
    }

    public String getSmsSenderAlias() {
        return smsSenderAlias;
    }

    public void setSmsSenderAlias(String smsSenderAlias) {
        this.smsSenderAlias = smsSenderAlias;
    }

    public String getMailSender() {
        return mailSender;
    }

    public void setMailSender(String mailSender) {
        this.mailSender = mailSender;
    }

    public String getMailSenderAsString() {
        return mailSenderAsString;
    }

    public void setMailSenderAsString(String mailSenderAsString) {
        this.mailSenderAsString = mailSenderAsString;
    }

    public int getTipoConfederazione() {
        return tipoConfederazione;
    }

    public void setTipoConfederazione(int tipoConfederazione) {
        this.tipoConfederazione = tipoConfederazione;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Province> getProvinces() {
        return provinces;
    }

    public void setProvinces(List<Province> provinces) {
        this.provinces = provinces;
    }

    @Override
    public String toString() {
        return this.description;
    }

    public boolean containProvince(String provinceName) {
        for (Province province : provinces) {
            if (province.getDescription().toLowerCase().equals(provinceName.toLowerCase()))
                return true;
        }

        return false;
    }
}
