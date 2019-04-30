package applica.feneal.domain.data.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.setting.Attribuzione2;
import applica.framework.Repository;
import org.hibernate.Session;

/**
 * Created by fgran on 06/05/2017.
 */
public interface Attribuzione2Repository extends Repository<Attribuzione2> {

    void executeCommand(Command command);

    Session getSession();

}
