package applica.feneal.services.impl.importData;


import applica.feneal.domain.data.UsersRepository;
import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.data.core.quote.QuoteAssocRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.feneal.domain.model.core.quote.fenealgestImport.DettaglioQuotaDTO;
import applica.feneal.domain.model.core.quote.fenealgestImport.RiepilogoQuotaDTO;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.services.GeoService;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by fgran on 17/11/2016.
 * Questoa class ,valida, e salva i riepiloghi delle quote e i relativi dettagli
 */
@Component
public class ImportQuoteHelper {

    @Autowired
    private Security sec;

    @Autowired
    private GeoService geo;

    @Autowired
    private CompanyRepository compRep;

    @Autowired
    private ImportDataDeleghe importDataDeleghe;

    @Autowired
    private ImportQuoteLogger logger;

    @Autowired
    private QuoteAssocRepository quoteRep;


    @Autowired
    private UsersRepository usersRep;

    @Autowired
    private DettaglioQuoteAssociativeRepository dettRep;


    /**
     * validazione usato dalle importazioni massive da file excel delle quote e dalla creazione di quote associativa da azienda
     * @param dto
     * @throws Exception
     */
    public void ValidateDTO(RiepilogoQuotaDTO dto) throws Exception {


        //verifico la presenza della provincia e che tale provincia sia annoverata tra le province
        //della company specificata nella companyId
        if (org.apache.commons.lang.StringUtils.isEmpty(dto.getProvincia())){
            throw new Exception("Provincia non impostata");
        }

        Company cc = compRep.get(dto.getCompanyId()).orElse(null);
        checkProvincia(dto.getProvincia(), dto, cc);

        //verifico che ci sia un guid identificativo
        if (org.apache.commons.lang.StringUtils.isEmpty(dto.getGuid())){

            dto.setGuid(UUID.randomUUID().toString());
        }


        if (dto.getDataDocumento() == null)
            throw new Exception("Data documento non impostata");
        if (dto.getDataRegistrazione() == null)
            throw new Exception("Data registrazione non impostata");
        if (org.apache.commons.lang.StringUtils.isEmpty(dto.getTipoDocumento())){
            throw new Exception("Tipo documento non impostato");
        }



        //adesso verifico che ogni item sdeve avere una azienda
        for (DettaglioQuotaDTO dettaglioQuotaDTO : dto.getDettagli()) {

            //ATTENZIONE
            //se nel dettagliodto è presente anche la provincia allora vuol dire che sto eseguendo una operazione
            //massiva(import da file excel)
            //questo siginifica che anche la provicnia deve essere validata
            if (!StringUtils.isEmpty(dettaglioQuotaDTO.getProvincia()))
                checkProvincia(dettaglioQuotaDTO.getProvincia(), dto, cc);


            //se l'azienda è nulla lancio eccezione
            if (dettaglioQuotaDTO.getFirm() == null || org.apache.commons.lang.StringUtils.isEmpty(dettaglioQuotaDTO.getFirm().getDescription()))
                throw new Exception("Azienda obbligatoria");

            //se l'utente è nullo lanco l'eccezione
            if (dettaglioQuotaDTO.getWorker() == null || org.apache.commons.lang.StringUtils.isEmpty(dettaglioQuotaDTO.getWorker().getFiscalcode()))
                throw new Exception("Lavoratore obbligatorio");


            if (dettaglioQuotaDTO.getDataInizio().getTime() > dettaglioQuotaDTO.getDataFine().getTime()){
                throw new Exception("La data  di inizio è posteriore alla data fine competenza");
            }
        }

        //moltro probabilmente quando l'appplicazione sarà in produzione la funzionalita
        //di gestione delle quote sarà completamente affidata al nazionale. Questo siginifica
        //che entità come le deleghe che necessitano di essere create e gestite dagli operatori dei
        //territori non potrebbero essere create. Pertanto per ovviare a questo problema verifico che
        //esista per il territorio nel quale si stanno
        //inserendo le quote (dto.getcompanyId()) un utente regionale con il quale simulero l'impoersonificazione
        //nella creazione delle deleghe
        //se non esiste non proseguo....
        User reg1 = usersRep.getRegionalUserForCompany(dto.getCompanyId());
        if (reg1 == null)
            throw new Exception("Non esiste un utente regionale per il territorio con id : " + dto.getCompanyId());

    }

    private void checkProvincia(String provinvia,RiepilogoQuotaDTO dto, Company company) throws Exception {
        Province p = geo.getProvinceByName(provinvia);
        if (p == null){
            throw new Exception("Provincia non riconosciuta: " + provinvia);
        }

        if (dto.getCompanyId() == 0)
            throw new Exception("Company non specificata");



        if (company == null){
            throw new Exception("Company non esistente");
        }

        if (!company.containProvince(p.getDescription()))
            throw new Exception("Provincia non compresa tra le province del territorio:"  + provinvia);
    }


    /**
     * Metodo usato dalle importazioni massive da file excel delle quote
     * e dalla creazione di quote associativa da azienda
     * @param dto
     * @param filename
     * @param logFile
     * @param cache
     * @return
     * @throws Exception
     */
    public RiepilogoQuoteAssociative saveRiepilogoQuote(RiepilogoQuotaDTO dto, String filename, String logFile, ImportCache cache) throws Exception {


        RiepilogoQuoteAssociative q = createRiepilogoQuotaFromDto(dto, logFile);
        quoteRep.save(q);
        logger.log(filename, "Salvataggio riepilogo quota", "Nuovo Riepilogo quote salvato", false);


        //recupero la lista delle quote associative a partrire cdal dto
        List<DettaglioQuotaAssociativa> quote = saveDettagliQuote(filename, q, cache, dto);

//        //se devo creare le deleghe o le devo attivare ciclo su tutte le quote
//        Categoria s = importCausaliService.getSettore(dto.getSettore());

        try{

            importDataDeleghe.activateDeleghe(filename, quote, dto, cache);

        }catch(Exception e){
            logger.log(filename, "Errore Attivazione deleghe", String.format("Si è verificato il seguente errore nella attivazione delle deleghe:  %s " , e.getMessage()) , false);
        }

        return q;

    }


    /**
     *  Metodo usato dalle importazioni massive da file excel delle quote e dalla creazione di quote associativa da azienda
     *  PER CREARE I DETTAGLI DI UN RIEPILOGO
     * @param filename
     * @param q
     * @param cache1
     * @param dto
     * @return
     * @throws Exception
     */
    private List<DettaglioQuotaAssociativa> saveDettagliQuote(String filename, RiepilogoQuoteAssociative q, ImportCache cache1, RiepilogoQuotaDTO dto) throws Exception {
        logger.log(filename, "Recupero lista quote", "Recupero la lista dei dettagli quote associative a partrire dal dto", false);
        List<DettaglioQuotaAssociativa> quote = retrieveDettagliFromDtos(dto, cache1);
        logger.log(filename, "Recupero lista quote", "Lista dei dettagli quote associative recuperata con " + quote.size() + " righe", false);

        int j = 0;
        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : quote) {
            try{
                j++;
                logger.log(filename, "ImportData", "Importazione dettaglio quota num : "  + String.valueOf(j), false);
                dettaglioQuotaAssociativa.setIdRiepilogoQuotaAssociativa(q.getLid());
                dettRep.save(dettaglioQuotaAssociativa);
            }catch(Exception ex){

                ex.printStackTrace();
                logger.log(filename, "Errore Importazione dettaglio quota", String.format("Errore in importazione dettagli quote num %s. Errore: %s", String.valueOf(j), ex.getMessage() ) , false);
            }

        }
        return quote;
    }



    //************************************************************
    //METODI DI UTILITA PER CONVERTIRE dto IN QUITE ASSOCIATIVE E VICEVERSA

    public RiepilogoQuoteAssociative createRiepilogoQuotaFromDto(RiepilogoQuotaDTO dto, String logFilename) {


        RiepilogoQuoteAssociative result = new RiepilogoQuoteAssociative();

        result.setDataRegistrazione(dto.getDataRegistrazione());
        result.setDataDocumento(dto.getDataDocumento());


        result.setCompanyId(dto.getCompanyId());
        result.setProvincia(dto.getProvincia().toUpperCase());
        result.setAzienda(dto.getAzienda().toUpperCase());


        //result.setSettore(dto.getSettore().toUpperCase());
        result.setCompetenza(dto.getCompentenza());
        result.setGuid(dto.getGuid());
        result.setOriginalFileServerPath(dto.getOriginalFilename());
        result.setTipoDocumento(dto.getTipoDocumento().toUpperCase());

        result.setImportedLogFilePath(new File(logFilename).getName());


        result.setExporterName(dto.getExporterName());
        result.setExporterMail(dto.getExporterMail());

        result.setTotalAmount(calculateTotal(dto));


        return result;
    }

    private double calculateTotal(RiepilogoQuotaDTO dto) {

        double result = 0;

        for (DettaglioQuotaDTO dettaglioQuotaDTO : dto.getDettagli()) {
            result =+ dettaglioQuotaDTO.getQuota();
        }

        return result;
    }

    public List<DettaglioQuotaAssociativa> retrieveDettagliFromDtos(RiepilogoQuotaDTO dto, ImportCache cache1) {
        User u = ((User) sec.getLoggedUser());
        List<DettaglioQuotaAssociativa> result = new ArrayList<>();

        for (DettaglioQuotaDTO dettaglioQuotaDTO : dto.getDettagli()) {

            DettaglioQuotaAssociativa dett = new DettaglioQuotaAssociativa();

            Company cc = compRep.get(dto.getCompanyId()).orElse(null);
            if (cc != null)
                dett.setRegione(cc.getDescription());


            dett.setDataRegistrazione(dto.getDataRegistrazione());
            dett.setDataInizio(dettaglioQuotaDTO.getDataInizio());
            dett.setDataFine(dettaglioQuotaDTO.getDataFine());
            dett.setDataDocumento(dto.getDataDocumento());
            dett.setContratto(dettaglioQuotaDTO.getContratto());
            dett.setLivello(dettaglioQuotaDTO.getLivello());
            dett.setNote(dettaglioQuotaDTO.getNote());
            dett.setQuota(dettaglioQuotaDTO.getQuota());
            dett.setTipoDocumento(dto.getTipoDocumento().toUpperCase());
            dett.setCompanyId(dto.getCompanyId());

            dett.setRipresaDati(dto.isRipresaDati());

            //nel caso di importazioni massive recupero tutto dal dto relativamente la provicnia
            //questo è stato fatto per permettere l'inseriemnto massivo regione per regione
            //e non per ogni provincia...
            if (StringUtils.isEmpty(dettaglioQuotaDTO.getProvincia())){
                dett.setProvincia(dto.getProvincia().toUpperCase());


            }
            else
                dett.setProvincia(dettaglioQuotaDTO.getProvincia().toUpperCase());

            //stessa cosa per operatore e settore
            if (!StringUtils.isEmpty(dettaglioQuotaDTO.getOperatoreDelega()))
                dett.setOperatoreDelega(dettaglioQuotaDTO.getOperatoreDelega());

            if (!StringUtils.isEmpty(dettaglioQuotaDTO.getSettore()))
                dett.setSettore(dettaglioQuotaDTO.getSettore().toUpperCase());

            //materializzo l'azienda...
            Azienda a = null;
            //se non cè in cache è perchè ho modificato la procedura di creazione quote massive
            //per permettere la creazione direttamente da dto e non da file
            //vedi QuoteCreationControlller e chiamate successive....
            if (cache1 != null){
                a = cache1.getAziende().get(dettaglioQuotaDTO.getFirm().getDescription());
            }else{
                a = new Azienda();
                a.setId(dettaglioQuotaDTO.getFirm().getId());
            }

            if (a != null)
                dett.setIdAzienda(a.getLid());


            //materializzo il lavoratoree
            Lavoratore la = null;
            //se non cè in cache è perchè ho modificato la procedura di creazione quote massive
            //per permettere la creazione direttamente da dto e non da file
            //vedi QuoteCreationControlller e chiamate successive....
            if (cache1 != null){
                la = cache1.getLavoratoriFiscalCode().get(dettaglioQuotaDTO.getWorker().getFiscalcode());
            }
            else{
                la = new Lavoratore();
                la.setId(dettaglioQuotaDTO.getWorker().getId());
            }

            if (la != null){
                dett.setIdLavoratore(la.getLid());
                result.add(dett);
            }

        }

        return result;
    }

    public DettaglioQuotaAssociativa convertDettaglioFromDto(RiepilogoQuoteAssociative dto ,DettaglioQuotaDTO dettaglioQuotaDTO) {
        User u = ((User) sec.getLoggedUser());

        DettaglioQuotaAssociativa dett = new DettaglioQuotaAssociativa();
        dett.setIdRiepilogoQuotaAssociativa(dto.getLid());
        dett.setRegione(dettaglioQuotaDTO.getRegione());
        dett.setDataRegistrazione(dto.getDataRegistrazione());
        dett.setDataInizio(dettaglioQuotaDTO.getDataInizio());
        dett.setDataFine(dettaglioQuotaDTO.getDataFine());
        dett.setDataDocumento(dto.getDataDocumento());
        dett.setContratto(dettaglioQuotaDTO.getContratto());
        dett.setCompanyId(dto.getCompanyId());

        dett.setLivello(dettaglioQuotaDTO.getLivello());
        dett.setNote(dettaglioQuotaDTO.getNote());
        dett.setProvincia(dto.getProvincia().toUpperCase());
        dett.setQuota(dettaglioQuotaDTO.getQuota());
        dett.setTipoDocumento(dto.getTipoDocumento().toUpperCase());

        Azienda a = null;
        if (dettaglioQuotaDTO.getFirm() != null){
            if (!org.apache.commons.lang.StringUtils.isEmpty(dettaglioQuotaDTO.getFirm().getDescription()))

                a = new Azienda();
            a.setId(dettaglioQuotaDTO.getFirm().getId());
        }

        if (a != null)
            dett.setIdAzienda(a.getLid());



        Lavoratore la = null;
        la = new Lavoratore();
        la.setId(dettaglioQuotaDTO.getWorker().getId());


        if (la != null){
            dett.setIdLavoratore(la.getLid());
        }



        return dett;
    }



//    public RiepilogoQuoteAssociative saveRiepilogoQuoteManuale(RiepilogoQuotaDTO dto, String logFile) {
//
//        RiepilogoQuoteAssociative q = createRiepilogoQuotaFromDto(dto, logFile);
//        quoteRep.save(q);
//
//        return q;
//    }
}
