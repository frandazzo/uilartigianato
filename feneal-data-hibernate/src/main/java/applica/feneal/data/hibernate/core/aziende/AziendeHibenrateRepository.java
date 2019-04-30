package applica.feneal.data.hibernate.core.aziende;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.framework.LoadRequest;
import applica.framework.LoadResponse;
import applica.framework.Sort;
import applica.framework.data.hibernate.HibernateRepository;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fgran on 07/04/2016.
 */
@Repository
public class AziendeHibenrateRepository extends HibernateRepository<Azienda> implements AziendeRepository {

    @Autowired
    private CompanyRepository crep;

    @Override
    public void executeCommand(Command command) {
        command.execute();
    }

    @Override
    public Class<Azienda> getEntityType() {
        return Azienda.class;
    }

    @Override
    public List<Sort> getDefaultSorts() {
        return Arrays.asList(new Sort("description", false));
    }
    @Override
    public Azienda searchAziendaForProvince(String name, String provinceName) {
        Company c = crep.findCompanyByProvinceName(provinceName, 1);

        if (c == null)
            return null;


        return searchAzienda(c.getLid(), name);
    }


    private Azienda searchAzienda(long companyId, String description){
        Session s = this.getSession();
        Transaction tx = s.beginTransaction();
        try {
            String sql = "SELECT * FROM fenealweb_azienda WHERE companyId = :company_id and description = :az";
            List<Azienda> cd = new ArrayList<Azienda>();
            SQLQuery query = s.createSQLQuery(sql);
            query.setParameter("company_id", companyId);
            query.setParameter("az", description);
            query.addEntity(Azienda.class);
            cd = (List<Azienda>) query.list();

            if (cd.size() >0)
                return cd.get(0);
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            //tx.rollback();
            throw ex;
        } finally {
            s.close();
        }
    }




    @Override
    public LoadResponse<Azienda> find(LoadRequest loadRequest) {
        return super.find(loadRequest);
    }
}