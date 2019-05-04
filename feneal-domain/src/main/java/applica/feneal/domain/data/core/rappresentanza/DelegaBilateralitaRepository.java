package applica.feneal.domain.data.core.rappresentanza;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.core.rappresentanza.DelegaBilateralita;
import applica.framework.Repository;
import org.hibernate.Session;

public interface DelegaBilateralitaRepository extends Repository<DelegaBilateralita> {

    void executeCommand(Command command);

    Session getSession();

}
