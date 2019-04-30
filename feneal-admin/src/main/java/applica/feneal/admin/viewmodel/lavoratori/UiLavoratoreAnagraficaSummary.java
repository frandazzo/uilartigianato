package applica.feneal.admin.viewmodel.lavoratori;

import java.util.Date;

/**
 * Created by fgran on 06/04/2016.
 */
public class UiLavoratoreAnagraficaSummary {

    private String surname;
    private String name;
    private String image;
    private String nationality;
    private String birthProvince;
    private String birthPlace;   // comune di nascita
    private Date birthDate;
    private String livingPlace;
    private String livingProvince;
    private String address;
    private String addressco;
    private String cap;
    private String fiscalcode;
    private String phone;
    private String cellphone;
    private String mail;
    private String ce;
    private String ec;
    private String fund;
    private long id;
    private String attribuzione1;
    private String attribuzione2;
    private String attribuzione3;
    private boolean privacy;
    private long companyId;

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public String getAddressco() {
        return addressco;
    }

    public void setAddressco(String addressco) {
        this.addressco = addressco;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getBirthProvince() {
        return birthProvince;
    }

    public void setBirthProvince(String birthProvince) {
        this.birthProvince = birthProvince;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getLivingPlace() {
        return livingPlace;
    }

    public void setLivingPlace(String livingPlace) {
        this.livingPlace = livingPlace;
    }

    public String getLivingProvince() {
        return livingProvince;
    }

    public void setLivingProvince(String livingProvince) {
        this.livingProvince = livingProvince;
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

    public String getFiscalcode() {
        return fiscalcode;
    }

    public void setFiscalcode(String fiscalcode) {
        this.fiscalcode = fiscalcode;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCe() {
        return ce;
    }

    public void setCe(String ce) {
        this.ce = ce;
    }

    public String getEc() {
        return ec;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getFund() {
        return fund;
    }

    public void setFund(String fund) {
        this.fund = fund;
    }

    public void setAttribuzione1(String attribuzione1) {
        this.attribuzione1 = attribuzione1;
    }

    public String getAttribuzione1() {
        return attribuzione1;
    }

    public void setAttribuzione2(String attribuzione2) {
        this.attribuzione2 = attribuzione2;
    }

    public String getAttribuzione2() {
        return attribuzione2;
    }

    public void setAttribuzione3(String attribuzione3) {
        this.attribuzione3 = attribuzione3;
    }

    public String getAttribuzione3() {
        return attribuzione3;
    }

    public void setCompanyId(long companyId) {
        this.companyId = companyId;
    }

    public long getCompanyId() {
        return companyId;
    }
}
