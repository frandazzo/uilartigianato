package applica.feneal.admin.fields.renderers;


import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.framework.library.SimpleItem;
import applica.framework.security.Security;
import applica.framework.widgets.fields.renderers.SelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fgran on 16/05/2016.
 */
@Component
public class SectorNonOptionalSelectFieldRenderer extends SelectFieldRenderer {


    @Autowired
    private CategoriaRepository catRep;

    @Autowired
    private Security sec;

    @Override
    public List<SimpleItem> getItems() {

        List<Categoria> sectors = catRep.find(null).getRows();
        return SimpleItem.createList(sectors, (s) -> s.getDescription(), (s) -> String.valueOf(s.getId()));
    }

}
