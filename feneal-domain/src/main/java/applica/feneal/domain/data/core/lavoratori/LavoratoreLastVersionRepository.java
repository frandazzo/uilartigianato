package applica.feneal.domain.data.core.lavoratori;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.model.core.lavoratori.LavoratoreLastVersion;
import applica.framework.Repository;
import org.hibernate.Session;

public interface LavoratoreLastVersionRepository extends Repository<LavoratoreLastVersion> {

    void executeCommand(Command command);

    Session getSession();



}