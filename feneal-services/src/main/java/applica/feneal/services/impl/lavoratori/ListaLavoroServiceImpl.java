package applica.feneal.services.impl.lavoratori;

import applica.feneal.domain.data.core.deleghe.DelegheRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.core.lavoratori.ListaLavoroRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.domain.model.core.lavoratori.ListeLavoroComparison;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.servizi.Comunicazione;
import applica.feneal.domain.model.core.servizi.Documento;
import applica.feneal.services.LavoratoreService;
import applica.feneal.services.ListaLavoroService;
import applica.framework.LoadRequest;
import applica.framework.security.Security;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by fgran on 07/06/2016.
 */
@Service
public class ListaLavoroServiceImpl implements ListaLavoroService {
    @Autowired
    private ListaLavoroRepository liRep;

    @Autowired
    private LavoratoriRepository lavRep;

    @Autowired
    private LavoratoreService lavSvc;



    @Autowired
    private DelegheRepository delRep;

    @Autowired
    private Security sec;


    @Override
    public ListaLavoro getListaById(long listaId) {
        return liRep.get(listaId).orElse(null);
    }

    @Override
    public ListaLavoro createListaFromQuote(List<DettaglioQuotaAssociativa> quote, String description) throws Exception {


        HashMap<String, Lavoratore> workers = new HashMap<>();
        for (DettaglioQuotaAssociativa dettaglioQuotaAssociativa : quote) {
            if (!workers.containsKey(dettaglioQuotaAssociativa.getIdLavoratore())){
                Lavoratore l = lavRep.get(dettaglioQuotaAssociativa.getIdLavoratore()).orElse(null);
                if (l != null)
                    workers.put(l.getFiscalcode(), l);
            }
        }


        return createListFromWorkersHashMap(workers, description);
    }

    @Override
    public void deleteLista(long listaId) {

        ListaLavoro l = liRep.get(listaId).orElse(null);
        if (l == null)
            return;

        if (l.getUserId() != ((User) sec.getLoggedUser()).getLid())
            return;

        liRep.delete(listaId);
    }

    @Override
    public ListaLavoro createListaFromDeleghe(List<Delega> deleghe, String description) throws Exception {
        HashMap<String, Lavoratore> workers = new HashMap<>();
        for (Delega delega : deleghe) {
            if (!workers.containsKey(delega.getWorker().getLid())){
                Lavoratore l = delega.getWorker();
                if (l != null)
                    workers.put(l.getFiscalcode(), l);
            }
        }


        return createListFromWorkersHashMap(workers, description);
    }





    @Override
    public ListaLavoro createListaFromArchivioDocumentale(List<Documento> documenti, String description) throws Exception {
        HashMap<String, Lavoratore> workers = new HashMap<>();
        for (Documento doc : documenti) {
            if (!workers.containsKey(doc.getLavoratore().getLid())){
                Lavoratore l = doc.getLavoratore();
                if (l != null)
                    workers.put(l.getFiscalcode(), l);
            }
        }


        return createListFromWorkersHashMap(workers, description);
    }



    @Override
    public ListaLavoro createListaFromComunicazioni(List<Comunicazione> comunicazioni, String description) throws Exception {
        HashMap<String, Lavoratore> workers = new HashMap<>();
        for (Comunicazione doc : comunicazioni) {
            if (!workers.containsKey(doc.getLavoratore().getLid())){
                Lavoratore l = doc.getLavoratore();
                if (l != null)
                    workers.put(l.getFiscalcode(), l);
            }
        }

        return createListFromWorkersHashMap(workers, description);
    }


    private ListaLavoro createListFromWorkersHashMap(HashMap<String, Lavoratore> workers, String description) throws Exception {
        List<Lavoratore> lista = new ArrayList<>(workers.values());

        validateLista(lista, description);


        ListaLavoro l = new ListaLavoro();
        l.setDescription(description);
        l.setLavoratori(lista);

        l.setUserId(((User) sec.getLoggedUser()).getLid());
        l.setUserCompleteName(((User) sec.getLoggedUser()).getCompleteName());

        liRep.save(l);

        return l;
    }

    private void validateLista(List<Lavoratore> workers, String description) throws Exception {
        if (StringUtils.isEmpty(description))
            throw new Exception("Nome lista non specificato");


        if (workers.size() == 0)
            throw new Exception("Nessuna lavoratore nella lista");

        //tento di recuperare una lista con lo stesso nome
        LoadRequest req = LoadRequest.build().filter("description", description);
        ListaLavoro lll = liRep.find(req).findFirst().orElse(null);
        if (lll != null)
            throw new Exception("Nome della lista gia esistente");




    }


    @Override
    public ListaLavoro mergeWithOtherList(ListaLavoro a, ListaLavoro b, String description) throws Exception {

        if (a.getLid() == b.getLid())
            throw new Exception("Le liste devono essere diverse");

        //devo creare una nuova lista di lavoro con l'unione di tutti i lavoratori di entrambe le liste
        HashMap<String, Lavoratore> h1 = new HashMap<>();
        for (Lavoratore lavoratore : a.getLavoratori()) {
            h1.put(lavoratore.getFiscalcode(), lavoratore);
        }

        for (Lavoratore lavoratore : b.getLavoratori()) {
            if (!h1.containsKey(lavoratore.getFiscalcode()))
                h1.put(lavoratore.getFiscalcode(), lavoratore);
        }

        return createListFromWorkersHashMap(h1,description);

    }

    @Override
    public ListaLavoro excludeWithOtherList(ListaLavoro a, ListaLavoro b, String description) throws Exception {
        //devo escludere i lavoratori della lista b dalla lista a

        if (a.getLid() == b.getLid())
            throw new Exception("Le liste devono essere diverse");

        ListaLavoro l = a.excludeLavoratoriOfOtherList(b, description);
        //inserisco automaticamente lam categoria
        l.setUserId(((User) sec.getLoggedUser()).getLid());
        l.setUserCompleteName(((User) sec.getLoggedUser()).getCompleteName());

        validateLista(l.getLavoratori(), description);

        liRep.save(l);
        return l;
    }

    @Override
    public ListaLavoro intersectWithOtherList(ListaLavoro a, ListaLavoro b, String description) throws Exception {

        if (a.getLid() == b.getLid())
            throw new Exception("Le liste devono essere diverse");

        ListaLavoro l = a.intersects(b, description);
        l.setUserId(((User) sec.getLoggedUser()).getLid());
        l.setUserCompleteName(((User) sec.getLoggedUser()).getCompleteName());

        validateLista(l.getLavoratori(), description);

        liRep.save(l);
        return l;
    }

    @Override
    public ListeLavoroComparison compareLists(ListaLavoro a, ListaLavoro b, String description) throws Exception {

        if (a.getLid() == b.getLid())
            throw new Exception("Le liste devono essere diverse");


        ListaLavoro aa = a.clone();
        ListaLavoro bb = b.clone();

        ListaLavoro aaa = a.clone();
        ListaLavoro bbb = b.clone();

        ListeLavoroComparison c = new ListeLavoroComparison();
        ListaLavoro aMinusB = aa.excludeLavoratoriOfOtherList(bb, description + "_(presenti_solo_in_" + aa.getDescription() + " _lista)");
        ListaLavoro bMinusA = b.excludeLavoratoriOfOtherList(a, description + "_(presenti_solo_in_" + bb.getDescription() + " _lista)");
        ListaLavoro intersection = aaa.intersects(bbb, description + "_presenti_in_entrambe");

        //   validateLista(aMinusB.getLavoratori(), description);
        //   validateLista(bMinusA.getLavoratori(), description);
        //   validateLista(intersection.getLavoratori(), description);

        c.setaMinusBList(aMinusB);
        c.setbMinusAListr(bMinusA);
        c.setIntersectionList(intersection);

        return c;



    }


    @Override
    public void saveListaLavoro(ListaLavoro lista) {
        List<Lavoratore> lavs = lista.getLavoratori();

        Hashtable<Long, Lavoratore> h = new Hashtable<>();

        for (Lavoratore lav : lavs) {
            if (!h.containsKey(lav.getLid()))
                h.put(lav.getLid(), lav);
        }

        List<Lavoratore> oneTimeLavoratore = new ArrayList<>(h.values());
        //adesso ho la lista univoca
        lista.setLavoratori(oneTimeLavoratore);

        SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy_hh-mm-ss");

        //calcolo adesso una possibile descrizione
        String description = String.format("Lista_%s_%s", "importAnagrafiche", f.format(new Date()) );

        lista.setDescription(description);
        lista.setUserId(((User) sec.getLoggedUser()).getLid());
        lista.setUserCompleteName(((User) sec.getLoggedUser()).getCompleteName());


        liRep.save(lista);
    }

    @Override
    public void deleteWorkerFromLista(long listaId, long lavId) {
        ListaLavoro listaLavoro  = getListaById(listaId);

        if (listaLavoro != null) {
            listaLavoro.getLavoratori().removeIf(l -> l.getLid() == lavId);
        }

        liRep.save(listaLavoro);
    }

    @Override
    public void addWorker(long listaId, long workerId) throws Exception {
        ListaLavoro listaLavoro  = getListaById(listaId);



        if (listaLavoro != null) {
            Lavoratore lav = lavSvc.getLavoratoreById(((User) sec.getLoggedUser()).getLid(), workerId);


            if (lav != null) {

                if (!listaLavoro.containsWorker(lav.getFiscalcode()))
                {
                    listaLavoro.getLavoratori().add(lav);
                    liRep.save(listaLavoro);
                }else{
                    throw new Exception("Lavoratore già presente");
                }



            }
        }
    }


}
