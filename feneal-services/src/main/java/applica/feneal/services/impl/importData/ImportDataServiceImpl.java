package applica.feneal.services.impl.importData;

import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.ImportData;
import applica.feneal.services.ImportDataService;
import applica.framework.fileserver.FileServer;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;

/**
 * Created by fgran on 19/04/2017.
 */
@Service
public class ImportDataServiceImpl implements ImportDataService {




    @Autowired
    private FileServer server;


    @Autowired
    private Security sec;

    @Autowired
    private ImportDataLavoratori importDataLavoratori;

    @Autowired
    private ImportDataDeleghe importDeleghe;

    @Autowired
    private ImportDataQuote importQuote;

    @Autowired
    private ImportDataUtils importDataUtils;

    @Autowired
    private ImportDataDocumenti importDataDocumenti;


    @Override
    public String importaDeleghe(ImportData importData) throws Exception {


        String user = ((User) sec.getLoggedUser()).getUsername();

        //creo una cartella temporanea dove inserirò tutti i dati per fare l'analisi
        File temp1 = File.createTempFile("import_deleghe_" + user,"");
        temp1.delete();
        temp1.mkdir();
        //creo una cartella corrante temporanea che alla fine sarà zippata
        //e restituita
        File temp = File.createTempFile("LogImportazioneDeleghe_" + user,"");
        temp.delete();
        temp.mkdir();

        importDeleghe.doImportDeleghe(importData, temp1, temp);

        String zippedFile = importDataUtils.compressData(temp);


        //questa funxione restituiraà l'url della cartella zippata
        //pertanto dovro' inviare tramite fileserver il file zippato
        //al file server appunto
        String pathToFile =  server.saveFile("files/importazionedeleghe/", "zip", new FileInputStream(new File(zippedFile)));
        return pathToFile;
    }


    @Override
    public String importaAnagrafiche(ImportData importData) throws Exception {


        String user = ((User) sec.getLoggedUser()).getUsername();

        //creo una cartella temporanea dove inserirò tutti i dati per fare l'analisi
        File temp1 = File.createTempFile("import_anagrafiche_" + user,"");
        temp1.delete();
        temp1.mkdir();
        //creo una cartella corrante temporanea che alla fine sarà zippata
        //e restituita
        File temp = File.createTempFile("LogImportazioneAnagrafiche_" + user,"");
        temp.delete();
        temp.mkdir();

        importDataLavoratori.doImportAnagrafiche(importData, temp1, temp);

        String zippedFile = importDataUtils.compressData(temp);


        //questa funxione restituiraà l'url della cartella zippata
        //pertanto dovro' inviare tramite fileserver il file zippato
        //al file server appunto
        String pathToFile =  server.saveFile("files/importazioneAnagrafiche/", "zip", new FileInputStream(new File(zippedFile)));
        return pathToFile;
    }


    @Override
    public String importaQuote(ImportData importData) throws Exception {
        String user = ((User) sec.getLoggedUser()).getUsername();

        //creo una cartella temporanea dove inserirò tutti i dati per fare l'analisi
        File temp1 = File.createTempFile("import_quote_" + user,"");
        temp1.delete();
        temp1.mkdir();
        //creo una cartella corrante temporanea che alla fine sarà zippata
        //e restituita
        File temp = File.createTempFile("LogImportazioneQuote_" + user,"");
        temp.delete();
        temp.mkdir();

        importQuote.doImportQuote(importData, temp1, temp);

        String zippedFile = importDataUtils.compressData(temp);


        //questa funxione restituiraà l'url della cartella zippata
        //pertanto dovro' inviare tramite fileserver il file zippato
        //al file server appunto
        String pathToFile =  server.saveFile("files/importazioneQuote/", "zip", new FileInputStream(new File(zippedFile)));
        return pathToFile;
    }



    @Override
    public String importaDocumenti(ImportData importData) throws Exception {
        String user = ((User) sec.getLoggedUser()).getUsername();

        //creo una cartella temporanea dove inserirò tutti i dati per fare l'analisi
        File temp1 = File.createTempFile("import_documenti_" + user,"");
        temp1.delete();
        temp1.mkdir();
        //creo una cartella corrante temporanea che alla fine sarà zippata
        //e restituita
        File temp = File.createTempFile("LogImportazioneDocumenti_" + user,"");
        temp.delete();
        temp.mkdir();

        importDataDocumenti.doImportDocumenti(importData, temp1, temp);

        String zippedFile = importDataUtils.compressData(temp);


        //questa funxione restituiraà l'url della cartella zippata
        //pertanto dovro' inviare tramite fileserver il file zippato
        //al file server appunto
        String pathToFile =  server.saveFile("files/importazionedocumenti/", "zip", new FileInputStream(new File(zippedFile)));
        return pathToFile;
    }


}
