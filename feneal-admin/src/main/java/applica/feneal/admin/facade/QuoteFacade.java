package applica.feneal.admin.facade;

import applica.feneal.admin.viewmodel.quote.UiDettaglioQuota;
import applica.feneal.domain.data.core.quote.QuoteAssocRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.domain.model.core.quote.DettaglioQuotaAssociativa;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.feneal.domain.model.core.servizi.search.UiQuoteHeaderParams;
import applica.feneal.services.AziendaService;
import applica.feneal.services.LavoratoreService;
import applica.feneal.services.ListaLavoroService;
import applica.feneal.services.QuoteAssociativeService;
import applica.framework.LoadRequest;
import applica.framework.fileserver.FileServer;
import applica.framework.library.options.OptionsManager;
import applica.framework.security.Security;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by angelo on 26/05/2016.
 */
@Component
public class QuoteFacade {

    @Autowired
    private LavoratoreService lavSvc;

    @Autowired
    private AziendaService azSvc;

    @Autowired
    private ListaLavoroService lSrv;

    @Autowired
    private Security security;

    @Autowired
    private OptionsManager optManager;

    @Autowired
    private QuoteAssociativeService quoteService;

    @Autowired
    private QuoteAssocRepository quoteRep;

    @Autowired
    private FileServer server;


    public List<UiDettaglioQuota> getDettaglioQuota(long id, Long idWorker){

        List<DettaglioQuotaAssociativa> quoteDetails = quoteService.getDettagliQuota(id, idWorker);

        return convertDettaglioQuoteToUiDettaglioQuote(quoteDetails);
    }

    private List<UiDettaglioQuota> convertDettaglioQuoteToUiDettaglioQuote(List<DettaglioQuotaAssociativa> quoteDetails) {

        List<UiDettaglioQuota> result = new ArrayList<>();

        for (DettaglioQuotaAssociativa dettaglio : quoteDetails) {
            UiDettaglioQuota q = convertDettaglioToUiDettaglioQuota(dettaglio);

            result.add(q);
        }

        return result;
    }

    public UiDettaglioQuota convertDettaglioToUiDettaglioQuota(DettaglioQuotaAssociativa dettaglio) {
        UiDettaglioQuota q = new UiDettaglioQuota();

        q.setRipresaDati(dettaglio.isRipresaDati());

        q.setCompanyId(dettaglio.getCompanyId());
        q.setRegione(dettaglio.getRegione());
        q.setProvincia(dettaglio.getProvincia());
        q.setSettore(dettaglio.getSettore());

        q.setOperatoreDelega(dettaglio.getOperatoreDelega());
        q.setRipresaDati(dettaglio.isRipresaDati());
        q.setId(dettaglio.getLid());
        q.setIdQuota(dettaglio.getIdRiepilogoQuotaAssociativa());


        q.setQuota(dettaglio.getQuota());
        q.setLivello(dettaglio.getLivello());
        q.setContratto(dettaglio.getContratto());

        q.setDataRegistrazione(dettaglio.getDataRegistrazione());
        q.setDataInizio(dettaglio.getDataInizio());
        q.setDataFine(dettaglio.getDataFine());
        q.setDataDocumento(dettaglio.getDataDocumento());
        q.setTipoDocumento(dettaglio.getTipoDocumento());
        q.setNote(dettaglio.getNote());


        Lavoratore lav = lavSvc.getLavoratoreById(((User) security.getLoggedUser()).getLid(), dettaglio.getIdLavoratore());
        if (lav != null) {
            q.setLavoratoreNomeCompleto(String.format("%s %s", lav.getSurname(), lav.getName()));
            q.setLavoratoreCodiceFiscale(lav.getFiscalcode());
            q.setLavoratoreId(lav.getLid());
        }

        Azienda az = azSvc.getAziendaById(((User) security.getLoggedUser()).getLid(), dettaglio.getIdAzienda());
        if (az != null) {
            q.setAziendaRagioneSociale(az.getDescription());
            q.setAziendaId(az.getLid());
        }
        return q;
    }


    public void deleteQuota(long id) {
        quoteService.deleteQuota(id);
    }

    public void downloadFile(long id, HttpServletResponse response) throws IOException {

        RiepilogoQuoteAssociative r = quoteService.getRiepilogoQuotaById(id);

        if (r != null) {

            String logFolder = optManager.get("applica.fenealquote.logfolder");
            String fullPath = logFolder + r.getImportedLogFilePath();

            InputStream is = new FileInputStream(fullPath);

            response.setHeader("Content-Disposition", "attachment;filename=" + r.getImportedLogFilePath());
            //response.setContentType("text/plain");
            response.setStatus(200);

            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            is.close();
            outStream.close();

        }

    }


    public ListaLavoro createListalavoro(List<UiDettaglioQuota> quote, String description) throws Exception {
        List<DettaglioQuotaAssociativa> com = convertUiToQuote(quote);
        return lSrv.createListaFromQuote(com, description);
    }

    private List<DettaglioQuotaAssociativa> convertUiToQuote(List<UiDettaglioQuota> quote) {
        List<DettaglioQuotaAssociativa> a = new ArrayList<>();

        for (UiDettaglioQuota uiDettaglio : quote) {
            DettaglioQuotaAssociativa s = new DettaglioQuotaAssociativa();
            s.setIdLavoratore(uiDettaglio.getLavoratoreId());

            //non mi serviranno gli altri campi.... dato che utilizzo il metodo solo per la crerazione
            //della òlista di lavoro

            a.add(s);
        }

        return a;
    }

    public void downloadOriginalFile(long id, HttpServletResponse response) throws IOException {
        RiepilogoQuoteAssociative r = quoteService.getRiepilogoQuotaById(id);

        if (r != null) {


            String fullPath =  r.getOriginalFileServerPath();

            InputStream is = server.getFile(fullPath);

            response.setHeader("Content-Disposition", "attachment;filename=originale.xlsx");
            //response.setContentType("text/plain");
            response.setStatus(200);

            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            is.close();
            outStream.close();

        }
    }

    public void downloadXmlFile(long id, HttpServletResponse response) throws IOException {
        RiepilogoQuoteAssociative r = quoteService.getRiepilogoQuotaById(id);

        if (r != null) {


            String fullPath =  r.getXmlFileServerPath();
            InputStream is = new FileInputStream(fullPath);

            response.setHeader("Content-Disposition", "attachment;filename=" + r.getXmlFileServerPath());
            //response.setContentType("text/plain");
            response.setStatus(200);

            OutputStream outStream = response.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead = -1;

            while ((bytesRead = is.read(buffer)) != -1) {
                outStream.write(buffer, 0, bytesRead);
            }

            is.close();
            outStream.close();

        }
    }

    public List<RiepilogoQuoteAssociative> findQuote(long companyId) {

        if (companyId == -1){
            LoadRequest req = LoadRequest.build();
            return quoteRep.find(req).getRows();
        }
        LoadRequest req = LoadRequest.build().filter("companyId", companyId);
        return quoteRep.find(req).getRows();

    }

    public void modifyCompetenceQuotaItems(long quotaId, UiQuoteHeaderParams data) throws ParseException {
        DateFormat f = new SimpleDateFormat("dd/MM/yyyy");
        Date inizio = f.parse(data.getDataInizio());
        Date fine = f.parse(data.getDataFine());
        quoteService.modifyCompetenceQuotaItems(quotaId, inizio, fine);
    }
}
