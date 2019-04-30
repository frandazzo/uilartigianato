package applica.feneal.domain.model.core.lavoratori;

import applica.feneal.domain.model.setting.Attribuzione1;
import applica.feneal.domain.model.setting.Attribuzione2;
import applica.feneal.domain.model.setting.Attribuzione3;
import applica.feneal.domain.model.setting.Fondo;
import applica.feneal.domain.model.utils.SecuredDomainEntity;

import java.util.Date;


public class LavoratoreLastVersion extends SecuredDomainEntity {

    public LavoratoreLastVersion(){}

    public static final String MALE = "M";
    public static final String FEMALE = "F";

    private String name;
    private String surname;
    private String sex;
    private String image;
    private String fiscalcode;
    private Date birthDate;
    private String nationality;
    private String birthProvince;
    private String birthPlace;   // comune di nascita
    private String livingProvince;
    private String livingCity;
    private String address;
    private String addressco;
    private String cap;
    private String phone;
    private String cellphone;
    private String mail;

    private Fondo fund; //attribuzione 3
    private Attribuzione1 attribuzione1; //attribuzione 3
    private Attribuzione2 attribuzione2; //attribuzione 3
    private Attribuzione3 attribuzione3; //attribuzione 4
    private String notes;
    private boolean privacy;
    //proprieà che concatenanao in modi differenti il nome e cognome per farorie ricerche globali
    //con stringa unica
    //esse vengono impostate prima del salvataggio nel servizio che aggiorna l'anagrafica...
    private String namesurname;
    private String surnamename;

    private Date creationDate;
    private String creatorUser;
    private Date lastUpdateDate;
    private String updateUser;

    private long lavoratoreId;

    public LavoratoreLastVersion(Lavoratore lav) {
        this.lavoratoreId = lav.getLid();

        this.name = lav.getName();
        this.surname = lav.getSurname();
        this.sex = lav.getSex();
        this.image = lav.getImage();
        this.fiscalcode = lav.getFiscalcode();
        this.birthDate = lav.getBirthDate();
        this.nationality = lav.getNationality();
        this.birthProvince = lav.getBirthProvince();
        this.birthPlace = lav.getBirthPlace();   // comune di nascita
        this.livingProvince = lav.getLivingProvince();
        this.livingCity = lav.getLivingCity();
        this.address = lav.getAddress();
        this.addressco = lav.getAddressco();
        this.cap = lav.getCap();
        this.phone = lav.getPhone();
        this.cellphone = lav.getCellphone();
        this.mail = lav.getMail();

        this.fund = lav.getFund(); //attribuzione 3
        this.attribuzione1 = lav.getAttribuzione1(); //attribuzione 3
        this.attribuzione2 = lav.getAttribuzione2(); //attribuzione 3
        this.attribuzione3 = lav.getAttribuzione3(); //attribuzione 4
        this.notes = lav.getNotes();
        this.privacy = lav.isPrivacy();
        this.namesurname = lav.getNamesurname();
        this.surnamename = lav.getSurnamename();

        this.creationDate = lav.getCreationDate();
        this.creatorUser = lav.getCreatorUser();
        this.lastUpdateDate = lav.getLastUpdateDate();
        this.updateUser = lav.getUpdateUser();



    }

    public long getLavoratoreId() {
        return lavoratoreId;
    }

    public void setLavoratoreId(long lavoratoreId) {
        this.lavoratoreId = lavoratoreId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getCreatorUser() {
        return creatorUser;
    }

    public void setCreatorUser(String creatorUser) {
        this.creatorUser = creatorUser;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

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

    public Attribuzione3 getAttribuzione3() {
        return attribuzione3;
    }

    public void setAttribuzione3(Attribuzione3 attribuzione3) {
        this.attribuzione3 = attribuzione3;
    }

    public Attribuzione1 getAttribuzione1() {
        return attribuzione1;
    }

    public void setAttribuzione1(Attribuzione1 attribuzione1) {
        this.attribuzione1 = attribuzione1;
    }

    public Attribuzione2 getAttribuzione2() {
        return attribuzione2;
    }

    public void setAttribuzione2(Attribuzione2 attribuzione2) {
        this.attribuzione2 = attribuzione2;
    }






    @Override
    public String toString() {
        return String.format("%s %s", surname, name);
    }

    public String getNamesurname() {
        return namesurname;
    }

    public void setNamesurname(String namesurname) {
        this.namesurname = namesurname;
    }

    public String getSurnamename() {
        return surnamename;
    }

    public void setSurnamename(String surnamename) {
        this.surnamename = surnamename;
    }

    public String getName() {
        if (name!=null)
            return name.toUpperCase();
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        if (surname!=null)
            return surname.toUpperCase();
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getFiscalcode() {
        if (fiscalcode!=null)
            return fiscalcode.toUpperCase();
        return fiscalcode;
    }

    public void setFiscalcode(String fiscalcode) {
        this.fiscalcode = fiscalcode;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getNationality() {
        if (nationality!=null)
            return nationality.toUpperCase();
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getBirthProvince() {
        if (birthProvince!=null)
            return birthProvince.toUpperCase();
        return birthProvince;
    }

    public void setBirthProvince(String birthProvince) {
        this.birthProvince = birthProvince;
    }

    public String getBirthPlace() {
        if (birthPlace!=null)
            return birthPlace.toUpperCase();
        return birthPlace;
    }

    public void setBirthPlace(String birthPlace) {
        this.birthPlace = birthPlace;
    }

    public String getLivingProvince() {
        if (livingProvince!=null)
            return livingProvince.toUpperCase();
        return livingProvince;
    }

    public void setLivingProvince(String livingProvince) {
        this.livingProvince = livingProvince;
    }

    public String getLivingCity() {
        if (livingCity!=null)
            return livingCity.toUpperCase();
        return livingCity;
    }

    public void setLivingCity(String livingCity) {
        this.livingCity = livingCity;
    }

    public String getAddress() {
        if (address!=null)
            return address.toUpperCase();
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCap() {
        return cap;
    }

    public void setCap(String cap) {
        this.cap = cap;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
    }


    public Fondo getFund() {
        return fund;
    }

    public void setFund(Fondo fund) {
        this.fund = fund;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}