package applica.feneal.admin.crudconfig;

import applica.feneal.admin.fields.renderers.EmailLayoutImageFieldRenderer;
import applica.feneal.admin.fields.renderers.EmailLayoutTypeFieldRenderer;
import applica.feneal.domain.data.core.EmailLayoutRepository;
import applica.feneal.domain.model.setting.EmailLayout;
import applica.feneal.services.impl.setup.AppSetup;
import applica.framework.widgets.builders.FormConfigurator;
import applica.framework.widgets.builders.GridConfigurator;
import applica.framework.widgets.fields.Params;
import applica.framework.widgets.fields.Values;
import org.springframework.stereotype.Component;

/**
 * Applica
 * User: Angelo D'Alconzo
 * Date: 09/06/17
 * Time: 10:30 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class EmailLayoutCrudConfig implements AppSetup {
    @Override
    public void setup() {

        FormConfigurator.configure(EmailLayout.class, "emaillayout")
                .repository(EmailLayoutRepository.class)
                .field("name", "Nome").param("name", Params.ROW, "dt1").param("name", Params.COLS, Values.COLS_12)
                .field("type", "Tipologia", EmailLayoutTypeFieldRenderer.class).param(Params.ROW, "dt2").param(Params.COLS, Values.COLS_12)
                .field("imagePath1", "Immagine1", EmailLayoutImageFieldRenderer.class).param(Params.ROW, "dt3").param(Params.COLS, Values.COLS_6)
                .field("imagePath2", "Immagine2", EmailLayoutImageFieldRenderer.class).param(Params.ROW, "dt3").param(Params.COLS, Values.COLS_6);


        GridConfigurator.configure(EmailLayout.class, "emaillayout")
                .repository(EmailLayoutRepository.class)
                .column("name", "Nome", true)
                .column("type", "Tipo", false);
    }
}
