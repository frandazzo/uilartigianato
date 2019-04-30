package applica.feneal.admin.fields.renderers;


import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.framework.library.SimpleItem;
import applica.framework.widgets.fields.renderers.OptionalSelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by angelo on 17/04/16.
 */
@Component
public class SectorSelectFieldRenderer extends OptionalSelectFieldRenderer {

    @Autowired
    private CategoriaRepository sec;

    @Override
    public List<SimpleItem> getItems() {

        List<Categoria> sectors = sec.find(null).getRows();
        return SimpleItem.createList(sectors, (s) -> s.getDescription(), (s) -> String.valueOf(s.getId()));
    }

}
