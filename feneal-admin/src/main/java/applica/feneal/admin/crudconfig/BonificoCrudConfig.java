package applica.feneal.admin.crudconfig;


import applica.feneal.admin.data.BonificoRepositoryWrapper;
import applica.feneal.domain.model.core.quote.Bonifico;
import applica.feneal.services.impl.setup.AppSetup;
import applica.framework.widgets.builders.GridConfigurator;
import org.springframework.stereotype.Component;

@Component
public class BonificoCrudConfig implements AppSetup {
    @Override
    public void setup() {




        GridConfigurator.configure(Bonifico.class, "bonifico")
                .repository(BonificoRepositoryWrapper.class)
                .column("data", "Descrizione", false)
         .column("user", "Utente", false)
         .column("note", "Note", false);

    }
}

