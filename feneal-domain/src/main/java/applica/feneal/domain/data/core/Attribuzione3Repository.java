package applica.feneal.domain.data.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.setting.Attribuzione3;
import applica.framework.Repository;
import org.hibernate.Session;

/**
 * Created by fgran on 22/09/2017.
 */
public interface Attribuzione3Repository extends Repository<Attribuzione3> {

    void executeCommand(Command command);

    Session getSession();

}
