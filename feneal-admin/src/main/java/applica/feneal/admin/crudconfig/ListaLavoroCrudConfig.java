package applica.feneal.admin.crudconfig;

import applica.feneal.admin.data.ListaLavoroRepositoryWrapper;
import applica.feneal.admin.search.ListeLavoroSearchForm;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.services.impl.setup.AppSetup;
import applica.framework.widgets.builders.GridConfigurator;
import org.springframework.stereotype.Component;

/**
 * Created by angelo on 11/06/2016.
 */
@Component
public class ListaLavoroCrudConfig implements AppSetup {




    @Override
    public void setup() {

        GridConfigurator.configure(ListaLavoro.class, "listalavoro")
                .repository(ListaLavoroRepositoryWrapper.class)
                .searchForm(ListeLavoroSearchForm.class)

                .column("description", "Descrizione", true);
    }
}

