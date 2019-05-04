package applica.feneal.services;

import applica.feneal.domain.model.core.ImportData;

/**
 * Created by fgran on 19/04/2017.
 */
public interface ImportDataService {

    //l'importazione delle deleghe va fatta da un utetne regionale o un operatore
    String importaDeleghe(ImportData importData) throws Exception;
    //l'importazione delle anagrafiche va fatta da un utetne regionale o un operatore
    String importaAnagrafiche(ImportData importData) throws Exception;
    //l'importazione delle quote va fatta da un utetne nazionale
    String importaQuote(ImportData file) throws Exception;



    String importaBiolateralita(ImportData file) throws Exception;

    String importaUnc(ImportData file) throws Exception;

    String importaDocumenti(ImportData file) throws Exception;
}
