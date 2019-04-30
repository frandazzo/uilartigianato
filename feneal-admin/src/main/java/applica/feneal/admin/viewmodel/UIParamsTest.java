package applica.feneal.admin.viewmodel;

import applica.feneal.admin.fields.renderers.ParamsPrinterRenderer;
import applica.framework.IEntity;
import applica.framework.data.hibernate.annotations.IgnoreMapping;
import applica.framework.widgets.annotations.*;

/**
 * Applica (www.applica.guru)
 * User: bimbobruno
 * Date: 20/02/14
 * Time: 13:12
 */
@Form("params_test")
@IgnoreMapping
public class UIParamsTest extends IEntity {

    @FormField(description = "name")
    @FormFieldRenderer(ParamsPrinterRenderer.class)
    @Param(key = "iaco", value = "bucci")
    private String name;

    @FormField(description = "name")
    @FormFieldRenderer(ParamsPrinterRenderer.class)
    @Param(key = "fortu", value = "nato")
    private String surname;


    @FormField(description = "name")
    @FormFieldRenderer(ParamsPrinterRenderer.class)
    @Params({
            @Param(key = "eret", value = "erterter"),
            @Param(key = "sfsfds", value = "fdsfdsf"),
            @Param(key = "gnoff", value = "sdfafasd")
    })
    private String strazzaplle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getStrazzaplle() {
        return strazzaplle;
    }

    public void setStrazzaplle(String strazzaplle) {
        this.strazzaplle = strazzaplle;
    }
}
