package applica.feneal.services;

import applica.feneal.domain.model.core.lavoratori.IscrittoAnnoInCorso;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.search.LavoratoreSearchParams;

import java.util.List;

/**
 * Created by fgran on 06/04/2016.
 */
public interface LavoratoreService {

    Lavoratore getLavoratoreById(long loggedUserId, long lavId);

    void saveOrUpdate(long loggedUserId, Lavoratore lav) throws Exception;

    void saveOrUpdateWithImpersonation(long loggedUserId, Lavoratore lav) throws Exception;

    void delete(long loggedUserId, long idLav) throws Exception;

    List<Lavoratore> findLocalLavoratori(long loggedUserId, LavoratoreSearchParams params);


    Lavoratore findLavoratoreByFiscalCode(String fiscalcode, long companyId);


    IscrittoAnnoInCorso checkIfIscrittoAnnoInCorso(long workerId) throws Exception;
}
