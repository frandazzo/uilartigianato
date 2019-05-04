package applica.feneal.services.impl.importData;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.core.rappresentanza.DelegaBilateralitaRepository;
import applica.feneal.domain.data.core.rappresentanza.DelegaUncRepository;
import applica.feneal.domain.model.core.ImportData;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.importData.ImportBilateralita_UncValidator;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.rappresentanza.DelegaBilateralita;
import applica.feneal.domain.model.core.rappresentanza.DelegaUnc;
import applica.feneal.domain.model.geo.City;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.domain.utils.Box;
import applica.feneal.services.GeoService;
import applica.framework.LoadRequest;
import applica.framework.library.options.OptionsManager;
import applica.framework.management.csv.RowData;
import applica.framework.management.excel.ExcelInfo;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Component
public class ImportDataBilateralita_Unc {
        @Autowired
        private ImportCausaliService importCausaliService;

        @Autowired
        private GeoService geo;

        @Autowired
        private ImportDataUtils importDataUtils;

        @Autowired
        private AziendeRepository azRep;


        @Autowired
        private OptionsManager optMan;

        @Autowired
        private ImportQuoteLogger logger;

        @Autowired
        private LavoratoriRepository lavSvc;

        @Autowired
        private DelegaBilateralitaRepository delRep;

    @Autowired
    private DelegaUncRepository delUncRep;

    public void doImportUnc(ImportData importData, File temp1, File outputDir) throws Exception {
        baseValidate(importData);

        ExcelInfo data = importDataUtils.importData(importData, temp1, new ImportBilateralita_UncValidator());
        validateExcelData(data, false);

        //se non ci sono errori posso importare i lavoratori e quindi importare
        importAll(data, outputDir, importData, false);
    }




    public void doImportBilateralita(ImportData importData, File temp1, File outputDir) throws Exception {


        baseValidate(importData);

        ExcelInfo data = importDataUtils.importData(importData, temp1, new ImportBilateralita_UncValidator());
        validateExcelData(data, true);

        //se non ci sono errori posso importare i lavoratori e quindi importare
        importAll(data, outputDir, importData, true);
    }


        private void importAll(ExcelInfo data, File outputdir, ImportData importData, boolean bilateralita) throws Exception {

            String guid = UUID.randomUUID().toString();
            String logFile = optMan.get("applica.fenealquote.logfolder") +"importData_ " + guid + ".txt";

            //il file di log lo copio alla fine dell'elaborazione
            String filename = outputdir + "\\logImportazione.txt";

            try {


                logger.log(filename, "StartImport", "Avvio import " + data.getOnlyValidRows().size() , false);
                logger.log(filename, "StartImport", "******************************", false);
                logger.log(filename, "StartImport", "******************************", false);

                logger.log(filename,  "Prerequisiti", "Inizio importazione dati", false);

                ImportCache cache = new ImportCache();
                //Importo il lavoratore se non esiste e ne creo la relativa delega
                //numro di riga
                importCacheLavoratori(data, importData, filename,cache);
                if (!bilateralita)
                    importCacheAziende(data, importData, filename,cache);

                logger.log(filename,  "Prerequisiti", "Termine importazione dati ", false);



                logger.log(filename,  "Dati", "Creazione record bilateralità ", false);
                if (bilateralita)
                    createRecordBilateralita(data, importData, filename, cache);
                else
                    createRecordUnc(data, importData, filename, cache);


                FileUtils.copyFile(new File(filename),new File(logFile));

            }catch (Exception ex){

                logger.log(filename, "Errore irreversibile","Errore irreveribile nella importazione dei dati bilateralita: " + ex.getMessage(), false);
                try{
                    FileUtils.copyFile(new File(logFile),new File(filename));
                }catch (Exception e1){
                    //
                }

                throw ex;
            }



        }

    private void createRecordUnc(ExcelInfo data, ImportData importData, String filename, ImportCache cache) throws Exception {
        int i = 1;
        for (RowData rowData : data.getOnlyValidRows()) {
            try {

                createDelegaUnc(importData, rowData, cache);

            }catch (Exception ex){
                logger.log(filename, "Errore", "ERRORE nella aggiunta delega all riga " + String.valueOf(i) + "; " + ex.getMessage() , false);
            }
            i++;
        }
    }

    private void createDelegaUnc(ImportData importData, RowData rowData, ImportCache cache) throws Exception {

        Categoria cat = importCausaliService.getSettore(rowData.getData().get("Categoria"));
        if (cat == null)
            throw new Exception(String.format("La categoria %s non esiste", rowData.getData().get("Categoria")));
        Lavoratore lav = cache.getLavoratoriFiscalCode().get(rowData.getData().get("Codice Fiscale"));
        if (lav == null)
            throw new Exception(String.format("Lavoratore con cf %s non esiste", rowData.getData().get("Codice Fiscale")));



        //poichè puo' esistere un solo record per codice fiscale e regione(companyid)
        //verifico se esite già oppure ne creo una nuova
        DelegaUnc d = createOrGetIfExistUnc(lav, Long.parseLong(importData.getCompany()));

        String dataInizioSt = rowData.getData().get("Data validità");
        String dataFineSt = rowData.getData().get("Data scadenza");
        Date dataInizio = tryParse(dataInizioSt,getDate(GregorianCalendar.getInstance().get(Calendar.YEAR)));
        Date dataFine = tryParse(dataFineSt, null);

        //se ci sono e sono validi inseriso le date del file
        if (dataFine != null && dataInizio != null){
            if (dataFine.getTime() > dataInizio.getTime()){
                d.setDocumentDate(dataInizio);
                d.setCancelDate(dataFine);
            }
        }
        d.setNotes(rowData.getData().get("Note"));
        d.setSector(cat);
        d.setWorker(lav);
        Province pp = geo.getProvinceById(Integer.parseInt(importData.getProvince()));
        d.setProvince(pp);
        d.setCompanyId(Long.parseLong(importData.getCompany()));

        Azienda az = cache.getAziende().get(rowData.getData().get("Azienda"));
        d.setWorkerCompany(az);






        saveEntity(d);
    }


    private void createRecordBilateralita(ExcelInfo data, ImportData importData, String filename, ImportCache cache) throws Exception {
            int i = 1;
            for (RowData rowData : data.getOnlyValidRows()) {
                try {
                    //costruisco i dati del lavoratore
                    //Categoria s = importCausaliService.getSettore(importData.getSettore());
                    createDelegaBilateralita(importData, rowData, cache);

                }catch (Exception ex){
                    logger.log(filename, "Errore", "ERRORE nella aggiunta delega all riga " + String.valueOf(i) + "; " + ex.getMessage() , false);
                }
                i++;
            }
        }

        private void createDelegaBilateralita(ImportData importData, RowData rowData, ImportCache cache) throws Exception {
            Categoria cat = importCausaliService.getSettore(rowData.getData().get("Categoria"));
            if (cat == null)
                throw new Exception(String.format("La categoria %s non esiste", rowData.getData().get("Categoria")));
            Lavoratore lav = cache.getLavoratoriFiscalCode().get(rowData.getData().get("Codice Fiscale"));
            if (lav == null)
                throw new Exception(String.format("Lavoratore con cf %s non esiste", rowData.getData().get("Codice Fiscale")));



            //poichè puo' esistere un solo record per codice fiscale e regione(companyid)
            //verifico se esite già oppure ne creo una nuova
            DelegaBilateralita d = createOrGetIfExistBilateralita(lav, Long.parseLong(importData.getCompany()));

            populateDelega(importData, rowData, cat, lav, d);


            saveEntity(d);
        }

    private void populateDelega(ImportData importData, RowData rowData, Categoria cat, Lavoratore lav, DelegaBilateralita d) {
        String dataInizioSt = rowData.getData().get("Data validità");
        String dataFineSt = rowData.getData().get("Data scadenza");
        Date dataInizio = tryParse(dataInizioSt,getDate(GregorianCalendar.getInstance().get(Calendar.YEAR)));
        Date dataFine = tryParse(dataFineSt, null);

        //se ci sono e sono validi inseriso le date del file
        if (dataFine != null && dataInizio != null){
            if (dataFine.getTime() > dataInizio.getTime()){
                d.setDocumentDate(dataInizio);
                d.setCancelDate(dataFine);
            }
        }
        d.setNotes(rowData.getData().get("Note"));
        d.setSector(cat);
        d.setWorker(lav);
        Province pp = geo.getProvinceById(Integer.parseInt(importData.getProvince()));
        d.setProvince(pp);
        d.setCompanyId(Long.parseLong(importData.getCompany()));
    }

    private DelegaBilateralita createOrGetIfExistBilateralita(Lavoratore lav, long companyId) {
        DelegaBilateralita a = delRep.find(LoadRequest.build().disableOwnershipQuery()
        .filter("companyId",companyId )
        .filter("worker", lav.getLid())).findFirst().orElse(null);
        if (a != null)
            return a;
        return new DelegaBilateralita();
    }

    private DelegaUnc createOrGetIfExistUnc(Lavoratore lav, long companyId) {
        DelegaUnc a = delUncRep.find(LoadRequest.build().disableOwnershipQuery()
                .filter("companyId",companyId )
                .filter("worker", lav.getLid())).findFirst().orElse(null);
        if (a != null)
            return a;
        return new DelegaUnc();
    }

    private <T> void saveEntity(T d) throws Exception {
        Box v = new Box() ;
        delRep.executeCommand(new Command() {
            @Override
            public void execute() {
                Session ses = delRep.getSession();
                Transaction tx = null;
                try{
                    tx = ses.beginTransaction();
                    ses.saveOrUpdate(d);
                    tx.commit();
                }catch (Exception e){
                    tx.rollback();
                    e.printStackTrace();
                    v.setException(e);
                }finally {
                    ses.close();
                }
            }
        });

        if (v.getException() != null)
            throw  new Exception(v.getException());
    }

    private void importCacheAziende(ExcelInfo data, ImportData importData, String filename, ImportCache cache) throws Exception {
        int i = 1;
        for (RowData rowData : data.getOnlyValidRows()) {
            //costruisco i dati del lavoratore
            Azienda l = null;
            try {
                l = constructAzienda(rowData,  importData);

                //metto tutto in cache
                if (l != null){
                    logger.log(filename,"Creazione aziende","Azienda " + rowData.getData().get("Azienda") + " correttamente costruita", false);
                    if (!cache.getAziende().containsKey(l.getDescription())){
                        cache.getAziende().put(l.getDescription(), l);
                        cache.getAziendeById().put(l.getLid(), l);
                    }
                }


            } catch (Exception e) {
                logger.log(filename, "ERRORE",  "Errore nella costruzione dell'azienda riga " + String.valueOf(i) + "; " + e.getMessage(), false );
            }
            i++;

        }
    }

    private void importCacheLavoratori(ExcelInfo data, ImportData importData, String filename, ImportCache cache) throws Exception {
        int i = 1;
        for (RowData rowData : data.getOnlyValidRows()) {
            //costruisco i dati del lavoratore
            Lavoratore l = null;
            try {
                l = constructLavoratore(rowData,  importData);
                logger.log(filename,"Creazione lavoratori - Prerequisiti","Lavoratore " + rowData.getData().get("Codice Fiscale") + " correttamente costruito", false);
                //metto tutto in cache
                if (!cache.getLavoratoriFiscalCode().containsKey(l.getFiscalcode())){
                    cache.getLavoratoriFiscalCode().put(l.getFiscalcode(), l);
                    cache.getLavoratori().put(l.getLid(), l);
                }


            } catch (Exception e) {
                logger.log(filename,"ERRORE", "ERRORE nella costruzione del lavoratore riga " + String.valueOf(i) + "; " + e.getMessage(), false );
            }
            i++;

        }
    }

    private Lavoratore constructLavoratore(RowData rowData, ImportData importData) throws Exception {
        Lavoratore l = lavSvc.find(LoadRequest.build().disableOwnershipQuery()
                .filter("fiscalcode",rowData.getData().get("Codice Fiscale").trim())
                .filter("companyId", Long.parseLong(importData.getCompany()))).findFirst().orElse(null);
        if (l == null){


            //creo il lavoratore
            l = new Lavoratore();
            l.setCompanyId( Long.parseLong(importData.getCompany()));
            l.setSex("M");


            l.setName(rowData.getData().get("Nome").trim());
            l.setSurname(rowData.getData().get("Cognome").trim());

            Date c = getDate(1900);
            l.setBirthDate(c);


            l.setFiscalcode(rowData.getData().get("Codice Fiscale").trim());



            if (rowData.getData().get("Comune") != null){

                City cc1 = geo.getCityByName(rowData.getData().get("Comune"));
                if (cc1!= null){
                    l.setLivingCity(cc1.getDescription());
                    l.setLivingProvince(geo.getProvinceById(cc1.getIdProvince()).getDescription());
                }
            }


            if (rowData.getData().get("Indirizzo") != null)
                l.setAddress(rowData.getData().get("Indirizzo").trim());

            if (rowData.getData().get("Cap") != null)
                l.setCap(rowData.getData().get("Cap").trim());

            if (rowData.getData().get("Telefono") != null)
                l.setCellphone(rowData.getData().get("Telefono").trim().replaceAll("[^\\d.]", ""));


            if (rowData.getData().get("Email") != null)
                l.setMail(rowData.getData().get("Email").trim());

            l.setNamesurname(String.format("%s %s", l.getName(), l.getSurname()));
            l.setSurnamename(String.format("%s %s", l.getSurname(),l.getName()));


            saveEntity(l);

        }
        else{


            //devo verificare se aggiornare i dati anagrafici  oppure i dati di telefono
            if (importData.getUpdateTelefoni() != null)
                if (importData.getUpdateTelefoni() == 1){
                    //aggiorno i num di telefono
                    if (rowData.getData().get("Telefono") != null)
                        if (!StringUtils.isEmpty(rowData.getData().get("Telefono").toString().trim()))
                            l.setCellphone(rowData.getData().get("Telefono").toString().trim().replaceAll("[^\\d.]", ""));



                    if (rowData.getData().get("Email") != null)
                        if (!StringUtils.isEmpty(rowData.getData().get("Email").trim()))
                            l.setMail(rowData.getData().get("Email").trim());

                }


            //devo verificare se aggiornare i dati anagrafici  oppure i dati di residenza
            if (importData.getUpdateResidenza() != null)
                if (importData.getUpdateResidenza() == 1){
                    //aggiorno la residenza se esiste il comune
                    if (rowData.getData().get("Comune") != null)
                        if (!StringUtils.isEmpty(rowData.getData().get("Comune").toString().trim())){

                            City city = geo.getCityByName(rowData.getData().get("Comune").toString().trim());
                            if (city != null){
                                l.setLivingCity(city.getDescription());
                                l.setLivingProvince(geo.getProvinceById(city.getIdProvince()).getDescription());

                                //qui impost indirizzo e cap

                                if (rowData.getData().get("Indirizzo") != null)
                                    l.setAddress(rowData.getData().get("Indirizzo").trim());

                                if (rowData.getData().get("Cap") != null)
                                    l.setCap(rowData.getData().get("Cap").trim());

                            }
                        }
                }

            saveEntity(l);


        }
        return l;

    }

    private Date getDate(int year) {
        GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        c.set(Calendar.HOUR, 0);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }

    public Azienda constructAzienda(RowData rowData,  ImportData importData) throws Exception {

        if (StringUtils.isEmpty(String.valueOf(rowData.getData().get("Azienda"))))
            return null;

        Azienda l = azRep.find(LoadRequest.build()
                .disableOwnershipQuery()
                .filter("description",rowData.getData().get("Azienda"))
                .filter("companyId", Long.parseLong(importData.getCompany()))).findFirst().orElse(null);
        if (l == null){
            //creo il lavoratore
            l = new Azienda();
            l.setCompanyId(Long.parseLong(importData.getCompany()));
            l.setDescription(rowData.getData().get("Azienda"));

            saveEntity(l);
        }

        return l;
    }

    private void validateExcelData(ExcelInfo data, boolean bilateralita) throws Exception {
            if (!fr.opensagres.xdocreport.core.utils.StringUtils.isEmpty(data.getError())){
                //recupero il nome del file
                String name = FilenameUtils.getName(data.getSourceFile());

                throw new Exception(String.format("Un file contiene errori: %s  <br>",  data.getError()));
            }

            //prendo l'intestazione e verifico che sia la stessa che mi aspetto
            List<String> template  = new ArrayList<>();
            template.add("Cognome");
            template.add("Nome");
            template.add("Codice Fiscale");
            template.add("Indirizzo");
            template.add("Cap");
            template.add("Comune");
            template.add("CAP");
            template.add("Telefono");
            template.add("Email");
            template.add("Categoria");
            if (!bilateralita){
                template.add("Azienda");
            }
            template.add("Territorio");
            template.add("Data validità");
            template.add("Data scadenza");
            template.add("Note");


            List<String> headers = data.getHeaderFields();
            String name = FilenameUtils.getName(data.getSourceFile());
            String err = importDataUtils.headersContainsTemplate(headers, template);
            boolean equal = StringUtils.isEmpty(err);
            if (!equal){
                //recupero il nome del file
                throw new Exception(String.format("Il file %s non contiene le intestazioni richieste<br>: Campi mancanti: %s", name, err));
            }
            if (equal){
                //verifico che ci sia almeno una riga
                if (data.getOnlyValidRows().size() == 0){
                    throw new Exception(String.format("Il file %s non contiene informazioni<br>", name));
                }

            }
        }

    private void baseValidate(ImportData importData) throws Exception {

            if (importData == null){
                throw new Exception("Nessun file indicato per l'importazione; Null");
            }

            //verifico che ci sia almeno un file
            if (fr.opensagres.xdocreport.core.utils.StringUtils.isEmpty(importData.getFile1())){
                throw new Exception("Nessun file indicato per l'importazione");
            }
        }

    private Date tryParse(String s, Date defaultDate){
            SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");


            try {
                return ff.parse(s);
            } catch (ParseException e) {
                return  defaultDate;
            }


        }

}
