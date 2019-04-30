package applica.feneal.services.impl.lavoratori;

import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoreLastVersionRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.core.lavoratori.ListaLavoroRepository;
import applica.feneal.domain.data.core.quote.DettaglioQuoteAssociativeRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.lavoratori.IscrittoAnnoInCorso;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.LavoratoreLastVersion;
import applica.feneal.domain.model.core.lavoratori.search.LavoratoreSearchParams;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.dbnazionale.Iscrizione;
import applica.feneal.domain.validation.LavoratoreValidator;
import applica.feneal.domain.validation.LavoratoreValidatorWithImpersonation;
import applica.feneal.services.DelegheService;
import applica.feneal.services.LavoratoreService;
import applica.feneal.services.ReportIscrittiService;
import applica.framework.Conjunction;
import applica.framework.Disjunction;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fgran on 06/04/2016.
 */
@Service
public class LavoratoriServiceImpl implements LavoratoreService {

    @Autowired
    private CategoriaRepository sectRep;



    @Autowired
    private LavoratoriRepository lavRep;

    @Autowired
    private LavoratoreValidatorWithImpersonation lavValWithImpersonation;

    @Autowired
    private LavoratoreValidator lavVal;

    @Autowired
    private Security sec;

    @Autowired
    private DelegheService delsvc;

    @Autowired
    private LavoratoreLastVersionRepository lavLastVersionRep;

    @Autowired
    private DettaglioQuoteAssociativeRepository quoRep;

    @Autowired
    private ListaLavoroRepository liRep;

//    @Autowired
//    private UtenteDBNazioneRepository utBRep;

    @Autowired
    private ReportIscrittiService reportIscrittiService;


    @Autowired
    private AziendeRepository azRep;

    @Override
    public Lavoratore getLavoratoreById(long loggedUserId, long lavId) {
        return lavRep.get(lavId).orElse(null);
    }


    private void checkUserPermissions(long lavId) throws Exception {

        User u = ((User) sec.getLoggedUser());
        if (u.getCompany() == null)
            throw new Exception("Un amministratore non può creare anagrafiche");
        if (u.getCompany().getTipoConfederazione() != 1){
            //se non sono in un contesto regionale non posso creare anagrafiche
            throw new Exception("Le anagrafiche possono essere salvate solamente da operatori e segretari regionali");
        }


        if (lavId == 0)
            return;

        Lavoratore l = this.getLavoratoreById(((User) sec.getLoggedUser()).getLid(),lavId);
        if (l == null)
            return;

        if (l.getLid() >0){
            if (l.getCompanyId() != u.getCompany().getLid())
                throw new Exception(String.format("Operazione non ammessa. Solo gli utenti dell'area %s possono compiere operazioni su questa anagrafica", u.getCompany().getDescription()));

        }
    }

    @Override
    public void saveOrUpdate(long loggedUserId, Lavoratore lav) throws Exception {

        String error = lavVal.validate(lav);
        //verifico che l'utente abbia i permessi per modificare creare o cancellare l'entità
        checkUserPermissions(lav.getLid());

        if (org.apache.commons.lang.StringUtils.isEmpty(error))
        {
            boolean isCreation = false;
            if (lav.getLid() == 0)
            {
                isCreation = true;
                lav.setCreationDate(new Date());
                lav.setCreatorUser(((User) sec.getLoggedUser()).getCompleteName());
            }else{
                isCreation = false;
                lav.setUpdateUser(((User) sec.getLoggedUser()).getCompleteName());
                lav.setLastUpdateDate(new Date());
            }
            lavRep.save(lav);

            //una volta salvato o aggiornato
            //creo un oggetto last version copia del lavoratore
            try{
                LavoratoreLastVersion last = new LavoratoreLastVersion(lav);
                lavLastVersionRep.save(last);
            }catch (Exception ex){
                throw new Exception("Errore nel salvataggio lavoratore last version: " + ex.getMessage());
            }




            return;
        }

        throw new Exception(error);
    }

    @Override
    public void saveOrUpdateWithImpersonation(long loggedUserId, Lavoratore lav) throws Exception {
        String error = lavValWithImpersonation.validate(lav);

        if (org.apache.commons.lang.StringUtils.isEmpty(error))
        {
            lavRep.save(lav);
            return;
        }

        throw new Exception(error);
    }

    @Override
    public void delete(long loggedUserId, long idLav) throws Exception {
        User u = ((User) sec.getLoggedUser());
        //devo eseguire "a mano alcuni controlli di integrità referenziale"
        //devo verificare che non ci siano per l'utente le seguenti entità

        //Quote associative (dettaglio quota)
        //lista lavoro

        //cerco se ci sono quote riferite all'id del lavoratore
        LoadRequest req = LoadRequest.build().filter("idLavoratore", idLav);
        List<DettaglioQuotaAssociativa> l = quoRep.find(req).getRows();
        if (l.size() > 0 )
            throw new Exception("Sono presenti quote associative");


        //devo adesso eseguire una query direttamente nella tabella di associazione
        //delle lista di lalvoro
        Long numOfOccurrenceInListeLavoro = liRep.getNumberOfListForWorker(idLav);
        if (numOfOccurrenceInListeLavoro > 0)
            throw new Exception("Il lavoratore è presente in una o più liste di lavoro");

        //verifico che l'utente abbia i permessi per modificare creare o cancellare l'entità

        checkUserPermissions(idLav);


        lavRep.delete(idLav);
    }

    @Override
    public List<Lavoratore> findLocalLavoratori(long loggedUserId, LavoratoreSearchParams params) {

//        if (params.isEmpty())
//            return new ArrayList<>();

      //  if (params.getPage() == null || params.getPage() == 0)
            params.setPage(1);

        int rowsPerPage = 200;

        LoadRequest req = LoadRequest.build();

        if (StringUtils.hasLength(params.getNamesurname())){
            //ricerca dal tasto in alto nella barra di ricerca globale

            Disjunction or = new Disjunction();

            Filter f = new Filter();
            f.setProperty("namesurname");
            f.setType(Filter.LIKE);
            f.setValue(params.getNamesurname());


            Filter f1 = new Filter();
            f1.setProperty("surnamename");
            f1.setType(Filter.LIKE);
            f1.setValue(params.getNamesurname());

            or.setChildren(Arrays.asList(f,f1));

            req.getFilters().add(or);



        }else{
            Conjunction and = new Conjunction();

            List<Filter> filters = new ArrayList<>();


            Filter f1 = new Filter();
            f1.setProperty("name");
            f1.setType(Filter.LIKE);
            f1.setValue(params.getName());
            filters.add(f1);


            Filter f2 = new Filter();
            f2.setProperty("surname");
            f2.setType(Filter.LIKE);
            f2.setValue(params.getSurname());
            filters.add(f2);

            Filter f3 = new Filter();
            f3.setProperty("fiscalcode");
            f3.setType(Filter.LIKE);
            f3.setValue(params.getFiscalcode());
            filters.add(f3);


            if (!StringUtils.isEmpty(params.getCaratteristica3()))
            {
                Filter f4 = new Filter();
                f4.setProperty("attribuzione3");
                f4.setType(Filter.EQ);
                f4.setValue(params.getCaratteristica3());
                filters.add(f4);
            }

            SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
            if (!StringUtils.isEmpty(params.getBirthDateFrom())){
                try{
                    Date d1 = f.parse(params.getBirthDateFrom());
                    Filter f4 = new Filter();
                    f4.setProperty("birthDate");
                    f4.setType(Filter.GTE);
                    f4.setValue(d1);
                    filters.add(f4);
                }catch(Exception e){

                }
            }

            if (!StringUtils.isEmpty(params.getBirthDateTo())){
                try{
                    Date d1 = f.parse(params.getBirthDateTo());
                    Filter f4 = new Filter();
                    f4.setProperty("birthDate");
                    f4.setType(Filter.LTE);
                    f4.setValue(d1);
                    filters.add(f4);
                }catch(Exception e){

                }
            }


            and.setChildren(filters);

            req.getFilters().add(and);
        }



        if (params.getCompanyId() != null && params.getCompanyId() > 0){
            Filter company = new Filter();
            company.setProperty("companyId");
            company.setType(Filter.EQ);
            company.setValue(params.getCompanyId());
            req.getFilters().add(company);
        }





        req.setPage(params.getPage());
        req.setRowsPerPage(rowsPerPage);




        return lavRep.find(req).getRows();
    }

//    @Override
//    public List<UtenteDbNazionale> findRemoteLavoratori(long loggedUserId, LavoratoreSearchParams params) throws ParseException {
//
//
//        //if (params.getPage() == null || params.getPage() == 0)
//        params.setPage(1);
//
//        int rowsPerPage = 200;
//
//        LoadRequest req = LoadRequest.build();
//
//
//        Conjunction and = new Conjunction();
//
//        List<Filter> filters = new ArrayList<>();
//
//        if (!StringUtils.isEmpty(params.getSurname())){
//            Filter f1 = new Filter();
//            f1.setProperty("cognome");
//            f1.setType(Filter.LIKE);
//            f1.setValue(params.getSurname());
//            filters.add(f1);
//        }
//
//        if (!StringUtils.isEmpty(params.getName())){
//            Filter f2 = new Filter();
//            f2.setProperty("nome");
//            f2.setType(Filter.LIKE);
//            f2.setValue(params.getName());
//            filters.add(f2);
//        }
//
//        if (!StringUtils.isEmpty(params.getFiscalcode())){
//            Filter f8 = new Filter();
//            f8.setProperty("codiceFiscale");
//            f8.setType(Filter.LIKE);
//            f8.setValue(params.getFiscalcode());
//            filters.add(f8);
//        }
//
//        if (!StringUtils.isEmpty(params.getSex())){
//
//            Filter f3 = new Filter();
//            f3.setProperty("sesso");
//            f3.setType(Filter.EQ);
//            f3.setValue(params.getSex());
//            filters.add(f3);
//        }
//
//
//        if (!StringUtils.isEmpty(params.getBirthDate())) {
//            DateFormat formatter = new SimpleDateFormat("dd/MM/yy");
//            Date birthDate = null;
//            try{
//                birthDate = formatter.parse(params.getBirthDate());
//            }catch(Exception e){
//                birthDate = null;
//            }
//           if (birthDate != null){
//               Filter f4 = new Filter();
//               f4.setProperty("dataNascita");
//               f4.setType(Filter.EQ);
//               f4.setValue(birthDate);
//               filters.add(f4);
//           }
//
//        }
//
//        if (!StringUtils.isEmpty(params.getNationality())) {
//            Filter f5 = new Filter();
//            f5.setProperty("id_Nazione");
//            f5.setType(Filter.EQ);
//            f5.setValue(Integer.parseInt(params.getNationality()));
//            filters.add(f5);
//        }
//
//        if (!StringUtils.isEmpty(params.getLivingProvince())) {
//            Filter f6 = new Filter();
//            f6.setProperty("id_Provincia_Residenza");
//            f6.setType(Filter.EQ);
//            f6.setValue(Integer.parseInt(params.getLivingProvince()));
//            filters.add(f6);
//        }
//
//        if (!StringUtils.isEmpty(params.getLivingCity())) {
//            Filter f7 = new Filter();
//            f7.setProperty("id_Comune_Residenza");
//            f7.setType(Filter.EQ);
//            f7.setValue(Integer.parseInt(params.getLivingCity()));
//            filters.add(f7);
//        }
//
//
//        and.setChildren(filters);
//
//        req.getFilters().add(and);
//
//
//        req.setPage(params.getPage());
//        req.setRowsPerPage(rowsPerPage);
//        //req.setSorts(Arrays.asList(new Sort("cognome", false)));
//        List<UtenteDbNazionale> utenti = utenteNazRep.find(req).getRows();
//
//        for (UtenteDbNazionale utenteDbNazionale : utenti) {
//            utenteDbNazionale.setIscrizioni(reportIscrittiService.findIscrizioniByWorkerId(Long.parseLong(String.valueOf(utenteDbNazionale.getId()))));
//            utenteDbNazionale.setNumIscrizioni(utenteDbNazionale.getIscrizioni().size());
//        }
//
//        return utenti;
//    }
//


    @Override
    public Lavoratore findLavoratoreByFiscalCode(String fiscalcode, long companyId) {

        if (StringUtils.isEmpty(fiscalcode))
            return null;
        User u = ((User) sec.getLoggedUser());
        if (u.getCompany().getTipoConfederazione() == 1) {

            //se sono regionale non cè problema perchè il filtro sulla company id è fatto automaticamente
            return lavRep.searchLavoratoreForCompany(u.getCompany().getLid(), fiscalcode);
        }

        return lavRep.searchLavoratoreForCompany(companyId, fiscalcode);

    }





//    @Override
//    public Lavoratore getLavoratoreByFiscalCodeOrCreateItIfNotexist(String fiscalCode) throws Exception {
//
//        Lavoratore l = this.findLavoratoreByFiscalCode(fiscalCode);
//
//        if (l == null) {
//
//            //lo recupero dal database nazionale
//            UtenteDbNazionale n = this.findRemoteLavoratoreByFiscalCode(fiscalCode);
//            if (n== null)
//                return null;
//
//
//
//            //se cè lo salvo nel db
//            Lavoratore l1 = new Lavoratore(n);
//            saveOrUpdate(((User) sec.getLoggedUser()).getLid(), l1);
//            return l1;
//        }
//
//        return l;
//    }




//    private Lavoratore superPerformSearch(String fiscalCode) {
//        final Box box = new Box();
//        final String fiscal = fiscalCode;
//
//        isRep.executeCommand(new Command() {
//            @Override
//            public void execute() {
//                Session s = isRep.getSession();
//                Transaction tx = null;
//
//                try{
//
//                    tx = s.beginTransaction();
//
//                    String query = "Select id ,fiscalCode from fenealweb_lavoratore where fiscalCode = '" + fiscal + "' and companyId = " + ((User) sec.getLoggedUser()).getCompany().getLid();
//                    List<Object[]> objects = s.createSQLQuery(query)
//                            .addScalar("ID")
//                            .addScalar("fiscalCode")
//                            .list();
//
//                    tx.commit();
//
//                    List<Lavoratore> a = new ArrayList<>();
//                    for (Object[] object : objects) {
//
//                        Lavoratore v = new Lavoratore();
//                        v.setId((BigInteger)object[0]);
//                        v.setFiscalcode((String)object[1]);
//
//                        a.add(v);
//                    }
//
//
//                    box.setValue(a);
//
//
//                }
//                catch(Exception e){
//                    e.printStackTrace();
//                    tx.rollback();
//                }
//                finally{
//                    s.close();
//
//                }
//            }
//
//
//        });


//
//        List<Lavoratore> l = ((List<Lavoratore>) box.getValue());
//
//        if (l.size() > 0)
//            return l.get(0);
//
//        return null;
//
//    }

//    @Override
//    public Lavoratore getLavoratoreByRemoteIdOrCreateItIfNotexist(int workerId) throws Exception {
//
//
//        UtenteDbNazionale ut = utBRep.get(workerId).orElse(null);
//        if (ut == null)
//            return null;
//
//        return getLavoratoreByFiscalCodeOrCreateItIfNotexist(ut.getCodiceFiscale());
//
//    }


    @Override
    public IscrittoAnnoInCorso checkIfIscrittoAnnoInCorso(long workerId) throws Exception {

        //devo verificare se se negli ultimi due semestri risulta iscritto
        //se siamo nel periodo da luglio a dicembre devo verificare che ci sia una quota per il secondo semestre dell'anno precedente
        //o del primo dell'anno in corso
        //se siamo nel periodo da gennaio al 30 giugno devo verifcre che ci sia una quota nelll'anno precedetne


        Calendar c = Calendar.getInstance();

        int year = c.get(Calendar.YEAR);


        //recuoero il lavoratore
        Lavoratore lav = lavRep.get(workerId).orElse(null);
        if (lav == null)
            throw new Exception("Lavoratore nullo");

        //recupero le iscrizioni per il lavoratore
        List<Iscrizione> iscrizioni = reportIscrittiService.findIscrizioniByWorkerId(lav.getLid());
        if (iscrizioni.size() == 0)
            return new IscrittoAnnoInCorso();


        //pero prima di ricorrere allo stratagemma dei periodi verifico se cè una iscrizione per l'anno corrente
        //nel primo o secondo semestre

        Iscrizione iscrizione = null;

        //se si tratta di utenti di categoria
        //l'iscrizione la valuto sulla prorpia categoria
        //altrimenti ne prendo la prima disponibile

        for (Iscrizione iscr : iscrizioni) {
            if (iscr.getAnno() == year ){
                iscrizione = iscr;
                break;
            }
        }



        if (iscrizione == null)
            return new IscrittoAnnoInCorso();



        IscrittoAnnoInCorso result = new IscrittoAnnoInCorso();
        result.setIscritto(true);
        result.setSettore(iscrizione.getSettore());
        result.setCategoria(iscrizione.getNomeProvincia());

        SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
        result.setPeriodo(ff.format(iscrizione.getDataInizio()) + "-" +  ff.format(iscrizione.getDataFine()));
        result.setAzienda(iscrizione.getAzienda());
        //result.setEnte(iscrizione.getEnte());

        result.setDelega(delsvc.hasWorkerDelegaAttivaOAccettata( lav.getLid(),iscrizione.getAzienda(), iscrizione.getNomeProvincia()));




        return result;
    }




}
