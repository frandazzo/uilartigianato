package applica.feneal.admin.data;

import applica.feneal.domain.data.core.quote.QuoteAssocRepository;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.framework.LoadRequest;
import applica.framework.LoadResponse;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Created by fgran on 15/05/2017.
 */
@Repository
public class QuoteRepositoryWrapper implements applica.framework.Repository<RiepilogoQuoteAssociative> {

    @Autowired
    private QuoteAssocRepository docRep;

    @Autowired
    private Security secure;


    @Override
    public Optional<RiepilogoQuoteAssociative> get(Object o) {
        return docRep.get(o);
    }

    @Override
    public LoadResponse<RiepilogoQuoteAssociative> find(LoadRequest loadRequest) {



        return docRep.find(loadRequest);
    }

    @Override
    public void save(RiepilogoQuoteAssociative riepilogoQuoteAssociative) {

    }

    @Override
    public void delete(Object o) {

    }


    @Override
    public Class<RiepilogoQuoteAssociative> getEntityType() {
        return RiepilogoQuoteAssociative.class;
    }
}
