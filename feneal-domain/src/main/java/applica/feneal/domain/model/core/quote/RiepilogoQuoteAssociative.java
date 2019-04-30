package applica.feneal.domain.model.core.quote;


import applica.feneal.domain.model.utils.SecuredDomainEntity;
import fr.opensagres.xdocreport.core.utils.StringUtils;

import java.io.File;
import java.util.Date;

/**
 * Created by angelo on 05/05/16.
 */
public class RiepilogoQuoteAssociative extends SecuredDomainEntity {

//    public static final String IQA = "IQA";
//    public static final String IQI = "IQI";
//    public static final String IQP = "IQP";

    private Date dataRegistrazione;
    private Date dataDocumento;
    private String tipoDocumento;
    private String azienda;
    private String competenza;

    //provincia di riferimento dell'incasso
    private String provincia;
    //identificativo univoco dell'incasso quote
    //tale identificativo è necessario durante i meccanismi di creazione delgli incassi
    //lato fenealgest
    private String guid;
    private String originalFileServerPath;
    private String xmlFileServerPath;
    private String importedLogFilePath;

    private String originalFileServerName;


    private String exporterMail;
    private String exporterName;

    private double totalAmount;

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String calculateDataInizio(){
        if (StringUtils.isEmpty(competenza))
            return "";

        String[] l = competenza.split("-");
        if (l.length == 2)
            return l[0].trim();

        return "";
    }

    public String calculateDataFine(){
        if (StringUtils.isEmpty(competenza))
            return "";

        String[] l = competenza.split("-");
        if (l.length == 2)
            return l[1].trim();

        return "";
    }

//    public void setXmlFileServerName(String xmlFileServerName) {
//        this.xmlFileServerName = xmlFileServerName;
//    }
//
    public void setOriginalFileServerName(String originalFileServerName) {

    }



    public String getOriginalFileServerName() {
        if (StringUtils.isEmpty(originalFileServerPath))
            return "";
        return new File(originalFileServerPath).getName();
    }

    public String getXmlFileServerPath() {
        return xmlFileServerPath;
    }

    public void setXmlFileServerPath(String xmlFileServerPath) {
        this.xmlFileServerPath = xmlFileServerPath;
    }
    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getOriginalFileServerPath() {
        return originalFileServerPath;
    }

    public void setOriginalFileServerPath(String originalFileServerPath) {
        this.originalFileServerPath = originalFileServerPath;
    }

    public String getImportedLogFilePath() {
        return importedLogFilePath;
    }

    public void setImportedLogFilePath(String importedLogFilePath) {
        this.importedLogFilePath = importedLogFilePath;
    }

    public Date getDataRegistrazione() {
        return dataRegistrazione;
    }

    public void setDataRegistrazione(Date dataRegistrazione) {
        this.dataRegistrazione = dataRegistrazione;
    }

    public Date getDataDocumento() {
        return dataDocumento;
    }

    public void setDataDocumento(Date dataDocumento) {
        this.dataDocumento = dataDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getAzienda() {
        return azienda;
    }

    public void setAzienda(String azienda) {
        this.azienda = azienda;
    }

    public String getCompetenza() {
        return competenza;
    }

    public void setCompetenza(String competenza) {
        this.competenza = competenza;
    }



    public void setExporterMail(String exporterMail) {
        this.exporterMail = exporterMail;
    }

    public String getExporterMail() {
        return exporterMail;
    }

    public void setExporterName(String exporterName) {
        this.exporterName = exporterName;
    }

    public String getExporterName() {
        return exporterName;
    }
}
