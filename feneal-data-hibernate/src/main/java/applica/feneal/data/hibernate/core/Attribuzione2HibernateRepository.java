package applica.feneal.data.hibernate.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.Attribuzione2Repository;
import applica.feneal.domain.model.setting.Attribuzione2;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by fgran on 06/05/2017.
 */
@Repository
public class Attribuzione2HibernateRepository extends HibernateRepository<Attribuzione2> implements Attribuzione2Repository {


    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public Class<Attribuzione2> getEntityType() {
        return Attribuzione2.class;
    }

}
