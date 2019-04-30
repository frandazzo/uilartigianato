package applica.feneal.services;

import applica.feneal.domain.model.core.aziende.Azienda;

/**
 * Created by fgran on 07/04/2016.
 */
public interface AziendaService {
    Azienda getAziendaById(long loggedUserId, long firmId);
    Azienda getAziendaByDescription(String description, long companyId);
    void saveOrUpdate(long loggedUserId, Azienda az) throws Exception;

    void saveOrUpdateWithImpersonation(long loggedUserId, Azienda az) throws Exception;

    void delete(long loggedUserId, long idLav);





}
