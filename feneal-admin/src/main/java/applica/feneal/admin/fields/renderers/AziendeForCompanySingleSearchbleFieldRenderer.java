package applica.feneal.admin.fields.renderers;

import applica.feneal.admin.fields.renderers.custom.CategorySingleSearchbleInputFieldRenderer;
import applica.framework.widgets.FormField;
import org.springframework.stereotype.Component;

/**
 * Created by fgran on 12/02/2018.
 */
@Component
public class AziendeForCompanySingleSearchbleFieldRenderer extends CategorySingleSearchbleInputFieldRenderer {
    @Override
    public String getLabel(FormField formField, Object o) {
        return o != null? o.toString() : "NA";
    }

    @Override
    public String getServiceUrl() {
        return "values/aziendebyregion";
    }

    @Override
    public String getCategoryServiceUrl() {
        return "values/companies";
    }
}