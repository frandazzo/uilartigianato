package applica.feneal.domain.data.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.core.CommunicationStructure;
import applica.framework.Repository;
import org.hibernate.Session;

public interface CommunicationStructureRepository extends Repository<CommunicationStructure> {
    void executeCommand(Command command);

    Session getSession();
}
