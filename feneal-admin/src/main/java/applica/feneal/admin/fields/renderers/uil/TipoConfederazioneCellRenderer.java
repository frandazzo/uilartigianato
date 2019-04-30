package applica.feneal.admin.fields.renderers.uil;

import applica.framework.widgets.GridColumn;
import applica.framework.widgets.cells.renderers.BaseCellRenderer;
import org.springframework.stereotype.Component;

import java.io.Writer;

/**
 * Created by fgran on 04/04/2017.
 */
@Component
public class TipoConfederazioneCellRenderer extends BaseCellRenderer {


    @Override
    public void render(Writer writer, GridColumn column, Object value) {



        String vale = "REGIONALE";

        if (Integer.parseInt(value.toString()) == 2)
            vale = "NAZIONALE";

        if (Integer.parseInt(value.toString()) == 3)
            vale = "UNC";

        super.render(writer, column, vale);
    }
}