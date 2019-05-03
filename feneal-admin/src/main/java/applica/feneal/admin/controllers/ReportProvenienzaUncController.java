package applica.feneal.admin.controllers;

import applica.feneal.admin.facade.DelegheFacade;
import applica.feneal.admin.fields.renderers.*;
import applica.feneal.admin.form.renderers.ReportsSearchFormRenderer;
import applica.feneal.admin.viewmodel.reports.UiDelega;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.deleghe.UiDelegheReportSearchParams;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class ReportProvenienzaUncController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private Security security;

    @Autowired
    private DelegheFacade delFac;


    @RequestMapping(value="/provenienzeunc/report", method = RequestMethod.POST)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse reportProvUNC(@RequestBody UiDelegheReportSearchParams params){

        try {
            List<UiDelega> f = delFac.reportDeleghe(params);
            return new ValueResponse(f);
        } catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }



    @RequestMapping(value = "/provenienzauncform",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody SimpleResponse searchview() {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(ReportsSearchFormRenderer.class));
            form.setIdentifier("provenienzauncreport");

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



            if (((User) security.getLoggedUser()).getCompany().getTipoConfederazione() == 3){
                //se sono unc
                formDescriptor.addField("sector", String.class, "Settore", "Filtri", applicationContext.getBean(SectorTypeSelectRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt1")
                        .putParam(Params.FORM_COLUMN, " ");
            }else{
                formDescriptor.addField("sector", String.class, "Settore", "Filtri", applicationContext.getBean(SectorTypeOptionalSelectRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt1")
                        .putParam(Params.FORM_COLUMN, " ");


                formDescriptor.addField("firm", String.class, "Azienda", "Filtri", applicationContext.getBean(AziendeForCompanySingleSearchbleFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt1")
                        .putParam(Params.FORM_COLUMN, " ");
            }



            if (((User) security.getLoggedUser()).getCompany().getTipoConfederazione() == 1) {
                //se non si parla di contesto regionale ma di contesto nazionale abilito il flag
                formDescriptor.addField("causaleIscrizione", String.class, "Caus. sottosc.", null, applicationContext.getBean(CausaleIscrizSelectFieldRenderer.class))
                        .putParam(Params.COLS, Values.COLS_12)
                        .putParam(Params.ROW, "dt5")
                        .putParam(Params.FORM_COLUMN, " ");
            }


            formDescriptor.addField("subscribed", Boolean.class, "Sottoscritta", null, applicationContext.getBean(CustomCheckboxFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("subscribedFromDate", String.class, "Da", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "Da")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("subscribedToDate", String.class, "a", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "A")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt6")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("sent", Boolean.class, "Inoltrata", null, applicationContext.getBean(CustomCheckboxFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt7")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("sentFromDate", String.class, "Da", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "Da")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt7")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("sentToDate", String.class, "a", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "A")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt7")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("accepted", Boolean.class, "Accettata", null, applicationContext.getBean(CustomCheckboxFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt8")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("acceptedFromDate", String.class, "Da", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "Da")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt8")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("acceptedToDate", String.class, "a", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "A")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt8")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("activated", Boolean.class, "Attivata", null, applicationContext.getBean(CustomCheckboxFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt9")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("activatedFromDate", String.class, "Da", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "Da")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt9")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("activatedToDate", String.class, "a", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "A")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt9")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("cancelled", Boolean.class, "Annullata", null, applicationContext.getBean(CustomCheckboxFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt10")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("cancelledFromDate", String.class, "Da", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "Da")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt10")
                    .putParam(Params.FORM_COLUMN, "  ");
            formDescriptor.addField("cancelledToDate", String.class, "a", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
                    .putParam(Params.PLACEHOLDER, "A")
                    .putParam(Params.COLS, Values.COLS_4)
                    .putParam(Params.ROW, "dt10")
                    .putParam(Params.FORM_COLUMN, "  ");
//            formDescriptor.addField("revoked", Boolean.class, "Revocata", null, applicationContext.getBean(CustomCheckboxFieldRenderer.class))
//                    .putParam(Params.COLS, Values.COLS_4)
//                    .putParam(Params.ROW, "dt9")
//                    .putParam(Params.FORM_COLUMN, "  ");
//            formDescriptor.addField("revokedFromDate", String.class, "Da", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
//                    .putParam(Params.PLACEHOLDER, "Da")
//                    .putParam(Params.COLS, Values.COLS_4)
//                    .putParam(Params.ROW, "dt9")
//                    .putParam(Params.FORM_COLUMN, "  ");
//            formDescriptor.addField("revokedToDate", String.class, "a", null, applicationContext.getBean(DatepickerReportDelegheFieldRenderer.class))
//                    .putParam(Params.PLACEHOLDER, "A")
//                    .putParam(Params.COLS, Values.COLS_4)
//                    .putParam(Params.ROW, "dt9")
//                    .putParam(Params.FORM_COLUMN, "  ");


            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Report provenienza UNC");

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
