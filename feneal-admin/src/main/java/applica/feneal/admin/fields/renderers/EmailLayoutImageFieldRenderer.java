package applica.feneal.admin.fields.renderers;

import applica.framework.widgets.fields.renderers.ImagesFieldRenderer;
import org.springframework.stereotype.Component;

/**
 * Created by angelo on 12/06/2017.
 */
@Component
public class EmailLayoutImageFieldRenderer extends ImagesFieldRenderer {

    @Override
    public String getPath() {
        return "emaillayout_tipo/";
    }
}
