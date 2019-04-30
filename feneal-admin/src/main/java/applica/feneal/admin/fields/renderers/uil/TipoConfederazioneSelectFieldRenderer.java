package applica.feneal.admin.fields.renderers.uil;

import applica.framework.library.SimpleItem;
import applica.framework.widgets.FormField;
import applica.framework.widgets.fields.renderers.SelectFieldRenderer;
import org.springframework.stereotype.Component;

import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgran on 04/04/2017.
 */
@Component
public class TipoConfederazioneSelectFieldRenderer extends SelectFieldRenderer {


    @Override
    public List<SimpleItem> getItems() {

        SimpleItem t1 = new SimpleItem("REGIONALE", "1");
        SimpleItem t2 = new SimpleItem("NAZIONALE", "2");
        SimpleItem t3 = new SimpleItem("UNC", "3");

        List<SimpleItem> l = new ArrayList<SimpleItem>();
        l.add(t1);
        l.add(t2);
        l.add(t3);

        return l;

    }


    public void render(Writer writer, FormField field, Object value) {
        this.setTemplatePath("/templates/fields/select.vm");
        if(value == null) {
            value = 3;
        }

        super.render(writer, field, value);
    }
}
