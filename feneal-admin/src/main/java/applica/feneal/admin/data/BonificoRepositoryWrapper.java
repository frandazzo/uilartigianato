package applica.feneal.admin.data;

import applica.feneal.domain.data.core.quote.BonificoRepository;
import applica.feneal.domain.model.core.quote.Bonifico;
import applica.feneal.services.QuoteAssociativeService;
import applica.framework.LoadRequest;
import applica.framework.LoadResponse;
import applica.framework.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@org.springframework.stereotype.Repository
public class BonificoRepositoryWrapper implements Repository<Bonifico> {

    @Autowired
    private BonificoRepository docRep;

   @Autowired
   private QuoteAssociativeService quoRep;


    @Override
    public Optional<Bonifico> get(Object o) {
        return docRep.get(o);
    }

    @Override
    public LoadResponse<Bonifico> find(LoadRequest loadRequest) {
        return docRep.find(loadRequest);
    }

    @Override
    public void save(Bonifico documento) {

    }

    @Override
    public void delete(Object o) {

        Long l = Long.parseLong(o.toString());
        try {
            quoRep.deleteBonifico(l);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public Class<Bonifico> getEntityType() {
        return Bonifico.class;
    }
}
