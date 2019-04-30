package applica.feneal.admin.controllers;

import applica.feneal.admin.fields.renderers.CausaleComunicazioneFieldRenderer;
import applica.feneal.admin.fields.renderers.EmailLayoutSelectFieldRenderer;
import applica.feneal.admin.fields.renderers.LoggedUserProvinceNonOptionalSelectFieldRenderer;
import applica.feneal.admin.fields.renderers.uil.CategoryButMineSelectFieldrenderer;
import applica.feneal.admin.viewmodel.lavoratori.UiListaLavoroSendEmail;
import applica.feneal.admin.viewmodel.lavoratori.UiListaLavoroSendSms;
import applica.feneal.domain.data.core.lavoratori.LavoratoriRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.Company;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.geo.Province;
import applica.feneal.services.ComunicazioniService;
import applica.feneal.services.GeoService;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.FormResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.security.Security;
import applica.framework.widgets.Form;
import applica.framework.widgets.FormDescriptor;
import applica.framework.widgets.fields.Params;
import applica.framework.widgets.fields.Values;
import applica.framework.widgets.fields.renderers.DefaultFieldRenderer;
import applica.framework.widgets.fields.renderers.HtmlFieldRenderer;
import applica.framework.widgets.fields.renderers.TextAreaFieldRenderer;
import applica.framework.widgets.forms.renderers.DefaultFormRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by angelo on 30/04/2016.
 */
@Controller
public class ComunicazioniController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ComunicazioniService comSvc;

    @Autowired
    private LavoratoriRepository lavoratoriRepository;



    @Autowired
    private Security sec;

    @Autowired
    private GeoService geoSvc;



    @RequestMapping(value = "/comunicazioni/sms", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showSms(Long workerId, HttpServletRequest request) {

        Lavoratore lavoratore = lavoratoriRepository.get(workerId).orElseThrow(() -> new RuntimeException("Worker does not exist"));

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("sms");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("province", String.class, "Provincia", null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class)).putParam(Params.ROW, "dt").putParam(Params.COLS, Values.COLS_12);
            formDescriptor.addField("cell", String.class, "Num.", null, applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_6);
            formDescriptor.addField("causaleCom", String.class, "Causale", null, applicationContext.getBean(CausaleComunicazioneFieldRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_6);
            formDescriptor.addField("text", String.class, "Testo (max 160 car.)", null, applicationContext.getBean(TextAreaFieldRenderer.class)).putParam(Params.ROW, "dt2");

            Map<String, Object> data = new HashMap<>();

            data.put("cell", lavoratore.getCellphone().replaceAll("\\+39", "").replaceAll("\\s+", ""));
            form.setData(data);

            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());

            return response;
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/comunicazioni/creditsms", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse residualCreditSMS(HttpServletRequest request) {

        try {

            return new ValueResponse(comSvc.getResidualCredit());

        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/comunicazioni/sms/fiscalcode", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showSmsWithFiscalCode(String fiscalCode, HttpServletRequest request) throws Exception {

        Company c = ((User) sec.getLoggedUser()).getCompany();
        Lavoratore lavoratore = lavoratoriRepository.searchLavoratoreForCompany(c.getLid(), fiscalCode);
        if (lavoratore == null)
            throw new Exception("Lavoratore inesistente: cf " + fiscalCode);


        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("sms");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("cell", String.class, "Num.", null, applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_6);
            formDescriptor.addField("causaleCom", String.class, "Causale", null, applicationContext.getBean(CausaleComunicazioneFieldRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_6);
            formDescriptor.addField("text", String.class, "Testo (max 160 car.)", null, applicationContext.getBean(TextAreaFieldRenderer.class)).putParam(Params.ROW, "dt2");

            Map<String, Object> data = new HashMap<>();

            data.put("cell", lavoratore.getCellphone().replaceAll("\\+39", "").replaceAll("\\s+", ""));
            form.setData(data);

            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());

            return response;
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }

    }


    @RequestMapping(value = "/comunicazioni/sendsms", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse sendSms(@RequestParam(value = "workerId", required = true) Long workerId,
                           @RequestParam(value = "province", required = true) String province,
                           @RequestParam(value = "cell", required = true) String cell,
                           @RequestParam(value = "causaleCom", required = true) String causaleCom,
                           @RequestParam(value = "text", required = true) String text) throws Exception {

        Province p = geoSvc.getProvinceById(Integer.parseInt(province));
        comSvc.sendSms(cell,workerId,causaleCom,text,p.getDescription());
        return new ValueResponse("OK");
    }

    /* Segnalazione lavoratore*/
    @RequestMapping(value = "/comunicazioni/signalworker", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showSignalWorker(Long workerId, boolean workerSignedToFenealProvince, HttpServletRequest request) throws Exception {

        Lavoratore lavoratore = lavoratoriRepository.get(workerId).orElse(null);
        if (lavoratore == null)
            throw new Exception("Il lavoratore non esiste");

        try {

            Map<String, Object> data = new HashMap<>();
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("signalworker");

            FormDescriptor formDescriptor = new FormDescriptor(form);

            formDescriptor.addField("category", String.class, "Categoria", null, applicationContext.getBean(CategoryButMineSelectFieldrenderer.class)).putParam(Params.COLS, Values.COLS_12);
            formDescriptor.addField("text", String.class, "Testo", null, applicationContext.getBean(HtmlFieldRenderer.class));

            form.setData(data);

            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());

            return response;
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }
    }




    @RequestMapping(value = "/comunicazioni/listalavoro/sms", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showListaLavoroMultipleSms(HttpServletRequest request) throws Exception {

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("listalavorosms");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("province", String.class, "Provincia", null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class)).putParam(Params.ROW, "dt1");
            formDescriptor.addField("campagna", String.class, "Campagna", null, applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.ROW, "dt2").putParam(Params.COLS, Values.COLS_12);
            formDescriptor.addField("text", String.class, "Testo", null, applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.ROW, "dt3").putParam(Params.COLS, Values.COLS_12);

            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());

            return response;
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }

    }

    @RequestMapping(value = "/comunicazioni/listalavoro/sendsms", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse sendSmsListaLavoro(@RequestBody UiListaLavoroSendSms uiListaSms) {

        try {
            Province p = geoSvc.getProvinceById(Integer.parseInt(uiListaSms.getProvince()));
            comSvc.sendSmsToMultipleWorkers(uiListaSms.getLavoratori(), uiListaSms.getText(), p.getDescription(), uiListaSms.getCampagna());

            return new ValueResponse("OK");
        } catch(Exception e) {
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/comunicazioni/listalavoro/email", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showListaLavoroMultipleEmail(HttpServletRequest request) throws Exception {

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("listalavoroemail");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("layout", String.class, "Layout", null, applicationContext.getBean(EmailLayoutSelectFieldRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_6);
            formDescriptor.addField("province", String.class, "Provincia", null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_6);
            formDescriptor.addField("campagna", String.class, "Campagna", null, applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.ROW, "dt2").putParam(Params.COLS, Values.COLS_6);
            formDescriptor.addField("subject", String.class, "Oggetto", null, applicationContext.getBean(DefaultFieldRenderer.class)).putParam(Params.ROW, "dt2").putParam(Params.COLS, Values.COLS_6);
            formDescriptor.addField("text", String.class, "", null, applicationContext.getBean(HtmlFieldRenderer.class)).putParam(Params.ROW, "dt3");

            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());

            return response;
        } catch (Exception e) {
            return new ErrorResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/comunicazioni/listalavoro/sendemail", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse sendEmailListaLavoro(@RequestBody UiListaLavoroSendEmail uiListaEmail) {

        try {
            comSvc.sendMailingList(uiListaEmail.getLavoratori(), uiListaEmail.getText(), uiListaEmail.getSubject(), uiListaEmail.getProvince(), uiListaEmail.getCampagna());

            return new ValueResponse("OK");
        } catch(Exception e) {
            return new ErrorResponse(e.getMessage());
        }
    }
}
