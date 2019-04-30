package applica.feneal.domain.data.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.setting.Attribuzione1;
import applica.framework.Repository;
import org.hibernate.Session;

/**
 * Created by fgran on 06/05/2017.
 */
public interface Attribuzione1Repository extends Repository<Attribuzione1> {

    void executeCommand(Command command);

    Session getSession();

}
