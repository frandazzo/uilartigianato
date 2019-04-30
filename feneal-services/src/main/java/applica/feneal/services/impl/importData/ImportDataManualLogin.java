package applica.feneal.services.impl.importData;

import applica.feneal.domain.data.UsersRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.quote.fenealgestImport.RiepilogoQuotaDTO;
import applica.framework.LoadRequest;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by fgran on 11/05/2017.
 */
@Component
public class ImportDataManualLogin {

    @Autowired
    private ImportQuoteLogger logger;

    @Autowired
    private UsersRepository compRep;


    public void executeManualLogin(int companyId, RiepilogoQuotaDTO dto, String filename) throws Exception {
        User user = retrieveUserForLogin(companyId, dto, filename);

        if (user == null){
            logger.log(filename, "StartImport", "Nessuna credenziale trovata", false);
            throw new Exception("Nessun utente trovato!!!");
        }

        logger.log(filename, "StartImport", "Avvio il login", false);
        Security.manualLogin(user.getUsername(), user.getDecPass());
    }

    private User retrieveUserForLogin(int companyId, RiepilogoQuotaDTO dto, String filename) throws Exception {
        User user = null;
        try {
            logger.log(filename, "StartImport", "Ricerco le credenziali di segratario per la company: " + companyId, false);

            user = getUserForLogin(companyId, dto, user);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return user;
    }

    private User getUserForLogin(int companyId, RiepilogoQuotaDTO dto, User user) {
        LoadRequest req = LoadRequest.build().filter("company", companyId);
        List<User> users = compRep.find(req).getRows();
        for (User user1 : users) {

                if (user1.retrieveUserRole().getRole().equals("REGIONALE"))
                    {
                        user = user1;
                        break;
                    }
        }
        return user;
    }

}
