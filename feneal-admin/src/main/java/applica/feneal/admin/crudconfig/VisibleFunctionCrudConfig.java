package applica.feneal.admin.crudconfig;

import applica.feneal.admin.fields.renderers.VisibleFunctionSelectRenderer;
import applica.feneal.domain.data.core.VisibleFunctionRepository;
import applica.feneal.domain.model.core.VisibleFunction;
import applica.feneal.services.impl.setup.AppSetup;
import applica.framework.widgets.builders.FormConfigurator;
import applica.framework.widgets.builders.GridConfigurator;
import org.springframework.stereotype.Component;

/**
 * Applica
 * User: Ciccio Randazzo
 * Date: 10/13/15
 * Time: 1:25 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class VisibleFunctionCrudConfig implements AppSetup {
    @Override
    public void setup() {

        FormConfigurator.configure(VisibleFunction.class, "visiblefunction")
                .repository(VisibleFunctionRepository.class)
                .field("description", "Descrizione")
                .field("type", "Tipo", VisibleFunctionSelectRenderer.class);


        GridConfigurator.configure(VisibleFunction.class, "visiblefunction")
                .repository(VisibleFunctionRepository.class)
                .column("description", "Descrizione", true)
                .column("type", "Tipo", false);
    }
}
