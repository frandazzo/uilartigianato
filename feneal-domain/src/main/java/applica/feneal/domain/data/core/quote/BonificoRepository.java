package applica.feneal.domain.data.core.quote;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.core.quote.Bonifico;
import applica.framework.Repository;
import org.hibernate.Session;

public interface BonificoRepository extends Repository<Bonifico> {

        void executeCommand(Command command);

        Session getSession();



        }