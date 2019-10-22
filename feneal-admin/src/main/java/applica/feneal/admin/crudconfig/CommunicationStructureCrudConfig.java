package applica.feneal.admin.crudconfig;


import applica.feneal.admin.fields.renderers.DocumentFileRenderer;
import applica.feneal.admin.fields.renderers.empty.EmptyFieldRenderer;
import applica.feneal.domain.data.core.CommunicationStructureRepository;
import applica.feneal.domain.model.core.CommunicationStructure;
import applica.feneal.services.impl.setup.AppSetup;
import applica.framework.widgets.builders.FormConfigurator;
import applica.framework.widgets.builders.GridConfigurator;
import applica.framework.widgets.fields.Params;
import applica.framework.widgets.fields.Values;
import applica.framework.widgets.fields.renderers.DatePickerRenderer;
import applica.framework.widgets.fields.renderers.HtmlFieldRenderer;
import applica.framework.widgets.fields.renderers.TextAreaFieldRenderer;
import org.springframework.stereotype.Component;

@Component
public class CommunicationStructureCrudConfig implements AppSetup {

    @Override
    public void setup() {
        FormConfigurator.configure(CommunicationStructure.class, "communicationstruct")
                .repository(CommunicationStructureRepository.class)
                .fieldSet("Dati per invio comunicazione")
                .field("briefDescription", "Titolo")
                .field("date","Data Comunicazione", DatePickerRenderer.class)
                .field("description", "Comunicazione", HtmlFieldRenderer.class)
                .fieldSet("Caricamento Allegati")
                .field("nomeattached1", "Nome 1° Allegato")
                .param("nomeattached1", Params.ROW, "dt1")
                .param("nomeattached1", Params.COLS, Values.COLS_6)
                .field("attached1", "1° Allegato", DocumentFileRenderer.class)
                .param("attached1", Params.ROW, "dt1")
                .param("attached1", Params.COLS, Values.COLS_6)
                .field("nomeattached2", "Nome 2° Allegato")
                .param("nomeattached2", Params.ROW, "dt2")
                .param("nomeattached2", Params.COLS, Values.COLS_6)
                .field("attached2", "2° Allegato", DocumentFileRenderer.class)
                .param("attached2", Params.ROW, "dt2")
                .param("attached2", Params.COLS, Values.COLS_6)
                .field("nomeattached3", "Nome 3° Allegato")
                .param("nomeattached3", Params.ROW, "dt3")
                .param("nomeattached3", Params.COLS, Values.COLS_6)
                .field("attached3", "3° Allegato", DocumentFileRenderer.class)
                .param("attached3", Params.ROW, "dt3")
                .param("attached3", Params.COLS, Values.COLS_6);


        GridConfigurator.configure(CommunicationStructure.class, "communicationstruct")
                .repository(CommunicationStructureRepository.class)
                .column("briefDescription", "Titolo", true)
                .column("date", "Data Comunicazione", false)
                .column("nomeattached1","1° Allegato",false)
                .column("nomeattached2", "2° Allegato", false)
                .column("nomeattached3", "3° Allegato", false);
    }
}
