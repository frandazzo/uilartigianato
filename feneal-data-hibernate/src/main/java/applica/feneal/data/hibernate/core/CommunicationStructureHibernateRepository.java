package applica.feneal.data.hibernate.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.CommunicationStructureRepository;
import applica.feneal.domain.model.core.CommunicationStructure;
import applica.framework.data.hibernate.HibernateRepository;

import org.springframework.stereotype.Repository;

@Repository
public class CommunicationStructureHibernateRepository extends HibernateRepository<CommunicationStructure> implements CommunicationStructureRepository {

    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public Class<CommunicationStructure> getEntityType() {
        return CommunicationStructure.class;
    }
}
