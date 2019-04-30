package applica.feneal.admin.fields.renderers;

import applica.framework.widgets.FormField;
import applica.framework.widgets.fields.renderers.SingleSearchableInputFieldRenderer;
import org.springframework.stereotype.Component;

/**
 * Created by fgran on 14/09/2017.
 */
@Component
public class WorkersSingleSearchblafieldRenderer extends SingleSearchableInputFieldRenderer {
    @Override
    public String getLabel(FormField formField, Object o) {
        return o != null? o.toString() : "NA";
    }

    @Override
    public String getServiceUrl() {
        return "values/workers";
    }
}