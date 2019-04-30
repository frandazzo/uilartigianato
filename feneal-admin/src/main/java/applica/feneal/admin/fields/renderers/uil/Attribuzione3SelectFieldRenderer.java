package applica.feneal.admin.fields.renderers.uil;

import applica.feneal.domain.data.core.Attribuzione3Repository;
import applica.feneal.domain.model.setting.Attribuzione3;
import applica.framework.LoadRequest;
import applica.framework.library.SimpleItem;
import applica.framework.widgets.fields.renderers.OptionalSelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fgran on 22/09/2017.
 */
@Component
public class Attribuzione3SelectFieldRenderer extends OptionalSelectFieldRenderer {

    @Autowired
    private Attribuzione3Repository fundRepository;

    @Override
    public List<SimpleItem> getItems() {

        List<Attribuzione3> funds = fundRepository.find(LoadRequest.build()).getRows();
        return SimpleItem.createList(funds, (f) -> f.getDescription(), (f) -> String.valueOf(f.getId()));
    }
}

