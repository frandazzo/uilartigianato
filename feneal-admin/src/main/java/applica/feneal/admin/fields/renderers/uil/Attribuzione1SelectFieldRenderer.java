package applica.feneal.admin.fields.renderers.uil;

import applica.feneal.domain.data.core.Attribuzione1Repository;
import applica.feneal.domain.model.setting.Attribuzione1;
import applica.framework.LoadRequest;
import applica.framework.library.SimpleItem;
import applica.framework.widgets.fields.renderers.OptionalSelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fgran on 06/05/2017.
 */
@Component
public class Attribuzione1SelectFieldRenderer extends OptionalSelectFieldRenderer {

    @Autowired
    private Attribuzione1Repository fundRepository;

    @Override
    public List<SimpleItem> getItems() {

        List<Attribuzione1> funds = fundRepository.find(LoadRequest.build()).getRows();
        return SimpleItem.createList(funds, (f) -> f.getDescription(), (f) -> String.valueOf(f.getId()));
    }
}
