package applica.feneal.services.impl.importData;

import applica.feneal.domain.data.core.servizi.DocumentiRepository;
import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.ImportData;
import applica.feneal.domain.model.core.importData.ImportDocumentiValidator;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.servizi.Documento;
import applica.feneal.domain.model.setting.TipoDocumento;
import applica.feneal.services.GeoService;
import applica.framework.LoadRequest;
import applica.framework.management.csv.RowData;
import applica.framework.management.excel.ExcelInfo;
import applica.framework.security.Security;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by fgran on 11/05/2017.
 */
@Component
public class ImportDataDocumenti {



    @Autowired
    private Security sec;

    @Autowired
    private DocumentiRepository delSvc;

    @Autowired
    private ImportDataUtils importDataUtils;

    @Autowired
    private ImportDataLavoratori importDataLavoratori;

    @Autowired
    private ImportCausaliService importCausaliService;

 @Autowired
 private GeoService geo;


    public void doImportDocumenti(ImportData importData, File temp1, File temp) throws Exception {

        baseValidate(importData);


        ExcelInfo data = importDataUtils.importData(importData, temp1, new ImportDocumentiValidator());
        validateExcelDataForDocumenti(data);


        //se non ci sono errori posso importare i lavoratori e quindi importare
        importAllDocumenti(data, temp, importData);
    }

    private void importAllDocumenti(ExcelInfo data, File tempdir, ImportData importData) throws Exception {
        String filename = tempdir + "\\logImportazioneDocumenti.txt";

        File f = new File(filename);
        f.createNewFile();

        importDataUtils.writeToFile(f,  "Avvio import documenti: num ( " + data.getOnlyValidRows().size() + " )");
        importDataUtils.writeToFile(f,  "*****************************");
        importDataUtils.writeToFile(f,  "*****************************");





        //Importo il lavoratore se non esiste e ne creo la relativa delega
        int i = 1; //numro di riga
        for (RowData rowData : data.getOnlyValidRows()) {
            //costruisco i dati del lavoratore
            Lavoratore l = null;


            boolean lavoratoreOk = false;
            try {
                l = importDataLavoratori.constructLavoratore(rowData,  importData);
                lavoratoreOk = true;
            } catch (Exception e) {
                importDataUtils.writeToFile(f, "ERRORE nella costruzione del lavoratore riga " + String.valueOf(i) + "; " + e.getMessage() );
            }

            if (lavoratoreOk){



                Documento d =  findDocumento(rowData);

                if (d == null)
                    d =createDocumento(l, rowData, importData);
                else
                    d.setNotes(rowData.getData().get("NOTE"));

                delSvc.save( d);



            }
            i++;

        }
    }

    private Documento findDocumento(RowData rowData) {
        Date d = tryParse(rowData.getData().get("DATA"), new Date());
        String tipo = rowData.getData().get("TIPO_DOCUMENTO");

        importCausaliService.createIfNotExistTipoDocumento(tipo);
        TipoDocumento dd = importCausaliService.getTipoDocumento(tipo);

        LoadRequest req = LoadRequest.build()
                .filter("data", d)
                .filter("tipo", dd.getLid());

        return delSvc.find(req).findFirst().orElse(null);


    }

    private Documento createDocumento(Lavoratore l, RowData rowData, ImportData importData) {
        Documento d = new Documento();
        d.setData(tryParse(rowData.getData().get("DATA"), new Date()));
        String tipo = rowData.getData().get("TIPO_DOCUMENTO");
        importCausaliService.createIfNotExistTipoDocumento(tipo);
        d.setTipo(importCausaliService.getTipoDocumento(tipo));
        d.setNotes(rowData.getData().get("NOTE"));
        d.setLavoratore(l);
        d.setProvince(geo.getProvinceById(Integer.parseInt(importData.getProvince())));


        User u = ((User) sec.getLoggedUser());
        d.setUserCompleteName(u.getCompleteName());
        d.setUserId(u.getLid());

        return d;
    }

    private Date tryParse(String s, Date defaultDate){
        SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");


        try {
            return ff.parse(s);
        } catch (ParseException e) {
            return defaultDate;
        }

    }

    private void validateExcelDataForDocumenti(ExcelInfo data) throws Exception {
        if (!fr.opensagres.xdocreport.core.utils.StringUtils.isEmpty(data.getError())){
            //recupero il nome del file
            String name = FilenameUtils.getName(data.getSourceFile());

            throw new Exception(String.format("Un file contiene errori: %s  <br>",  data.getError()));
        }

        //prendo l'intestazione e verifico che sia la stessa che mi aspetto
        List<String> template  = new ArrayList<>();
        template.add("COGNOME_UTENTE");
        template.add("NOME_UTENTE");
        template.add("DATA_NASCITA_UTENTE");
        template.add("SESSO");
        template.add("FISCALE");
        template.add("COMUNE_NASCITA");
        template.add("COMUNE");
        template.add("INDIRIZZO");
        template.add("CAP");
        template.add("TELEFONO1");
        template.add("TELEFONO2");
        template.add("NOTE_UTENTE");

        template.add("MAIL");
        template.add("ATTRIBUZIONE1");
        template.add("ATTRIBUZIONE2");
        template.add("ATTRIBUZIONE3");



        template.add("DATA");
        template.add("TIPO_DOCUMENTO");
        template.add("NOTE");


        List<String> headers = data.getHeaderFields();
        String name = FilenameUtils.getName(data.getSourceFile());
        String err = importDataUtils.headersContainsTemplate(headers, template);
        boolean equal = StringUtils.isEmpty(err);
        if (!equal){
            //recupero il nome del file

            throw new Exception(String.format("Il file %s non contiene le intestazioni richieste<br>: Campi mancanti: %s", name, err));
        }
        if (equal){
            //verifico che ci sia almeno una riga
            if (data.getOnlyValidRows().size() == 0){
                throw new Exception(String.format("Il file %s non contiene informazioni<br>", name));
            }

        }
    }

    private void baseValidate(ImportData importData) throws Exception {

        if (importData == null){
            throw new Exception("Nessun file indicato per l'importazione; Null");
        }

        //verifico che ci sia almeno un file
        if (fr.opensagres.xdocreport.core.utils.StringUtils.isEmpty(importData.getFile1())){
            throw new Exception("Nessun file indicato per l'importazione");
        }
    }






}
