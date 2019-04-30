package applica.feneal.domain.data.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.setting.Fondo;
import applica.framework.Repository;
import org.hibernate.Session;

/**
 * Applica
 * User: Ciccio Randazzo
 * Date: 10/12/15
 * Time: 9:57 AM
 * To change this template use File | Settings | File Templates.
 */
public interface FundRepository extends Repository<Fondo> {

    void executeCommand(Command command);

    Session getSession();

}
