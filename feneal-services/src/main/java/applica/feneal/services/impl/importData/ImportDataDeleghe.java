package applica.feneal.services.impl.importData;

import applica.feneal.domain.data.UsersRepository;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.model.core.ImportData;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.importData.ImportDelegaSummary;
import applica.feneal.domain.model.core.importData.ImportDelegheValidator;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.feneal.domain.model.core.quote.fenealgestImport.RiepilogoQuotaDTO;
import applica.feneal.services.DelegheService;
import applica.feneal.services.GeoService;
import applica.framework.LoadRequest;
import applica.framework.management.csv.RowData;
import applica.framework.management.excel.ExcelInfo;
import applica.framework.security.Security;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fgran on 09/05/2017.
 */
@Component
public class ImportDataDeleghe {

    @Autowired
    private ImportDataAziende importDataAziende;

    @Autowired
    private LavoratoriRepository lavRep;

    @Autowired
    private AziendeRepository azServ;

    @Autowired
    private CategoriaRepository catREp;

    @Autowired
    private ImportQuoteLogger logger;

    @Autowired
    private Security sec;

    @Autowired
    private DelegheService delSvc;

    @Autowired
    private ImportDataUtils importDataUtils;

    @Autowired
    private ImportDataLavoratori importDataLavoratori;

    @Autowired
    private ImportCausaliService importCausaliService;

    @Autowired
    private GeoService geo;


    @Autowired
    private UsersRepository usersRep;

    @Autowired
    private DettaglioQuoteAssociativeRepository dettRep;


    /**
     * Questa procedura di attivazione della delega è richiamata nel contesto della  aggiunta di una nuova posizione
     * ad una quota associativa
     * @param riepilogo
     * @param dettaglioQuotaAssociativa
     * @param createDelegaIfNotExist
     * @param associateDelega
     * @throws Exception
     */
    public void activateDelega( RiepilogoQuoteAssociative riepilogo, DettaglioQuotaAssociativa dettaglioQuotaAssociativa, boolean createDelegaIfNotExist, boolean associateDelega) throws Exception {

        //variabile per verificare se la delega è stata inserita precedentemente...
        //in modo da evitare un aggiornamento della quota associativa

        if (associateDelega){

            //recupero la delega
            Delega d =delSvc.retrieveActivableWorkerDelega(dettaglioQuotaAssociativa.getIdLavoratore(),geo.getProvinceByName( dettaglioQuotaAssociativa.getProvincia()).getIid(),
                    dettaglioQuotaAssociativa.getIdAzienda() );


            if (dettaglioQuotaAssociativa.getIdLavoratore() > 0){

                if (associateDelega){
                    if (d != null){
                        delSvc.activateDelega(new Date(), d);
                        addDelegaInfoToCreatedQuota(dettaglioQuotaAssociativa, d);

                    }
                }
            }
        }
    }



    /**
     * Questa funzione è utilizzata nel contesto della creazione di quote associati
     * da azienda oppure di importazione quote associative per attivare le deleghe dopo il salvataggio della quota
     * @param filename
     * @param quote
     * @param dto
     * @param cache1
     * @throws Exception
     */
    public void activateDeleghe(String filename, List<DettaglioQuotaAssociativa> quote, RiepilogoQuotaDTO dto, ImportCache cache1) throws Exception {

        logger.log(filename, "Attivazione deleghe", String.format("Avvio procedura attivazione delle deleghe") , false);
        if (dto.isAssociaDelega()){
            logger.log(filename, "Attivazione deleghe", String.format("Avvio procedura attivazione delle deleghe con i seguenti parametri:" +
                    "" +
                    "AssociaDelega = %s; --", String.valueOf(dto.isAssociaDelega())) , false);

            int jj = 0;
            for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : quote) {
                //recupero le delega che è possibile attivare per l'utente
                Delega d =delSvc.retrieveActivableWorkerDelega(dettaglioQuotaAssociativa.getIdLavoratore(),geo.getProvinceByName( dto.getProvincia()).getIid(),
                        dettaglioQuotaAssociativa.getIdAzienda() );

                logger.log(filename, "Attivazione delega", "Attivazione delega per dettaglio quota num " + String.valueOf(jj) + " per id lavoratore : "  + dettaglioQuotaAssociativa.getIdLavoratore(), false);
                if (dettaglioQuotaAssociativa.getIdLavoratore() > 0){
                    if (d != null){
                        //tento di associaree la delega alla quota associativa
                        associateDelega(filename, dto,  dettaglioQuotaAssociativa, d);

                        logger.log(filename, "Attivazione delega", "Delega attivabile trovata. Si procede alla attivazione se il flag Activate Delega è impostato a true", false);
                    }else{
                        logger.log(filename, "Attivazione delega", "Nessuna delega presente", false);
                    }

                }else{
                    logger.log(filename, "Attivazione delega", "Nessuna operazione eseguita poichè non esiste l'id del lavoratore", false);
                }

                jj++;
            }
        }
    }


    public void doImportDeleghe(ImportData importData, File temp1, File temp) throws Exception {

        baseValidate(importData);


        ExcelInfo data = importDataUtils.importData(importData, temp1, new ImportDelegheValidator());
        validateExcelDataForQuote(data);


        //se non ci sono errori posso importare i lavoratori e quindi importare
        importAllDeleghe(data, temp, importData);
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

    private void baseValidateDeleghe(ImportData importData) throws Exception {
        baseValidate(importData);


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

    private void validateExcelDataForDeleghe(ExcelInfo data) throws Exception {
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


        template.add("AZIENDA");
        template.add("DESCR_ALTERNATIVA");
        template.add("PARTITA_IVA");
        template.add("COMUNE_AZIENDA");
        template.add("INDIRIZZO_AZIENDA");
        template.add("CAP_AZIENDA");
        template.add("TELEFONO_AZIENDA");
        template.add("NOTE_AZIENDA");
        template.add("CONTRATTO");

        template.add("DATA");
        template.add("DATA_ACCETTAZIONE");
        template.add("DATA_ANNULLAMENTO");
        template.add("DATA_REVOCA");
        template.add("CAUSALE_ISCRIZIONE");
        template.add("CAUSALE_REVOCA");
        template.add("NOTE");
        template.add("COLLABORATORE");

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

    private void addDelegaInfoToCreatedQuota(DettaglioQuotaAssociativa dettaglioQuotaAssociativa, Delega d) {

            //solo se la delega gia essiteva posso inserire i dati di associazione
            //dleega quota...
            dettaglioQuotaAssociativa.setDelegaMansione(d.getMansione());
            dettaglioQuotaAssociativa.setDelegaLuogoLavoro(d.getLuogoLavoro());
            dettaglioQuotaAssociativa.setDelegaNote(d.getNotes());
            dettaglioQuotaAssociativa.setOperatoreDelega(d.getUserCompleteName());
            dettaglioQuotaAssociativa.setOperatoreId(d.getUserId());
            if (d.getContract() != null)
                dettaglioQuotaAssociativa.setContratto(d.getContract().getDescription());
            dettaglioQuotaAssociativa.setSettore(d.getSector().getDescription());
            if (d.getCollaborator() != null)
                dettaglioQuotaAssociativa.setDelegaCollaboratore(d.getCollaborator().getDescription());
            dettRep.save(dettaglioQuotaAssociativa);

    }

    private void associateDelega(String filename, RiepilogoQuotaDTO dto, DettaglioQuotaAssociativa dettaglioQuotaAssociativa, Delega d) throws Exception {
        if (dto.isAssociaDelega()){
            if (d != null){

                    logger.log(filename, "Attivazione delega", "Attivo delega", false);
                    delSvc.activateDelega(new Date(), d);

                    addDelegaInfoToCreatedQuota(dettaglioQuotaAssociativa, d);

            }else{
                logger.log(filename, "Attivazione delega", "Non si procede alla attivazione poichè non ci sono deleghe attivabili", false);
            }
        }else{
            logger.log(filename, "Attivazione delega", "Non si procede alla attivazione poichè il flag Activate Delega è impostato a false", false);
        }
    }



    private void importAllDeleghe(ExcelInfo data, File tempdir, ImportData importData) throws Exception {
        String filename = tempdir + "\\logImportazioneDeleghe.txt";

        File f = new File(filename);
        f.createNewFile();

        importDataUtils.writeToFile(f,  "Avvio import deleghe: num ( " + data.getOnlyValidRows().size() + " )");
        importDataUtils.writeToFile(f,  "*****************************");
        importDataUtils.writeToFile(f,  "*****************************");

        //Importo il lavoratore se non esiste e ne creo la relativa delega
        int i = 1; //numro di riga
        for (RowData rowData : data.getOnlyValidRows()) {
            //costruisco i dati del lavoratore
            Lavoratore l = null;
            ImportDelegaSummary summ = null;

            boolean lavoratoreOk = false;
            try {
                LoadRequest ee = LoadRequest.build().disableOwnershipQuery().filter("fiscalcode",rowData.getData().get("FISCALE").trim());
                l = lavRep.find(ee).findFirst().get();


                lavoratoreOk = true;
            } catch (Exception e) {
                importDataUtils.writeToFile(f, "ERRORE nella costruzione del lavoratore riga " + String.valueOf(i) + "; " + e.getMessage() );
            }

            if (lavoratoreOk){
                //posso procedere al salvataggio della delega
                    summ = constructDelega(rowData, f, importData);
                    try {

                        Delega d = null;
                        d =  createDelega(l, summ, importData);
                        //posso adesso salvare la delega
                        delSvc.saveImportedDelega(d);

                    } catch (Exception e) {
                        importDataUtils.writeToFile(f, "ERRORE nel salvataggio della delega riga " + String.valueOf(i) + "; " + e.getMessage() );
                    }
            }
            i++;

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

    private ImportDelegaSummary constructDelega(RowData rowData, File f, ImportData importData) throws Exception {
        importDataUtils.writeToFile(f, "Creo il summary per il lavoratore: " + rowData.getData().get("FISCALE"));

        ImportDelegaSummary  l = new ImportDelegaSummary();

        GregorianCalendar c = new GregorianCalendar();
        c.set(Calendar.YEAR, 2016);
        c.set(Calendar.MONTH, 1);
        c.set(Calendar.DAY_OF_MONTH, 1);



        l.setData(c.getTime());
        l.setDataAccettazione(c.getTime());
        importCausaliService.createIfNotExistCausaleIscrizioneDelega("RIPRESA DATI");
        l.setCausaleIscrizione("RIPRESA DATI");
        l.setSettore(rowData.getData().get("SETTORE"));
        l.setAzienda(rowData.getData().get("AZIENDA").trim());
        l.setProvincia(rowData.getData().get("PROVINCIA").trim());

        return l;
    }

    private Delega createDelega(Lavoratore l, ImportDelegaSummary summary, ImportData data) throws Exception {
        Delega d;
        d = new Delega();        //creo la delega.....
        d.setDocumentDate(summary.getData());
        d.setAcceptDate(summary.getDataAccettazione());
        SimpleDateFormat s = new SimpleDateFormat("dd-MM-yyyy");
        d.setImportGuid(s.format(new Date()));
        d.setWorker(l);
        d.setProvince(geo.getProvinceByName(summary.getProvincia()));
        d.setSubscribeReason( importCausaliService.getCausaleIscrizione( summary.getCausaleIscrizione()));
        LoadRequest req = LoadRequest.build().filter("description", summary.getSettore());


        d.setSector(catREp.find(req).findFirst().get());


        //d.setNotes(summary.getNote());
        //carico l'azienda
        Azienda a = azServ.searchAziendaForProvince(summary.getAzienda(), summary.getProvincia());
        d.setWorkerCompany(a);

        d.setState(Delega.state_accepted);
        d.setPreecedingState(Delega.state_subscribe);


        return d;
    }


}
