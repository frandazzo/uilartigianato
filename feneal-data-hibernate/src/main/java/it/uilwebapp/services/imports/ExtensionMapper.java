/**
 * ExtensionMapper.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.1  Built on : Feb 20, 2016 (10:02:19 GMT)
 */
package it.uilwebapp.services.imports;


/**
 *  ExtensionMapper class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class ExtensionMapper {
    public static java.lang.Object getTypeObject(
        java.lang.String namespaceURI, java.lang.String typeName,
        javax.xml.stream.XMLStreamReader reader) throws java.lang.Exception {
        if ("http://schemas.datacontract.org/2004/07/WIN.FENGEST_NAZIONALE.DOMAIN.ExcelExport".equals(
                    namespaceURI) && "ExcelRow".equals(typeName)) {
            return org.datacontract.schemas._2004._07.win_fengest_nazionale_domain_excelexport.ExcelRow.Factory.parse(reader);
        }

        if ("http://schemas.microsoft.com/2003/10/Serialization/".equals(
                    namespaceURI) && "char".equals(typeName)) {
            return com.microsoft.schemas._2003._10.serialization._char.Factory.parse(reader);
        }

        if ("http://schemas.microsoft.com/2003/10/Serialization/".equals(
                    namespaceURI) && "guid".equals(typeName)) {
            return com.microsoft.schemas._2003._10.serialization.Guid.Factory.parse(reader);
        }

        if ("http://schemas.datacontract.org/2004/07/WIN.FENGEST_NAZIONALE.DOMAIN.ExcelExport".equals(
                    namespaceURI) && "ExcelDocument".equals(typeName)) {
            return org.datacontract.schemas._2004._07.win_fengest_nazionale_domain_excelexport.ExcelDocument.Factory.parse(reader);
        }

        if ("http://schemas.datacontract.org/2004/07/WIN.FENGEST_NAZIONALE.DOMAIN.ExcelExport".equals(
                    namespaceURI) && "ArrayOfExcelRow".equals(typeName)) {
            return org.datacontract.schemas._2004._07.win_fengest_nazionale_domain_excelexport.ArrayOfExcelRow.Factory.parse(reader);
        }

        if ("http://schemas.datacontract.org/2004/07/WIN.FENGEST_NAZIONALE.DOMAIN.ExcelExport".equals(
                    namespaceURI) && "ExcelProperty".equals(typeName)) {
            return org.datacontract.schemas._2004._07.win_fengest_nazionale_domain_excelexport.ExcelProperty.Factory.parse(reader);
        }

        if ("http://schemas.datacontract.org/2004/07/WIN.FENGEST_NAZIONALE.DOMAIN.ExcelExport".equals(
                    namespaceURI) && "ArrayOfExcelProperty".equals(typeName)) {
            return org.datacontract.schemas._2004._07.win_fengest_nazionale_domain_excelexport.ArrayOfExcelProperty.Factory.parse(reader);
        }

        if ("http://schemas.datacontract.org/2004/07/WIN.UILUTILS".equals(
                    namespaceURI) && "MailData".equals(typeName)) {
            return org.datacontract.schemas._2004._07.win_uilutils.MailData.Factory.parse(reader);
        }

        if ("http://schemas.microsoft.com/2003/10/Serialization/".equals(
                    namespaceURI) && "duration".equals(typeName)) {
            return com.microsoft.schemas._2003._10.serialization.Duration.Factory.parse(reader);
        }

        if ("http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB".equals(
                    namespaceURI) && "ArrayOfTesserato".equals(typeName)) {
            return org.datacontract.schemas._2004._07.print_card_core_vb.ArrayOfTesserato.Factory.parse(reader);
        }

        if ("http://schemas.microsoft.com/2003/10/Serialization/Arrays".equals(
                    namespaceURI) && "ArrayOfstring".equals(typeName)) {
            return com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring.Factory.parse(reader);
        }

        if ("http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB".equals(
                    namespaceURI) && "Tesserato".equals(typeName)) {
            return org.datacontract.schemas._2004._07.print_card_core_vb.Tesserato.Factory.parse(reader);
        }

        if ("http://schemas.datacontract.org/2004/07/WIN.UILUTILS".equals(
                    namespaceURI) && "FiscalData".equals(typeName)) {
            return org.datacontract.schemas._2004._07.win_uilutils.FiscalData.Factory.parse(reader);
        }

        throw new org.apache.axis2.databinding.ADBException("Unsupported type " +
            namespaceURI + " " + typeName);
    }
}
