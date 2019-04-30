package applica.feneal.domain.data.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.setting.EmailLayout;
import applica.framework.Repository;
import org.hibernate.Session;

/**
 * Applica
 * User: Angelo D'Alconzo
 * Date: 09/06/17
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
public interface EmailLayoutRepository extends Repository<EmailLayout> {

    void executeCommand(Command command);

    Session getSession();

}
