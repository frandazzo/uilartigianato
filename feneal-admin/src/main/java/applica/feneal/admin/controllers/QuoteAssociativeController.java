package applica.feneal.admin.controllers;

import applica.feneal.admin.facade.QuoteFacade;
import applica.feneal.admin.fields.renderers.CompanyForRiepilogoQuoteNazionaleFieldRenderer;
import applica.feneal.admin.fields.renderers.CompanyForTerritorySelectFieldrenderer;
import applica.feneal.admin.viewmodel.quote.UiDettaglioQuota;
import applica.feneal.admin.viewmodel.quote.UiDettaglioQuotaView;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.feneal.domain.model.core.quote.fenealgestImport.RiepilogoQuotaDTO;
import applica.feneal.domain.model.core.servizi.search.UiQuoteHeaderParams;
import applica.feneal.services.QuoteAssociativeService;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.FormResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.library.ui.PartialViewRenderer;
import applica.framework.security.Security;
import applica.framework.widgets.CrudConfigurationException;
import applica.framework.widgets.Form;
import applica.framework.widgets.FormCreationException;
import applica.framework.widgets.FormDescriptor;
import applica.framework.widgets.fields.Params;
import applica.framework.widgets.fields.Values;
import applica.framework.widgets.fields.renderers.DefaultFieldRenderer;
import applica.framework.widgets.forms.renderers.DefaultFormRenderer;
import applica.framework.widgets.forms.renderers.SearchFormRenderer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

/**
 * Created by angelo on 26/05/2016.
 */
@Controller
public class QuoteAssociativeController {

    @Autowired
    private ViewResolver viewResolver;

    @Autowired
    private QuoteAssociativeService quoteService;

    @Autowired
    private Security sec;

    @Autowired
    private QuoteFacade quoteFacade;

    @Autowired
    private QuoteAssociativeService qServ;


    @Autowired
    private ApplicationContext applicationContext;

    @RequestMapping(value="/importquote/{companyId}")
    public @ResponseBody SimpleResponse importData(@PathVariable int companyId, @RequestBody RiepilogoQuotaDTO dto) {
        if (dto == null)
            return new ValueResponse("vuoto");
        try {
            qServ.importQuoteAssociativeFromDatiTerritori(companyId, dto);
        } catch (Exception e) {
            return new ErrorResponse("Auth exception");
        }
        return new ValueResponse("OK");
    }


    @RequestMapping(value = "/quoteassociative/search",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse searchview(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(SearchFormRenderer.class));
            form.setIdentifier("quote");

            FormDescriptor formDescriptor = new FormDescriptor(form);

            User u = (User) sec.getLoggedUser();
            if (((applica.feneal.domain.model.Role) u.getRoles().get(0)).getIid() == 5){

                //tab per le varie ricerche da locale e da db nazionale
                formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(CompanyForRiepilogoQuoteNazionaleFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_6)
                        .putParam(Params.ROW, "dt")
                        .putParam(Params.TAB, "Ricerca quote")
                        .putParam(Params.FORM_COLUMN, " ");
            }else{
                //tab per le varie ricerche da locale e da db nazionale
                formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(CompanyForTerritorySelectFieldrenderer.class))
                        .putParam(Params.COLS, Values.COLS_6)
                        .putParam(Params.ROW, "dt")
                        .putParam(Params.TAB, "Ricerca quote")
                        .putParam(Params.FORM_COLUMN, " ");
            }




            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Ricerca quote associative");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/quoteassociative/dettaglio/{id}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse view(HttpServletRequest request, @PathVariable long id, Long idWorker) {

        try {

            List<UiDettaglioQuota> quoteDetails = quoteFacade.getDettaglioQuota(id, idWorker);

            HashMap<String, Object> model = new HashMap<String, Object>();

            PartialViewRenderer renderer = new PartialViewRenderer();
            UiDettaglioQuotaView dettaglioView = new UiDettaglioQuotaView();
            String content = renderer.render(viewResolver, "quote/dettaglioQuota", model, LocaleContextHolder.getLocale(), request);
            dettaglioView.setContent(content);
            dettaglioView.setQuoteDetails(quoteDetails);
            dettaglioView.setQuotaCompanyId(quoteService.getRiepilogoQuotaById(id).getCompanyId());

            return new ValueResponse(dettaglioView);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/quoteassociative/delete/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse delete(@PathVariable long id) {

        try {
            quoteFacade.deleteQuota(id);
            return new ValueResponse("ok");
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping("/quoteassociative/{id}/downloadlog")
    public void downloadLogFile(@PathVariable("id") long id, HttpServletResponse response) {

        try {
            quoteFacade.downloadFile(id, response);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    @RequestMapping("/quoteassociative/{id}/downloadoriginal")
    public void downloadoriginalFile(@PathVariable("id") long id, HttpServletResponse response) {

        try {
            quoteFacade.downloadOriginalFile(id, response);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }


    @RequestMapping("/quoteassociative/{id}/downloadxml")
    public void downloadxmlFile(@PathVariable("id") long id, HttpServletResponse response) {

        try {
            quoteFacade.downloadXmlFile(id, response);
        } catch (IOException e) {
            e.printStackTrace();
            response.setStatus(500);
        }
    }

    @RequestMapping(value="/quote", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse findLocalLavoratori(@RequestParam(value="company", required=false) String company){



        try {

            User u = (User) sec.getLoggedUser();
            if (((applica.feneal.domain.model.Role) u.getRoles().get(0)).getIid() == 5){

                if (!StringUtils.isEmpty(company)){

                    List<RiepilogoQuoteAssociative> lavs = quoteFacade.findQuote(Long.parseLong(company));
                    return new ValueResponse(lavs);
                }else{
                    List<RiepilogoQuoteAssociative> lavs = quoteFacade.findQuote(-1);
                    return new ValueResponse(lavs);
                }


            }else{
                if (!StringUtils.isEmpty(company)){

                    List<RiepilogoQuoteAssociative> lavs = quoteFacade.findQuote(Long.parseLong(company));
                    return new ValueResponse(lavs);
                }
            }



            return new ValueResponse(null);

        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/quoteassociativeform", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse modifyCompetenceForm(HttpServletRequest request) {

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("quoteedit");

            FormDescriptor formDescriptor = new FormDescriptor(form);

            formDescriptor.addField("dataInizio", String.class, "Data inizio",null, applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt3")
                    .putParam(Params.FORM_COLUMN, "  ");

            formDescriptor.addField("dataFine", String.class, "Data fine",null, applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt3")
                    .putParam(Params.FORM_COLUMN, "  ");


            GregorianCalendar c = new GregorianCalendar();
            int year = c.get(Calendar.YEAR) + 1;


            Map<String, Object> data = new HashMap<>();
            data.put("dataInizio", String.format("01/01/%s", year));
            data.put("dataFine", String.format("31/12/%s", year));
            form.setData(data);

            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());

            return response;
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value="/modifycompetencequoteassociativeitem/{quotaId}", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse modifyCompetenceQuotaItems(@PathVariable long quotaId,@RequestBody UiQuoteHeaderParams data) {

        try{

            quoteFacade.modifyCompetenceQuotaItems(quotaId, data);

            return new ValueResponse("ok");
        }catch(Exception e){
            return new ErrorResponse(e.getMessage());
        }

    }
}


