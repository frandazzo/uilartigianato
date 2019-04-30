package applica.feneal.admin.fields.renderers.uil;

import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.framework.library.SimpleItem;
import applica.framework.widgets.fields.renderers.OptionalSelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fgran on 28/12/2017.
 */
@Component
public class OptionalSectorTypeWithoutInpsSelectRenderer extends OptionalSelectFieldRenderer {

    @Autowired
    private CategoriaRepository catRep;
    @Override
    public List<SimpleItem> getItems() {


        List<Categoria> sectors = catRep.find(null).getRows();
        List<SimpleItem> res = SimpleItem.createList(sectors, (s) -> s.getDescription(), (s) -> s.getDescription());

        return res;
    }
}
