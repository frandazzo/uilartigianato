package applica.feneal.services.impl.importData;

import applica.feneal.domain.model.core.ImportData;
import applica.framework.fileserver.FileServer;
import applica.framework.management.csv.RowValidator;
import applica.framework.management.excel.ExcelInfo;
import applica.framework.management.excel.ExcelReader;
import applica.framework.management.zip.ZipFacade;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.List;

/**
 * Created by fgran on 09/05/2017.
 */
@Component
public class ImportDataUtils {
    @Autowired
    private FileServer server;


    public void writeToFile(File myFile, String content) {
        BufferedWriter bufferedWriter = null;
        try{
            Writer writer = new FileWriter(myFile,true);
            bufferedWriter = new BufferedWriter(writer);
            PrintWriter out = new PrintWriter(bufferedWriter);
            out.println(content);
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            try{
                if(bufferedWriter != null) bufferedWriter.close();
            } catch(Exception ex){

            }
        }

    }

    public String headersContainsTemplate(List<String> headers, List<String> template) {
        String errors = "";
        for (String s : template) {
            if (!headers.contains(s)){
                errors = errors + ";" + s;
            }
        }
        return errors;

    }

    public String compressData(File temp) throws IOException {
        //adesso posso zippare l'intera cartella
        ZipFacade zipper = new ZipFacade();
        //zippo la cartella in un file nella cartella stessa
        String zippedFile = temp.getAbsolutePath() + "/logImportazione.zip";
        zipper.CompressFolder(temp.getAbsolutePath() , zippedFile);
        return zippedFile;
    }


    public ExcelInfo importData(ImportData importData, File temp1, RowValidator validator) throws IOException {
        String file = getTempFile(importData.getFile1(), temp1);

        ExcelReader reader = new ExcelReader(file, 0,0 ,validator);

        return reader.readFile();
    }

    private String getTempFile(String  filePath, File temp ) throws IOException {
        if (fr.opensagres.xdocreport.core.utils.StringUtils.isEmpty(filePath))
            return null;

        InputStream file =server.getFile(filePath);
        String mime = "." + FilenameUtils.getExtension(filePath);
        String  a=FilenameUtils.getName(filePath);


        return addToTempFolder(file, a, mime, temp);
    }

    private String addToTempFolder(InputStream inputStream, String name, String mime, File temp ) throws IOException {

        //aggiungo il file alla direcotry
        String filename = temp.getAbsolutePath() + "\\" + name;
        File nn = new File(filename);
        nn.createNewFile();

        //copio il file inviato nella cartella temporanea
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


        return nn.getAbsolutePath();
    }


}
