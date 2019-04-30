package applica.feneal.admin.fields.renderers.custom;

import applica.framework.widgets.FormField;
import applica.framework.widgets.velocity.VelocityFormFieldRenderer;
import org.apache.velocity.VelocityContext;

import java.io.Writer;

/**
 * Created by fgran on 12/02/2018.
 */
public abstract class CategorySingleSearchbleInputFieldRenderer extends VelocityFormFieldRenderer {
    public CategorySingleSearchbleInputFieldRenderer() {
    }

    public abstract String getLabel(FormField var1, Object var2);

    public abstract String getServiceUrl();

    public abstract String getCategoryServiceUrl();

    public void render(Writer writer, FormField field, Object value) {
        this.putExtraContextValue("label", this.getLabel(field, value));
        super.render(writer, field, value);
    }

    protected void setupContext(VelocityContext context) {
        super.setupContext(context);
        context.put("serviceUrl", this.getServiceUrl());
        context.put("categoryServiceUrl", this.getCategoryServiceUrl());
    }

    public String getTemplatePath() {
        return "/templates/fields/category_single_searchable_input.vm";
    }
}
