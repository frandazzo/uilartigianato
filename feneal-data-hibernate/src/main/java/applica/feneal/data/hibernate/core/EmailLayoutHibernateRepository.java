package applica.feneal.data.hibernate.core;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.EmailLayoutRepository;
import applica.feneal.domain.model.setting.EmailLayout;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

/**
 * Applica
 * User: Angelo D'Alconzo
 * Date: 09/06/17
 * Time: 10:00 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class EmailLayoutHibernateRepository extends HibernateRepository<EmailLayout> implements EmailLayoutRepository {


    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public Class<EmailLayout> getEntityType() {
        return EmailLayout.class;
    }

}
