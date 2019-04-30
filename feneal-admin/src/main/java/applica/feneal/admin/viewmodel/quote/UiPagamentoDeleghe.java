package applica.feneal.admin.viewmodel.quote;

import applica.feneal.admin.viewmodel.reports.UiDelega;

import java.util.List;

public class UiPagamentoDeleghe {
    private List<UiDelega> deleghe;
    private String dataInizio;
    private String dataFine;

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

    private double importo;

    public double getImporto() {
        return importo;
    }

    public void setImporto(double importo) {
        this.importo = importo;
    }

    public List<UiDelega> getDeleghe() {
        return deleghe;
    }

    public void setDeleghe(List<UiDelega> deleghe) {
        this.deleghe = deleghe;
    }
}
