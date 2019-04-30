package applica.feneal.admin.fields.renderers;

import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.framework.library.SimpleItem;
import applica.framework.security.Security;
import applica.framework.widgets.fields.renderers.SelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Applica
 * User: Ciccio Randazzo
 * Date: 10/12/15
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SectorTypeSelectRenderer extends SelectFieldRenderer {
    @Autowired
    private Security sec;


    @Autowired
    private CategoriaRepository settRep;

    @Override
    public List<SimpleItem> getItems() {


        User u = ((User) sec.getLoggedUser());
        Role r = u.retrieveUserRole();
        List<Categoria> sectors = settRep.find(null).getRows();


        //se l'utent eè un amministratore generale mostro tutto
        if (r.getLid() == 1){
            return SimpleItem.createList(sectors, (s) -> s.getDescription(), (s) -> String.valueOf(s.getId()));
        }else if (r.getLid() == 2 || r.getLid() == 6){
            sectors = new ArrayList<>();
            //se sono amministratore di una company allora devo verificare se la company
            //di cui sono un amministratoree è una unc
            if (u.getCompany().getTipoConfederazione() == 3){
                sectors.add(u.getCategory());
            }
            return SimpleItem.createList(sectors, (s) -> s.getDescription(), (s) -> String.valueOf(s.getId()));
        }else{

            return SimpleItem.createList(sectors, (s) -> s.getDescription(), (s) -> String.valueOf(s.getId()));
        }
    }
}