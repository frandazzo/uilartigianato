package applica.feneal.webservices;

import com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring;
import it.uilwebapp.services.*;
import junit.framework.Assert;
import org.apache.axis2.AxisFault;
import org.datacontract.schemas._2004._07.print_card_core_vb.ArrayOfTesserato;
import org.datacontract.schemas._2004._07.print_card_core_vb.Tesserato;
import org.datacontract.schemas._2004._07.win_fengest_nazionale_domain_excelexport.*;
import org.datacontract.schemas._2004._07.win_uilutils.MailData;

import javax.activation.DataHandler;
import java.awt.*;
import java.io.*;
import java.util.Calendar;

/**
 * Created by fgran on 06/04/2016.
 */
public class TraceTest {//extends TestCase {

    private String url = "http://173.212.207.209/wcf/uilutils.svc";
    //private String url = "http://192.168.0.100/wcf/uilutils.svc";
//
//    protected void setUp() {
//    }
//
    public void xxxxtestCalcoloDatiFiscali() throws Exception {
        UilUtils i = new UilUtilsStub(null, url);
        CalcolaDatiFiscaliResponse result = null;
        try{

            CalcolaDatiFiscali f = new CalcolaDatiFiscali();
            f.setCodiceFiscale("rndfnc77l14f052f");
            result = i.calcolaDatiFiscali(f);
        }catch (Exception ex){
            Assert.assertNotNull(ex);
        }

        Assert.assertEquals(result.getCalcolaDatiFiscaliResult().getNazione(), "ITALIA");

    }
//
//
    public void xxxtestCalcoloCodiceFiscale() throws Exception {


        UilUtils i = new UilUtilsStub(null, url);
        CalcolaCodiceFiscaleResponse result = null;
        try{

            CalcolaCodiceFiscale f = new CalcolaCodiceFiscale();
            f.setCognome("randazzo");
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(0);
            cal.set(1977, 6, 14, 0, 0, 0);
            f.setDataNascita(cal);
            f.setNome("francesco");
            f.setNomeComuneNascita("matera");
            f.setNomeNazione("ITALIA");
            f.setSesso("MASCHIO");
            result = i.calcolaCodiceFiscale(f);
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        Assert.assertNotNull(result.getCalcolaCodiceFiscaleResult());


    }
//
    public void xxxxxtestExportTessere()  throws Exception{

        UilUtils svc = new UilUtilsStub(null, url);

        long timeout = 5 * 60 * 1000; // Two minutes
        svc._getServiceClient().getOptions().setTimeOutInMilliSeconds(timeout);

        ExportTessereResponse result = null;

        ExportTessere f = new ExportTessere();
        f.setSettore("EDILE");
        f.setProvincia("BOLZANO");

        ArrayOfTesserato t = new ArrayOfTesserato();
        Tesserato t1 = new Tesserato();
        t1.setAzienda("PROVA");
        t1.setProvincia("BOLZANO");
        t1.setComune("MATERA");
        t1.setCap("75100");
        t1.setVia("via gravina 39");
        t1.setNome("francesco");
        t1.setCognome("randazzo");
        t1.setProvinciaSindacale("BOLZANO");
        t1.setSettoreTessera("EDILE");
        t1.setStampataDa("Maurizio D'aurelio");
        Tesserato[] arr = new Tesserato[]{t1};
        t.setTesserato(arr);
        f.setTesserati(t);

        result = svc.exportTessere(f);

        DataHandler webResult = result.getExportTessereResult();



        InputStream inputStream = webResult.getInputStream();
        File nn = File.createTempFile("tessere-matera", ".zip");

        nn.createNewFile();
        OutputStream outputStream = new FileOutputStream(nn);

            try{

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }

                System.out.println("Done!");

            } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (outputStream != null) {
                            try {
                                // outputStream.flush();
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
            }

        Desktop.getDesktop().open(nn);

    }



    public void xxxxtestExportExcel() throws IOException {
        UilUtils svc = new UilUtilsStub(null, url);
        ExportDocumentToExcelResponse result = null;

        ExportDocumentToExcel f = new ExportDocumentToExcel();

        ExcelDocument document = new ExcelDocument();
        ArrayOfExcelRow rows = new ArrayOfExcelRow();

        //creo la prima riga
        ExcelRow row = new ExcelRow();

        ArrayOfExcelProperty propList = new ArrayOfExcelProperty();
        ExcelProperty name = new ExcelProperty();
        name.setName("Lavoratore");
        name.setValue("Francesco Randazzo");
        name.setPriority(1);
        propList.addExcelProperty(name);

        ExcelProperty ente = new ExcelProperty();
        ente.setName("Ente");
        ente.setValue("Cassa edile");
        ente.setPriority(2);
        propList.addExcelProperty(ente);

        row.setProperties(propList);

        ExcelRow row1 = new ExcelRow();


        ArrayOfExcelProperty propList1 = new ArrayOfExcelProperty();
        ExcelProperty name1 = new ExcelProperty();
        name1.setName("Lavoratore");
        name1.setValue("Bruno Fortunato");
        name1.setPriority(1);
        propList1.addExcelProperty(name1);

        ExcelProperty ente1 = new ExcelProperty();
        ente1.setName("Ente");
        ente1.setValue("Cassa edile");
        ente1.setPriority(2);
        propList1.addExcelProperty(ente1);

        row1.setProperties(propList1);


        rows.addExcelRow(row);
        rows.addExcelRow(row1);

        document.setRows(rows);


        f.setDocument(document);

        result = svc.exportDocumentToExcel(f);



        DataHandler webResult = result.getExportDocumentToExcelResult();



        InputStream inputStream = webResult.getInputStream();
        File nn = File.createTempFile("excel", ".xlsx");

        nn.createNewFile();
        OutputStream outputStream = new FileOutputStream(nn);

            try{

                int read = 0;
                byte[] bytes = new byte[1024];

                while ((read = inputStream.read(bytes)) != -1) {
                    outputStream.write(bytes, 0, read);
                }



            } finally {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (outputStream != null) {
                            try {
                                // outputStream.flush();
                                outputStream.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
            }
        Desktop.getDesktop().open(nn);

    }



    public void xxxxxtestSendMail() throws AxisFault {
        UilUtils svc = new UilUtilsStub(null, url);
        SendMailsResponse result = null;

        try{

            SendMails f = new SendMails();



            MailData mail = new MailData();
            mail.setBody("<p><b>body</b></p> cicillooooo");
            mail.setEnableSSL(true);
            mail.setPort(587);
            mail.setSender("Francesco Randazzo cip cip");
            mail.setSmtpMailFrom("noreply@fenealuil.it");
            mail.setSubject("subject");

            String[] tos = new String[2];
            tos[0] = "fg.randazzo@hotmail.it";
            tos[1] = "sissio80@hotmail.com";

            ArrayOfstring a = new ArrayOfstring();
            a.setString(tos);


            mail.setTos(a);

            f.setMaildata(mail);


            result = svc.sendMails(f);
        }catch (Exception ex){

        }

        String resultString = result.getSendMailsResult();
        Assert.assertEquals(resultString, "");
    }


    protected void tearDown() {

    }
}