package applica.feneal.admin.fields.renderers;

import applica.feneal.domain.model.core.configuration.Categoria;
import applica.framework.library.SimpleItem;
import applica.framework.widgets.fields.renderers.OptionalSelectFieldRenderer;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgran on 27/01/2017.
 */
@Component
public class SectorTypeForStampaTessereSelectRenderer extends OptionalSelectFieldRenderer {


    @Override
    public List<SimpleItem> getItems() {


        List<Categoria> sectors = new ArrayList<>();
        List<SimpleItem> res = SimpleItem.createList(sectors, (s) -> s.getDescription(), (s) -> String.valueOf(s.getId()));


        return res;
    }
}

