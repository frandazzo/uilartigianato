package applica.feneal.admin.fields.renderers.uil;

import applica.framework.library.SimpleItem;
import applica.framework.widgets.fields.renderers.SelectFieldRenderer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgran on 11/11/2017.
 */

    @Component
    public class TesserePrintPositionFieldRenderer extends SelectFieldRenderer {


        @Override
        public List<SimpleItem> getItems() {

            List<SimpleItem> result = new ArrayList<SimpleItem>();

            SimpleItem i = new SimpleItem();
            i.setValue("1");
            i.setLabel("Stampa dalla prima");
            result.add(i);

            SimpleItem i1 = new SimpleItem();
            i1.setValue("2");
            i1.setLabel("Stampa dalla seconda");
            result.add(i1);

            SimpleItem i2 = new SimpleItem();
            i2.setValue("3");
            i2.setLabel("Stampa dalla terza");
            result.add(i2);




            return result;
        }
    }
