package applica.feneal.admin.viewmodel.lavoratori;

import applica.feneal.domain.model.core.lavoratori.Lavoratore;

import java.util.List;

/**
 * Created by angelo on 15/06/2017.
 */
public class UiListaLavoroSendEmail {

    private List<Lavoratore> lavoratori;
    private String text;
    private String subject;
    private String province;
    private String campagna;


    public List<Lavoratore> getLavoratori() {
        return lavoratori;
    }

    public void setLavoratori(List<Lavoratore> lavoratori) {
        this.lavoratori = lavoratori;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCampagna() {
        return campagna;
    }

    public void setCampagna(String campagna) {
        this.campagna = campagna;
    }
}
