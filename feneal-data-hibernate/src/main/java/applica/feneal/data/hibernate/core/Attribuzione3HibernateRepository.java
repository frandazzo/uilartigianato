package applica.feneal.data.hibernate.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.Attribuzione3Repository;
import applica.feneal.domain.model.setting.Attribuzione3;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by fgran on 22/09/2017.
 */
@Repository
public class Attribuzione3HibernateRepository  extends HibernateRepository<Attribuzione3> implements Attribuzione3Repository {


    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public Class<Attribuzione3> getEntityType() {
        return Attribuzione3.class;
    }

}
