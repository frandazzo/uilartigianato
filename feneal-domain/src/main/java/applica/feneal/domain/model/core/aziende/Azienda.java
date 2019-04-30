package applica.feneal.domain.model.core.aziende;

import applica.feneal.domain.model.utils.SecuredDomainEntity;
import org.apache.commons.lang.StringUtils;

/**
 * Created by fgran on 05/04/2016.
 */
public class Azienda extends SecuredDomainEntity {

    private String description;
    private String alternativeDescription;
    private String city;
    private String province;
    private String cap;
    private String address;
    private String phone;
    private String piva;
    private String notes;

    private transient String completeDescription;

    public String getCompleteDescription() {
        return description + (StringUtils.isEmpty(alternativeDescription)? "": " - " + alternativeDescription);
    }

    public void setCompleteDescription(String completeDescription) {
        this.completeDescription = completeDescription;
    }

    public String getAlternativeDescription() {
        return alternativeDescription;
    }

    public void setAlternativeDescription(String alternativeDescription) {
        this.alternativeDescription = alternativeDescription;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPiva() {
        return piva;
    }

    public void setPiva(String piva) {
        this.piva = piva;
    }

    @Override
    public String toString() {
        return description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}
