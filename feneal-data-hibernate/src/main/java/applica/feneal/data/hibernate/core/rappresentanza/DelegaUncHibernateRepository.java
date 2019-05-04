package applica.feneal.data.hibernate.core.rappresentanza;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.rappresentanza.DelegaUncRepository;
import applica.feneal.domain.model.core.rappresentanza.DelegaUnc;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DelegaUncHibernateRepository extends HibernateRepository<DelegaUnc> implements DelegaUncRepository {


    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public Class<DelegaUnc> getEntityType() {
        return DelegaUnc.class;
    }

}
