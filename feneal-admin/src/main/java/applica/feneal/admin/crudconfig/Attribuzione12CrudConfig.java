package applica.feneal.admin.crudconfig;

import applica.feneal.domain.data.core.Attribuzione1Repository;
import applica.feneal.domain.data.core.Attribuzione2Repository;
import applica.feneal.domain.data.core.Attribuzione3Repository;
import applica.feneal.domain.model.setting.Attribuzione1;
import applica.feneal.domain.model.setting.Attribuzione2;
import applica.feneal.domain.model.setting.Attribuzione3;
import applica.feneal.services.impl.setup.AppSetup;
import applica.framework.widgets.builders.FormConfigurator;
import applica.framework.widgets.builders.GridConfigurator;
import org.springframework.stereotype.Component;

/**
 * Created by fgran on 06/05/2017.
 */
@Component
public class Attribuzione12CrudConfig implements AppSetup {
    @Override
    public void setup() {

        FormConfigurator.configure(Attribuzione1.class, "attribuzione1")
                .repository(Attribuzione1Repository.class)
                .field("description", "Descrizione");


        GridConfigurator.configure(Attribuzione1.class, "attribuzione1")
                .repository(Attribuzione1Repository.class)
                .column("description", "Descrizione", true);



        FormConfigurator.configure(Attribuzione2.class, "attribuzione2")
                .repository(Attribuzione2Repository.class)
                .field("description", "Descrizione");


        GridConfigurator.configure(Attribuzione2.class, "attribuzione2")
                .repository(Attribuzione2Repository.class)
                .column("description", "Descrizione", true);


        FormConfigurator.configure(Attribuzione3.class, "attribuzione3")
                .repository(Attribuzione3Repository.class)
                .field("description", "Descrizione");


        GridConfigurator.configure(Attribuzione3.class, "attribuzione3")
                .repository(Attribuzione3Repository.class)
                .column("description", "Descrizione", true);
    }
}

