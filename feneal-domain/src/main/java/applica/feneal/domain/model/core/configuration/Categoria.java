package applica.feneal.domain.model.core.configuration;

import applica.framework.AEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by fgran on 17/03/2017.
 */
@JsonIgnoreProperties({ "iid", "sid", "lid" })
public class Categoria extends AEntity {

    public static final  String feneal = "FENEAL";
    public static final String uilm  = "UILM";

    public static final  String uila = "UILA";
    public static final String uilt  = "UILT";
    public static final  String uiltucs = "UILTUCS";
    public static final String uilcom  = "UILCOM";
    public static final  String uiltec = "UILTEC";
    public static final  String uiltemp = "UILTEMP";




    private String description;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        return description;
    }
}
