package applica.feneal.admin.fields.renderers;

import applica.feneal.domain.data.core.EmailLayoutRepository;
import applica.feneal.domain.model.setting.EmailLayout;
import applica.framework.LoadRequest;
import applica.framework.widgets.Form;
import applica.framework.widgets.FormField;
import applica.framework.widgets.fields.renderers.TemplateFormFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.util.List;

/**
 * Created by fgran on 02/05/2017.
 */
@Component
public class EmailLayoutSelectFieldRenderer extends TemplateFormFieldRenderer {

    @Autowired
    private EmailLayoutRepository emailLayoutRep;

    @Override
    public void render(Writer writer, FormField formField, Object value) {
        List<EmailLayout> emailLayouts = emailLayoutRep.find(LoadRequest.build()).getRows();
        putExtraContextValue("emailLayouts", emailLayouts);

        super.render(writer, formField, value);
    }


    @Override
    protected String createTemplatePath(Form form, FormField formField) {
        return "templates/forms/email_layout_select.vm";
    }
}
