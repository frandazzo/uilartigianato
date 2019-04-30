package applica.feneal.domain.model.core.deleghe;

import java.math.BigInteger;

public class DelegaPerCreazioneQuota
{

    private String azienda;
    private BigInteger idAzienda;
    private String regione;
    private BigInteger idRegione;
    private String provincia;
    private Integer idProvincia;

    public String getAzienda() {
        return azienda;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    public BigInteger getIdAzienda() {
        return idAzienda;
    }

    public void setIdAzienda(BigInteger idAzienda) {
        this.idAzienda = idAzienda;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public BigInteger getIdRegione() {
        return idRegione;
    }

    public void setIdRegione(BigInteger idRegione) {
        this.idRegione = idRegione;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Integer getIdProvincia() {
        return idProvincia;
    }

    public void setIdProvincia(Integer idProvincia) {
        this.idProvincia = idProvincia;
    }
}
