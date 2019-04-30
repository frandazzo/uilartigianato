package applica.feneal.admin.fields.renderers;

import applica.framework.widgets.Form;
import applica.framework.widgets.FormField;
import applica.framework.widgets.fields.renderers.TemplateFormFieldRenderer;
import org.springframework.stereotype.Component;


@Component
public class EmailLayoutTypeFieldRenderer extends TemplateFormFieldRenderer {

    @Override
    protected String createTemplatePath(Form form, FormField formField) {
        return "templates/forms/email_layout_type.vm";
    }

}
