package applica.feneal.admin.controllers.importData;

import applica.feneal.admin.fields.renderers.CompanyForTerritorySelectFieldrenderer;
import applica.feneal.admin.fields.renderers.ImportDataFileFieldRenderer;
import applica.feneal.admin.fields.renderers.LoggedUserProvinceNonOptionalSelectFieldRenderer;
import applica.feneal.admin.fields.renderers.SectorTypeWithoutInpsSelectRenderer;
import applica.feneal.admin.form.renderers.ReportsSearchFormRenderer;
import applica.feneal.domain.model.core.ImportData;
import applica.feneal.services.ImportDataService;
import applica.framework.library.responses.ErrorResponse;
import applica.framework.library.responses.FormResponse;
import applica.framework.library.responses.SimpleResponse;
import applica.framework.library.responses.ValueResponse;
import applica.framework.widgets.CrudConfigurationException;
import applica.framework.widgets.Form;
import applica.framework.widgets.FormCreationException;
import applica.framework.widgets.FormDescriptor;
import applica.framework.widgets.fields.Params;
import applica.framework.widgets.fields.Values;
import applica.framework.widgets.fields.renderers.DatePickerRenderer;
import applica.framework.widgets.fields.renderers.DefaultFieldRenderer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

@Controller
@RequestMapping("/importDatiBil")
public class ImportDatiBilController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ImportDataService importDataService;

    @RequestMapping(value = "/quotetemplate", method = RequestMethod.GET)
    public void getQuoteTemplate(HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = getClass().getResourceAsStream("/templates/templateProvenienzaBilateralita.xlsx");
            // copy it to response's OutputStream+
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {

            throw new RuntimeException("IOError writing file to output stream");
        }

    }

    @RequestMapping(value = "/dati",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse importQuote(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(ReportsSearchFormRenderer.class));
            form.setIdentifier("importaQuote");

            FormDescriptor formDescriptor = new FormDescriptor(form);

            formDescriptor.addField("company", String.class, "Territorio", null, applicationContext.getBean(CompanyForTerritorySelectFieldrenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            //poichè questa operazione è permessa solo al nazionale il componente
            // "LoggedUserProvinceNonOptionalSelectFieldRenderer" non troverà alcuna provincia
            //lascio stare il componente anche se dovrei , in verità, creare un emptySelectFieldRendere
            //che viene valorizzato da script lato client
            formDescriptor.addField("province", String.class, "Provincia", null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("file1", String.class, "File", null,applicationContext.getBean(ImportDataFileFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

//            formDescriptor.addField("settore", String.class, "Settore",null, applicationContext.getBean(SectorTypeSelectRenderer.class)).putParam(Params.COLS, Values.COLS_12)
//                    .putParam(Params.COLS, Values.COLS_12)
//                    .putParam(Params.ROW, "dt1")
//                    .putParam(Params.FORM_COLUMN, " ");


//            formDescriptor.addField("ente", String.class, "Ente bilaterale",null, applicationContext.getBean(ContractNonOptionalSelectFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12)
//                    .putParam(Params.COLS, Values.COLS_12)
//                    .putParam(Params.ROW, "dt1")
//                    .putParam(Params.FORM_COLUMN, " ")


            formDescriptor.addField("updateResidenza", Boolean.class, "Agg. residenze", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("updateTelefoni", Boolean.class, "Agg. contatti", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");



//            Map<String, Object> data = new HashMap<String, Object>();
//            data.put("data", DateUtils.createTodayDate());
//
//
//            form.setData(data);


            FormResponse response = new FormResponse();

            response.setContent(form.writeToString());
            response.setTitle("Importazione Dati Bilateralità");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }

    @RequestMapping(value = "/importquote",method = RequestMethod.POST)
    public @ResponseBody
    SimpleResponse executeimportquote(HttpServletRequest request, HttpServletResponse response, @RequestBody ImportData file) {

        try{
            String fileToDownload = importDataService.importaBiolateralita(file);

            return new ValueResponse(fileToDownload);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }


}
