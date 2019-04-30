package applica.feneal.admin.viewmodel.widget.real;

import applica.feneal.admin.viewmodel.widget.RowListInteger;

import java.util.List;

/**
 * Created by david on 30/05/2016.
 */
public class UIAndamentoData {

    private List<String> anniLabels;
    private List<Integer> anni;
    private List<RowListInteger> values;

    public List<String> getAnniLabels() {
        return anniLabels;
    }

    public void setAnniLabels(List<String> anniLabels) {
        this.anniLabels = anniLabels;
    }

    private String provincia;

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public List<Integer> getAnni() {
        return anni;
    }

    public void setAnni(List<Integer> anni) {
        this.anni = anni;
    }


    public List<RowListInteger> getValues() {
        return values;
    }

    public void setValues(List<RowListInteger> values) {
        this.values = values;
    }
}
