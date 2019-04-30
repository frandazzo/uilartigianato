package applica.feneal.admin.fields.renderers.uil;

import applica.feneal.domain.model.User;
import applica.framework.security.Security;
import applica.framework.widgets.FormField;
import applica.framework.widgets.fields.renderers.ReadOnlyFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Writer;

/**
 * Created by fgran on 03/05/2017.
 */
@Component
public class UilCategoryForEntityFieldRenderer extends ReadOnlyFieldRenderer {

    @Autowired
    private Security sec;



    public void render(Writer writer, FormField formField, Object value) {
        String templatePath = this.createTemplatePath(formField.getForm(), formField);
        this.setTemplatePath(templatePath);

        Object id = formField.getForm().getData().get("id");
        if (id == null)
            super.render(writer, formField, ((User) sec.getLoggedUser()).getCompleteName());
        else
            super.render(writer, formField,value);

    }


}
