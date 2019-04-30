package applica.feneal.services;

import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.domain.model.core.lavoratori.ListeLavoroComparison;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.servizi.Comunicazione;
import applica.feneal.domain.model.core.servizi.Documento;

import java.util.List;

/**
 * Created by fgran on 07/06/2016.
 */
public interface ListaLavoroService {

    ListaLavoro getListaById(long listaId);
    void deleteLista(long listaId);

    ListaLavoro createListaFromQuote(List<DettaglioQuotaAssociativa> quote, String description) throws Exception;
    ListaLavoro createListaFromDeleghe(List<Delega> deleghe, String description) throws Exception;


    ListaLavoro createListaFromArchivioDocumentale(List<Documento> documenti, String description) throws Exception;

    ListaLavoro createListaFromComunicazioni(List<Comunicazione> comunicazioni, String description) throws Exception;


    ListaLavoro mergeWithOtherList(ListaLavoro a, ListaLavoro b, String description) throws Exception;
    ListaLavoro excludeWithOtherList(ListaLavoro a, ListaLavoro b, String description) throws Exception;
    ListaLavoro intersectWithOtherList(ListaLavoro a, ListaLavoro b, String description) throws Exception;

    ListeLavoroComparison compareLists (ListaLavoro a, ListaLavoro b, String description) throws Exception;


    void saveListaLavoro(ListaLavoro lista);

    void addWorker(long listaId, long workerId) throws Exception;

    void deleteWorkerFromLista(long listaId, long lavId);
}
