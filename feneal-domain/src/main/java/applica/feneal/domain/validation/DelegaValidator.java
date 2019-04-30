package applica.feneal.domain.validation;

import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.data.core.deleghe.DelegheRepository;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.framework.AEntity;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by antoniolovicario on 16/04/16.
 */
@Component
public class DelegaValidator  implements DomainClassValidator  {
    @Autowired
    private Security sec;

    @Autowired
    private DelegheRepository delRep;


    @Autowired
    private CompanyRepository comRep;




    @Override
    public String validate(AEntity elem) throws Exception {

        //le deleghe possono essere create solo da operatori o regionali
        User u = ((User) sec.getLoggedUser());
        Role r = ((Role) u.getRoles().get(0));

        if (r.getLid() != 3 && r.getLid() != 4)
            throw new Exception("Operazione non ammessa. Solo operatori regionali possono creare le deleghe");

        //provvedo solo alla validazione delle nuove deleghe.
        //quelle aggiornabili, infatti possono aggiornare
        // solo dati ininfluenti e non obbligatori
        StringBuilder b = new StringBuilder();
        Delega d = (Delega)elem;


        if (d.getLid() > 0)
        {

            //se sto aggiornando devo assicurarmi che chi aggiorna sia
            //sia lo stesso utente che ha creato la delega
            Delega dbData = delRep.get(d.getLid()).orElse(null);

            //se gli utenti che hanno creato e ora modificano la delega
            //sono diversi devo assicurarmi che l'utente corrente sia un regionale

            if (dbData.getUserId() != (u.getLid())){
               // if (r.getLid() != 3) //regionale
                    b.append("La delega è stata creata da un'altro utente");
            }


            return b.toString();
        }

        //devo verificare che ci sia un settore che equivale ad una categoria
        //un contratto,  e una azienda

        if (d.getSector() == null)
            b.append("Categoria mancante");

        if (d.getContract() == null)
            b.append("Contratto mancante");

        if (d.getProvince() == null)
            b.append("Provincia mancante");

        if (d.getWorkerCompany() == null)
            b.append("Azienda mancante");


        //devo assicurarmi che la provincia faccia parte dell'utnete loggato
        long companyId = u.getCompany().getLid();
        Company cc = comRep.get(companyId).orElse(null);
        if (cc == null){
            return "Company nulla";
        }

        if (d.getProvince() != null)
            if (!cc.containProvince(d.getProvince().getDescription()))
                b.append("Provincia non ammessa");


        return b.toString();


    }
}
