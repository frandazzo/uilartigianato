package applica.feneal.domain.data.core.configuration;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.framework.Repository;
import org.hibernate.Session;

/**
 * Created by fgran on 17/03/2017.
 */
public interface CategoriaRepository extends Repository<Categoria> {

    void executeCommand(Command command);

    Session getSession();
}
