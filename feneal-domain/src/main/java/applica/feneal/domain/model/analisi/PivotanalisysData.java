package applica.feneal.domain.model.analisi;

public class PivotanalisysData {


//    { dataField: "Anno", area: "column", sortByPath: [] },
//    { dataField: "Categoria", area: "row",  sortOrder: "desc" },
//    { dataField: "Regione", area: "filter",  sortOrder: "desc" },
//    { dataField: "Provincia", area: "filter",  sortOrder: "desc" },
//    { dataField: "Nazionalita", area: "filter",  sortOrder: "desc" },
//    { dataField: "Id_Lavoratore", caption: "Num. Lavoratori", summaryType: "count", area: "data" }


    private int anno;
    private String categoria;
    private String regione;
    private String provincia;
    private String nazionalita;
    private long id_Lavoratore;
    private  String settore;

    public String getSettore() {
        return settore;
    }

    public void setSettore(String settore) {
        this.settore = settore;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getRegione() {
        return regione;
    }

    public void setRegione(String regione) {
        this.regione = regione;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getNazionalita() {
        return nazionalita;
    }

    public void setNazionalita(String nazionalita) {
        this.nazionalita = nazionalita;
    }

    public long getId_Lavoratore() {
        return id_Lavoratore;
    }

    public void setId_Lavoratore(long id_Lavoratore) {
        this.id_Lavoratore = id_Lavoratore;
    }
}
