package applica.feneal.data.hibernate;

import applica.feneal.domain.data.TraceLoginRepository;
import applica.feneal.domain.model.TraceLogin;
import applica.framework.Sort;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class TraceLoginHibernateRepository extends HibernateRepository<TraceLogin> implements TraceLoginRepository {

    @Override
    public List<Sort> getDefaultSorts() {
        return Arrays.asList(new Sort("company", false));
    }

    @Override
    public Class<TraceLogin> getEntityType() {
        return TraceLogin.class;
    }

}

