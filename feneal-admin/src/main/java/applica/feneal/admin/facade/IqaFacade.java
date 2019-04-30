package applica.feneal.admin.facade;

import applica.feneal.admin.viewmodel.BonificoDto;
import applica.feneal.admin.viewmodel.quote.UiDettaglioQuota;
import applica.feneal.domain.data.core.CompanyRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.servizi.search.UiIqaReportSearchParams;
import applica.feneal.services.AziendaService;
import applica.feneal.services.LavoratoreService;
import applica.feneal.services.ListaLavoroService;
import applica.feneal.services.QuoteAssociativeService;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by fgran on 15/04/2016.
 */
@Component
public class IqaFacade {

    @Autowired
    private QuoteAssociativeService quoteService;

    @Autowired
    private CompanyRepository comRep;

    @Autowired
    private LavoratoreService lavSvc;

    @Autowired
    private ListaLavoroService lSrv;

    @Autowired
    private AziendaService azSvc;

    @Autowired
    private Security security;


    public List<UiDettaglioQuota> reportQuote(UiIqaReportSearchParams params) throws Exception {
        List<DettaglioQuotaAssociativa> quote = quoteService.retrieveQuote(params);

        return convertQuoteToUiQuote(quote);
    }


    private List<UiDettaglioQuota> convertQuoteToUiQuote(List<DettaglioQuotaAssociativa> quote) {
        List<UiDettaglioQuota> result = new ArrayList<>();

        List<Company> companies = comRep.find(null).getRows();

        for (DettaglioQuotaAssociativa dettaglio : quote) {
            UiDettaglioQuota q = new UiDettaglioQuota();
            q.setId(dettaglio.getLid());
            q.setOperatoreDelega(dettaglio.getOperatoreDelega());
            q.setCompanyId(dettaglio.getCompanyId());
            q.setIdQuota(dettaglio.getIdRiepilogoQuotaAssociativa());
            q.setRegione(companies.stream().filter(a -> a.getLid() == dettaglio.getCompanyId()).findFirst().orElse(null).getDescription());
            q.setQuota(dettaglio.getQuota());
            q.setLivello(dettaglio.getLivello());
            q.setContratto(dettaglio.getContratto());
            q.setProvincia(dettaglio.getProvincia());
            q.setDataRegistrazione(dettaglio.getDataRegistrazione());
            q.setDataInizio(dettaglio.getDataInizio());
            q.setDataFine(dettaglio.getDataFine());
            q.setTipoDocumento(dettaglio.getTipoDocumento());
            q.setDataDocumento(dettaglio.getDataDocumento());
            q.setDataRegistrazione(dettaglio.getDataRegistrazione());
            q.setSettore(dettaglio.getSettore());
            Lavoratore lav = lavSvc.getLavoratoreById(((User) security.getLoggedUser()).getLid(), dettaglio.getIdLavoratore());
            if (lav != null) {
                q.setLavoratoreNomeCompleto(String.format("%s %s", lav.getSurname(), lav.getName()));
                q.setLavoratoreCodiceFiscale(lav.getFiscalcode());
                q.setLavoratoreId(lav.getLid());
                q.setLavoratoreCognome(lav.getSurname());
                q.setLavoratoreNome(lav.getName());
            }

            Azienda az = azSvc.getAziendaById(((User) security.getLoggedUser()).getLid(), dettaglio.getIdAzienda());
            if (az != null) {
                q.setAziendaRagioneSociale(az.getDescription());
                q.setAziendaId(az.getLid());
            }

            q.setBonificoId(dettaglio.getBonificoId());
            q.setNoteBonifico(dettaglio.getNoteBonifico());
            q.setDataBonifico(dettaglio.getDataBonifico());

            result.add(q);
        }

        return result;
    }


    public ListaLavoro createListalavoro(List<UiDettaglioQuota> iqa, String description) throws Exception {
        List<DettaglioQuotaAssociativa> com = convertUiToDettaglioQuote(iqa);
        return lSrv.createListaFromQuote(com,description);
    }

    private List<DettaglioQuotaAssociativa> convertUiToDettaglioQuote(List<UiDettaglioQuota> iqa) {
        List<DettaglioQuotaAssociativa> a = new ArrayList<>();

        for (UiDettaglioQuota uiDettaglio : iqa) {
            DettaglioQuotaAssociativa s = new DettaglioQuotaAssociativa();
            s.setIdLavoratore(uiDettaglio.getLavoratoreId());

            //non mi serviranno gli altri campi.... dato che utilizzo il metodo solo per la crerazione
            //della òlista di lavoro

            a.add(s);
        }

        return a;
    }

    public String BonificaQuote(BonificoDto params) {
        return quoteService.bonificaQuote(params.getNote(), params.getQuoteIds());
    }

    public String Deletebonifico(long id) throws Exception {
        return quoteService.deleteBonifico(id);
    }
}
