package applica.feneal.admin.controllers;

import applica.feneal.admin.facade.IscrittiFacade;
import applica.feneal.admin.fields.renderers.*;
import applica.feneal.admin.fields.renderers.uil.TesserePrintPositionFieldRenderer;
import applica.feneal.admin.form.renderers.ReportsSearchFormRenderer;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.servizi.search.UiIscrittoReportSearchParams;
import applica.feneal.services.impl.report.fastPerformanceData.Iscritto;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.FormResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.security.Security;
import applica.framework.widgets.CrudConfigurationException;
import applica.framework.widgets.Form;
import applica.framework.widgets.FormCreationException;
import applica.framework.widgets.FormDescriptor;
import applica.framework.widgets.fields.Params;
import applica.framework.widgets.fields.Values;
import applica.framework.widgets.forms.renderers.DefaultFormRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fgran on 15/04/2016.
 */
@Controller
public class ReportIscrittiController {
    @Autowired
    private Security security;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IscrittiFacade iscrittiReportFac;

    @RequestMapping(value="/iscritti/report", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse reportIscritti(@RequestBody UiIscrittoReportSearchParams params){
        List<Iscritto> f;
        try{
            f = iscrittiReportFac.reportIscritti(params);
            return new ValueResponse(f);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }



    @RequestMapping(value = "/iscritti",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse searchview(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(ReportsSearchFormRenderer.class));
            form.setIdentifier("iscrittireport");

            FormDescriptor formDescriptor = new FormDescriptor(form);

            if (((User) security.getLoggedUser()).getCompany().getTipoConfederazione() == 1){

                formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(CompanyForTerritorySelectFieldrenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt1")
                        .putParam(Params.FORM_COLUMN, " ");

            }else{
                formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(OptionalCompanyFotTerritorySelectFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt1")
                        .putParam(Params.FORM_COLUMN, " ");
            }
            //poichè questa operazione è permessa solo al nazionale il componente
            // "LoggedUserProvinceNonOptionalSelectFieldRenderer" non troverà alcuna provincia
            //lascio stare il componente anche se dovrei , in verità, creare un emptySelectFieldRendere
            //che viene valorizzato da script lato client
            formDescriptor.addField("province", String.class, "Provincia", null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("date", String.class, "Da", null, applicationContext.getBean(DateFromMonthFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");
            formDescriptor.addField("date", String.class, "a", null, applicationContext.getBean(DateToMonthFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt2")
                    .putParam(Params.FORM_COLUMN, " ");


            if (((User) security.getLoggedUser()).getCompany().getTipoConfederazione() == 1){
                //se non si parla di contesto regionale ma di contesto nazionale abilito il flag
                formDescriptor.addField("delegationActiveExist", Boolean.class, "Verifica esistenza delega attiva", "", applicationContext.getBean(CustomCheckboxFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt4")
                        .putParam(Params.FORM_COLUMN, " ");
            }





            if (((User) security.getLoggedUser()).getCompany().getTipoConfederazione() == 3){
                //se sono unc
                formDescriptor.addField("sector", String.class, "Settore", "Filtri", applicationContext.getBean(SectorTypeSelectRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt5")
                        .putParam(Params.FORM_COLUMN, "  ");
            }else{
                formDescriptor.addField("sector", String.class, "Settore", "Filtri", applicationContext.getBean(SectorTypeOptionalSelectRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt5")
                        .putParam(Params.FORM_COLUMN, "  ");


                formDescriptor.addField("firm", String.class, "Azienda", "Filtri", applicationContext.getBean(AziendeForCompanySingleSearchbleFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt7")
                        .putParam(Params.FORM_COLUMN, "  ");
            }





            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Report iscritti");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/iscritti/tessera/print", method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public
    @ResponseBody
    SimpleResponse showPrintTessera(HttpServletRequest request) {

        try {

            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("iscrittitessera");

            FormDescriptor formDescriptor = new FormDescriptor(form);
            formDescriptor.addField("province", String.class, "Provincia", null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class)).putParam(Params.ROW, "dt").putParam(Params.COLS, Values.COLS_12);
            formDescriptor.addField("sector", String.class, "Settore", null, applicationContext.getBean(SectorTypeForStampaTessereSelectRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_12);
            formDescriptor.addField("position", String.class, "Posizione stampa", null, applicationContext.getBean(TesserePrintPositionFieldRenderer.class)).putParam(Params.ROW, "dt1").putParam(Params.COLS, Values.COLS_12);



            formDescriptor.addField("onlyWithoutTessera", Boolean.class, "Stampa tessera se non  già stampata", null, applicationContext.getBean(CustomCheckboxFieldRenderer.class)).putParam(Params.ROW, "dt2").putParam(Params.COLS, Values.COLS_12);
            formDescriptor.addField("global", Boolean.class, "Stampa tessera se non già stampata da altre categorie", null, applicationContext.getBean(CustomCheckboxFieldRenderer.class)).putParam(Params.ROW, "dt3").putParam(Params.COLS, Values.COLS_12);

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


