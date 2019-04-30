package applica.feneal.services.impl.quote;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.core.quote.BonificoRepository;
import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.data.core.quote.QuoteAssocRepository;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.configuration.Categoria;
import applica.feneal.domain.model.core.deleghe.DelegaPerCreazioneQuota;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.quote.Bonifico;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.feneal.domain.model.core.quote.UpdatableDettaglioQuota;
import applica.feneal.domain.model.core.quote.fenealgestImport.DettaglioQuotaDTO;
import applica.feneal.domain.model.core.quote.fenealgestImport.RiepilogoQuotaDTO;
import applica.feneal.domain.model.core.servizi.search.UiIqaReportSearchParams;
import applica.feneal.domain.model.geo.City;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.domain.utils.Box;
import applica.feneal.services.*;
import applica.feneal.services.impl.importData.*;
import applica.feneal.services.impl.report.fastPerformanceData.Iscritto;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.library.options.OptionsManager;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fgran on 20/05/2016.
 */
@Service
public class QuoteAssociativeServiceImpl implements QuoteAssociativeService {

    @Autowired
    private ImportQuoteLogger logger;

    @Autowired
    private BonificoRepository bonRep;

    @Autowired
    private ImportDataDeleghe importDataDeleghe;

    @Autowired
    private QuoteAssocRepository quoteRep;

    @Autowired
    private DettaglioQuoteAssociativeRepository dettRep;

    @Autowired
    private LavoratoreService lavRep;

    @Autowired
    private LavoratoriRepository lavRepository;

    @Autowired
    private AziendaService  azSvc;

    @Autowired
    private ImportQuoteHelper importHelper;

    @Autowired
    private OptionsManager optManager;

    @Autowired
    private GeoService geo;

    @Autowired
    private CategoriaRepository settoreRepository;

    @Autowired
    private ImportDataManualLogin importDataManualLogin;

//    @Autowired
//    private ReportDelegheService rptDelSvc;

    @Autowired
    private Security sec;

    @Autowired
    private ImportDataQuote importQuote;

    @Autowired
    private ReportIscrittiService rptIscrittiSvc;


    @Override
    public String importQuoteAssociativeFromDatiTerritori(int companyId, RiepilogoQuotaDTO dto) throws Exception {

        if (dto == null)
            return "";

        //definisco il nnome del file che farà il log dell'importazione
        String filename = "importData_ " + dto.getGuid() + ".txt";


        //eseguo il login manuale
        importDataManualLogin.executeManualLogin(companyId, dto, filename);

        try {

            logger.log(filename, "StartImport", "Avvio import num " + dto.getExportNumber(), false);
            logger.log(filename, "StartImport", "*****************************", false);
            logger.log(filename, "StartImport", "*****************************", false);


            importHelper.ValidateDTO(dto);



            logger.log(filename, "Recupero riepilogo quota", "Quota non trovata: creo una quota dal DTO", false);
            String logFile = optManager.get("applica.fenealquote.logfolder") + filename;



            //adesso non devo fare altro che creare tutti i prerequisiti
            //"Aziende" e "Lavoratori" prima di ciclare ed inserire tutte le quote

            //anagrafo le aziedne e i lavoratori
            ImportCache cache = new ImportCache();
            importLavoratori(dto, cache, filename);
            importAziende(dto, cache, filename );


            importHelper.saveRiepilogoQuote(dto, filename, logFile, cache);


        } catch (Exception e) {
            try {
                logger.log(filename, "Errore irreversibile","Errore irreveribile nella importazione dei dati: " + e.getMessage(), false);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        }


        return "";


    }


    private void importAziende(RiepilogoQuotaDTO dto, ImportCache cache, String filename) throws Exception {
        logger.log(filename, "StartAnagraficheAz", "Avvio importazione anagrafiche aziende", false);
        int i = 0;
        for (DettaglioQuotaDTO iscrizione : dto.getDettagli()) {
            if (iscrizione.getFirm() != null){
                String azienda = iscrizione.getFirm().getDescription();
                if(!StringUtils.isEmpty(azienda)){
                    if (!StringUtils.isEmpty(azienda.trim())){

                        if (!cache.getAziende().containsKey(azienda.trim())){
                            try {
                                i++;
                                logger.log(filename, "AnagraficheAz", "Inserimento anagrafica num. " + String.valueOf(i) + " per azienda " + iscrizione.getFirm().getDescription(), false );

                                Azienda a = azSvc.getAziendaByDescription(iscrizione.getFirm().getDescription(), dto.getCompanyId());
                                if (a == null)
                                {
                                    a = new Azienda();
                                    a.setDescription(iscrizione.getFirm().getDescription());
                                    a.setCity(iscrizione.getFirm().getCity());

                                    a.setAddress(iscrizione.getFirm().getAddress());
                                    a.setCap(iscrizione.getFirm().getCap());
                                    a.setPhone(iscrizione.getFirm().getPhone());
                                    a.setPiva(iscrizione.getFirm().getPiva());
                                    if (!StringUtils.isEmpty(iscrizione.getFirm().getCity())) {
                                        City c = geo.getCityByName(iscrizione.getFirm().getCity());

                                        if (c != null)
                                        {
                                            Province cc = geo.getProvinceById(c.getIdProvince());
                                            if (cc != null)
                                                a.setProvince(cc.getDescription());

                                        }

                                    }
                                    azSvc.saveOrUpdate(0, a);
                                }else{
                                    if (dto.isUpdateFirmas()){

                                        boolean executeUpdate = dto.isUpdateFirmas();

                                        if (executeUpdate) {
                                            a.setCity(iscrizione.getFirm().getCity());
                                            a.setAddress(iscrizione.getFirm().getAddress());
                                            a.setCap(iscrizione.getFirm().getCap());
                                            a.setPhone(iscrizione.getFirm().getPhone());
                                            a.setPiva(iscrizione.getFirm().getPiva());
                                            if (!StringUtils.isEmpty(iscrizione.getFirm().getCity())) {
                                                City c = geo.getCityByName(iscrizione.getFirm().getCity());

                                                if (c != null)
                                                {
                                                    Province cc = geo.getProvinceById(c.getIdProvince());
                                                    if (cc != null)
                                                        a.setProvince(cc.getDescription());

                                                }

                                            }
                                            azSvc.saveOrUpdate(0, a);
                                        }
                                    }
                                }
                                cache.getAziende().put(iscrizione.getFirm().getDescription(), a);
                                cache.getAziendeById().put(a.getLid(), a);

                            } catch (Exception e) {
                                logger.log(filename, "", "Errore nell'inserimento dell'azienda: " + iscrizione.getFirm().getDescription() + " -- " + e.getMessage(), false);
                                e.printStackTrace();
                            }
                        }

                    }
                }
            }
        }
        logger.log(filename, "EndAnagraficheAz", "Termine importazione anagrafiche aziende", false);

    }

    private void importLavoratori(RiepilogoQuotaDTO dto, ImportCache cache, String filename) throws Exception {
        logger.log(filename, "StartAnagraficheLav", "Avvio importazione anagrafiche lavoratori", false);
        int i = 0;
        for (DettaglioQuotaDTO iscrizione : dto.getDettagli()) {
            i++;
            logger.log(filename, "AnagraficheLav", "Inserimento anagrafica num. " + String.valueOf(i) + " per il lavoratore " + iscrizione.getWorker().getFiscalcode() + " -- " + iscrizione.getWorker().getName() + " " + iscrizione.getWorker().getSurname(), false);
            try{
                if (!cache.getLavoratoriFiscalCode().containsKey(iscrizione.getWorker().getFiscalcode()))
                {
                    LoadRequest req = LoadRequest.build().filter("fiscalcode", iscrizione.getWorker().getFiscalcode());
                    Lavoratore lav = lavRepository.find(req).findFirst().orElse(null);
                    if (lav == null){
                        //lo creo e lo inserisco nella cache
                        lav = new Lavoratore();
                        lav.setSex(Lavoratore.MALE);
                        lav.setNationality(iscrizione.getWorker().getNationality());
                        lav.setBirthDate(iscrizione.getWorker().getBirthDate());
                        lav.setLivingCity(iscrizione.getWorker().getLivingPlace());
                        lav.setBirthPlace(iscrizione.getWorker().getBirthPlace());
                        if (!StringUtils.isEmpty(iscrizione.getWorker().getLivingPlace())){
                            City cc = geo.getCityByName(iscrizione.getWorker().getLivingPlace());
                            if (cc != null)
                            {
                                Province cc1 = geo.getProvinceById(cc.getIdProvince());
                                if (cc1!=null)
                                    lav.setLivingProvince(cc1.getDescription());
                            }
                        }
                        if (!StringUtils.isEmpty(iscrizione.getWorker().getBirthPlace())){
                            City cc = geo.getCityByName(iscrizione.getWorker().getBirthPlace());
                            if (cc != null)
                            {
                                Province cc1 = geo.getProvinceById(cc.getIdProvince());
                                if (cc1!=null)
                                    lav.setBirthProvince(cc1.getDescription());
                            }
                        }
                        lav.setFiscalcode(iscrizione.getWorker().getFiscalcode());
                        lav.setName(iscrizione.getWorker().getName());
                        lav.setSurname(iscrizione.getWorker().getSurname());
                        lav.setAddress(iscrizione.getWorker().getAddress());
                        lav.setCap(iscrizione.getWorker().getCap());
                        lav.setCellphone(iscrizione.getWorker().getPhone());
                        lav.setPhone(iscrizione.getWorker().getPhone2());
//                        lav.setCe(iscrizione.getWorker().getCE());
//                        lav.setEc(iscrizione.getWorker().getEC());
                        lavRep.saveOrUpdate(0, lav);

                    }else{
                        if (dto.isUpdateWorkers()){

                            boolean executeUpdate = dto.isUpdateWorkers();

                            if (executeUpdate){
                                if (!StringUtils.isEmpty(iscrizione.getWorker().getNationality()))
                                    lav.setNationality(iscrizione.getWorker().getNationality());
                                if (iscrizione.getWorker().getBirthDate() != null)
                                    lav.setBirthDate(iscrizione.getWorker().getBirthDate());
                                if (!StringUtils.isEmpty(iscrizione.getWorker().getLivingPlace()))
                                    lav.setLivingCity(iscrizione.getWorker().getLivingPlace());
                                if (!StringUtils.isEmpty(iscrizione.getWorker().getBirthPlace()))
                                    lav.setBirthPlace(iscrizione.getWorker().getBirthPlace());


                                if (!StringUtils.isEmpty(iscrizione.getWorker().getLivingPlace())){
                                    City cc = geo.getCityByName(iscrizione.getWorker().getLivingPlace());
                                    if (cc != null)
                                    {
                                        Province cc1 = geo.getProvinceById(cc.getIdProvince());
                                        if (cc1!=null)
                                            lav.setLivingProvince(cc1.getDescription());
                                    }
                                }
                                if (!StringUtils.isEmpty(iscrizione.getWorker().getBirthPlace())){
                                    City cc = geo.getCityByName(iscrizione.getWorker().getBirthPlace());
                                    if (cc != null)
                                    {
                                        Province cc1 = geo.getProvinceById(cc.getIdProvince());
                                        if (cc1!=null)
                                            lav.setBirthProvince(cc1.getDescription());
                                    }
                                }
                                lav.setName(iscrizione.getWorker().getName());
                                lav.setSurname(iscrizione.getWorker().getSurname());
                                if (!StringUtils.isEmpty(iscrizione.getWorker().getAddress()))
                                    lav.setAddress(iscrizione.getWorker().getAddress());

                                if (!StringUtils.isEmpty(iscrizione.getWorker().getCap()))
                                    lav.setCap(iscrizione.getWorker().getCap());

                                if (!StringUtils.isEmpty(iscrizione.getWorker().getPhone()))
                                    lav.setCellphone(iscrizione.getWorker().getPhone());
                                if (!StringUtils.isEmpty(iscrizione.getWorker().getPhone2()))
                                    lav.setPhone(iscrizione.getWorker().getPhone2());
//                                if (!StringUtils.isEmpty(iscrizione.getWorker().getCE()))
//                                    lav.setCe(iscrizione.getWorker().getCE());
//                                if (!StringUtils.isEmpty(iscrizione.getWorker().getEC()))
//                                    lav.setEc(iscrizione.getWorker().getEC());
                                lavRep.saveOrUpdate(0, lav);
                            }



                        }
                    }

                    cache.getLavoratoriFiscalCode().put(lav.getFiscalcode(), lav);
                    cache.getLavoratori().put(lav.getLid(), lav);
                }

            }catch(Exception ex){
                logger.log(filename, "", "Errore nell'inserimento del lavoratore: " + iscrizione.getWorker().getFiscalcode() + " -- " + ex.getMessage(), false);
                ex.printStackTrace();

            }


        }
        logger.log(filename, "EndAnagraficheLav", "Termine importazione anagrafiche lavoratori", false);
    }

//
//    @Override
//    public List<DettaglioQuotaAssociativa> findCurrentIscrizioniForAzienda(long firmId) {
//
//        User u = ((User) sec.getLoggedUser());
//
//        //devo ricercare tutti i dettagli quote associative che hanno come idAzienda quella selezionata
//        Azienda a = azSvc.getAziendaById(((User) sec.getLoggedUser()).getLid(),firmId);
//        if (a == null)
//            return new ArrayList<>();
//
//
//        List<DettaglioQuotaAssociativa> d = dettRep.find(LoadRequest.build()
//
//                .filter("idAzienda", firmId)).getRows();
//
//        HashMap<Long, DettaglioQuotaAssociativa> g = new HashMap<>();
//
//        long roleId = ((User) sec.getLoggedUser()).retrieveUserRole().getLid();
//        long categoryId = ((User) sec.getLoggedUser()).getLid();
//        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : d) {
//            if (!g.containsKey(dettaglioQuotaAssociativa.getIdLavoratore()))
//                if (dettaglioQuotaAssociativa.checkIfValidForYear(Calendar.getInstance().get(Calendar.YEAR))){
//
//                    //se si tratta di categoria allora recupero solo gli iscritti per la categoria
//                    //altrimenti li recupero tutti
//                    if (roleId == 3 || roleId == 4 || roleId == 5){
//                        if (dettaglioQuotaAssociativa.getUserId() == categoryId)
//                            g.put(dettaglioQuotaAssociativa.getIdLavoratore(),dettaglioQuotaAssociativa);
//
//                    }else{
//                        //aggiungo senza verificare la categoria
//                        g.put(dettaglioQuotaAssociativa.getIdLavoratore(), dettaglioQuotaAssociativa);
//                    }
//
//
//                }
//
//        }
//
//
//
//        return new ArrayList<>(g.values());
//    }

    @Override
    public List<Iscritto> fastfindCurrentIscrizioniForAzienda(long firmId) throws Exception {
        List<Iscritto> d = rptIscrittiSvc.retrieveFastPerformaceQuoteIscritti(firmId);


        HashMap<Long, Iscritto> g = new HashMap<>();

        for (Iscritto dettaglioQuotaAssociativa : d) {
            if (!g.containsKey(dettaglioQuotaAssociativa.getLavoratoreId()))
                if (dettaglioQuotaAssociativa.checkIfValidForYear(Calendar.getInstance().get(Calendar.YEAR))){

                    //aggiungo senza verificare la categoria
                    g.put(dettaglioQuotaAssociativa.getLavoratoreId(), dettaglioQuotaAssociativa);
                }

        }



        return new ArrayList<>(g.values());

    }


    @Override
    public List<DettaglioQuotaAssociativa> retrieveQuote(UiIqaReportSearchParams params) throws Exception {

        Date dateDocFrom = (StringUtils.isEmpty(params.getDataDocfromMonthReport()) || StringUtils.isEmpty(params.getDataDocfromYearReport()))? null : createDate(1, params.getDataDocfromMonthReport(), params.getDataDocfromYearReport());
        Date dateDocTo = (StringUtils.isEmpty(params.getDataDoctoMonthReport()) || StringUtils.isEmpty(params.getDataDoctoYearReport()))? null : createDate(0, params.getDataDoctoMonthReport(), params.getDataDoctoYearReport());
        Date competenceFrom = (StringUtils.isEmpty(params.getCompetencefromMonthReport()) || StringUtils.isEmpty(params.getCompetencefromYearReport()))? null : createDate(1, params.getCompetencefromMonthReport(), params.getCompetencefromYearReport());
        Date competenceTo = (StringUtils.isEmpty(params.getCompetencetoMonthReport()) || StringUtils.isEmpty(params.getCompetencetoYearReport()))? null : createDate(0, params.getCompetencetoMonthReport(), params.getCompetencetoYearReport());
        String province = params.getProvince();
        String regione = params.getCompany();
        String settore =  params.getSector();
        String azienda = params.getFirm();


        //posso adesso fare la query

        LoadRequest req = LoadRequest.build();

        if (dateDocFrom != null) {
            Filter f1 = new Filter("dataRegistrazione", dateDocFrom, Filter.GTE);
            req.getFilters().add(f1);
        }

        if (dateDocTo != null) {
            Filter f2 = new Filter("dataRegistrazione", dateDocTo, Filter.LTE);
            req.getFilters().add(f2);
        }




        if (competenceFrom != null && competenceTo != null) {
            if (!competenceFrom.before(competenceTo)){
                Date dd = competenceTo;
                competenceTo = competenceFrom;
                competenceFrom = dd;
            }
        }

        req.getFilters().add(new Filter("dataInizio", competenceTo, Filter.LTE));
        req.getFilters().add(new Filter("dataFine", competenceFrom, Filter.GTE));


      /*  if (!StringUtils.isEmpty(province)){
            Filter f5 = new Filter("provincia", province, Filter.LIKE);
            req.getFilters().add(f5);
        }


        Categoria s = settoreRepository.find(LoadRequest.build().filter("description", settore)).findFirst().orElse(null);
        if (s == null)
            throw new Exception("Settore nullo");


        Filter f7 = new Filter("settore", settore, Filter.EQ);
        req.getFilters().add(f7);

*/

        if (!StringUtils.isEmpty(regione)){

            Filter fc = new Filter("companyId", Long.parseLong(regione), Filter.EQ);
            req.getFilters().add(fc);
        }


        if (!StringUtils.isEmpty(province)){
            Province p = geo.getProvinceById(Integer.parseInt(province));
            if (p != null){
                Filter f1 = new Filter("provincia", p.getDescription(), Filter.LIKE);
                req.getFilters().add(f1);
            }

        }


        if (!StringUtils.isEmpty(settore)){
            Categoria s = settoreRepository.get(params.getSector()).orElse(null);

            if (s != null){
                Filter f2 = new Filter("settore", s.getDescription(), Filter.LIKE);
                req.getFilters().add(f2);
            }
        }


        if (!StringUtils.isEmpty(azienda)){
            Filter f4 = new Filter("idAzienda", Long.parseLong(azienda), Filter.EQ);
            req.getFilters().add(f4);
        }



       /* if (!StringUtils.isEmpty(azienda)) {
            Filter f9 = new Filter("idAzienda", azienda, Filter.EQ);
            req.getFilters().add(f9);
        }*/





        List<DettaglioQuotaAssociativa> quote = dettRep.find(req).getRows();

        return quote;
    }

    @Override
    public void deleteQuota(long idRiepilogoQuota) {

        //ottengo l quota
        RiepilogoQuoteAssociative r = quoteRep.get(idRiepilogoQuota).orElse(null);


        if (r != null){
            //procedo alla cancellazione
            quoteRep.executeCommand(new Command() {
                @Override
                public void execute() {
                    Session s = quoteRep.getSession();
                    Transaction tx = s.beginTransaction();
                    try {
                        //rimuovo tutti i dettagli per una determinata quota
                        s.createSQLQuery(String.format("Delete from fenealweb_dettaglioquote where idRiepilogoQuotaAssociativa = %d", idRiepilogoQuota)).executeUpdate();

                        s.createSQLQuery(String.format("Delete from fenealweb_riepilogoQuoteAssociative where id = %d", idRiepilogoQuota)).executeUpdate();



                        tx.commit();

                    } catch (Exception ex) {
                        ex.printStackTrace();
                        tx.rollback();
                    } finally {
                        s.close();
                    }
                }
            });
        }


    }

    @Override
    public String creaQuotemassive(RiepilogoQuotaDTO dto) throws Exception {

        importQuote.doImportQuote(dto);

        return "";
    }


    @Override
    public List<DettaglioQuotaAssociativa> getStoricoVersamenti(long workerId) {

        return  dettRep.find(LoadRequest.build()
                .filter("idLavoratore", workerId)).getRows();



    }


    public List<DettaglioQuotaAssociativa> getDettagliQuota(long idRiepilogoQuota, Long idWorker) {

        LoadRequest req = LoadRequest.build()
                .filter("idRiepilogoQuotaAssociativa", idRiepilogoQuota)
                .filter("idLavoratore", idWorker);
        return dettRep.find(req).getRows();

    }

    @Override
    public RiepilogoQuoteAssociative getRiepilogoQuotaById(long id) {
        RiepilogoQuoteAssociative r = quoteRep.get(id).orElse(null);

        return r;
    }

//    @Override
//    public RiepilogoQuoteAssociative creaQuoteManuali(RiepilogoQuotaDTO dto) throws Exception {
//        return importQuote.doImportQuoteManuali(dto);
//    }
//
//    @Override
//    public RiepilogoQuoteAssociative creaQuoteBreviMano(RiepilogoQuotaDTO dto) throws Exception {
//        return importQuote.doImportQuoteBreviMano(dto);
//    }

    @Override
    public DettaglioQuotaAssociativa addItem(RiepilogoQuoteAssociative riepilogoQuoteAssociative, DettaglioQuotaDTO dettaglioQuotaDTO) throws Exception {


        DettaglioQuotaAssociativa a = importHelper.convertDettaglioFromDto(riepilogoQuoteAssociative, dettaglioQuotaDTO);

        dettRep.save(a);

        // Aggiorno il totale quota padre
        List<DettaglioQuotaAssociativa> dettagliQuote = dettRep.find(LoadRequest.build().filter("idRiepilogoQuotaAssociativa", riepilogoQuoteAssociative.getId())).getRows();
        Double totalAmount = 0.0;
        for (DettaglioQuotaAssociativa dett : dettagliQuote) {
            totalAmount += dett.getQuota();
        }

        riepilogoQuoteAssociative.setTotalAmount(totalAmount);
        quoteRep.save(riepilogoQuoteAssociative);

        //una volta salvata creo e attivo la delega

        importDataDeleghe.activateDelega(riepilogoQuoteAssociative,a,true,true);


        return a;

    }

    @Override
    public void deleteItem(long quotaId, long itemId) {
        //recupero la quota per verificare che non si tratti di id contraffatti
        RiepilogoQuoteAssociative r = quoteRep.get(quotaId).orElse(null);
        if (r == null)
            return;
        DettaglioQuotaAssociativa a = dettRep.get(itemId).orElse(null);
        if (a == null)
            return;

        dettRep.delete(itemId);

        // Aggiorno il totale quota padre
        List<DettaglioQuotaAssociativa> dettagliQuote = dettRep.find(LoadRequest.build().filter("idRiepilogoQuotaAssociativa", quotaId)).getRows();
        Double totalAmount = 0.0;
        for (DettaglioQuotaAssociativa dett : dettagliQuote) {
            totalAmount += dett.getQuota();
        }

        r.setTotalAmount(totalAmount);
        quoteRep.save(r);
    }

    @Override
    public void updateItem(long quotaId, long itemId, UpdatableDettaglioQuota updatedData) {
        RiepilogoQuoteAssociative r = quoteRep.get(quotaId).orElse(null);
        if (r == null)
            return;
        DettaglioQuotaAssociativa a = dettRep.get(itemId).orElse(null);
        if (a == null)
            return;

        if (updatedData.getQuota() != null)
            a.setQuota(updatedData.getQuota());
        if (updatedData.getLivello() != null)
            a.setLivello(updatedData.getLivello());
        if (updatedData.getNote() != null)
            a.setNote(updatedData.getNote());
        if (updatedData.getDataInizio() != null)
            a.setDataInizio(updatedData.getDataInizio());
        if (updatedData.getDataFine() != null)
            a.setDataFine(updatedData.getDataFine());

        dettRep.save(a);

        // Aggiorno totale quota padre se ho modificato importo quota
        if (updatedData.getQuota() != null) {
            List<DettaglioQuotaAssociativa> dettagliQuote = dettRep.find(LoadRequest.build().filter("idRiepilogoQuotaAssociativa", quotaId)).getRows();
            Double totalAmount = 0.0;
            for (DettaglioQuotaAssociativa dett : dettagliQuote) {
                totalAmount += dett.getQuota();
            }

            r.setTotalAmount(totalAmount);
            quoteRep.save(r);
        }

}

    @Override
    public void duplicaQuota(long quotaId, Date inizio, Date fine) {
        RiepilogoQuoteAssociative quotaDaDuplicare = quoteRep.get(quotaId).orElse(null);
        List<DettaglioQuotaAssociativa> quoteDaDuplicare = this.getDettagliQuota(quotaId,null);

        if (inizio.getTime() > fine.getTime()){
            Date temp = fine;
            fine = inizio;
            inizio = temp;

        }
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        String competenza = String.format("%s - %s",f.format(inizio), f.format(fine));
        //adesso rimuovo tutti gli id
        quotaDaDuplicare.setId(null);
        quotaDaDuplicare.setCompetenza(competenza);
        quotaDaDuplicare.setImportedLogFilePath("");
        quotaDaDuplicare.setOriginalFileServerPath("");
        quotaDaDuplicare.setXmlFileServerPath("");

        Date d = new Date();
        quotaDaDuplicare.setDataRegistrazione(d);
        quotaDaDuplicare.setDataDocumento(inizio);

        quoteRep.save(quotaDaDuplicare);

        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : quoteDaDuplicare) {
            dettaglioQuotaAssociativa.setId(null);
            dettaglioQuotaAssociativa.setIdRiepilogoQuotaAssociativa(-1);
            dettaglioQuotaAssociativa.setDataInizio(inizio);
            dettaglioQuotaAssociativa.setDataFine(fine);
            dettaglioQuotaAssociativa.setDataRegistrazione(d);
            dettaglioQuotaAssociativa.setDataDocumento(inizio);
            dettaglioQuotaAssociativa.setIdRiepilogoQuotaAssociativa(quotaDaDuplicare.getLid());
            dettRep.save(dettaglioQuotaAssociativa);
        }

    }

    @Override
    public void modifyCompetenceQuotaItems(long quotaId, Date inizio, Date fine) {
        RiepilogoQuoteAssociative quotaToModify = quoteRep.get(quotaId).orElse(null);
        List<DettaglioQuotaAssociativa> quoteDetailsToModify = this.getDettagliQuota(quotaId, null);

        if (inizio.getTime() > fine.getTime()){
            Date temp = fine;
            fine = inizio;
            inizio = temp;
        }

        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");

        String competenza = String.format("%s - %s",f.format(inizio), f.format(fine));
        quotaToModify.setCompetenza(competenza);
        quoteRep.save(quotaToModify);

        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : quoteDetailsToModify) {
            dettaglioQuotaAssociativa.setDataInizio(inizio);
            dettaglioQuotaAssociativa.setDataFine(fine);
            dettRep.save(dettaglioQuotaAssociativa);
        }

    }

    @Override
    public List<DelegaPerCreazioneQuota> searchAziendeByName(final String name) {



        if (StringUtils.isEmpty(name)){
            return new ArrayList<>();
        }

        final Box b = new Box();
            //procedo alla cancellazione
        quoteRep.executeCommand(new Command() {
            @Override
            public void execute() {
                Session s = quoteRep.getSession();
                Transaction tx = s.beginTransaction();
                try {
                    //rimuovo tutti i dettagli per una determinata quota
                   String query = "select a.id as idAzienda, \n" +
                           "a.description as azienda,\n" +
                           "a.companyId as idRegione, c.description as regione,\n" +
                           "d.provinceId as idProvincia,\n" +
                           "p.DESCRIZIONE as provincia\n" +
                           "from fenealweb_azienda a inner join fenealweb_company  c \n" +
                           "on c.id = a.companyId\n" +
                           "left join fenealweb_delega d \n" +
                           "on a.id = d.workerCompanyId\n" +
                           "left join tb_provincie p \n" +
                           "on p.ID = d.provinceId\n" +
                           "where a.description like '%" + name +  "%' group by azienda, regione, provincia";


                    SQLQuery q = s.createSQLQuery(query);
                    q.setResultTransformer(Transformers.aliasToBean(DelegaPerCreazioneQuota.class));


                    b.setValue(q.list());


                    tx.commit();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    tx.rollback();
                } finally {
                    s.close();
                }
            }
        });

        return (List<DelegaPerCreazioneQuota>)b.getValue();
    }

    @Override
    public String bonificaQuote(String note, long[] quoteIds) {


        if (StringUtils.isEmpty(note)){
            note = String.format("Bonifico del %s", new Date().toString());
        }

        final String noteFinali = note;

        final Box b = new Box();
        //procedo alla cancellazione
        quoteRep.executeCommand(new Command() {
            @Override
            public void execute() {
                Session s = quoteRep.getSession();
                Transaction tx = s.beginTransaction();
                try {

                    //per prima cosa creo il bonifico
                    Bonifico bonifico = new Bonifico();
                    bonifico.setData(new Date());
                    bonifico.setNote(noteFinali);
                    bonifico.setUser(((User) sec.getLoggedUser()));
                    bonRep.save(bonifico);

                    int quoteBonificate = 0;
                    for (long quoteId : quoteIds) {

                        //recupero la quota
                        //verifico se esiste ed è bonificata
                        //e se non lo è la aggiorno
                        DettaglioQuotaAssociativa dett = dettRep.get(quoteId).orElse(null);
                        if (dett != null)
                            if (dett.getBonificoId()== null)
                            {
                                dett.setBonificoId(bonifico.getLid());
                                dett.setNoteBonifico(bonifico.getNote());

                                dett.setDataBonifico(new Date());
                                dettRep.save(dett);
                                quoteBonificate++;
                            }


                    }

                    if (quoteBonificate == 0){
                        bonRep.delete(bonifico.getLid());
                        throw new Exception("Nessuna quota creata! Le quote erano gia state bonificate");
                    }


                    tx.commit();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    tx.rollback();
                    b.setValue(ex.getMessage());
                } finally {
                    s.close();
                }
            }
        });

        if (b.getValue() != null)
            return b.getValue().toString();
        return "OK";



    }

    @Override
    public String deleteBonifico(long idBonifico) throws Exception {


        Role r = ((Role) ((User) sec.getLoggedUser()).getRoles().get(0));
        if (r.getIid() != 5)
            throw new Exception("Permission denied");

        final Box b = new Box();
        //procedo alla cancellazione
        quoteRep.executeCommand(new Command() {
            @Override
            public void execute() {
                Session s = quoteRep.getSession();
                Transaction tx = s.beginTransaction();
                try {



                   String query = "update fenealweb_dettaglioquote set bonificoId = null, noteBonifico = null, dataBonifico = null where bonificoId = " + String.valueOf(idBonifico);
                   s.createSQLQuery(query).executeUpdate();

                    //poi elimino il bonific
                    bonRep.delete(idBonifico);

                    tx.commit();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    tx.rollback();
                    b.setValue(ex.getMessage());
                } finally {
                    s.close();
                }
            }
        });

        if (b.getValue() != null)
            return b.getValue().toString();
        return "OK";

    }


    /* Crea oggetto java Date dato il mese e l'anno */
    private Date createDate(int day, String month, String year) {

        if (month == null || year == null)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        if (day == 0)
            calendar.set(Calendar.MONTH, Integer.valueOf(month));
        else
            calendar.set(Calendar.MONTH, Integer.valueOf(month) - 1);

        calendar.set(Calendar.YEAR, Integer.valueOf(year));
        Date date = calendar.getTime();

        return date;
    }

}
