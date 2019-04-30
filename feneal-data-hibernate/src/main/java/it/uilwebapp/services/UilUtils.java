/**
 * UilUtils.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.1  Built on : Feb 20, 2016 (10:01:29 GMT)
 */
package it.uilwebapp.services;


import org.apache.axis2.client.ServiceClient;

/*
 *  UilUtils java interface
 */
public interface UilUtils {
    public ServiceClient _getServiceClient();
    /**
     * Auto generated method signature
     *
     * @param calcolaCodiceFiscale0
     */
    public it.uilwebapp.services.CalcolaCodiceFiscaleResponse calcolaCodiceFiscale(
        it.uilwebapp.services.CalcolaCodiceFiscale calcolaCodiceFiscale0)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @param calcolaCodiceFiscale0
     */
    public void startcalcolaCodiceFiscale(
        it.uilwebapp.services.CalcolaCodiceFiscale calcolaCodiceFiscale0,
        final it.uilwebapp.services.UilUtilsCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param exportDocumentToExcel2
     */
    public it.uilwebapp.services.ExportDocumentToExcelResponse exportDocumentToExcel(
        it.uilwebapp.services.ExportDocumentToExcel exportDocumentToExcel2)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @param exportDocumentToExcel2
     */
    public void startexportDocumentToExcel(
        it.uilwebapp.services.ExportDocumentToExcel exportDocumentToExcel2,
        final it.uilwebapp.services.UilUtilsCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param calcolaDatiFiscali4
     */
    public it.uilwebapp.services.CalcolaDatiFiscaliResponse calcolaDatiFiscali(
        it.uilwebapp.services.CalcolaDatiFiscali calcolaDatiFiscali4)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @param calcolaDatiFiscali4
     */
    public void startcalcolaDatiFiscali(
        it.uilwebapp.services.CalcolaDatiFiscali calcolaDatiFiscali4,
        final it.uilwebapp.services.UilUtilsCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param exportTessere6
     */
    public it.uilwebapp.services.ExportTessereResponse exportTessere(
        it.uilwebapp.services.ExportTessere exportTessere6)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @param exportTessere6
     */
    public void startexportTessere(
        it.uilwebapp.services.ExportTessere exportTessere6,
        final it.uilwebapp.services.UilUtilsCallbackHandler callback)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature
     *
     * @param sendMails8
     */
    public it.uilwebapp.services.SendMailsResponse sendMails(
        it.uilwebapp.services.SendMails sendMails8)
        throws java.rmi.RemoteException;

    /**
     * Auto generated method signature for Asynchronous Invocations
     *
     * @param sendMails8
     */
    public void startsendMails(it.uilwebapp.services.SendMails sendMails8,
        final it.uilwebapp.services.UilUtilsCallbackHandler callback)
        throws java.rmi.RemoteException;

    //
}
