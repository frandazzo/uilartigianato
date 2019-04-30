/**
 * UilUtilsCallbackHandler.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.1  Built on : Feb 20, 2016 (10:01:29 GMT)
 */
package it.uilwebapp.services;


/**
 *  UilUtilsCallbackHandler Callback class, Users can extend this class and implement
 *  their own receiveResult and receiveError methods.
 */
public abstract class UilUtilsCallbackHandler {
    protected Object clientData;

    /**
     * User can pass in any object that needs to be accessed once the NonBlocking
     * Web service call is finished and appropriate method of this CallBack is called.
     * @param clientData Object mechanism by which the user can pass in user data
     * that will be avilable at the time this callback is called.
     */
    public UilUtilsCallbackHandler(Object clientData) {
        this.clientData = clientData;
    }

    /**
     * Please use this constructor if you don't want to set any clientData
     */
    public UilUtilsCallbackHandler() {
        this.clientData = null;
    }

    /**
     * Get the client data
     */
    public Object getClientData() {
        return clientData;
    }

    /**
     * auto generated Axis2 call back method for calcolaCodiceFiscale method
     * override this method for handling normal response from calcolaCodiceFiscale operation
     */
    public void receiveResultcalcolaCodiceFiscale(
        it.uilwebapp.services.CalcolaCodiceFiscaleResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from calcolaCodiceFiscale operation
     */
    public void receiveErrorcalcolaCodiceFiscale(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for exportDocumentToExcel method
     * override this method for handling normal response from exportDocumentToExcel operation
     */
    public void receiveResultexportDocumentToExcel(
        it.uilwebapp.services.ExportDocumentToExcelResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from exportDocumentToExcel operation
     */
    public void receiveErrorexportDocumentToExcel(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for calcolaDatiFiscali method
     * override this method for handling normal response from calcolaDatiFiscali operation
     */
    public void receiveResultcalcolaDatiFiscali(
        it.uilwebapp.services.CalcolaDatiFiscaliResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from calcolaDatiFiscali operation
     */
    public void receiveErrorcalcolaDatiFiscali(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for exportTessere method
     * override this method for handling normal response from exportTessere operation
     */
    public void receiveResultexportTessere(
        it.uilwebapp.services.ExportTessereResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from exportTessere operation
     */
    public void receiveErrorexportTessere(java.lang.Exception e) {
    }

    /**
     * auto generated Axis2 call back method for sendMails method
     * override this method for handling normal response from sendMails operation
     */
    public void receiveResultsendMails(
        it.uilwebapp.services.SendMailsResponse result) {
    }

    /**
     * auto generated Axis2 Error handler
     * override this method for handling error response from sendMails operation
     */
    public void receiveErrorsendMails(java.lang.Exception e) {
    }
}
