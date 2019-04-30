package applica.feneal.data.hibernate.core.configuration;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.framework.Sort;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * Created by fgran on 17/03/2017.
 */
@Repository
public class CategoriaHibernateRepository extends HibernateRepository<Categoria> implements CategoriaRepository {


    @Override
    public void executeCommand(Command command) {
        command.execute();
    }



    @Override
    public Class<Categoria> getEntityType() {
        return Categoria.class;
    }

    @Override
    public List<Sort> getDefaultSorts() {
        return Arrays.asList(new Sort("description", false));
    }


}
