package applica.feneal.admin.fields.renderers;

import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.model.core.Company;
import applica.framework.LoadRequest;
import applica.framework.library.SimpleItem;
import applica.framework.security.Security;
import applica.framework.widgets.fields.renderers.OptionalSelectFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CompanyForRiepilogoQuoteNazionaleFieldRenderer extends OptionalSelectFieldRenderer {

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private Security sec;

    @Override
    public List<SimpleItem> getItems() {

        List<Company> companies = companyRepository.find(LoadRequest.build().filter("tipoConfederazione",1)).getRows();

        return SimpleItem.createList(companies, (c) -> c.getDescription(), (c) -> String.valueOf(c.getId()));


    }
}
