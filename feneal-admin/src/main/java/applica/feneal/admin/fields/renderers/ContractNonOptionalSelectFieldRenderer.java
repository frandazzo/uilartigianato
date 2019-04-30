package applica.feneal.admin.fields.renderers;


import applica.feneal.domain.data.core.ContractRepository;
import applica.feneal.domain.model.core.configuration.Contract;
import applica.framework.library.SimpleItem;
import applica.framework.widgets.fields.renderers.SelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by antoniolovicario on 16/04/16.
 */
@Component
public class ContractNonOptionalSelectFieldRenderer extends SelectFieldRenderer {



    @Autowired
    private ContractRepository sec;

    @Override
    public List<SimpleItem> getItems() {



        List<Contract> enti = sec.find(null).getRows();




        return SimpleItem.createList(enti, (s) -> s.getDescription(), (s) -> String.valueOf(s.getLid()));
    }
}
