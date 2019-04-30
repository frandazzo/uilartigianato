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

/**
 * Created by fgran on 18/04/2017.
 */
@Controller
@RequestMapping("/import")
public class ImportController {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private ImportDataService importDataService;


    @RequestMapping(value = "/anagrafichetemplate", method = RequestMethod.GET)
    public void getAnagraficheTemplate(HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = getClass().getResourceAsStream("/templates/TemplateImportUtenti1.xlsx");
            // copy it to response's OutputStream+
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {

            throw new RuntimeException("IOError writing file to output stream");
        }

    }

    @RequestMapping(value = "/documentitemplate", method = RequestMethod.GET)
    public void getDocumentiTemplate(HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = getClass().getResourceAsStream("/templates/TemplateImportDocumenti.xlsx");
            // copy it to response's OutputStream+
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {

            throw new RuntimeException("IOError writing file to output stream");
        }

    }


    @RequestMapping(value = "/quotetemplate", method = RequestMethod.GET)
    public void getQuoteTemplate(HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = getClass().getResourceAsStream("/templates/TemplateImportIQA.xlsx");
            // copy it to response's OutputStream+
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {

            throw new RuntimeException("IOError writing file to output stream");
        }

    }

    @RequestMapping(value = "/deleghetemplate", method = RequestMethod.GET)
    public void getDelegheTemplate(HttpServletResponse response) {
        try {
            // get your file as InputStream
            InputStream is = getClass().getResourceAsStream("/templates/TemplateImportDeleghe1.xlsx");
            // copy it to response's OutputStream+
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            org.apache.commons.io.IOUtils.copy(is, response.getOutputStream());
            response.flushBuffer();
        } catch (IOException ex) {

            throw new RuntimeException("IOError writing file to output stream");
        }

    }


    @RequestMapping(value = "/documenti",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse importDocumenti(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(ReportsSearchFormRenderer.class));
            form.setIdentifier("importadocumenti");

            FormDescriptor formDescriptor = new FormDescriptor(form);

            formDescriptor.addField("province", String.class, "Territorio",null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");



            formDescriptor.addField("file1", Date.class, "File documenti", null,applicationContext.getBean(ImportDataFileFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("updateResidenza", Boolean.class, "Agg. residenze", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("updateTelefoni", Boolean.class, "Agg. contatti", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("updateAttribuzioni", Boolean.class, "Agg. attribuzione", null, applicationContext.getBean(DefaultFieldRenderer.class))
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
            response.setTitle("Importazione documenti");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/anagrafiche",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse importAnagrafiche(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(ReportsSearchFormRenderer.class));
            form.setIdentifier("importaAnagrafiche");

            FormDescriptor formDescriptor = new FormDescriptor(form);




            formDescriptor.addField("file1", Date.class, "File Anagrafiche", null,applicationContext.getBean(ImportDataFileFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("updateResidenza", Boolean.class, "Agg. residenze", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("updateTelefoni", Boolean.class, "Agg. contatti", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("updateAttribuzioni", Boolean.class, "Agg. attribuzione", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("createLista", Boolean.class, "Crea lista lavoro", null, applicationContext.getBean(DefaultFieldRenderer.class))
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
            response.setTitle("Importazione anagrafiche");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/deleghe",method = RequestMethod.GET)
    @PreAuthorize("isAuthenticated()")
    public @ResponseBody
    SimpleResponse importDeleghe(HttpServletRequest request) {
        try{
            Form form = new Form();
            form.setRenderer(applicationContext.getBean(ReportsSearchFormRenderer.class));
            form.setIdentifier("importaDeleghe");

            FormDescriptor formDescriptor = new FormDescriptor(form);


            formDescriptor.addField("province", String.class, "Territorio",null, applicationContext.getBean(LoggedUserProvinceNonOptionalSelectFieldRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("file1", Date.class, "File Deleghe", null,applicationContext.getBean(ImportDataFileFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("settore", String.class, "Settore",null, applicationContext.getBean(SectorTypeWithoutInpsSelectRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("updateResidenza", Boolean.class, "Agg. residenze", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("updateTelefoni", Boolean.class, "Agg. contatti", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("updateAttribuzioni", Boolean.class, "Agg. attribuzioni", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");



            formDescriptor.addField("updateAzienda", Boolean.class, "Agg. azienda", null, applicationContext.getBean(DefaultFieldRenderer.class))
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
            response.setTitle("Importazione deleghe");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }



    @RequestMapping(value = "/quote",method = RequestMethod.GET)
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


            formDescriptor.addField("file1", String.class, "File quote", null,applicationContext.getBean(ImportDataFileFieldRenderer.class))
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
//                    .putParam(Params.FORM_COLUMN, " ");




            formDescriptor.addField("dataInizio", String.class, "Data inizio",null, applicationContext.getBean(DatePickerRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("dataFine", String.class, "Data fine",null, applicationContext.getBean(DatePickerRenderer.class)).putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");



            formDescriptor.addField("updateResidenza", Boolean.class, "Agg. residenze", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("updateTelefoni", Boolean.class, "Agg. contatti", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");

            formDescriptor.addField("updateAttribuzioni", Boolean.class, "Agg. attribuzioni", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");



            formDescriptor.addField("updateAzienda", Boolean.class, "Agg. azienda", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");


            formDescriptor.addField("createDelega", Boolean.class, "Crea delega", null, applicationContext.getBean(DefaultFieldRenderer.class))
                    .putParam(Params.COLS, Values.COLS_12)
                    .putParam(Params.ROW, "dt1")
                    .putParam(Params.FORM_COLUMN, " ");



            formDescriptor.addField("associateDelega", Boolean.class, "Associa delega", null, applicationContext.getBean(DefaultFieldRenderer.class))
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
            response.setTitle("Importazione quote");

            return response;
        } catch (FormCreationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        } catch (CrudConfigurationException e) {
            e.printStackTrace();
            return new ErrorResponse(e.getMessage());
        }
    }


    @RequestMapping(value = "/importanagrafiche",method = RequestMethod.POST)
    public @ResponseBody
    SimpleResponse executeimportanagrafiche(HttpServletRequest request, HttpServletResponse response, @RequestBody ImportData file) {

        try{
            String fileToDownload = importDataService.importaAnagrafiche(file);

            return new ValueResponse(fileToDownload);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }

    @RequestMapping(value = "/importdocumenti",method = RequestMethod.POST)
    public @ResponseBody
    SimpleResponse executeimportdocumenti(HttpServletRequest request, HttpServletResponse response, @RequestBody ImportData file) {

        try{
            String fileToDownload = importDataService.importaDocumenti(file);

            return new ValueResponse(fileToDownload);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }

    @RequestMapping(value = "/importdeleghe",method = RequestMethod.POST)
    public @ResponseBody
    SimpleResponse executeimportdeleghe(HttpServletRequest request, HttpServletResponse response, @RequestBody ImportData file) {

        try{
            String fileToDownload = importDataService.importaDeleghe(file);

            return new ValueResponse(fileToDownload);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }


    @RequestMapping(value = "/importquote",method = RequestMethod.POST)
    public @ResponseBody
    SimpleResponse executeimportquote(HttpServletRequest request, HttpServletResponse response, @RequestBody ImportData file) {

        try{
            String fileToDownload = importDataService.importaQuote(file);

            return new ValueResponse(fileToDownload);
        }catch(Exception ex){
            return new ErrorResponse(ex.getMessage());
        }
    }


}
