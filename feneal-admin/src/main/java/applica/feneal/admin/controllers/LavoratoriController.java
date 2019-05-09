package applica.feneal.admin.controllers;

import applica.feneal.admin.facade.DelegheFacade;
import applica.feneal.admin.facade.LavoratoriFacade;
import applica.feneal.admin.fields.renderers.CompanyForTerritorySelectFieldrenderer;
import applica.feneal.admin.fields.renderers.LoggedUserProvinceNonOptionalSelectFieldRenderer;
import applica.feneal.admin.fields.renderers.SectorTypeWithoutInpsSelectRenderer;
import applica.feneal.admin.fields.renderers.uil.Attribuzione3SelectFieldRenderer;
import applica.feneal.admin.fields.renderers.uil.TesserePrintPositionFieldRenderer;
import applica.feneal.admin.utils.FormUtils;
import applica.feneal.admin.viewmodel.lavoratori.UiAnagrafica;
import applica.feneal.admin.viewmodel.lavoratori.UiCompleteLavoratoreSummary;
import applica.feneal.admin.viewmodel.lavoratori.UiWorkerIscrizioniChart;
import applica.feneal.admin.viewmodel.reports.UiIscrizione;
import applica.feneal.domain.data.core.lavoratori.LavoratoreLastVersionRepository;
import applica.feneal.domain.model.FenealEntities;
import applica.feneal.domain.model.Role;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.deleghe.Delega;
import applica.feneal.domain.model.core.lavoratori.FiscalData;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.LavoratoreLastVersion;
import applica.feneal.domain.model.core.lavoratori.search.LavoratoreSearchParams;
import applica.feneal.domain.model.geo.City;
import applica.feneal.domain.model.geo.Country;
import applica.feneal.services.DelegheService;
import applica.feneal.services.GeoService;
import applica.feneal.services.LavoratoreService;
import applica.feneal.services.exceptions.FormNotFoundException;
import applica.framework.LoadRequest;
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
import applica.framework.widgets.fields.renderers.DatePickerRenderer;
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
import java.rmi.RemoteException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fgran on 06/04/2016.
 */
@Controller
public class LavoratoriController {

    @Autowired
    private ViewResolver viewResolver;

    @Autowired
    private DelegheService delServ;
    @Autowired
    private DelegheFacade delFac;

    @Autowired
    private FormUtils formUtils;

    @Autowired
    private Security security;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private LavoratoriFacade tcFacade;

    @Autowired
    private LavoratoreService svc;


    @Autowired
    private GeoService geoSvc;

    @Autowired
    private LavoratoreLastVersionRepository lastversionRep;


    @RequestMapping(value = "/workerlastversion/{id}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse storicoData(@PathVariable long id) {

        try {
            LoadRequest req = LoadRequest.build().filter("lavoratoreId", id);
            List<LavoratoreLastVersion> lavs = lastversionRep.find(req).getRows();
           return new ValueResponse(lavs);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/workerlastversionpage/{id}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse viewStoricoData(HttpServletRequest request, @PathVariable long id) {

        try {

            UiCompleteLavoratoreSummary c = tcFacade.getLavoratoreSummaryById(id);
            HashMap<String, Object> model = new HashMap<String, Object>();
            model.put("worker", id);



            PartialViewRenderer renderer = new PartialViewRenderer();
            String content = renderer.render(viewResolver, "lavoratori/workerLastVersions", model, LocaleContextHolder.getLocale(), request);
            return new ValueResponse(content);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }




    @RequestMapping(value="/localworkers", method= RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse findLocalLavoratori(@RequestParam(value="name", required=false) String name,
                                                            @RequestParam(value="surname", required=false) String surname,
                                                            @RequestParam(value="fiscalcode", required=false) String fiscalcode,
                                                            @RequestParam(value="namesurname", required=false) String namesurname,

                                                            @RequestParam(value="birthDateFrom", required=false) String birthDateFrom,
                                                            @RequestParam(value="birthDateTo", required=false) String birthDateTo,
                                                            @RequestParam(value="caratteristica3", required=false) String caratteristica3,


                                                            @RequestParam(value="page", required=false) Integer page,
                                                            @RequestParam(value="company", required=false) String company){


        //se mnon cÃ¨ una pagina la imposto alla prima pagina

        if (page == null)
            page = 1;

        if (page == 0)
            page = 1;


        try {

            LavoratoreSearchParams s = new LavoratoreSearchParams();
            s.setName(name);
            s.setSurname(surname);
            s.setFiscalcode(fiscalcode);
            s.setNamesurname(namesurname);
            s.setBirthDateFrom(birthDateFrom);
            s.setBirthDateTo(birthDateTo);
            s.setCaratteristica3(caratteristica3);
            s.setPage(page);
            if (!StringUtils.isEmpty(company)){
                s.setCompanyId(Long.parseLong(company));
            }


            //se è un ricerca nazionale cerco su tutti i te


            List<Lavoratore> lavs = tcFacade.findLocalLavoratori(s);
            return new ValueResponse(lavs);

        } catch (Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }



    @RequestMapping(value = "/worker/summary/{id}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse view(HttpServletRequest request, @PathVariable long id) {

        try {

            UiCompleteLavoratoreSummary c = tcFacade.getLavoratoreSummaryById(id);
            HashMap<String, Object> model = new HashMap<String, Object>();
            model.put("summary", c);
            model.put("roleId", ((User) security.getLoggedUser()).retrieveUserRole().getLid());
//            UiWidgetManager m = widgetFacade.getEnabledWidgets("lavoratore");
//            model.put("widgets", m);

            model.put("displaySignalUser", true);
            model.put("isWorkerSigned", false);

            // Se esistono le credenziali per l'invio SMS allora visualizzo il pulsante relativo
            if (tcFacade.existSmsCredentials()){
                if (((Role) security.getLoggedUser().getRoles().get(0)).getLid() > 4)
                    model.put("existSMSCredentials", false);
                else
                    model.put("existSMSCredentials", true);
            }
            else
                model.put("existSMSCredentials", false);



            PartialViewRenderer renderer = new PartialViewRenderer();
            String content = renderer.render(viewResolver, "lavoratori/workerSummary", model, LocaleContextHolder.getLocale(), request);
            return new ValueResponse(content);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/workers/search",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse searchview(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(SearchFormRenderer.class));
            form.setIdentifier("worker");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            //tab per le varie ricerche da locale e da db nazionale
            formDescriptor.addField("surname", String.class, "Cognome", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_6)
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("name", String.class, "Nome", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_6)
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("fiscalcode", String.class, "Cod. fisc.", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_6)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("birthDateFrom", String.class, "Nato dal", null, applicationContext.getBean(DatePickerRenderer.class))
                    .putParam(Params.COLS, Values.COLS_7)
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("birthDateTo", String.class, "Nato fino al", null, applicationContext.getBean(DatePickerRenderer.class))
                    .putParam(Params.COLS, Values.COLS_7)
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("caratteristica3", String.class, "Incarico", null, applicationContext.getBean(Attribuzione3SelectFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_7)
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");



            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Ricerca lavoratori");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }



    @RequestMapping(value = "/workers/search/nazionale",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse searchviewForNazionale(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(SearchFormRenderer.class));
            form.setIdentifier("worker");

            FormDescriptor formDescriptor = new FormDescriptor(form);


            User u = ((User) security.getLoggedUser()) ;
            Role  r = ((Role) u.getRoles().get(0));


            //tab per le varie ricerche da locale e da db nazionale

            if (!r.getRole().equals("NAZIONALE") && !r.getRole().equals("NAZIONALE UNC")  ){
                formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(CompanyForTerritorySelectFieldrenderer.class))
                        .putParam(Params.COLS, Values.COLS_6)
                        .putParam(Params.ROW, "dt")
                        .putParam(Params.TAB, "Ricerca locale")
                        .putParam(Params.FORM_COLUMN, " ");
            }

            formDescriptor.addField("surname", String.class, "Cognome", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_6)
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("name", String.class, "Nome", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_6)
                    .putParam(Params.ROW, "dt")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("fiscalcode", String.class, "Cod. fisc.", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_6)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("birthDateFrom", String.class, "Nato dal", null, applicationContext.getBean(DatePickerRenderer.class))
                    .putParam(Params.COLS, Values.COLS_7)
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("birthDateTo", String.class, "Nato fino al", null, applicationContext.getBean(DatePickerRenderer.class))
                    .putParam(Params.COLS, Values.COLS_7)
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.TAB, "Ricerca locale")
                    .putParam(Params.FORM_COLUMN, " ");




            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Ricerca lavoratori");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }






    @RequestMapping(value = "/worker",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse create(HttpServletRequest request) {

        try {
            Form form = formUtils.generateFormForEntity(FenealEntities.LAVORATORE, null);
            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Creazione anagrafica");


            return response;


        } catch (FormCreationException | CrudConfigurationException | FormNotFoundException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }






    @RequestMapping(value = "/worker/remote/{fiscalCode}/{companyId}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public String viewRemoteWorkerAnagrafica(HttpServletRequest request, @PathVariable String fiscalCode, @PathVariable long companyId) throws Exception {


        try {

            //qui recupero se cè l'id del lavoratore con quel codice fiscale
            //e se non cè lo predno dalla tabella dei lavoratori del db nazionel e lo creo per il territorio dell'utente
            //corrente
            long idWorker = tcFacade.getIdLavoratoreByFiscalCode(fiscalCode, companyId);
            if (idWorker == -1)
                throw new Exception("Lavoratore non trovato");

            return "redirect:/worker/summary/" + String.valueOf(idWorker);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }





    @RequestMapping(value = "/worker/{id}",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse update(HttpServletRequest request, @PathVariable long id) {


        try {
            Form form = formUtils.generateFormForEntity(FenealEntities.LAVORATORE, null);
            Lavoratore d = svc.getLavoratoreById(((User) security.getLoggedUser()).getLid(),id);

            if (d!= null){
                Map<String, Object> data = new HashMap<>();
                data.put("id", id);

                data.put("privacy", d.isPrivacy());
                data.put("name", d.getName());
                data.put("surname", d.getSurname());
                data.put("sex", d.getSex());
                data.put("fiscalcode", d.getFiscalcode());
                data.put("birthDate", d.getBirthDate());
                data.put("image", d.getImage());

                if (StringUtils.isEmpty(d.getNationality()))
                    data.put("nationality", null);
                else
                    data.put("nationality", geoSvc.getCountryByName(d.getNationality()));

                if (StringUtils.isEmpty(d.getBirthProvince()))
                    data.put("birthProvince", null);
                else
                    data.put("birthProvince", geoSvc.getProvinceByName(d.getBirthProvince()));

                if (StringUtils.isEmpty(d.getBirthPlace()))
                    data.put("birthPlace", null);
                else
                    data.put("birthPlace", geoSvc.getCityByName(d.getBirthPlace()));

                if (StringUtils.isEmpty(d.getLivingProvince()))
                    data.put("livingProvince", null);
                else
                    data.put("livingProvince", geoSvc.getProvinceByName(d.getLivingProvince()));

                if (StringUtils.isEmpty(d.getLivingCity()))
                    data.put("livingCity", null);
                else
                    data.put("livingCity", geoSvc.getCityByName(d.getLivingCity()));
                data.put("addressco", d.getAddressco());
                data.put("address", d.getAddress());
                data.put("cap", d.getCap());
                data.put("phone", d.getPhone());
                data.put("cellphone", d.getCellphone());
                data.put("mail", d.getMail());
//                data.put("ce", d.getCe());
//                data.put("ec", d.getEc());


                data.put("attribuzione1", d.getAttribuzione1());
                data.put("attribuzione2", d.getAttribuzione2());
                data.put("attribuzione4", d.getAttribuzione3());
                data.put("fund", d.getFund());
                data.put("notes", d.getNotes());


                form.setData(data);
            }


            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Aggiornamento anagrafica");


            return response;


        } catch (FormCreationException | CrudConfigurationException | FormNotFoundException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }



    @RequestMapping(value = "/worker", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse save(@RequestBody UiAnagrafica anag) {

        try {
            long id = tcFacade.saveAnagrafica(anag);
            return new ValueResponse(id);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/worker/{id}/iscrizionichart", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse save(@PathVariable long id) {

        try {
            UiWorkerIscrizioniChart chartData = tcFacade.getIscrizioniChart(id);
            return new ValueResponse(chartData);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/worker/{id}/deleghedetail", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse iscrizionidetailDeleghe(@PathVariable long id) {

        try {
            List<Delega> a = delServ.getAllWorkerDeleghe(id);
            return new ValueResponse(delFac.convertDelegheToUiDeleghe(a));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/worker/{id}/deleghedetailunc", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse iscrizionidetailDelegheUnc(@PathVariable long id) {

        try {
            List<Delega> a = delServ.getAllWorkerDeleghe(id);
            return new ValueResponse(delFac.convertDelegheToUiDeleghe(a));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/worker/{id}/deleghedetailbil", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse iscrizionidetailDelegheBil(@PathVariable long id) {

        try {
            List<Delega> a = delServ.getAllWorkerDeleghe(id);
            return new ValueResponse(delFac.convertDelegheToUiDeleghe(a));
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }





    @RequestMapping(value = "/worker/{id}/iscrizionidetail", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse iscrizionidetail(@PathVariable long id) {

        try {
            List<UiIscrizione>chartData = tcFacade.getIscrizioniDetails(id);
            return new ValueResponse(chartData);
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }





    @RequestMapping(value = "/worker/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse delete(@PathVariable long id) {

        try {
            tcFacade.deleteLavoratore(id);
            return new ValueResponse("ok");
        } catch(Exception e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }

    }





    @RequestMapping(value = "/worker/fiscalcode", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse calculateFiscalcode(String name, String surname, String dateBirth, Integer birthPlaceId,  String sex, Integer birthNationId) throws ParseException, RemoteException {


        if (birthNationId == null)
            birthNationId = 0;
        if (birthPlaceId == null)
            birthPlaceId = 0;

        Country c = geoSvc.getCountryById(birthNationId);
        City cc = geoSvc.getCityById(birthPlaceId);

        String countryName = c != null? c.getDescription():"";
        String cityName = cc != null? cc.getDescription():"";

        SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
        Date d = ff.parse(dateBirth);

        return new ValueResponse(geoSvc.calculateFiscalCode(name, surname,d,sex,cityName, countryName));

    }

    @RequestMapping(value = "/worker/fiscaldata", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse calculateFiscalData(String fiscalcode) throws RemoteException {

        FiscalData fd = tcFacade.getFiscalData(fiscalcode);

        return new ValueResponse(fd);

    }

    @RequestMapping(value = "/worker/tessera/print", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showPrintTessera(HttpServletRequest request) {

        //Lavoratore lavoratore = lavoratoriRepository.get(workerId).orElseThrow(() -> new RuntimeException("Worker does not exist"));

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("printtessera");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("province", String.class, "Provincia", null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class)).putParam(Params.ROW, "dt").putParam(Params.COLS, Values.COLS_12);
            formDescriptor.addField("sector", String.class, "Settore", null, applicationContext.getBean(SectorTypeWithoutInpsSelectRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_12);
            formDescriptor.addField("position", String.class, "Posizione stampa", null, applicationContext.getBean(TesserePrintPositionFieldRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_12);

            Map<String, Object> data = new HashMap<>();

            form.setData(data);

            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());

            return response;
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }

    }






}


