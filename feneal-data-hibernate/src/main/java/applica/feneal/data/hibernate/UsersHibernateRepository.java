package applica.feneal.data.hibernate;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.UsersRepository;
import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.framework.LoadRequest;
import applica.framework.Sort;
import applica.framework.data.hibernate.HibernateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

/**
 * Applica (www.applica.guru)
 * User: bimbobruno
 * Date: 28/10/13
 * Time: 17:22
 */
@Repository
public class UsersHibernateRepository extends HibernateRepository<User> implements UsersRepository {
    @Autowired
    private CompanyRepository rep;

    @Override
    public Class<User> getEntityType() {
        return User.class;
    }

    @Override
    public List<Sort> getDefaultSorts() {
        return Arrays.asList(new Sort("username", false));
    }


    @Override
    public User getUserByUsername(String username) {
        LoadRequest req = LoadRequest.build().filter("username", username);
        return find(req).findFirst().orElse(null);

    }

    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public User getRegionalUserByProvinceName(String province) {

        Company c = rep.findCompanyByProvinceName(province, 1);
        return getRegionalUserForCompany(c.getLid());


    }

    @Override
    public User getRegionalUserForCompany(long companyId) {

        return find(LoadRequest.build()).getRows().stream().filter(a ->{
            if (a.getCompany() != null)
                if (a.getCompany().getLid() == companyId)
                {
                    Role r = ((Role) a.getRoles().get(0));
                    if (r.getLid() == 3)
                        return true;
                }

                return false;

        }).findFirst().orElse(null);
    }
}
