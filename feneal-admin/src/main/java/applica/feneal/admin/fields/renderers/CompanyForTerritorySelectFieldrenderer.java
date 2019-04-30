package applica.feneal.admin.fields.renderers;

import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.framework.LoadRequest;
import applica.framework.library.SimpleItem;
import applica.framework.security.Security;
import applica.framework.widgets.fields.renderers.SelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by fgran on 12/02/2018.
 */
@Component
public class CompanyForTerritorySelectFieldrenderer extends SelectFieldRenderer {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private Security sec;

    @Override
    public List<SimpleItem> getItems() {

        List<Company> companies = companyRepository.find(LoadRequest.build().filter("tipoConfederazione",1)).getRows();
        User u = ((User) sec.getLoggedUser());

        if (u.retrieveUserRole().getIid() == 3 || u.retrieveUserRole().getIid() == 4){
            //se sono operatore regionale o seg regionale
            //allora il filed renderer restituirà solamente il territorio di riferimento

            return SimpleItem.createList(companies.stream().filter(c -> c.getLid() == u.getCompany().getLid()).collect(Collectors.toList()), (c) -> c.getDescription(), (c) -> String.valueOf(c.getId()));
        }

        return SimpleItem.createList(companies, (c) -> c.getDescription(), (c) -> String.valueOf(c.getId()));


    }
}
