package applica.feneal.domain.model.core;

/**
 * Created by fgran on 18/04/2017.
 */
public class ImportData {

    private String file1;
    private Integer updateResidenza;
    private Integer updateAttribuzioni;
    private Integer updateTelefoni;
    private Integer updateAzienda;
    private Integer createLista;
    private Integer createDelega;
    private Integer associateDelega;
    private String settore;
    private String dataInizio;
    private String dataFine;
    private String company;
    private String province;


    public Integer getCreateDelega() {
        return createDelega;
    }

    public void setCreateDelega(Integer createDelega) {
        this.createDelega = createDelega;
    }

    public Integer getAssociateDelega() {
        return associateDelega;
    }

    public void setAssociateDelega(Integer associateDelega) {
        this.associateDelega = associateDelega;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }


    public String getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(String dataInizio) {
        this.dataInizio = dataInizio;
    }

    public String getDataFine() {
        return dataFine;
    }

    public void setDataFine(String dataFine) {
        this.dataFine = dataFine;
    }

    public String getFile1() {
        return file1;
    }

    public void setFile1(String file1) {
        this.file1 = file1;
    }

    public Integer getUpdateResidenza() {
        return updateResidenza;
    }

    public void setUpdateResidenza(Integer updateResidenza) {
        this.updateResidenza = updateResidenza;
    }

    public Integer getUpdateAttribuzioni() {
        return updateAttribuzioni;
    }

    public void setUpdateAttribuzioni(Integer updateAttribuzioni) {
        this.updateAttribuzioni = updateAttribuzioni;
    }

    public Integer getUpdateTelefoni() {
        return updateTelefoni;
    }

    public void setUpdateTelefoni(Integer updateTelefoni) {
        this.updateTelefoni = updateTelefoni;
    }

    public Integer getUpdateAzienda() {
        return updateAzienda;
    }

    public void setUpdateAzienda(Integer updateAzienda) {
        this.updateAzienda = updateAzienda;
    }

    public Integer getCreateLista() {
        return createLista;
    }

    public void setCreateLista(Integer createLista) {
        this.createLista = createLista;
    }

    public String getSettore() {
        return settore;
    }

    public void setSettore(String settore) {
        this.settore = settore;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }
}
