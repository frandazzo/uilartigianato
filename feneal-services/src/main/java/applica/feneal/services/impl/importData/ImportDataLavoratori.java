package applica.feneal.services.impl.importData;

import applica.feneal.domain.model.User;
import applica.feneal.domain.model.core.ImportData;
import applica.feneal.domain.model.core.importData.ImportAnagraficheValidator;
import applica.feneal.domain.model.core.lavoratori.Lavoratore;
import applica.feneal.domain.model.core.lavoratori.ListaLavoro;
import applica.feneal.domain.model.geo.City;
import applica.feneal.domain.model.geo.Country;
import applica.feneal.domain.model.setting.Attribuzione1;
import applica.feneal.domain.model.setting.Attribuzione2;
import applica.feneal.domain.model.setting.Fondo;
import applica.feneal.services.GeoService;
import applica.feneal.services.LavoratoreService;
import applica.feneal.services.ListaLavoroService;
import applica.framework.management.csv.RowData;
import applica.framework.management.excel.ExcelInfo;
import applica.framework.security.Security;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by fgran on 09/05/2017.
 */
@Component
public class ImportDataLavoratori {

    @Autowired
    private ImportDataUtils importDataUtils;

    @Autowired
    private Security sec;

    @Autowired
    private ListaLavoroService listaSvc;


    @Autowired
    private LavoratoreService lavSvc;

    @Autowired
    private GeoService geo;

    @Autowired
    private ImportCausaliService importCausaliService;


    public void doImportAnagrafiche(ImportData importData, File temp1, File temp) throws Exception {

        baseValidate(importData);

        ExcelInfo data = importDataUtils.importData(importData, temp1, new ImportAnagraficheValidator());


        validateExcelDataForAnagrafiche(data);


        //se non ci sono errori posso importare i lavoratori e quindi importare
        importAllAnagrafiche(data, temp, importData);
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

    private void validateExcelDataForAnagrafiche(ExcelInfo data) throws Exception {
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


//        template.add("SETTORE");
//        template.add("AZIENDA");
//        template.add("PARTITA_IVA");
//        template.add("COMUNE_AZIENDA");
//        template.add("INDIRIZZO_AZIENDA");
//        template.add("CAP_AZIENDA");
//        template.add("TELEFONO_AZIENDA");
//        template.add("NOTE_AZIENDA");
//        template.add("CONTRATTO");
//
//        template.add("DATA");
//        template.add("DATA_ACCETTAZIONE");
//        template.add("DATA_ANNULLAMENTO");
//        template.add("DATA_REVOCA");
//        template.add("CAUSALE_REVOCA");
//        template.add("NOTE");
//        template.add("COLLABORATORE");

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




    private void importAllAnagrafiche(ExcelInfo data, File tempDir, ImportData importData) throws Exception {
        String filename = tempDir + "\\logImportazioneAnagrafiche.txt";

        File f = new File(filename);
        f.createNewFile();

        importDataUtils.writeToFile(f,  "Avvio import anagrafiche: num ( " + data.getOnlyValidRows().size() + " )");
        importDataUtils.writeToFile(f,  "*****************************");
        importDataUtils.writeToFile(f,  "*****************************");





        //Importo il lavoratore se non esiste
        int i = 1; //numero di riga
        List<Lavoratore> lavs = new ArrayList<>();
        for (RowData rowData : data.getOnlyValidRows()) {
            Lavoratore l = null;
            try {
                l = constructLavoratore(rowData,  importData);
                lavs.add(l);
            } catch (Exception e) {
                importDataUtils.writeToFile(f, "ERRORE nella costruzione del lavoratore riga " + String.valueOf(i) + "; " + e.getMessage() );
            }

            i++;

        }

        if (importData.getCreateLista() != null )
            if (importData.getCreateLista() == 1 ){
                if (lavs.size() > 0){
                    //posso creare la lista di lavoro.....

                    ListaLavoro l1 = new ListaLavoro();
                    l1.setLavoratori(lavs);

                    listaSvc.saveListaLavoro(l1);


                }
            }

    }





    public Lavoratore constructLavoratore(RowData rowData,  ImportData importData) throws Exception {
        Lavoratore l = lavSvc.findLavoratoreByFiscalCode(rowData.getData().get("FISCALE").trim(), Long.parseLong(importData.getCompany()));
        if (l == null){

            SimpleDateFormat ff = new SimpleDateFormat("dd/MM/yyyy");
            //creo il lavoratore
            l = new Lavoratore();
            l.setCompanyId( Long.parseLong(importData.getCompany()));

            if (rowData.getData().get("SESSO") != null){
                String sex = rowData.getData().get("SESSO").trim();
                if (!sex.equals("M") && ! sex.equals("F"))
                    l.setSex("M");
                else
                    l.setSex(sex);
            }else{
                l.setSex("M");
            }

            l.setName(rowData.getData().get("NOME_UTENTE").trim());
            l.setSurname(rowData.getData().get("COGNOME_UTENTE").trim());
            try {
                l.setBirthDate(ff.parse(rowData.getData().get("DATA_NASCITA_UTENTE").trim()));
            }catch (Exception ex){
                GregorianCalendar c = new GregorianCalendar();
                c.set(Calendar.YEAR, 1900);
                c.set(Calendar.MONTH, 0);
                c.set(Calendar.DAY_OF_MONTH, 1);
                c.set(Calendar.HOUR, 0);
                c.set(Calendar.MINUTE, 0);
                c.set(Calendar.SECOND,0);
                l.setBirthDate(c.getTime());
            }

            l.setFiscalcode(rowData.getData().get("FISCALE").trim());

            if (rowData.getData().get("COMUNE_NASCITA") != null){
                City cc = geo.getCityByName(rowData.getData().get("COMUNE_NASCITA"));
                if (cc!= null){
                    l.setBirthPlace(cc.getDescription());
                    l.setBirthProvince(geo.getProvinceById(cc.getIdProvince()).getDescription());
                    l.setNationality("ITALIA");
                }else{
                    //altrimenti provo con la nazione di nascita
                    Country cou = geo.getCountryByName(rowData.getData().get("COMUNE_NASCITA"));
                    if (cou != null)
                        l.setNationality(cou.getDescription());

                }
            }

            if (rowData.getData().get("COMUNE") != null){

                City cc1 = geo.getCityByName(rowData.getData().get("COMUNE"));
                if (cc1!= null){
                    l.setLivingCity(cc1.getDescription());
                    l.setLivingProvince(geo.getProvinceById(cc1.getIdProvince()).getDescription());
                }
            }


            if (rowData.getData().get("INDIRIZZO") != null)
                l.setAddress(rowData.getData().get("INDIRIZZO").trim());

            if (rowData.getData().get("CAP") != null)
                l.setCap(rowData.getData().get("CAP").trim());

            if (rowData.getData().get("TELEFONO1") != null)
                l.setCellphone(rowData.getData().get("TELEFONO1").toString().trim().replaceAll("[^\\d.]", ""));

            if (rowData.getData().get("TELEFONO2") != null)
                l.setPhone(rowData.getData().get("TELEFONO2").toString().trim().replaceAll("[^\\d.]", ""));

            if (rowData.getData().get("MAIL") != null)
                l.setMail(rowData.getData().get("MAIL").toString().trim());


//            if (rowData.getData().get("ATTRIBUZIONE1") != null)
//                l.setCe(rowData.getData().get("ATTRIBUZIONE1").toString().trim());
//            if (rowData.getData().get("ATTRIBUZIONE2") != null)
//                l.setEc(rowData.getData().get("ATTRIBUZIONE2").toString().trim());

            if (rowData.getData().get("ATTRIBUZIONE1") != null){
                if (rowData.getData().get("ATTRIBUZIONE1") != ""){
                    importCausaliService.createIfNotExistAttribuzione1(rowData.getData().get("ATTRIBUZIONE1"));
                    Attribuzione1 d = importCausaliService.getAttribuzione1(rowData.getData().get("ATTRIBUZIONE1"));
                    l.setAttribuzione1(d);
                }

            }

            if (rowData.getData().get("ATTRIBUZIONE2") != null){
                if (rowData.getData().get("ATTRIBUZIONE2") != ""){
                    importCausaliService.createIfNotExistAttribuzione2(rowData.getData().get("ATTRIBUZIONE2"));
                    Attribuzione2 d = importCausaliService.getAttribuzione2( rowData.getData().get("ATTRIBUZIONE2"));
                    l.setAttribuzione2(d);
                }

            }


            if (rowData.getData().get("ATTRIBUZIONE3") != null){
                if (rowData.getData().get("ATTRIBUZIONE3") != ""){
                    importCausaliService.createIfNotExistFondo(rowData.getData().get("ATTRIBUZIONE3"));
                    Fondo d = importCausaliService.getFondo(rowData.getData().get("ATTRIBUZIONE3"));
                    l.setFund(d);
                }

            }




            if (rowData.getData().get("NOTE_UTENTE") != null)
                l.setNotes(rowData.getData().get("NOTE_UTENTE").toString().trim());

            lavSvc.saveOrUpdateWithImpersonation(((User) sec.getLoggedUser()).getLid(),l);

        }
        else{


            //devo verificare se aggiornare i dati anagrafici  oppure i dati di telefono
            if (importData.getUpdateTelefoni() != null)
                if (importData.getUpdateTelefoni() == 1){
                    //aggiorno i num di telefono
                    if (rowData.getData().get("TELEFONO1") != null)
                        if (!StringUtils.isEmpty(rowData.getData().get("TELEFONO1").toString().trim()))
                            l.setCellphone(rowData.getData().get("TELEFONO1").toString().trim().replaceAll("[^\\d.]", ""));

                    if (rowData.getData().get("TELEFONO2") != null)
                        if (!StringUtils.isEmpty(rowData.getData().get("TELEFONO2").toString().trim()))
                            l.setPhone(rowData.getData().get("TELEFONO2").toString().trim().replaceAll("[^\\d.]", ""));


                    if (rowData.getData().get("MAIL") != null)
                        if (!StringUtils.isEmpty(rowData.getData().get("MAIL").toString().trim()))
                            l.setMail(rowData.getData().get("MAIL").toString().trim());

                }


            //verifico se aggiornare le attribuzioni
            if (importData.getUpdateAttribuzioni() != null)
                if (importData.getUpdateAttribuzioni() == 1){

//                    if (rowData.getData().get("ATTRIBUZIONE1") != null)
//                        if (!StringUtils.isEmpty(rowData.getData().get("ATTRIBUZIONE1").toString().trim()))
//                            l.setCe(rowData.getData().get("ATTRIBUZIONE1").toString().trim());
//
//                    if (rowData.getData().get("ATTRIBUZIONE2") != null)
//                        if (!StringUtils.isEmpty(rowData.getData().get("ATTRIBUZIONE2").toString().trim()))
//                            l.setCe(rowData.getData().get("ATTRIBUZIONE2").toString().trim());


                    if (rowData.getData().get("ATTRIBUZIONE1") != null){
                        if (rowData.getData().get("ATTRIBUZIONE1") != ""){
                            importCausaliService.createIfNotExistAttribuzione1(rowData.getData().get("ATTRIBUZIONE1"));
                            Attribuzione1 d = importCausaliService.getAttribuzione1( rowData.getData().get("ATTRIBUZIONE1"));
                            l.setAttribuzione1(d);
                        }

                    }

                    if (rowData.getData().get("ATTRIBUZIONE2") != null){
                        if (rowData.getData().get("ATTRIBUZIONE2") != ""){
                            importCausaliService.createIfNotExistAttribuzione2(rowData.getData().get("ATTRIBUZIONE2"));
                            Attribuzione2 d = importCausaliService.getAttribuzione2(rowData.getData().get("ATTRIBUZIONE2"));
                            l.setAttribuzione2(d);
                        }

                    }


                    if (rowData.getData().get("ATTRIBUZIONE3") != null){
                        if (rowData.getData().get("ATTRIBUZIONE3") != ""){
                            importCausaliService.createIfNotExistFondo(rowData.getData().get("ATTRIBUZIONE3"));
                            Fondo d = importCausaliService.getFondo(rowData.getData().get("ATTRIBUZIONE3"));
                            l.setFund(d);
                        }

                    }


                }


            //devo verificare se aggiornare i dati anagrafici  oppure i dati di residenza
            if (importData.getUpdateResidenza() != null)
                if (importData.getUpdateResidenza() == 1){
                    //aggiorno la residenza se esiste il comune
                    if (rowData.getData().get("COMUNE") != null)
                        if (!StringUtils.isEmpty(rowData.getData().get("COMUNE").toString().trim())){

                            City city = geo.getCityByName(rowData.getData().get("COMUNE").toString().trim());
                            if (city != null){
                                l.setLivingCity(city.getDescription());
                                l.setLivingProvince(geo.getProvinceById(city.getIdProvince()).getDescription());

                                //qui impost indirizzo e cap

                                if (rowData.getData().get("INDIRIZZO") != null)
                                    l.setAddress(rowData.getData().get("INDIRIZZO").trim());

                                if (rowData.getData().get("CAP") != null)
                                    l.setCap(rowData.getData().get("CAP").trim());

                            }
                        }
                }

            lavSvc.saveOrUpdateWithImpersonation(((User) sec.getLoggedUser()).getLid(),l);


        }
        return l;
    }


}
