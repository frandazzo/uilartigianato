package applica.feneal.admin.facade;


import applica.feneal.admin.viewmodel.reports.UiIscritto;
import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.servizi.search.UiIscrittoReportSearchParams;
import applica.feneal.services.ListaLavoroService;
import applica.feneal.services.QuoteAssociativeService;
import applica.feneal.services.ReportIscrittiService;
import applica.feneal.services.impl.report.fastPerformanceData.Iscritto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgran on 15/04/2016.
 */
@Component
public class IscrittiFacade {

    @Autowired
    private ReportIscrittiService iscrittiSvc;


    @Autowired
    private ListaLavoroService lSrv;


    @Autowired
    private CompanyRepository compRep;





    @Autowired
    private QuoteAssociativeService quoteService;



    public List<Iscritto> reportIscritti(UiIscrittoReportSearchParams params) throws Exception {

//        long startTime = System.nanoTime();
//
//        List<DettaglioQuotaAssociativa> iscrittiQuote = iscrittiSvc.retrieveQuoteIscritti(params);
//
//        long endTime = System.nanoTime();
//
//        long duration = (endTime - startTime)/1000000;
//
//        System.out.println("la durata di retrieve quote è : " + String.valueOf(duration) +  " millisecondi" );
//
//        startTime = System.nanoTime();
//
//        List<UiIscritto> i =  convertQuoteIscrittiToUiIscritti(iscrittiQuote);
//
//        endTime = System.nanoTime();
//
//        duration = (endTime - startTime)/1000000;
//
//        System.out.println("la durata di convertQuoteIscrittiToUiIscritti è : " + String.valueOf(duration) +  " millisecondi" );


//        long startTime = System.nanoTime();

        if (StringUtils.isEmpty(params.getCompany())){
            List<Iscritto> result = new ArrayList<>();
            //prendo tutti i territori
            List<Company> companies = compRep.find(null).getRows();
            for (Company company : companies) {
                params.setCompany(String.valueOf(company.getLid()));
                result.addAll(iscrittiSvc.retrieveFastPerformaceQuoteIscritti(params));
            }

            return result;

        }else{
            return iscrittiSvc.retrieveFastPerformaceQuoteIscritti(params);
        }
    }


//    private List<UiIscritto> convertQuoteIscrittiToUiIscritti(List<DettaglioQuotaAssociativa> quoteIscritti) {
//        List<UiIscritto> result = new ArrayList<>();
//        DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
//
//        for (DettaglioQuotaAssociativa dettaglio : quoteIscritti) {
//            UiIscritto i = new UiIscritto();
//
//
//
//            i.setIscrittoQuota(dettaglio.getQuota());
//            i.setIscrittoLivello(dettaglio.getLivello());
//            i.setIscrittoContratto(dettaglio.getContratto());
//            i.setIscrittoProvincia(dettaglio.getProvincia());
//            i.setIscrittoDataRegistrazione(dettaglio.getDataRegistrazione());
//            i.setIscrittoCompetenza(df.format(dettaglio.getDataInizio()) + " - " + df.format(dettaglio.getDataFine()));
//            i.setIscrittoSettore(dettaglio.getSettore());
//            i.setTipo(dettaglio.getTipoDocumento());
//            Lavoratore lav = lavSvc.getLavoratoreById(((User) security.getLoggedUser()).getLid(), dettaglio.getIdLavoratore());
//            if (lav != null) {
//                i.setLavoratoreId(lav.getLid());
//                i.setLavoratoreCap(lav.getCap());
//                i.setLavoratoreCellulare(lav.getCellphone());
//                i.setLavoratorMail(lav.getMail());
//                i.setLavoratoreTelefono(lav.getPhone());
//                i.setLavoratoreCittaResidenza(lav.getLivingCity());
//                i.setLavoratoreCodiceFiscale(lav.getFiscalcode());
//                i.setLavoratoreCognome(lav.getSurname());
//
//                i.setLavoratoreDataNascita(lav.getBirthDate());
//                i.setLavoratoreIndirizzo(lav.getAddress());
//                i.setLavoratoreLuogoNascita(lav.getBirthPlace());
//                i.setLavoratoreNazionalita(lav.getNationality());
//                i.setLavoratoreNome(lav.getName());
//                i.setLavoratoreCittaResidenza(lav.getLivingCity());
//                i.setLavoratoreProvinciaNascita(lav.getBirthProvince());
//                i.setLavoratoreProvinciaResidenza(lav.getLivingProvince());
//                if ("MASCHIO".equals(lav.getSex()))
//                    i.setLavoratoreSesso(Lavoratore.MALE);
//                else
//                    i.setLavoratoreSesso(Lavoratore.FEMALE);
//
////                i.setLavoratoreCodiceCassaEdile(lav.getCe());
////                i.setLavoratoreCodiceEdilcassa(lav.getEc());
//
//                if (lav.getAttribuzione1() != null)
//                    i.setLavoratoreAttribuzione1(lav.getAttribuzione1().getDescription());
//
//                if (lav.getAttribuzione2() != null)
//                    i.setLavoratoreAttribuzione2(lav.getAttribuzione2().getDescription());
//
//                if (lav.getAttribuzione3() != null)
//                    i.setLavoratoreAttribuzione3(lav.getAttribuzione3().getDescription());
//
//
//                if (lav.getFund() != null)
//                    i.setLavoratoreFondo(lav.getFund().getDescription());
//                i.setLavoratoreNote(lav.getNotes());
//            }
//
//            Azienda az = azSvc.getAziendaById(((User) security.getLoggedUser()).getLid(), dettaglio.getIdAzienda());
//            if (az != null) {
//                i.setAziendaRagioneSociale(az.getDescription());
//                i.setAziendaCitta(az.getCity());
//                i.setAziendaProvincia(az.getProvince());
//                i.setAziendaCap(az.getCap());
//                i.setAziendaIndirizzo(az.getAddress());
//                i.setAziendaNote(az.getNotes());
//                i.setAziendaId(az.getLid());
//            }
//
//            result.add(i);
//        }
//
//
//
//        return result;
//    }







//    private List<DettaglioQuotaAssociativa> convertUiIscrittoToQuoteIscritti(List<UiIscritto> uiIscritti) {
//
//        List<DettaglioQuotaAssociativa> result = new ArrayList<>();
//
//        for (UiIscritto iscritto : uiIscritti) {
//
//            DettaglioQuotaAssociativa d = new DettaglioQuotaAssociativa();
//
//            d.setContratto(iscritto.getIscrittoContratto());
//
//            d.setQuota(iscritto.getIscrittoQuota());
//            d.setLivello(iscritto.getIscrittoLivello());
//            d.setProvincia(iscritto.getIscrittoProvincia());
//            d.setIdAzienda(iscritto.getAziendaId());
//            d.setIdLavoratore(iscritto.getLavoratoreId());
//
//            result.add(d);
//        }

//        return result;
//    }

    public ListaLavoro createListalavoro(List<UiIscritto> iscritti, String description) throws Exception {
        List<DettaglioQuotaAssociativa> com = convertUiToDettaglioQuote(iscritti);
        return lSrv.createListaFromQuote(com, description);
    }

    private List<DettaglioQuotaAssociativa> convertUiToDettaglioQuote(List<UiIscritto> iscritti) {
        List<DettaglioQuotaAssociativa> a = new ArrayList<>();

        for (UiIscritto uiIscritto : iscritti) {
            DettaglioQuotaAssociativa s = new DettaglioQuotaAssociativa();
            s.setIdLavoratore(uiIscritto.getLavoratoreId());

            //non mi serviranno gli altri campi.... dato che utilizzo il metodo solo per la crerazione
            //della òlista di lavoro

            a.add(s);
        }

        return a;
    }




//    private String getMonth(String monthName){
//        if (monthName.equals("Gennaio"))
//            return "1";
//        if (monthName.equals("Febbraio"))
//            return "2";
//        if (monthName.equals("Marzo"))
//            return "3";
//        if (monthName.equals("Aprile"))
//            return "4";
//        if (monthName.equals("Maggio"))
//            return "5";
//        if (monthName.equals("Giugno"))
//            return "6";
//        if (monthName.equals("Luglio"))
//            return "7";
//        if (monthName.equals("Agosto"))
//            return "8";
//        if (monthName.equals("Settembre"))
//            return "9";
//        if (monthName.equals("Ottobre"))
//            return "10";
//        if (monthName.equals("Novembre"))
//            return "11";
//
//        return "12";
//
//    }



    public List<Iscritto> findCurrentIscrizioniForAzienda(long id) throws Exception {
//        List<DettaglioQuotaAssociativa> dett = quoteService.findCurrentIscrizioniForAzienda(id);
//
//        return convertQuoteIscrittiToUiIscritti(dett);
        return quoteService.fastfindCurrentIscrizioniForAzienda(id);

    }
}
