package applica.feneal.services.impl.importData;

import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.ImportData;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.importData.ImportQuoteValidator;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.quote.fenealgestImport.DettaglioQuotaDTO;
import applica.feneal.domain.model.core.quote.fenealgestImport.FirmDTO;
import applica.feneal.domain.model.core.quote.fenealgestImport.RiepilogoQuotaDTO;
import applica.feneal.domain.model.core.quote.fenealgestImport.WorkerDTO;
import applica.feneal.services.GeoService;
import applica.framework.fileserver.FileServer;
import applica.framework.library.options.OptionsManager;
import applica.framework.management.csv.RowData;
import applica.framework.management.excel.ExcelInfo;
import applica.framework.security.Security;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fgran on 09/05/2017.
 */
@Component
public class ImportDataQuote {
    @Autowired
    private ImportCausaliService importCausaliService;

    @Autowired
    private GeoService geo;

    @Autowired
    private ImportDataAziende importDataAziende;


    @Autowired
    private ImportDataLavoratori importDataLavoratori;

    @Autowired
    private Security sec;

    @Autowired
    private ImportDataUtils importDataUtils;



    @Autowired
    private OptionsManager optMan;

    @Autowired
    private ImportQuoteLogger logger;

    @Autowired
    private ImportQuoteHelper importHelper;

    @Autowired
    private DettaglioQuoteAssociativeRepository dettRep;

    @Autowired
    private FileServer server;

    /**
     * Questo metodo è richiamato dalla creazione delle quote per azienda
     * @param dto
     * @throws Exception
     */
    public void doImportQuote(RiepilogoQuotaDTO dto) throws Exception {
        String guid = dto.getGuid();
        String logFile = optMan.get("applica.fenealquote.logfolder") +"importData_ " + guid + ".txt";

        dto.setLogFilename(logFile);

        try {

            logger.log(logFile, "StartImport", "Avvio import num " + dto.getDettagli().size() , false);
            logger.log(logFile, "StartImport", "*****************************", false);
            logger.log(logFile, "StartImport", "*****************************", false);

            logger.log(logFile,  "DTO", "Validazione dettagli dto ", false);
            if (dto.getDettagli().size() == 0)
            {
                logger.log(logFile,  "ERRORE", "Nessuna quota da inserire ", false);

                throw new Exception("Nessuna quota da inserire");
            }

            //adesso posso inserire il dto previa validazione
            importHelper.ValidateDTO(dto);

            //salvo i dati
            importHelper.saveRiepilogoQuote(dto,logFile,logFile,null);

        }catch (Exception ex){
            logger.log(logFile, "Errore irreversibile","Errore irreveribile nella importazione dei dati: " + ex.getMessage(), false);
            throw ex;
        }
    }

    /**
     * Questo metodo è richimaato nel contesto dell'importazione dati da file excel delle
     * quote eseguito dal nazionale
     * @param importData
     * @param temp1
     * @param outputDir
     * @throws Exception
     */
    public void doImportQuote(ImportData importData, File temp1, File outputDir) throws Exception {
        baseValidateQuote(importData);


        ExcelInfo data = importDataUtils.importData(importData, temp1, new ImportQuoteValidator());
        validateExcelDataForQuote(data);


        //se non ci sono errori posso importare i lavoratori e quindi importare
        importAllQuote(data, outputDir, importData);
    }

    /**
     * Metodo privato che importa le quote nel cintesto della importazione dei dati da file excel
     * @param data
     * @param outputdir
     * @param importData
     * @throws Exception
     */
    private void importAllQuote(ExcelInfo data, File outputdir, ImportData importData) throws Exception {

        String guid = UUID.randomUUID().toString();
        String logFile = optMan.get("applica.fenealquote.logfolder") +"importData_ " + guid + ".txt";

        //il file di log lo copio alla fine dell'elaborazione
        String filename = outputdir + "\\logImportazioneQuote.txt";

        try {


            logger.log(filename, "StartImport", "Avvio import num " + data.getOnlyValidRows().size() , false);
            logger.log(filename, "StartImport", "*****************************", false);
            logger.log(filename, "StartImport", "*****************************", false);

            logger.log(filename,  "Prerequisiti", "Inizio importazione dati ", false);

            ImportCache cache = new ImportCache();
            //Importo il lavoratore se non esiste e ne creo la relativa delega
            //numro di riga
            importCacheLavoratori(data, importData, filename,cache);
            importCacheAziende(data, importData, filename,  cache);

            logger.log(filename,  "Prerequisiti", "Termine importazione dati ", false);

            logger.log(filename,  "DTO", "Creazione DTO da importare ", false);
            RiepilogoQuotaDTO dto = createRiepilogoQuotaHeader(importData, logFile, guid);

            logger.log(filename,  "DTO", "Creazione dettagli dto ", false);
            createDtoDetails(data, importData, filename, dto);




            logger.log(filename,  "DTO", "Validazione dettagli dto ", false);
            if (dto.getDettagli().size() == 0)
            {
                logger.log(filename,  "ERRORE", "Nessuna quota da inserire ", false);

                throw new Exception("Nessuna quota da inserire");
            }


            //adesso posso inserire il dto previa validazione
            importHelper.ValidateDTO(dto);


            //salvo il file originale dalle cartella temporanea a quella canonica
            String fileToCopy = dto.getOriginalFilename();
            InputStream streamToCopy = server.getFile(fileToCopy);
            String folderPath = getImportFileServerFolderPath();

            dto.setOriginalFilename(server.saveFile(folderPath, "xlsx", streamToCopy));

            //salvo i dati
            importHelper.saveRiepilogoQuote(dto,filename,logFile,cache);


            FileUtils.copyFile(new File(filename),new File(logFile));

        }catch (Exception ex){

            logger.log(filename, "Errore irreversibile","Errore irreveribile nella importazione dei dati: " + ex.getMessage(), false);
            try{
                FileUtils.copyFile(new File(logFile),new File(filename));
            }catch (Exception e1){
                //
            }

            throw ex;
        }



    }


    private void createDtoDetails(ExcelInfo data, ImportData importData, String filename, RiepilogoQuotaDTO dto) throws Exception {
        int i = 1;
        dto.setDettagli(new ArrayList<>());
        for (RowData rowData : data.getOnlyValidRows()) {
            try {
                //costruisco i dati del lavoratore
                //Categoria s = importCausaliService.getSettore(importData.getSettore());
                DettaglioQuotaDTO dett = createDettaglioQuota(importData, rowData, dto, null);
                dto.getDettagli().add(dett);
            }catch (Exception ex){
                logger.log(filename, "Errore", "ERRORE nella aggiunta del dettaglio all riga " + String.valueOf(i) + "; " + ex.getMessage() , false);
            }
            i++;
        }
    }

    private DettaglioQuotaDTO createDettaglioQuota(ImportData importData, RowData rowData, RiepilogoQuotaDTO dto,  Categoria settore) throws Exception {
        DettaglioQuotaDTO d = new DettaglioQuotaDTO();

        d.setDataDocumento(dto.getDataDocumento());
        d.setDataRegistrazione(dto.getDataRegistrazione());
        d.setTipoDocumento(dto.getTipoDocumento());
        d.setOperatoreDelega(rowData.getData().get("OPERATORE"));

        String contratto = rowData.getData().get("CONTRATTO");
        if (!StringUtils.isEmpty(contratto)){
            d.setContratto(contratto);
            importCausaliService.createIfNotExistContratto(d.getContratto());
        }

        String dataInizioSt = rowData.getData().get("DATA_INIZIO");
        String dataFineSt = rowData.getData().get("DATA_FINE");

        Date dataInizio = tryParse(dataInizioSt,null);
        Date dataFine = tryParse(dataFineSt, null);

        //se ci sono e sono validi inseriso le date del file
        if (dataFine != null && dataInizio != null){
            if (dataFine.getTime() > dataInizio.getTime()){
                d.setDataInizio(dataInizio);
                d.setDataFine(dataFine);
            }
        }
        //altrimenti imposto i valori immessi da intefaccia
        if (d.getDataFine() == null)
        {
            d.setDataInizio(tryParse(importData.getDataInizio(),null));
            d.setDataFine(tryParse(importData.getDataFine(),null));
        }


        d.setLivello(rowData.getData().get("LIVELLO"));
        d.setQuota(tryParseQuota( rowData.getData().get("QUOTA")));
        d.setSettore(rowData.getData().get("SETTORE"));

        d.setNote(rowData.getData().get("NOTE"));

        //ovvimante avendoli già iniseriti non creo completamente i dto azienda e worker
        WorkerDTO w = new WorkerDTO();
        w.setFiscalcode(rowData.getData().get("FISCALE"));
        d.setWorker(w);


        d.setProvincia(rowData.getData().get("PROVINCIA"));
        FirmDTO f = new FirmDTO();
        f.setDescription(rowData.getData().get("AZIENDA"));
        d.setFirm(f);

        return d;
    }

    private RiepilogoQuotaDTO createRiepilogoQuotaHeader(ImportData importData, String logFile, String guid) {

        Date documentDate = addOneDayTodate(tryParse(importData.getDataFine(), null));

        //posso adesso costruire il riepilogo quota DTO
        RiepilogoQuotaDTO dto = new RiepilogoQuotaDTO();
        dto.setDataRegistrazione(new Date());
        dto.setDataDocumento(documentDate);
        dto.setRipresaDati(true);
        dto.setSettore("");
        dto.setAzienda("RIPRESA DATI");
        dto.setProvincia(geo.getProvinceById(Integer.parseInt(importData.getProvince())).getDescription());
        dto.setCompanyId(Long.parseLong(importData.getCompany()));

        SimpleDateFormat ss = new SimpleDateFormat("dd/MM/yyyy");
        dto.setCompentenza(String.format("%s - %s", ss.format(tryParse(importData.getDataInizio(), null)), ss.format(tryParse(importData.getDataFine(), null)) ));
        dto.setGuid(guid);
        dto.setTipoDocumento("IQA");
        if (importData.getCreateDelega() != null){
            if (importData.getCreateDelega() == 1)
                dto.setCreaDelegaIfNotExist(true);
        }
        if (importData.getAssociateDelega() != null){
            if (importData.getAssociateDelega() == 1)
                dto.setAssociaDelega(true);
        }

        dto.setOriginalFilename(importData.getFile1());
        dto.setLogFilename(logFile);
        dto.setExporterName(((User) sec.getLoggedUser()).getCompleteName());
        dto.setExporterMail(sec.getLoggedUser().getUsername());
        return dto;
    }

    private void importCacheAziende(ExcelInfo data, ImportData importData, String filename, ImportCache cache) throws Exception {
        int i = 1;
        for (RowData rowData : data.getOnlyValidRows()) {
            //costruisco i dati del lavoratore
            Azienda l = null;
            try {
                l = importDataAziende.constructAzienda(rowData,  importData);

                //metto tutto in cache
                if (l != null){
                    logger.log(filename,"Creazione aziende","Azienda " + rowData.getData().get("AZIENDA") + " correttamente costruita", false);

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
                l = importDataLavoratori.constructLavoratore(rowData,  importData);
                logger.log(filename,"Creazione lavoratori","Lavoratore " + rowData.getData().get("FISCALE") + " correttamente costruito", false);
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

    private void validateExcelDataForQuote(ExcelInfo data) throws Exception {
        if (!fr.opensagres.xdocreport.core.utils.StringUtils.isEmpty(data.getError())){
            //recupero il nome del file
            String name = FilenameUtils.getName(data.getSourceFile());

            throw new Exception(String.format("Un file contiene errori: %s  <br>",  data.getError()));
        }

        //prendo l'intestazione e verifico che sia la stessa che mi aspetto
        List<String> template  = new ArrayList<>();
        template.add("COGNOME_UTENTE");
        template.add("NOME_UTENTE");
        template.add("DATA_NASCITA_UTENTE");
        template.add("SESSO");
        template.add("FISCALE");
        template.add("COMUNE_NASCITA");
        template.add("COMUNE");
        template.add("INDIRIZZO");
        template.add("CAP");
        template.add("TELEFONO1");
        template.add("TELEFONO2");
        template.add("NOTE_UTENTE");

        template.add("MAIL");
        template.add("PROVINCIA");
        template.add("SETTORE");
        template.add("AZIENDA");
        template.add("DESCR_ALTERNATIVA");
        template.add("PARTITA_IVA");
        template.add("COMUNE_AZIENDA");
        template.add("INDIRIZZO_AZIENDA");
        template.add("CAP_AZIENDA");
        template.add("TELEFONO_AZIENDA");
        template.add("NOTE_AZIENDA");
        template.add("CONTRATTO");

        template.add("DATA_INIZIO");
        template.add("DATA_FINE");
        template.add("QUOTA");
        template.add("LIVELLO");
        template.add("NOTE");
        template.add("OPERATORE");


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

    private void baseValidateQuote(ImportData importData) throws Exception {
        baseValidate(importData);

       //devo validare le date
        Date dataInizio = tryParse(importData.getDataInizio(), null);
        if (dataInizio == null){
            throw new Exception("Data inizio non specificata");
        }


        Date dataFine = tryParse(importData.getDataFine(), null);
        if (dataFine == null){
            throw new Exception("Data fine non specificata");
        }


        if (dataInizio.getTime() > dataFine.getTime()){
           throw new Exception("Data inizio posteriore alla data fine");

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

    private String getImportFileServerFolderPath() {
        User u = ((User) sec.getLoggedUser());
        Long c = u.getLid();

        String folderName = "files/import_" + c + "/";

        return folderName;

    }

    private Date addOneDayTodate(Date date) {
        if (date == null)
            return null;

        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, 1);
        return c.getTime();
    }

    private Double tryParseQuota(String quota) {
        try {
            return Double.parseDouble(quota);
        }catch (Exception ex){
            return 0d;
        }
    }

    private Date tryParse(String s, Date defaultDate){
        SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");


        try {
            return ff.parse(s);
        } catch (ParseException e) {
            return defaultDate;
        }

    }
//    public List<DettaglioQuotaAssociativa> saveDettagliQuote(String filename, RiepilogoQuoteAssociative q, ImportCache cache, RiepilogoQuotaDTO dto) throws Exception {
//        logger.log(filename, "Recupero lista quote", "Recupero la lista dei dettagli quote associative a partrire dal dto", false);
//        List<DettaglioQuotaAssociativa> quote = importHelper.retrieveDettagliFromDtos(dto, cache);
//        logger.log(filename, "Recupero lista quote", "Lista dei dettagli quote associative recuperata con " + quote.size() + " righe", false);
//
//        int j = 0;
//        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : quote) {
//            try{
//                j++;
//                logger.log(filename, "ImportData", "Importazione dettaglio quota num : "  + String.valueOf(j), false);
//                dettaglioQuotaAssociativa.setIdRiepilogoQuotaAssociativa(q.getLid());
//                dettRep.save(dettaglioQuotaAssociativa);
//            }catch(Exception ex){
//
//                ex.printStackTrace();
//                logger.log(filename, "Errore Importazione dettaglio quota", String.format("Errore in importazione dettagli quote num %s. Errore: %s", String.valueOf(j), ex.getMessage() ) , false);
//            }
//
//        }
//        return quote;
//    }


//    public RiepilogoQuoteAssociative doImportQuoteManuali(RiepilogoQuotaDTO dto) throws Exception {
//        String guid = dto.getGuid();
//        String logFile = optMan.get("applica.fenealquote.logfolder") +"importData_ " + guid + ".txt";
//
//        dto.setLogFilename(logFile);
//
//        logger.log(logFile, "StartImport", "Avvio creazione intestazione quota ", false);
//        logger.log(logFile, "StartImport", "*****************************", false);
//        logger.log(logFile, "StartImport", "*****************************", false);
//
//        logger.log(logFile,  "DTO", "Validazione dettagli dto ", false);
////        if (dto.getDettagli().size() == 0)
////        {
////            logger.log(logFile,  "ERRORE", "Nessuna quota da inserire ", false);
////
////            throw new Exception("Nessuna quota da inserire");
////        }
//
//        //adesso posso inserire il dto previa validazione
//        importHelper.ValidateDTO(dto);
//
//        //salvo i dati
//        return importHelper.saveRiepilogoQuoteManuale(dto,logFile);
//
//
//    }
//
//    public RiepilogoQuoteAssociative doImportQuoteBreviMano(RiepilogoQuotaDTO dto) throws Exception {
//        String guid = dto.getGuid();
//        String logFile = optMan.get("applica.fenealquote.logfolder") +"importData_ " + guid + ".txt";
//
//        dto.setLogFilename(logFile);
//
//        logger.log(logFile, "StartImport", "Avvio creazione quota brevi mano ", false);
//        logger.log(logFile, "StartImport", "*****************************", false);
//        logger.log(logFile, "StartImport", "*****************************", false);
//
//        //adesso posso inserire il dto previa validazione
//        importHelper.ValidateDTO(dto);
//
//        //salvo i dati
//        return importHelper.saveRiepilogoQuote(dto,logFile,logFile,null);
//
//    }
}
