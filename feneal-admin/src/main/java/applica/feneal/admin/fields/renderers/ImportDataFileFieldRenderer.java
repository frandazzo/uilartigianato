package applica.feneal.admin.fields.renderers;

import applica.feneal.domain.model.User;
import applica.framework.security.Security;
import applica.framework.widgets.fields.renderers.FileFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by fgran on 15/05/2017.
 */
@Component
public class ImportDataFileFieldRenderer extends FileFieldRenderer {

    @Autowired
    private Security sec;

    @Override
    public String getPath() {
        User u = ((User) sec.getLoggedUser());
        Long c = u.getLid();

        String folderName = "import_" + c;


        return folderName + "/";
    }
}
