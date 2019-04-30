package applica.feneal.admin.fields.renderers.uil;

import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.framework.LoadRequest;
import applica.framework.library.SimpleItem;
import applica.framework.widgets.fields.renderers.SelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fgran on 02/05/2017.
 */
@Component
public class CategorySelectFieldRenderer extends SelectFieldRenderer {

    @Autowired
    private CategoriaRepository catRep;

    @Override
    public List<SimpleItem> getItems() {

        List<Categoria> causaleIscrizList = catRep.find(LoadRequest.build()).getRows();
        return SimpleItem.createList(causaleIscrizList, (c) -> c.getDescription(), (c) -> String.valueOf(c.getId()));
    }

}