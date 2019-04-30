package applica.feneal.admin.facade;


import applica.feneal.admin.viewmodel.quote.UiDettaglioQuota;
import applica.feneal.admin.viewmodel.quote.UiPagamentoDeleghe;
import applica.feneal.admin.viewmodel.quote.UiQuoteLavoratori;
import applica.feneal.admin.viewmodel.quote.UiRiepilogoQuota;
import applica.feneal.admin.viewmodel.reports.UiDelega;
import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.data.core.quote.QuoteAssocRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.deleghe.DelegaPerCreazioneQuota;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.feneal.domain.model.core.quote.UpdatableDettaglioQuota;
import applica.feneal.domain.model.core.quote.fenealgestImport.DettaglioQuotaDTO;
import applica.feneal.domain.model.core.quote.fenealgestImport.RiepilogoQuotaDTO;
import applica.feneal.domain.model.core.servizi.search.UiQuoteHeaderParams;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.services.DelegheService;
import applica.feneal.services.GeoService;
import applica.feneal.services.QuoteAssociativeService;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by angelo on 26/05/2016.
 */
@Component
public class QuoteCreationFacade {

    @Autowired
    private DelegheService delService;

    @Autowired
    private Security sec;

    @Autowired
    private LavoratoriRepository lavRep;

    @Autowired
    private QuoteAssocRepository riepilogoQuoteRep;


    @Autowired
    private AziendeRepository aziendeRepository;

    @Autowired
    private CompanyRepository compRep;


    @Autowired
    private GeoService geo;

    @Autowired
    private QuoteAssociativeService quoteAssociativeService;

    @Autowired
    private QuoteFacade quoteFacade;


    public List<UiQuoteLavoratori> retrieveDelegeLavoratoriForAzienda(UiQuoteHeaderParams params){

        List<Delega> deleghe = delService.getDelegeForAzienda(params);

        return convertDelegheToUiLavoratoriQuote(params, deleghe);
    }

    private List<UiQuoteLavoratori> convertDelegheToUiLavoratoriQuote(UiQuoteHeaderParams params, List<Delega> delegheImpiantiFissi) {

       return convertDelegheToUiLavoratoriQuote(params.getAmount(), delegheImpiantiFissi);
    }




    private List<UiQuoteLavoratori> convertDelegheToUiLavoratoriQuote(double params, List<Delega> delegheImpiantiFissi) {

        int numResults = delegheImpiantiFissi.size();
        List<UiQuoteLavoratori> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        for (Delega del : delegheImpiantiFissi) {
            UiQuoteLavoratori q = new UiQuoteLavoratori();

            q.setLavoratoreId(del.getWorker().getLid());
            q.setLavoratoreNomeCompleto(String.format("%s %s", del.getWorker().getSurname(), del.getWorker().getName()));
            q.setAzienda(del.getWorkerCompany().getDescription());
            if (del.getContract() != null)
                q.setContratto(del.getContract().getDescription());
            else
                q.setContratto("");
            q.setSettore(del.getSector().getDescription());

            try {
                String importoStr = df.format(params / numResults).replace(",", ".");
                q.setImporto(Double.parseDouble(importoStr));
            } catch (Exception e) {
                q.setImporto(0);
            }

            result.add(q);
        }

        return result;
    }


    private List<UiQuoteLavoratori> convertDelegheToUiLavoratoriQuote1(double params, List<UiDelega> delegheImpiantiFissi) {

        int numResults = delegheImpiantiFissi.size();
        List<UiQuoteLavoratori> result = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.##");
        df.setMaximumFractionDigits(2);

        for (UiDelega del : delegheImpiantiFissi) {
            if (del.getDelegaStato().equals("Accettata")
                    || del.getDelegaStato().equals("Attivata")){
                UiQuoteLavoratori q = new UiQuoteLavoratori();

                q.setLavoratoreId(del.getLavoratoreId());
                q.setLavoratoreNomeCompleto(String.format("%s %s", del.getLavoratoreCognome(), del.getLavoratoreNome()));
                q.setAzienda(del.getAziendaRagioneSociale());
                q.setAziendaId(del.getAziendaId());
                q.setContratto(del.getDelegaContract());
                q.setSettore(del.getDelegaSettore());
                q.setProvincia(del.getDelegaProvincia());
                q.setCompanyId(del.getCompanyId());
                try {
                    String importoStr = df.format(params / numResults).replace(",", ".");
                    q.setImporto(Double.parseDouble(importoStr));
                } catch (Exception e) {
                    q.setImporto(0);
                }

                result.add(q);
            }

        }

        return result;
    }



    public String createRiepilogoQuote(UiRiepilogoQuota data) throws Exception {
        RiepilogoQuotaDTO dto = createRiepilogoQuotaDTO(data);

        quoteAssociativeService.creaQuotemassive(dto);


       return "";

    }

    private RiepilogoQuotaDTO createRiepilogoQuotaDTO(UiRiepilogoQuota data) throws Exception {
        User u = ((User) sec.getLoggedUser());

       // Categoria s = settoreRepository.find(LoadRequest.build().filter("description",data.getHeader().getSettore())).findFirst().orElse(null);
        Azienda firm = aziendeRepository.get(data.getHeader().getFirm()).orElse(null);
        Province p = geo.getProvinceById(Integer.parseInt(data.getHeader().getProvince()));

        //creo il dto per fare in modo che la classe importData possa terminare il lavoro
        RiepilogoQuotaDTO dto = null;
            dto = data.createRiepilogoQuota(null,
                    firm.getDescription(),
                    p.getDescription(),
                    Long.parseLong(data.getHeader().getCompany()),
                    u.getCompleteName(),
                    u.getUsername(),
                    UUID.randomUUID().toString(),
                    "");

        List<DettaglioQuotaDTO> details = new ArrayList<>();
        for (UiQuoteLavoratori uiQuoteLavoratori : data.getQuoteRows()) {

            DettaglioQuotaDTO d = data.createDettaglioQuota(uiQuoteLavoratori,
                    dto,
                    lavRep.get(uiQuoteLavoratori.getLavoratoreId()).orElse(null),
                    firm);
            details.add(d);
        }

        dto.setDettagli(details);

        return  dto;
    }



    public UiDettaglioQuota addDettaglioQuota(long quotaId, UiQuoteHeaderParams data) throws Exception {

        RiepilogoQuoteAssociative r =  riepilogoQuoteRep.get(quotaId).orElse(null);
        if (r == null)
            throw  new Exception("Riepilogo quota non trovata");

        Company cc = compRep.get(r.getCompanyId()).orElse(null);


        DettaglioQuotaDTO d = UiRiepilogoQuota.createDettaglioQuota(data,
                r,
                lavRep.get(data.getWorker()).orElse(null),
                data.getFirm() != "" ?aziendeRepository.get(data.getFirm()).orElse(null) : null);


        d.setRegione(cc.getDescription());

        DettaglioQuotaAssociativa dettaglio = quoteAssociativeService.addItem(r, d);

        return quoteFacade.convertDettaglioToUiDettaglioQuota(dettaglio);
    }

    public void deleteDettaglioQuota(long quotaId, long itemId) {
        quoteAssociativeService.deleteItem(quotaId, itemId);
    }

    public void updateDettaglioQuota(long quotaId, long itemId, UpdatableDettaglioQuota updatedData) {
        quoteAssociativeService.updateItem(quotaId,itemId, updatedData);
    }

    public void duplicaQuota(long quotaId, UiQuoteHeaderParams data) throws ParseException {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Date inizio = f.parse(data.getDataInizio());
        Date fine = f.parse(data.getDataFine());
        quoteAssociativeService.duplicaQuota(quotaId, inizio, fine );
    }

    public List<DelegaPerCreazioneQuota> searchAziendeByName(String name) {
        return quoteAssociativeService.searchAziendeByName(name);
    }

    public void createPagamentoMassivo(UiPagamentoDeleghe pagamento) throws Exception {


        Date dataInizio = tryParse(pagamento.getDataInizio());
        Date dataFine = tryParse(pagamento.getDataFine());

        if (dataFine == null || dataInizio == null)
            throw new Exception("Inserire le date correttamente");
        if (dataFine.getTime() < dataInizio.getTime()){
            Date temp = dataInizio;
            dataInizio = dataFine;
            dataFine   = temp;
        }

        List<UiQuoteLavoratori> deleghe = convertDelegheToUiLavoratoriQuote1(pagamento.getImporto(), pagamento.getDeleghe());
        if (deleghe.size() == 0)
            throw new Exception("Nessuna delega in uno staot attivo!");
        //devo creare tanti pagamenti quanti sono le aziende e le provincie

        //creo una hash table di tutte le quote lavoratori
        //la chiave della hash table è una stringa con "idprovicnia-aziendaid"
        //mente il valore è una lista di uiquotelavoratori
        Hashtable<String, List<UiQuoteLavoratori>> data = createDeleghePerAziendaEProvicnia(deleghe);

        //adesso a partire dalla hash table posso creare una lista di uiriepilogo quote che posso importare

        List<UiRiepilogoQuota> dataToInsert = createDataToInsert(dataInizio, dataFine, data);


        for (UiRiepilogoQuota uiRiepilogoQuota : dataToInsert) {
            createRiepilogoQuote(uiRiepilogoQuota);
        }

    }

    private List<UiRiepilogoQuota> createDataToInsert(Date dataInizio, Date dataFine, Hashtable<String, List<UiQuoteLavoratori>> data) {

        SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
        String fine =ff.format(dataFine);
        String inizio =ff.format(dataInizio);

        List<UiRiepilogoQuota> result = new ArrayList<>();

        for (String s : data.keySet()) {
            UiQuoteHeaderParams header = new UiQuoteHeaderParams();
            header.setAmount(data.get(s).stream().mapToDouble( o -> o.getImporto()).sum());
            header.setFirm(String.valueOf(data.get(s).get(0).getAziendaId()));
            header.setProvince(String.valueOf(geo.getProvinceByName(data.get(s).get(0).getProvincia()).getIid()));
            header.setDataFine(fine);
            header.setDataInizio(inizio);
            header.setCompany(String.valueOf(data.get(s).get(0).getCompanyId()));

            UiRiepilogoQuota quota = createRiepilogoQuotaToInsert(header, data.get(s));
            result.add(quota);

        }

        return result;

    }

    private UiRiepilogoQuota createRiepilogoQuotaToInsert(UiQuoteHeaderParams header, List<UiQuoteLavoratori> uiQuoteLavoratoris) {
        UiRiepilogoQuota result = new UiRiepilogoQuota();
        result.setHeader(header);
        result.setQuoteRows(uiQuoteLavoratoris);

        return result;
    }

    private Hashtable<String, List<UiQuoteLavoratori>> createDeleghePerAziendaEProvicnia(List<UiQuoteLavoratori> deleghe) {
        Hashtable<String, List<UiQuoteLavoratori>> result = new Hashtable<>();

        for (UiQuoteLavoratori uiQuoteLavoratori : deleghe) {
            String idProvincia = uiQuoteLavoratori.getProvincia();
            long idAzienda = uiQuoteLavoratori.getAziendaId();

            String key = String.format("%s-%s", idProvincia, String.valueOf(idAzienda));
            if (result.containsKey(key))
                result.get(key).add(uiQuoteLavoratori);
            else {
                List<UiQuoteLavoratori> rr = new ArrayList<>();
                rr.add(uiQuoteLavoratori);
                result.put(key, rr);
            }

        }


        return result;
    }

    private Date tryParse(String dataInizio) {
        SimpleDateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return f.parse(dataInizio);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

//    public long createRiepilogoQuoteSettore(UiQuoteHeaderParams data) throws Exception {
//        DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
//        Date inizio = f.parse(data.getDataInizio());
//        Date fine = f.parse(data.getDataFine());
//        Categoria s = settoreRepository.find(LoadRequest.build().filter("description",data.getSettore())).findFirst().orElse(null);
//        return quoteAssociativeService.createQuotaAssociativaPerSettore(s,inizio, fine, data.getProvince());
//
//    }
}
