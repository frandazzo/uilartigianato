package applica.feneal.services.impl.importData;

import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;

import java.util.HashMap;

/**
 * Created by fgran on 20/05/2016.
 */
public class ImportCache {

    private HashMap<Long, Azienda>  aziendeById =new HashMap<Long, Azienda>();
    private HashMap<Long, Lavoratore>  lavoratori =new HashMap<Long, Lavoratore>();
    private HashMap<String, Lavoratore>  lavoratoriFiscalCode =new HashMap<String, Lavoratore>();
    private HashMap<String, Azienda>  aziende=new HashMap<String, Azienda>();


    public HashMap<Long, Azienda> getAziendeById() {
        return aziendeById;
    }

    public void setAziendeById(HashMap<Long, Azienda> aziendeById) {
        this.aziendeById = aziendeById;
    }

    public HashMap<String, Lavoratore> getLavoratoriFiscalCode() {
        return lavoratoriFiscalCode;
    }

    public void setLavoratoriFiscalCode(HashMap<String, Lavoratore> lavoratoriFiscalCode) {
        this.lavoratoriFiscalCode = lavoratoriFiscalCode;
    }

    public HashMap<String, Azienda> getAziende() {
        return aziende;
    }

    public void setAziende(HashMap<String, Azienda> aziende) {
        this.aziende = aziende;
    }

    public HashMap<Long, Lavoratore> getLavoratori() {
        return lavoratori;
    }

    public void setLavoratori(HashMap<Long, Lavoratore> lavoratori) {
        this.lavoratori = lavoratori;
    }
}
