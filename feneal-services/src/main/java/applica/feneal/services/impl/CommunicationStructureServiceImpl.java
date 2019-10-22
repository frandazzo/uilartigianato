package applica.feneal.services.impl;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.CommunicationStructureRepository;
import applica.feneal.domain.model.core.CommunicationStructure;
import applica.feneal.domain.utils.Box;
import applica.feneal.services.CommunicationStructureService;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;




import java.util.List;

@Service
public class CommunicationStructureServiceImpl implements CommunicationStructureService {

   @Autowired
   private CommunicationStructureRepository commStrRep;



    @Override
    public List<CommunicationStructure> retriveLastCommunicationStructure() {

        Box b = new Box();
        commStrRep.executeCommand(new Command() {
            @Override
            public void execute() {
                Transaction tx = null;
                Session s = commStrRep.getSession();
                try{

                    tx =s.beginTransaction();

                    String queryString = String.format("SELECT * FROM uilartigianato.fenealweb_communication_struct\n" +
                            "order by date DESC LIMIT 5;");

                    SQLQuery query = s.createSQLQuery(queryString).addEntity(CommunicationStructure.class);

                    List<CommunicationStructure>  results = query.list();

                    b.setValue(results);

                    tx.commit();
                }catch (Exception e){
                    e.printStackTrace();
                    tx.rollback();
                }finally {
                    s.close();
                }
            }
        });



        return (List<CommunicationStructure>)b.getValue();


    }
}
