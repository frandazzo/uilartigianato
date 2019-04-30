package applica.feneal.services;

import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.servizi.search.UiQuoteHeaderParams;
import applica.feneal.domain.model.setting.CausaleRevoca;
import applica.feneal.domain.model.setting.option.ApplicationOptions;

import java.util.Date;
import java.util.List;

/**
 * Created by fgran on 05/04/2016.
 */
public interface DelegheService {




    void accettaDeleghe(List<Delega> deleghe, Date date) throws Exception;
    void inoltraDeleghe(List<Delega> deleghe, Date date) throws Exception;

    void subscribeDelega(Delega del) throws Exception;

   // void subscribeDelegaWithOmpersonation(Delega del, User owner) throws Exception;

    void sendDelega(Date date, Delega del) throws Exception;

    void acceptDelega(Date date, Delega del) throws Exception;

    void cancelDelega(Date date, Delega del, CausaleRevoca reason) throws Exception;

    void revokeDelega(Date date, Delega del, CausaleRevoca reason) throws Exception;

    void activateDelega(Date date, Delega del) throws Exception;

    void goBack(Delega del) throws Exception;

    List<Delega> getAllWorkerDeleghe(long workerId);




    void deleteDelega(long userId, long delegaId) throws Exception;

    //void insertWithImpersonation(long userId, Delega l) throws Exception;

    void saveOrUpdate(long userId, Delega l) throws Exception;

    Delega getDelegaById(Long id);

    List<Integer> getDelegaPermittedNextStates(Delega delega, List<ApplicationOptions> opt);

    List<Delega> getDelegheForAzienda(long id);

    List<Delega> getDelegeForAzienda(UiQuoteHeaderParams params);

    boolean hasWorkerDelegaAttivaOAccettata(long workerId,  String azienda, String provincia);

    Delega retrieveActivableWorkerDelega(long idLavoratore, int provinceId, long idAzienda);

    Delega retrieveDelega(long idLavoratore, int provinceId, Date data,  long idAzienda);

    void saveImportedDelega(Delega d);
}
