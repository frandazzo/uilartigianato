package applica.feneal.domain.data.core.rappresentanza;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.core.rappresentanza.DelegaUnc;
import applica.framework.Repository;
import org.hibernate.Session;

public interface DelegaUncRepository extends Repository<DelegaUnc> {

    void executeCommand(Command command);

    Session getSession();

}

