package applica.feneal.data.hibernate.core.quote;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.quote.BonificoRepository;
import applica.feneal.domain.model.core.quote.Bonifico;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BonificoHibernateRepository extends HibernateRepository<Bonifico> implements BonificoRepository {


    @Override
    public void executeCommand(Command command) {
        command.execute();
    }



    @Override
    public Class<Bonifico> getEntityType() {
        return Bonifico.class;
    }

}

