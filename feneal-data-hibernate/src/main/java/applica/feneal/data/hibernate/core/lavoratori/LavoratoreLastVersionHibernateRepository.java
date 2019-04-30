package applica.feneal.data.hibernate.core.lavoratori;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.lavoratori.LavoratoreLastVersionRepository;
import applica.feneal.domain.model.core.lavoratori.LavoratoreLastVersion;
import applica.framework.Sort;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;
@Repository
public class LavoratoreLastVersionHibernateRepository extends HibernateRepository<LavoratoreLastVersion> implements LavoratoreLastVersionRepository {



    @Override
    public void executeCommand(Command command) {
        command.execute();
    }





    @Override
    public Class<LavoratoreLastVersion> getEntityType() {
        return LavoratoreLastVersion.class;
    }

    @Override
    public List<Sort> getDefaultSorts() {
        return Arrays.asList(new Sort("surname", false));
    }



}
