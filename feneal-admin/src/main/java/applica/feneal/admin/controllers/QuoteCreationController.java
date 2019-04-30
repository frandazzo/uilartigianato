package applica.feneal.admin.controllers;

import applica.feneal.admin.facade.QuoteCreationFacade;
import applica.feneal.admin.fields.renderers.*;
import applica.feneal.admin.form.renderers.QuoteImpiantiFormRenderer;
import applica.feneal.admin.viewmodel.quote.UiDettaglioQuota;
import applica.feneal.admin.viewmodel.quote.UiPagamentoDeleghe;
import applica.feneal.admin.viewmodel.quote.UiQuoteLavoratori;
import applica.feneal.admin.viewmodel.quote.UiRiepilogoQuota;
import applica.feneal.domain.data.core.quote.QuoteAssocRepository;
import applica.feneal.domain.model.core.deleghe.DelegaPerCreazioneQuota;
import applica.feneal.domain.model.core.quote.RiepilogoQuoteAssociative;
import applica.feneal.domain.model.core.quote.UpdatableDettaglioQuota;
import applica.feneal.domain.model.core.servizi.search.UiQuoteHeaderParams;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.FormResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.library.ui.PartialViewRenderer;
import applica.framework.widgets.CrudConfigurationException;
import applica.framework.widgets.Form;
import applica.framework.widgets.FormCreationException;
import applica.framework.widgets.FormDescriptor;
import applica.framework.widgets.fields.Params;
import applica.framework.widgets.fields.Values;
import applica.framework.widgets.fields.renderers.DatePickerRenderer;
import applica.framework.widgets.fields.renderers.DefaultFieldRenderer;
import applica.framework.widgets.fields.renderers.HiddenFieldRenderer;
import applica.framework.widgets.forms.renderers.DefaultFormRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ViewResolver;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Created by angelo on 15/04/2016.
 */
@Controller
public class QuoteCreationController {

    @Autowired
    private QuoteCreationFacade quoteCreationFacade;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private QuoteAssocRepository riepilogoQuoteRep;

    @Autowired
    private ViewResolver viewResolver;


    @RequestMapping(value = "/quoteimpiantifissinew",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse riepilogo(HttpServletRequest request) {

        try {

            HashMap<String, Object> model = new HashMap<String, Object>();

            PartialViewRenderer renderer = new PartialViewRenderer();
            String content = renderer.render(viewResolver, "quote/creaquote", model, LocaleContextHolder.getLocale(), request);
            return new ValueResponse(content);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }



    @RequestMapping(value="/aziende/creaquote", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse findLocalAziende(@RequestParam(value="name", required=true) String name){


        try {


            List<DelegaPerCreazioneQuota> result = quoteCreationFacade.searchAziendeByName(name);

            return new ValueResponse(result);

        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }




    @RequestMapping(value="/quoteimpiantifissi/proceed", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
        SimpleResponse recuperaDelegheLavoratoriAzienda(@RequestBody UiQuoteHeaderParams params){

        List<UiQuoteLavoratori> f;
        try{
            f = quoteCreationFacade.retrieveDelegeLavoratoriForAzienda(params);
            return new ValueResponse(f);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }

    @RequestMapping(value = "/quoteimpiantifissi",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse viewQuoteForm(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(QuoteImpiantiFormRenderer.class));
            form.setIdentifier("quoteimpianti");

            FormDescriptor formDescriptor = new FormDescriptor(form);

            formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(CompanyForTerritorySelectFieldrenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.FORM_COLUMN, " ");

            //poichè questa operazione è permessa solo al nazionale il componente
            // "LoggedUserProvinceNonOptionalSelectFieldRenderer" non troverà alcuna provincia
            //lascio stare il componente anche se dovrei , in verità, creare un emptySelectFieldRendere
            //che viene valorizzato da script lato client
            formDescriptor.addField("province", String.class, "Provincia", null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("firm", String.class, "Azienda", null, applicationContext.getBean(AziendeForCompanySingleSearchbleFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("dataInizio", String.class, "Data inizio",null, applicationContext.getBean(DatePickerRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt3")
                    .putParam(Params.FORM_COLUMN, "  ");

            formDescriptor.addField("dataFine", String.class, "Data fine",null, applicationContext.getBean(DatePickerRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt3")
                    .putParam(Params.FORM_COLUMN, "  ");

            formDescriptor.addField("amount", Double.class, "Importo", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt2")
                    .putParam(Params.FORM_COLUMN, "  ");


            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Creazione quote per azienda");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }






    @RequestMapping(value = "/duplicatequoteform", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse duplicatequoteform(HttpServletRequest request) {

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("quoteduplicate");

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

    @RequestMapping(value = "/quoteitem/{quotaId}", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showAddItemForm(HttpServletRequest request, @PathVariable long quotaId) {

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("quoteitem");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("worker", String.class, "Lavoratore",null, applicationContext.getBean(WorkerForCompanySingleSearchbleFieldrenderer.class))
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.COLS, Values.COLS_12);

            formDescriptor.addField("firm", String.class, "Azienda", null, applicationContext.getBean(AziendeForCompanySingleSearchbleFieldRenderer.class))
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.COLS, Values.COLS_12);

            formDescriptor.addField("dataInizio", String.class, "Data inizio",null, applicationContext.getBean(DatePickerRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt2")
                    .putParam(Params.FORM_COLUMN, "  ");

            formDescriptor.addField("dataFine", String.class, "Data fine",null, applicationContext.getBean(DatePickerRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt3")
                    .putParam(Params.FORM_COLUMN, "  ");

            formDescriptor.addField("amount", Double.class, "Importo", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.ROW, "dt4")
                    .putParam(Params.COLS, Values.COLS_12);


            formDescriptor.addField("contract", String.class, "Contratto", null, applicationContext.getBean(ContractSelectFieldRenderer.class))
                    .putParam(Params.ROW, "dt5")
                    .putParam(Params.COLS, Values.COLS_12);


            formDescriptor.addField("level", String.class, "Livello", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.COLS, Values.COLS_12);


            formDescriptor.addField("notes", String.class, "Note", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.ROW, "dt7")
                    .putParam(Params.COLS, Values.COLS_12);


            formDescriptor.addField("settore", String.class, "Settore", null, applicationContext.getBean(HiddenFieldRenderer.class))
                    .putParam(Params.ROW, "dt8")
                    .putParam(Params.COLS, Values.COLS_12);




            //recupero il settore della quota
            RiepilogoQuoteAssociative r = riepilogoQuoteRep.get(quotaId).orElse(null);
            if (r == null)
                return new ErrorResponse("Quota non trovata");

            GregorianCalendar c = new GregorianCalendar();
            int year = c.get(Calendar.YEAR) + 1;

            Map<String, Object> data = new HashMap<>();

            data.put("amount", 0);
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


    @RequestMapping(value="/duplicaquoteitem/{quotaId}", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse duplicaQuota(@PathVariable long quotaId,@RequestBody UiQuoteHeaderParams data) {

        try{

            quoteCreationFacade.duplicaQuota(quotaId, data);

            return new ValueResponse("ok");
        }catch(Exception e){
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value="/quoteitem/add/{quotaId}", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse createQuoteItem(@PathVariable long quotaId,@RequestBody UiQuoteHeaderParams data) {

        try{

            UiDettaglioQuota r = quoteCreationFacade.addDettaglioQuota(quotaId, data);

            return new ValueResponse(r);
        }catch(Exception e){
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value="/quoteitem/delete/{quotaId}/{itemId}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse delQuoteItem(@PathVariable long quotaId,@PathVariable long itemId) {

        try{

            quoteCreationFacade.deleteDettaglioQuota(quotaId, itemId);

            return new ValueResponse("ok");
        }catch(Exception e){
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/deleghe/pagamento", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showListaLavoroOperations() {

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("pagamento");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("importo", Double.class, "Importo pagamento", null, applicationContext.getBean(DefaultFieldRenderer.class));


            formDescriptor.addField("dataInizio", String.class, "Data inizio",null, applicationContext.getBean(DatePickerRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt3")
                    .putParam(Params.FORM_COLUMN, "  ");

            formDescriptor.addField("dataFine", String.class, "Data fine",null, applicationContext.getBean(DatePickerRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt3")
                    .putParam(Params.FORM_COLUMN, "  ");




            Map<String, Object> data = new HashMap<>();

            form.setData(data);

            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());

            return response;
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }

    }







    @RequestMapping(value="/quoteitem/update/{quotaId}/{itemId}", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse updateQuoteItem(@PathVariable long quotaId, @PathVariable long itemId, @RequestBody UpdatableDettaglioQuota updatedData) {

        try{

            quoteCreationFacade.updateDettaglioQuota(quotaId, itemId, updatedData);

            return new ValueResponse("ok");
        }catch(Exception e){
            return new ErrorResponse(e.getMessage());
        }

    }




    @RequestMapping(value="/quoteimpiantifissi/createquote", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse createQuotePerAzienda(@RequestBody UiRiepilogoQuota data) {

        try{
            return new ValueResponse(quoteCreationFacade.createRiepilogoQuote(data));
        }catch(Exception e){
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/deleghe/pagamento/execute", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showListaLavoroOperations(@RequestBody UiPagamentoDeleghe pagamento) {

        try {

            quoteCreationFacade.createPagamentoMassivo(pagamento);

            return new ValueResponse("OK");
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }

    }



}


