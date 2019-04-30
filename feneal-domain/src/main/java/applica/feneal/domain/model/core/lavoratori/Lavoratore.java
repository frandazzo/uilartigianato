package applica.feneal.domain.model.core.lavoratori;

import applica.feneal.domain.model.setting.Attribuzione1;
import applica.feneal.domain.model.setting.Attribuzione2;
import applica.feneal.domain.model.setting.Attribuzione3;
import applica.feneal.domain.model.setting.Fondo;
import applica.feneal.domain.model.utils.SecuredDomainEntity;
import applica.feneal.domain.utils.Box;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by fgran on 05/04/2016.
 */
public class Lavoratore extends SecuredDomainEntity {

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
//    private String ce; //  attribuzione 1
//    private String ec; // attribuzione 2
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




    public Lavoratore(){}
//

    private void calculateSplitNameAndSurname(Box box){

        String sur = box.getValue().toString().toUpperCase();
        //String nam = box.getValue1().toString().toUpperCase();

        //avvio l'algoritmo per la separazione del nome e del cognome
        //puo capitare nel database nazionale che sia valorizzato solo il cognome
        //allora bisognerà splittare il nome e il cognome in base algi spazi
        //per definizione viene scritto prima il nome e poi il cognome
        //pertanto eseguo una split del cognome sugli spazi e se il numero di elementi è 1 il nome sarà "(vuoto)"
        //altrimenti se il numero è 2 allora il secondo elemmento sarà il nome

        //per prima cosa rimuovo tutti gli eventuali spazi
        while (org.apache.commons.lang3.StringUtils.countMatches(sur, "  ") > 0){
            String reduced = sur.replace("  " , " ");
            sur = reduced;
        }
        //ottengo adesso la lista degli elementi del cognome nome...
        String[] p = sur.split(" ");

        if (p.length == 1){
            box.setValue1("(vuoto)");
            return;
        }

        if (p.length == 2){
            box.setValue(p[0]);
            box.setValue1(p[1]);
            return;
        }

        //se ci sono piu di due elementi controllo il primo elemento e se è diverso da
        List<String> dan = Arrays.asList("DE", "DI", "DEL", "DELLI" , "DELLA", "DALLA" , "LA", "LE", "LI", "LO", "EL");
        if (!dan.contains(p[0])){
            //allora metto il primo elemento nel cognome e tutti gli altri nel nome
            box.setValue(p[0]);
            box.setValue1(calculateNameFromArrayPosition(p, 1));
        }else{
            //metto i primi due nel cognome e i restanti nel nome
            box.setValue(p[0] + " " + p[1]);
            box.setValue1(calculateNameFromArrayPosition(p, 2));
        }

    }

    private String calculateNameFromArrayPosition(String[] elements, int position){
        String result = "";
        int i = 0;
        for (String element : elements) {
            if (i>=position)
                result = result + " " + element;
            i++;
        }

        return result.trim();
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
