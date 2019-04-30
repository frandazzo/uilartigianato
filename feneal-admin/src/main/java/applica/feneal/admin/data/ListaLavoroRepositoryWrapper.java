package applica.feneal.admin.data;

import applica.feneal.domain.data.core.lavoratori.ListaLavoroRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.LoadResponse;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by fgran on 18/05/2017.
 */
@Repository
public class ListaLavoroRepositoryWrapper implements applica.framework.Repository<ListaLavoro> {

    @Autowired
    private ListaLavoroRepository docRep;

    @Autowired
    private Security secure;


    @Override
    public Optional<ListaLavoro> get(Object o) {
        return docRep.get(o);
    }

    @Override
    public LoadResponse<ListaLavoro> find(LoadRequest loadRequest) {

        loadRequest.getFilters().add(new Filter("companyId", ((User) secure.getLoggedUser()).getCompany().getLid()));

        return docRep.find(loadRequest);
    }

    @Override
    public void save(ListaLavoro lista) {

    }

    @Override
    public void delete(Object o) {

    }


    @Override
    public Class<ListaLavoro> getEntityType() {
        return ListaLavoro.class;
    }
}

