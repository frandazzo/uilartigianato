package applica.feneal.data.hibernate.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.Attribuzione1Repository;
import applica.feneal.domain.model.setting.Attribuzione1;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by fgran on 06/05/2017.
 */
@Repository
public class Attribuzione1HibernateRepository extends HibernateRepository<Attribuzione1> implements Attribuzione1Repository {


    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public Class<Attribuzione1> getEntityType() {
        return Attribuzione1.class;
    }

}
