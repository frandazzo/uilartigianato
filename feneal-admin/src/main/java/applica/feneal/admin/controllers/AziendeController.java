package applica.feneal.admin.controllers;

import applica.feneal.admin.facade.AziendeFacade;
import applica.feneal.admin.facade.DelegheFacade;
import applica.feneal.admin.facade.IscrittiFacade;
import applica.feneal.admin.fields.renderers.CompanyForTerritorySelectFieldrenderer;
import applica.feneal.admin.fields.renderers.geo.OptionalCityFieldRenderer;
import applica.feneal.admin.fields.renderers.geo.OptionalProvinceFieldRenderer;
import applica.feneal.admin.form.renderers.MulticolumnFormRenderer;
import applica.feneal.admin.form.renderers.ReportsSearchFormRenderer;
import applica.feneal.admin.viewmodel.aziende.UiAnagraficaAzienda;
import applica.feneal.admin.viewmodel.aziende.UiCompleteAziendaSummary;
import applica.feneal.domain.data.core.aziende.AziendeRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.aziende.Azienda;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.services.AziendaService;
import applica.feneal.services.DelegheService;
import applica.feneal.services.GeoService;
import applica.feneal.services.impl.report.fastPerformanceData.Iscritto;
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
import applica.framework.widgets.fields.renderers.HiddenFieldRenderer;
import applica.framework.widgets.fields.renderers.TextAreaFieldRenderer;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fgran on 07/04/2016.
 */
@Controller
public class AziendeController {

    @Autowired
    private ViewResolver viewResolver;

    @Autowired
    private Security security;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private AziendeFacade tcFacade;


    @Autowired
    private GeoService geoSvc;


    @Autowired
    private IscrittiFacade iscrittiFacade;


    @Autowired
    private AziendaService svc;

    @Autowired
    private DelegheService delSrv;


    @Autowired
    private AziendeRepository azRep;


    @Autowired
    private DelegheFacade delfac;



    @RequestMapping(value = "/firm/{id}/deleghe", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse iscrittiDeleghe(@PathVariable long id) {

        try {
            List<Delega> l = delSrv.getDelegheForAzienda(id);
            return new ValueResponse(delfac.convertDelegheToUiDeleghe(l));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/firm/{id}/iscrizioni", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse iscritti(@PathVariable long id) {

        try {
            List<Iscritto> l = iscrittiFacade.findCurrentIscrizioniForAzienda(id);


            return new ValueResponse(l);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }



    @RequestMapping(value = "/firm/remote/{companyId}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String viewRemoteWorkerAnagrafica(HttpServletRequest request, @PathVariable long companyId, String description) throws Exception {


        try {

            //qui recupero se cè l'id dell'azienda con quella ragione sociale
            //e se non cè lo predno dalla tabella dei lavoratori del db nazionel e lo creo per il territorio dell'utente
            //corrente
            Azienda az = svc.getAziendaByDescription(description, companyId);
            if (az == null)
                throw new Exception("Azienda non trovata");

            return "redirect:/firm/summary/" + String.valueOf(az.getLid());
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }





    @RequestMapping(value = "/firm/summary/{id}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse view(HttpServletRequest request, @PathVariable long id) {

        try {

            UiCompleteAziendaSummary c = tcFacade.getFirmById(id);
            HashMap<String, Object> model = new HashMap<String, Object>();
            model.put("summary", c);

            PartialViewRenderer renderer = new PartialViewRenderer();
            String content = renderer.render(viewResolver, "aziende/firmSummary", model, LocaleContextHolder.getLocale(), request);
            return new ValueResponse(content);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/firm",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse create(HttpServletRequest request) {



        try {
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(MulticolumnFormRenderer.class));
            form.setIdentifier("firm");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("id", String.class, "id", null, applicationContext.getBean(HiddenFieldRenderer.class)).putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("description", String.class, "Ragione sociale", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12).putParam(Params.ROW, "dt").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("alternativeDescription", String.class, "Descrizione alternativa", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12).putParam(Params.ROW, "dt").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("province", String.class, "Provincia", "", applicationContext.getBean(OptionalProvinceFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("city", String.class, "Città", "", applicationContext.getBean(OptionalCityFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("address", String.class, "Indir.", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("cap", String.class, "CAP", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("phone", String.class, "Telefono", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("piva", String.class, "P. Iva", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");



            formDescriptor.addField("notes", String.class, "Note", "", applicationContext.getBean(TextAreaFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12).putParam(Params.ROW, "dt2").putParam(Params.FORM_COLUMN, " ");


            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Creazione azienda");


            return response;


        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/firm/{id}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse update(HttpServletRequest request, @PathVariable long id) {



        try {
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(MulticolumnFormRenderer.class));
            form.setIdentifier("firm");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("id", String.class, "id", null, applicationContext.getBean(HiddenFieldRenderer.class)).putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("description", String.class, "Ragione sociale", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12).putParam(Params.ROW, "dt").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("alternativeDescription", String.class, "Descrizione alternativa", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12).putParam(Params.ROW, "dt").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("province", String.class, "Provincia", "", applicationContext.getBean(OptionalProvinceFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("city", String.class, "Città", "", applicationContext.getBean(OptionalCityFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("address", String.class, "Indir.", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("cap", String.class, "CAP", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("phone", String.class, "Telefono", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("piva", String.class, "P. Iva", "", applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.COLS, Values.COLS_6).putParam(Params.ROW, "dt1").putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("notes", String.class, "Note", "", applicationContext.getBean(TextAreaFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12).putParam(Params.ROW, "dt2").putParam(Params.FORM_COLUMN, " ");

            Azienda d = svc.getAziendaById(((User) security.getLoggedUser()).getLid(),id);

            if (d!= null){
                Map<String, Object> data = new HashMap<>();

                data.put("id", id);
                data.put("description", d.getDescription());
                data.put("alternativeDescription", d.getAlternativeDescription());

                if (StringUtils.isEmpty(d.getProvince()))
                    data.put("province", null);
                else
                    data.put("province", geoSvc.getProvinceByName(d.getProvince()));

                if (StringUtils.isEmpty(d.getCity()))
                    data.put("city", null);
                else
                    data.put("city", geoSvc.getCityByName(d.getCity()));

                data.put("address", d.getAddress());
                data.put("notes", d.getNotes());
                data.put("piva", d.getPiva());
                data.put("cap", d.getCap());
                data.put("phone", d.getPhone());

                form.setData(data);
            }


            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Aggiornamento azienda");


            return response;


        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }



    @RequestMapping(value = "/firm", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse save(@RequestBody UiAnagraficaAzienda anag) {

        try {
            long id = tcFacade.saveAnagrafica(anag);
            return new ValueResponse(id);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/searchfirm", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse search(@RequestBody UiAnagraficaAzienda azienda) {

        try {



            return new ValueResponse(tcFacade.searchAziende(azienda.getDescription()));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }



    @RequestMapping(value = "/firm/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse delete(@PathVariable long id) {

        try {
            tcFacade.deleteAzienda(id);
            return new ValueResponse("ok");
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/firm/search/nazionale",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse searchviewForNazionale(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(SearchFormRenderer.class));
            form.setIdentifier("worker");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            //tab per le varie ricerche da locale e da db nazionale
            formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(CompanyForTerritorySelectFieldrenderer.class))
                    .putParam(Params.COLS, Values.COLS_6)
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("description", String.class, "Ragione Sociale", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_6)
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");




            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Ricerca aziende");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/firm/search/globalnazionale",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse searchglobalnazionaleViewForNazionale(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(ReportsSearchFormRenderer.class));
            form.setIdentifier("firmssearch");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            //tab per le varie ricerche da locale e da db nazionale
            formDescriptor.addField("description", String.class, "Ragione Sociale", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt")
                    //.putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");




            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Ricerca aziende");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value="/localfirms", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse findLocalLavoratori(@RequestParam(value="description", required=false) String description,
                                                            @RequestParam(value="company", required=false) String company){


        try {


            List<Azienda> aziende = tcFacade.searchAziende(description, company);
            return new ValueResponse(aziende);

        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }




}
