package applica.feneal.data.hibernate.core.rappresentanza;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.rappresentanza.DelegaBilateralitaRepository;
import applica.feneal.domain.model.core.rappresentanza.DelegaBilateralita;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DelegaBilateralitaHibernateRepository extends HibernateRepository<DelegaBilateralita> implements DelegaBilateralitaRepository {


    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public Class<DelegaBilateralita> getEntityType() {
        return DelegaBilateralita.class;
    }

}
