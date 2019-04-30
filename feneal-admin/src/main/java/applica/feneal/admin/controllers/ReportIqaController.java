package applica.feneal.admin.controllers;

import applica.feneal.admin.facade.IqaFacade;
import applica.feneal.admin.fields.renderers.*;
import applica.feneal.admin.form.renderers.ReportsSearchFormRenderer;
import applica.feneal.admin.viewmodel.BonificoDto;
import applica.feneal.admin.viewmodel.quote.UiDettaglioQuota;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.servizi.search.UiIqaReportSearchParams;
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
import applica.framework.widgets.fields.renderers.DefaultFieldRenderer;
import applica.framework.widgets.forms.renderers.DefaultFormRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by fgran on 15/04/2016.
 */
@Controller
public class ReportIqaController {
    @Autowired
    private Security security;

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private IqaFacade iqaReportFac;

    @RequestMapping(value="/iqa/report", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse reportIqa(@RequestBody UiIqaReportSearchParams params){
        List<UiDettaglioQuota> f;
        try{
            f = iqaReportFac.reportQuote(params);
            return new ValueResponse(f);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }



    @RequestMapping(value="/iqa/bonifico", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse reportIqa(@RequestBody BonificoDto params){

        try{
            String result = iqaReportFac.BonificaQuote(params);
            if (result.equals("OK"))
                return new ValueResponse(result);

            return new ErrorResponse(result);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }


    @RequestMapping(value="/iqa/bonifico/{id}", method = RequestMethod.DELETE)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse deletebonifico(@PathVariable long id){

        try{
            String result = iqaReportFac.Deletebonifico(id);
            if (result.equals("OK"))
                return new ValueResponse(result);

            return new ErrorResponse(result);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }



    @RequestMapping(value = "/iqa/bonifico/form",method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse bonificoFormView(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(DefaultFormRenderer.class));
            form.setIdentifier("bonifico");

            FormDescriptor formDescriptor = new FormDescriptor(form);


                formDescriptor.addField("note", String.class, "Note", null, applicationContext.getBean(DefaultFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt")
                        .putParam(Params.FORM_COLUMN, " ");





            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Bonifico");
            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/iqa",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse searchview(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(ReportsSearchFormRenderer.class));
            form.setIdentifier("iqareport");

            FormDescriptor formDescriptor = new FormDescriptor(form);

            if (((User) security.getLoggedUser()).getCompany().getTipoConfederazione() == 1){

                formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(CompanyForTerritorySelectFieldrenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt")
                        .putParam(Params.FORM_COLUMN, " ");

            }else{
                formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(OptionalCompanyFotTerritorySelectFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt")
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

            if (((User) security.getLoggedUser()).getCompany().getTipoConfederazione() == 3){
                //se sono unc
                formDescriptor.addField("sector", String.class, "Settore", "Filtri", applicationContext.getBean(SectorTypeSelectRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt")
                        .putParam(Params.FORM_COLUMN, " ");
            }else{
                formDescriptor.addField("sector", String.class, "Settore", "Filtri", applicationContext.getBean(SectorTypeOptionalSelectRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt")
                        .putParam(Params.FORM_COLUMN, " ");


                formDescriptor.addField("firm", String.class, "Azienda", "Filtri", applicationContext.getBean(AziendeForCompanySingleSearchbleFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt1")
                        .putParam(Params.FORM_COLUMN, " ");
            }




            formDescriptor.addField("dataDoc", String.class, "Da", "Data registrazione", applicationContext.getBean(OptionalDateFromMonthFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt2")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("dataDoc", String.class, "a", "Data registrazione", applicationContext.getBean(OptionalDateToMonthFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt3")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("competence", String.class, "Da", "Competenza", applicationContext.getBean(OptionalDateFromMonthFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt4")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("competence", String.class, "a", "Competenza", applicationContext.getBean(OptionalDateToMonthFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt5")
                    .putParam(Params.FORM_COLUMN, "  ");






            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Report incassi quote");
            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }

}

