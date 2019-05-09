package applica.feneal.services.impl.deleghe;

import applica.feneal.domain.data.Command;
import applica.feneal.domain.data.UsersRepository;
import applica.feneal.domain.data.core.configuration.CategoriaRepository;
import applica.feneal.domain.data.core.deleghe.DelegheRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.core.rappresentanza.DelegaBilateralitaRepository;
import applica.feneal.domain.data.core.rappresentanza.DelegaUncRepository;
import applica.feneal.domain.model.Filters;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.deleghe.DelegaState;
import applica.feneal.domain.model.core.deleghe.states.*;
import applica.feneal.domain.model.core.rappresentanza.DelegaBilateralita;
import applica.feneal.domain.model.core.rappresentanza.DelegaUnc;
import applica.feneal.domain.model.core.servizi.search.UiQuoteHeaderParams;
import applica.feneal.domain.model.setting.CausaleRevoca;
import applica.feneal.domain.model.setting.option.ApplicationOptions;
import applica.feneal.domain.validation.DelegaValidator;
import applica.feneal.services.DelegheService;
import applica.framework.Disjunction;
import applica.framework.Filter;
import applica.framework.LoadRequest;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fgran on 05/04/2016.
 */
@Component
public class DelegaServiceImpl implements DelegheService {

    @Autowired
    private CategoriaRepository sectRep;

    @Autowired
    private DelegheRepository rep;

    @Autowired
    private Security sec;

    @Autowired
    private DelegaValidator delegaValidator;

    @Autowired
    private UsersRepository urep;

    @Autowired
    private DelegaBilateralitaRepository bilRep;

    @Autowired
    private DelegaUncRepository uncRep;



    private DelegaState constructStateManager(Delega delega) {
        DelegaState stateManager = null;
        if (delega.getState() != null) {
            switch (delega.getState()) {
                case Delega.state_accepted:
                    stateManager = new AcceptState();
                    break;
                case Delega.state_subscribe:
                    stateManager = new SubscribeState();
                    break;
                case Delega.state_sent:
                    stateManager = new SendState();
                    break;
                case Delega.state_activated:
                    stateManager = new ActivateState();
                    break;
                case Delega.state_cancelled:
                    stateManager = new CancelledState();
                    break;
                case Delega.state_revoked:
                    stateManager = new RevokedState();
                    break;
            }
        }
        return stateManager;

    }


    @Override
    public void accettaDeleghe(List<Delega> deleghe, Date date) throws Exception {
        for (Delega delega : deleghe) {
            acceptDelega(date, delega);
        }
    }

    @Override
    public void inoltraDeleghe(List<Delega> deleghe, Date date) throws Exception {
        for (Delega delega : deleghe) {
            sendDelega(date, delega);
        }
    }

    @Override
    public void subscribeDelega(Delega del) throws Exception {

        if (canCancelAfterAccept(del)){
            User u = ((User) sec.getLoggedUser());

            DelegaState stateManager = constructStateManager(del);

            stateManager.subscribeDelega(del);

            checkCategory(del);

            rep.save(del);


            doAccept(del);
            return;
        }

       throw new Exception("Delega gia esistente e creata da un altro utente");


    }

//    @Override
//    public void subscribeDelegaWithOmpersonation(Delega del, User owner) throws Exception {
//       //qui  non cè il can cancelaafteraccept perchè sono sicuro che l'utente che crea
//       // la delega è un regionale...
//       //sottoscrivo
//        DelegaState stateManager = constructStateManager(del);
//        stateManager.subscribeDelega(del);
//        rep.save(del);
//       //accetto la delega
//
//        stateManager = constructStateManager(del);
//        stateManager.acceptDelega(del.getDocumentDate(),del);
//        rep.save(del);
//
//
//        //annullo eventuali deleghe da analoghe
//        cancelAfterAccept(del, owner);
//    }

    private boolean canCancelAfterAccept(Delega del) {
        User u = ((User) sec.getLoggedUser());
        Role role= ((Role) u.getRoles().get(0));
        List<Delega> d = rep.getDelegheByLavoratore(del.getWorker().getLid());
        for (Delega item : d) {
            //cerco tutte le deleghe attive o accettate
            //per la stessa provincia
            //per lo stesso settore
            //e per la stessa azienda
            if (item.checkIfActivateOrAccepted()) {

                if (del.getProvince().getIid() == item.getProvince().getIid()) {

                    //se sono la stessa azienda..
                    if (del.getWorkerCompany().getLid() == item.getWorkerCompany().getLid())

                        //in questo caso esiste una delega che
                        //sarà annullata dalla creazione della delega corrente(del)
                        //l'annullamento sarà possibile solo se l'utenteha un ruolo
                        // regionale oppure l'utente corrente è l'owner della delega che sarà annullata

                        if (!(item.getUserId() == u.getLid() || role.getLid() == 3)){
                            return false;
                        }


                }
            }

        }



        return true;
    }

    private void doAccept(Delega del) throws Exception {
        DelegaState stateManager;
        stateManager = constructStateManager(del);
        stateManager.acceptDelega(del.getDocumentDate(),del);

        checkCategory(del);


        rep.save(del);

        cancelAfterAccept(del, ((User) sec.getLoggedUser()));
    }


    @Override
    public void sendDelega(Date date, Delega del) throws Exception {
        DelegaState stateManager = constructStateManager(del);

        stateManager.sendDelega(date, del);

        checkCategory(del);

        rep.save(del);
    }


    @Override
    public void acceptDelega(Date date, Delega del) throws Exception {
        DelegaState stateManager = constructStateManager(del);

        stateManager.acceptDelega(date, del);

        checkCategory(del);

        rep.save(del);

        //dopo aver accettato la delga devo recuperare tutte le deleghe attive o accettate e annullarle
        cancelAfterAccept(del, ((User) sec.getLoggedUser()));


    }

    private void cancelAfterAccept(Delega del , User user) {
        User u = user;
        List<Delega> d = rep.getDelegheByLavoratore(del.getWorker().getLid());
        for (Delega item : d) {
                DelegaState sm = constructStateManager(item);
                if (item.checkIfActivateOrAccepted()) {

                    //se la delega è accettata o attiva devo verificare per poterla annullare se si stratta
                    //dello stesso settore, azienda e provincia

                    //se la delega è della stessa provincia
                    if (del.getProvince().getIid() == item.getProvince().getIid()) {

                            boolean proceedWithCancel = false;

                            if (del.getWorkerCompany().getLid() == item.getWorkerCompany().getLid())
                                if (del.getLid() != item.getLid()) //se ovviamente non è la stessa delega
                                    proceedWithCancel = true;

                            //prima di procedere devo verificare hce l'annullamento da accettazione
                            // sia possibile solo su deleghe su cui si ha un permesso
                            //per ognuno per le proprie deleghe
                            if (proceedWithCancel){
                               // Role role= ((Role) u.getRoles().get(0));
                                if (item.getUserId() == u.getLid()){// || role.getLid() == 3){
                                    //se sto annullando una delega fatta dallo stesso
                                    //urntwe
                                    // non cè problema
                                    sm.cancelDelega(new Date(),item, null, del);
                                    rep.save(item);
                                }else {
                                    //elimino la delega che ho creato
                                    rep.delete(del.getLid());


                                }
                            }

                    }

                }
        }
    }


    @Override
    public void cancelDelega(Date date, Delega del, CausaleRevoca reason) throws Exception {
        DelegaState stateManager = constructStateManager(del);

        stateManager.cancelDelega(date,del, reason, null);

        checkCategory(del);

        rep.save(del);
    }

    private void checkCategory(Delega del) throws Exception {
        User u = (User) sec.getLoggedUser();
       // Role r = ((Role) u.getRoles().get(0));
        if (u.getLid() != del.getUserId()){
            //verifico che se un utente diverso dall'utente che ha creato la delega
            //allora puo solo essere un regionale
            //if (r.getLid() != 3)
                throw new Exception("Operazione possibile solo sulle deleghe gestite da " +  u.getCompleteName() );
        }



    }


    @Override
    public void revokeDelega(Date date, Delega del, CausaleRevoca reason) throws Exception {
        DelegaState stateManager = constructStateManager(del);

        stateManager.revokeDelega(date,del, reason);


        checkCategory(del);

        rep.save(del);
    }


    @Override
    public void activateDelega(Date date, Delega del) throws Exception {
        DelegaState stateManager = constructStateManager(del);

        stateManager.activateDelega(del);

       // checkCategory(del);

        rep.save(del);
    }

    @Override
    public void goBack(Delega del) throws Exception {
        DelegaState stateManager = constructStateManager(del);

        stateManager.goBack(del);


        checkCategory(del);

        rep.save(del);
    }

    @Override
    public List<Delega> getAllWorkerDeleghe(long workerId) {
        return rep.find(LoadRequest.build().id(Filters.DELEGA_ID_LAVORATORE, workerId)).getRows();
    }



    @Override
    public void deleteDelega(long user, long delegaId) throws Exception {

        Delega d = rep.get(delegaId).orElse(null);
        if (d == null)
            return;

        User u = ((User) sec.getLoggedUser());


       // Role r = ((Role) u.getRoles().get(0));
        if (d.getUserId() != (u.getLid()))
            //if (r.getLid() != 3)
                throw new Exception("E' possibile eliminare solo deleghe gestite da " + u.getCompleteName());


        rep.delete(delegaId);
    }

//    @Override
//    public void insertWithImpersonation(long userId, Delega l) throws Exception {
//        String error = delegaValidatorWithImpersonation .validate(l);
//        if (org.apache.commons.lang.StringUtils.isEmpty(error)) {
//
//        }
//        throw new Exception(error);
//    }

    @Override
    public void saveOrUpdate(long userId, Delega l) throws Exception {

        String error = delegaValidator.validate(l);
        if (org.apache.commons.lang.StringUtils.isEmpty(error)) {
            if (l.getLid() == 0) {
                //imposto in questo contesto i dati sulla categoria
                l.setUserId(((User) sec.getLoggedUser()).getLid());
                l.setUserCompleteName(((User) sec.getLoggedUser()).getCompleteName());

                //creo
                subscribeDelega(l);
            }else{

                //recupero la delega dal db
                Delega del = rep.get(l.getLid()).get();
                //adesso in base allo stato posso aggiornarne i paramertri
                DelegaState stateManager = constructStateManager(del);
                stateManager.updateDelega(del, l);
                //aggiorno
                rep.save(del);
            }

            return;
        }

        throw new Exception(error);
    }

    @Override
    public Delega getDelegaById(Long id) {
        return rep.get(id).orElse(null);
    }

    @Override
    public List<Integer> getDelegaPermittedNextStates(Delega delega, List<ApplicationOptions> opt) {
        if (opt.size()> 0)
            return constructStateManager(delega).getSupportedNextStates(delega, opt.get(0));

        return constructStateManager(delega).getSupportedNextStates(delega,null);
    }

    @Override
    public List<Delega> getDelegheForAzienda(long id) {
        LoadRequest req = LoadRequest.build();
        Filter f3;
        f3 = new Filter("workerCompany", id, Filter.EQ);
        req.getFilters().add(f3);


        return rep.find(req).getRows();
    }

    @Override
    public List<Delega> getDelegeForAzienda(UiQuoteHeaderParams params) {

        String azienda = params.getFirm();
        String province = params.getProvince();
        Long companyId = Long.parseLong(params.getCompany());
        //posso adesso fare la query

        //filtro per la companyId
        LoadRequest req = LoadRequest.build();

        Role r = ((Role) ((User) sec.getLoggedUser()).getRoles().get(0));
        if (r.getLid() != 3 && r.getLid() != 4){
            Filter f0 = new Filter("companyId", companyId, Filter.EQ);
            req.getFilters().add(f0);
        }





        // Filtro per recuperare le deleghe accettate (stato 3) o attivate (stato 4)
        Disjunction d = new Disjunction();
        List<Filter> list = new ArrayList<>();

        Filter fd1 = new Filter("state", 3);
        Filter fd2 = new Filter("state", 4);
        list.add(fd1);
        list.add(fd2);
        d.setChildren(list);
        req.getFilters().add(d);

//        // Filtro per recuperare le deleghe del settore
//        Categoria s = sectRep.find(LoadRequest.build().filter("description", params.getSettore())).findFirst().orElse(null);
//        Filter f2 = new Filter("sector", s.getId(), Filter.EQ);
//        req.getFilters().add(f2);




        //filtro sull'azienda
        Filter f3;
        f3 = new Filter("workerCompany", Long.parseLong(azienda), Filter.EQ);
        req.getFilters().add(f3);

        if (!StringUtils.isEmpty(province)){
            Integer proId = Integer.parseInt(province);
            Filter f4 = new Filter("province.id", proId, Filter.EQ);
            req.getFilters().add(f4);
        }




        return rep.find(req).getRows();

//        List<Delega> delegheLavoratoriSenzaDuplicati = new ArrayList<>();
//        for (Delega delega : del) {
//
//            if (delegheLavoratoriSenzaDuplicati.stream().filter(q -> q.getWorker().getId() == delega.getWorker().getId()).count() == 0)
//                delegheLavoratoriSenzaDuplicati.add(delega);
//        }
//
//        return delegheLavoratoriSenzaDuplicati;
    }

    @Override
    public boolean hasWorkerDelegaAttivaOAccettata(long workerId, String azienda, String provincia) {

        //recupero la lista di tutte le deleghe attive o accettte per un lavoratore
        LoadRequest req = LoadRequest.build().filter("worker", workerId);
        List<Delega> del = rep.find(req).getRows();


        //ciclo su tutte le deleghe
        for (Delega delega : del) {
            if (delega.getState() == Delega.state_accepted || delega.getState() == Delega.state_activated)
                if (delega.getProvince().getDescription().toUpperCase().equals(provincia.toUpperCase()))
                        if (delega.getWorkerCompany().getDescription().equals(azienda))
                            return true;

        }

        return false;

    }

    @Override
    public Delega retrieveActivableWorkerDelega(long idLavoratore, int provinceId,  long idAzienda) {
        LoadRequest req = LoadRequest.build().filter("worker", idLavoratore);
        List<Delega> del = rep.find(req).getRows();

        List<Delega> result = new ArrayList<>();


        //ciclo su tutte le deleghe
        for (Delega delega : del) {
            if (delega.getState() == Delega.state_accepted || delega.getState() == Delega.state_activated || delega.getState() == Delega.state_sent || delega.getState() == Delega.state_subscribe)
                if (delega.getProvince().getIid() == provinceId)
                            if (delega.getWorkerCompany().getLid() == idAzienda)
                                result.add(delega);
        }
        if (result.size() == 0)
            return null;


        //se ho trovato piu deleghe
        //tento di restituirne in ordine di stato
        Delega d1 = result.stream().filter(d->d.getState() == Delega.state_activated).findFirst().orElse(null);
        if (d1 == null)
        {
            //tento di recuperare una delega accettata
            d1 = result.stream().filter(d->d.getState() == Delega.state_accepted).findFirst().orElse(null);


            if (d1 == null)
            {
                //tento di recuperare una delega inoltrata
                d1 = result.stream().filter(d->d.getState() == Delega.state_sent).findFirst().orElse(null);


                if (d1 == null)
                {
                    //tento di recuperare una delega sottoscritta
                    d1 = result.stream().filter(d->d.getState() == Delega.state_subscribe).findFirst().orElse(null);
                }
            }

        }

        return d1;

    }

    @Override
    public Delega retrieveDelega(long idLavoratore, int provinceId, Date data,  long idAzienda) {

        LoadRequest req = LoadRequest.build().filter("worker", idLavoratore);
        List<Delega> del = rep.find(req).getRows();

        List<Delega> result = new ArrayList<>();

        //ciclo su tutte le deleghe
        for (Delega delega : del) {
                if (delega.getProvince().getIid() == provinceId)
                    if (delega.getDocumentDate().getTime() == data.getTime())
                                if (delega.getWorkerCompany().getLid() == idAzienda)
                                    result.add(delega);

        }
        if (result.size() == 0)
            return null;


        //se ho trovato piu deleghe
        //tento di restituirne in ordine di stato
        Delega d1 = result.stream().findFirst().orElse(null);

        return d1;
    }

    @Override
    public void saveImportedDelega(Delega d) {

        User u = urep.getRegionalUserByProvinceName(d.getProvince().getDescription());

        d.setUserId(u.getLid());
        d.setUserCompleteName(u.getCompleteName());
        d.setCompanyId(u.getCompany().getLid());


        rep.executeCommand(new Command() {
            @Override
            public void execute() {
                Session s = urep.getSession();
                Transaction tx = s.beginTransaction();
                try {
                    //rimuovo tutti i dettagli per una determinata quota
                   s.saveOrUpdate(d);
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

    @Override
    public List<DelegaUnc> getAllWorkerDelegheUnc(long id) {
        LoadRequest req = LoadRequest.build().disableOwnershipQuery().filter("worker", id);
        return  uncRep.find(req).getRows();
    }

    @Override
    public List<DelegaBilateralita> getAllWorkerDelegheBilateralita(long id) {
        LoadRequest req = LoadRequest.build().disableOwnershipQuery().filter("worker", id);
        return  bilRep.find(req).getRows();

    }
}
