/**
 * Tesserato.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.1  Built on : Feb 20, 2016 (10:02:19 GMT)
 */
package org.datacontract.schemas._2004._07.print_card_core_vb;


/**
 *  Tesserato bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class Tesserato implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = Tesserato
       Namespace URI = http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB
       Namespace Prefix = ns4
     */

    /**
     * field for Azienda
     */
    protected java.lang.String localAzienda;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAziendaTracker = false;

    /**
     * field for AziendaOld
     */
    protected java.lang.String localAziendaOld;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localAziendaOldTracker = false;

    /**
     * field for Cap
     */
    protected java.lang.String localCap;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCapTracker = false;

    /**
     * field for Cognome
     */
    protected java.lang.String localCognome;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localCognomeTracker = false;

    /**
     * field for Comune
     */
    protected java.lang.String localComune;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localComuneTracker = false;

    /**
     * field for IdUtente
     */
    protected int localIdUtente;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIdUtenteTracker = false;

    /**
     * field for Indice
     */
    protected int localIndice;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localIndiceTracker = false;

    /**
     * field for Nome
     */
    protected java.lang.String localNome;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localNomeTracker = false;

    /**
     * field for Provincia
     */
    protected java.lang.String localProvincia;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localProvinciaTracker = false;

    /**
     * field for ProvinciaSindacale
     */
    protected java.lang.String localProvinciaSindacale;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localProvinciaSindacaleTracker = false;

    /**
     * field for SettoreTessera
     */
    protected java.lang.String localSettoreTessera;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSettoreTesseraTracker = false;

    /**
     * field for StampataDa
     */
    protected java.lang.String localStampataDa;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localStampataDaTracker = false;

    /**
     * field for Via
     */
    protected java.lang.String localVia;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localViaTracker = false;

    public boolean isAziendaSpecified() {
        return localAziendaTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAzienda() {
        return localAzienda;
    }

    /**
     * Auto generated setter method
     * @param param Azienda
     */
    public void setAzienda(java.lang.String param) {
        localAziendaTracker = true;

        this.localAzienda = param;
    }

    public boolean isAziendaOldSpecified() {
        return localAziendaOldTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getAziendaOld() {
        return localAziendaOld;
    }

    /**
     * Auto generated setter method
     * @param param AziendaOld
     */
    public void setAziendaOld(java.lang.String param) {
        localAziendaOldTracker = true;

        this.localAziendaOld = param;
    }

    public boolean isCapSpecified() {
        return localCapTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCap() {
        return localCap;
    }

    /**
     * Auto generated setter method
     * @param param Cap
     */
    public void setCap(java.lang.String param) {
        localCapTracker = true;

        this.localCap = param;
    }

    public boolean isCognomeSpecified() {
        return localCognomeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getCognome() {
        return localCognome;
    }

    /**
     * Auto generated setter method
     * @param param Cognome
     */
    public void setCognome(java.lang.String param) {
        localCognomeTracker = true;

        this.localCognome = param;
    }

    public boolean isComuneSpecified() {
        return localComuneTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getComune() {
        return localComune;
    }

    /**
     * Auto generated setter method
     * @param param Comune
     */
    public void setComune(java.lang.String param) {
        localComuneTracker = true;

        this.localComune = param;
    }

    public boolean isIdUtenteSpecified() {
        return localIdUtenteTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIdUtente() {
        return localIdUtente;
    }

    /**
     * Auto generated setter method
     * @param param IdUtente
     */
    public void setIdUtente(int param) {
        // setting primitive attribute tracker to true
        localIdUtenteTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIdUtente = param;
    }

    public boolean isIndiceSpecified() {
        return localIndiceTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getIndice() {
        return localIndice;
    }

    /**
     * Auto generated setter method
     * @param param Indice
     */
    public void setIndice(int param) {
        // setting primitive attribute tracker to true
        localIndiceTracker = param != java.lang.Integer.MIN_VALUE;

        this.localIndice = param;
    }

    public boolean isNomeSpecified() {
        return localNomeTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getNome() {
        return localNome;
    }

    /**
     * Auto generated setter method
     * @param param Nome
     */
    public void setNome(java.lang.String param) {
        localNomeTracker = true;

        this.localNome = param;
    }

    public boolean isProvinciaSpecified() {
        return localProvinciaTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getProvincia() {
        return localProvincia;
    }

    /**
     * Auto generated setter method
     * @param param Provincia
     */
    public void setProvincia(java.lang.String param) {
        localProvinciaTracker = true;

        this.localProvincia = param;
    }

    public boolean isProvinciaSindacaleSpecified() {
        return localProvinciaSindacaleTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getProvinciaSindacale() {
        return localProvinciaSindacale;
    }

    /**
     * Auto generated setter method
     * @param param ProvinciaSindacale
     */
    public void setProvinciaSindacale(java.lang.String param) {
        localProvinciaSindacaleTracker = true;

        this.localProvinciaSindacale = param;
    }

    public boolean isSettoreTesseraSpecified() {
        return localSettoreTesseraTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSettoreTessera() {
        return localSettoreTessera;
    }

    /**
     * Auto generated setter method
     * @param param SettoreTessera
     */
    public void setSettoreTessera(java.lang.String param) {
        localSettoreTesseraTracker = true;

        this.localSettoreTessera = param;
    }

    public boolean isStampataDaSpecified() {
        return localStampataDaTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getStampataDa() {
        return localStampataDa;
    }

    /**
     * Auto generated setter method
     * @param param StampataDa
     */
    public void setStampataDa(java.lang.String param) {
        localStampataDaTracker = true;

        this.localStampataDa = param;
    }

    public boolean isViaSpecified() {
        return localViaTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getVia() {
        return localVia;
    }

    /**
     * Auto generated setter method
     * @param param Via
     */
    public void setVia(java.lang.String param) {
        localViaTracker = true;

        this.localVia = param;
    }

    /**
     *
     * @param parentQName
     * @param factory
     * @return org.apache.axiom.om.OMElement
     */
    public org.apache.axiom.om.OMElement getOMElement(
        final javax.xml.namespace.QName parentQName,
        final org.apache.axiom.om.OMFactory factory)
        throws org.apache.axis2.databinding.ADBException {
        return factory.createOMElement(new org.apache.axis2.databinding.ADBDataSource(
                this, parentQName));
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        serialize(parentQName, xmlWriter, false);
    }

    public void serialize(final javax.xml.namespace.QName parentQName,
        javax.xml.stream.XMLStreamWriter xmlWriter, boolean serializeType)
        throws javax.xml.stream.XMLStreamException,
            org.apache.axis2.databinding.ADBException {
        java.lang.String prefix = null;
        java.lang.String namespace = null;

        prefix = parentQName.getPrefix();
        namespace = parentQName.getNamespaceURI();
        writeStartElement(prefix, namespace, parentQName.getLocalPart(),
            xmlWriter);

        if (serializeType) {
            java.lang.String namespacePrefix = registerPrefix(xmlWriter,
                    "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix + ":Tesserato", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "Tesserato", xmlWriter);
            }
        }

        if (localAziendaTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "Azienda", xmlWriter);

            if (localAzienda == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localAzienda);
            }

            xmlWriter.writeEndElement();
        }

        if (localAziendaOldTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "AziendaOld", xmlWriter);

            if (localAziendaOld == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localAziendaOld);
            }

            xmlWriter.writeEndElement();
        }

        if (localCapTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "Cap", xmlWriter);

            if (localCap == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localCap);
            }

            xmlWriter.writeEndElement();
        }

        if (localCognomeTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "Cognome", xmlWriter);

            if (localCognome == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localCognome);
            }

            xmlWriter.writeEndElement();
        }

        if (localComuneTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "Comune", xmlWriter);

            if (localComune == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localComune);
            }

            xmlWriter.writeEndElement();
        }

        if (localIdUtenteTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "IdUtente", xmlWriter);

            if (localIdUtente == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "IdUtente cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIdUtente));
            }

            xmlWriter.writeEndElement();
        }

        if (localIndiceTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "Indice", xmlWriter);

            if (localIndice == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "Indice cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localIndice));
            }

            xmlWriter.writeEndElement();
        }

        if (localNomeTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "Nome", xmlWriter);

            if (localNome == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localNome);
            }

            xmlWriter.writeEndElement();
        }

        if (localProvinciaTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "Provincia", xmlWriter);

            if (localProvincia == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localProvincia);
            }

            xmlWriter.writeEndElement();
        }

        if (localProvinciaSindacaleTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "ProvinciaSindacale", xmlWriter);

            if (localProvinciaSindacale == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localProvinciaSindacale);
            }

            xmlWriter.writeEndElement();
        }

        if (localSettoreTesseraTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "SettoreTessera", xmlWriter);

            if (localSettoreTessera == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localSettoreTessera);
            }

            xmlWriter.writeEndElement();
        }

        if (localStampataDaTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "StampataDa", xmlWriter);

            if (localStampataDa == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localStampataDa);
            }

            xmlWriter.writeEndElement();
        }

        if (localViaTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB";
            writeStartElement(null, namespace, "Via", xmlWriter);

            if (localVia == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localVia);
            }

            xmlWriter.writeEndElement();
        }

        xmlWriter.writeEndElement();
    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals(
                    "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB")) {
            return "ns4";
        }

        return org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
    }

    /**
     * Utility method to write an element start tag.
     */
    private void writeStartElement(java.lang.String prefix,
        java.lang.String namespace, java.lang.String localPart,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);

        if (writerPrefix != null) {
            xmlWriter.writeStartElement(writerPrefix, localPart, namespace);
        } else {
            if (namespace.length() == 0) {
                prefix = "";
            } else if (prefix == null) {
                prefix = generatePrefix(namespace);
            }

            xmlWriter.writeStartElement(prefix, localPart, namespace);
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }
    }

    /**
     * Util method to write an attribute with the ns prefix
     */
    private void writeAttribute(java.lang.String prefix,
        java.lang.String namespace, java.lang.String attName,
        java.lang.String attValue, javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String writerPrefix = xmlWriter.getPrefix(namespace);

        if (writerPrefix != null) {
            xmlWriter.writeAttribute(writerPrefix, namespace, attName, attValue);
        } else {
            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
            xmlWriter.writeAttribute(prefix, namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeAttribute(java.lang.String namespace,
        java.lang.String attName, java.lang.String attValue,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attValue);
        } else {
            xmlWriter.writeAttribute(registerPrefix(xmlWriter, namespace),
                namespace, attName, attValue);
        }
    }

    /**
     * Util method to write an attribute without the ns prefix
     */
    private void writeQNameAttribute(java.lang.String namespace,
        java.lang.String attName, javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String attributeNamespace = qname.getNamespaceURI();
        java.lang.String attributePrefix = xmlWriter.getPrefix(attributeNamespace);

        if (attributePrefix == null) {
            attributePrefix = registerPrefix(xmlWriter, attributeNamespace);
        }

        java.lang.String attributeValue;

        if (attributePrefix.trim().length() > 0) {
            attributeValue = attributePrefix + ":" + qname.getLocalPart();
        } else {
            attributeValue = qname.getLocalPart();
        }

        if (namespace.equals("")) {
            xmlWriter.writeAttribute(attName, attributeValue);
        } else {
            registerPrefix(xmlWriter, namespace);
            xmlWriter.writeAttribute(attributePrefix, namespace, attName,
                attributeValue);
        }
    }

    /**
     *  method to handle Qnames
     */
    private void writeQName(javax.xml.namespace.QName qname,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String namespaceURI = qname.getNamespaceURI();

        if (namespaceURI != null) {
            java.lang.String prefix = xmlWriter.getPrefix(namespaceURI);

            if (prefix == null) {
                prefix = generatePrefix(namespaceURI);
                xmlWriter.writeNamespace(prefix, namespaceURI);
                xmlWriter.setPrefix(prefix, namespaceURI);
            }

            if (prefix.trim().length() > 0) {
                xmlWriter.writeCharacters(prefix + ":" +
                    org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            } else {
                // i.e this is the default namespace
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        qname));
            }
        } else {
            xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                    qname));
        }
    }

    private void writeQNames(javax.xml.namespace.QName[] qnames,
        javax.xml.stream.XMLStreamWriter xmlWriter)
        throws javax.xml.stream.XMLStreamException {
        if (qnames != null) {
            // we have to store this data until last moment since it is not possible to write any
            // namespace data after writing the charactor data
            java.lang.StringBuffer stringToWrite = new java.lang.StringBuffer();
            java.lang.String namespaceURI = null;
            java.lang.String prefix = null;

            for (int i = 0; i < qnames.length; i++) {
                if (i > 0) {
                    stringToWrite.append(" ");
                }

                namespaceURI = qnames[i].getNamespaceURI();

                if (namespaceURI != null) {
                    prefix = xmlWriter.getPrefix(namespaceURI);

                    if ((prefix == null) || (prefix.length() == 0)) {
                        prefix = generatePrefix(namespaceURI);
                        xmlWriter.writeNamespace(prefix, namespaceURI);
                        xmlWriter.setPrefix(prefix, namespaceURI);
                    }

                    if (prefix.trim().length() > 0) {
                        stringToWrite.append(prefix).append(":")
                                     .append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    } else {
                        stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                qnames[i]));
                    }
                } else {
                    stringToWrite.append(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                            qnames[i]));
                }
            }

            xmlWriter.writeCharacters(stringToWrite.toString());
        }
    }

    /**
     * Register a namespace prefix
     */
    private java.lang.String registerPrefix(
        javax.xml.stream.XMLStreamWriter xmlWriter, java.lang.String namespace)
        throws javax.xml.stream.XMLStreamException {
        java.lang.String prefix = xmlWriter.getPrefix(namespace);

        if (prefix == null) {
            prefix = generatePrefix(namespace);

            javax.xml.namespace.NamespaceContext nsContext = xmlWriter.getNamespaceContext();

            while (true) {
                java.lang.String uri = nsContext.getNamespaceURI(prefix);

                if ((uri == null) || (uri.length() == 0)) {
                    break;
                }

                prefix = org.apache.axis2.databinding.utils.BeanUtil.getUniquePrefix();
            }

            xmlWriter.writeNamespace(prefix, namespace);
            xmlWriter.setPrefix(prefix, namespace);
        }

        return prefix;
    }

    /**
     *  Factory class that keeps the parse method
     */
    public static class Factory {
        private static org.apache.commons.logging.Log log = org.apache.commons.logging.LogFactory.getLog(Factory.class);

        /**
         * static method to create the object
         * Precondition:  If this object is an element, the current or next start element starts this object and any intervening reader events are ignorable
         *                If this object is not an element, it is a complex type and the reader is at the event just after the outer start element
         * Postcondition: If this object is an element, the reader is positioned at its end element
         *                If this object is a complex type, the reader is positioned at the end element of its outer element
         */
        public static Tesserato parse(javax.xml.stream.XMLStreamReader reader)
            throws java.lang.Exception {
            Tesserato object = new Tesserato();

            int event;
            javax.xml.namespace.QName currentQName = null;
            java.lang.String nillableValue = null;
            java.lang.String prefix = "";
            java.lang.String namespaceuri = "";

            try {
                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                currentQName = reader.getName();

                if (reader.getAttributeValue(
                            "http://www.w3.org/2001/XMLSchema-instance", "type") != null) {
                    java.lang.String fullTypeName = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "type");

                    if (fullTypeName != null) {
                        java.lang.String nsPrefix = null;

                        if (fullTypeName.indexOf(":") > -1) {
                            nsPrefix = fullTypeName.substring(0,
                                    fullTypeName.indexOf(":"));
                        }

                        nsPrefix = (nsPrefix == null) ? "" : nsPrefix;

                        java.lang.String type = fullTypeName.substring(fullTypeName.indexOf(
                                    ":") + 1);

                        if (!"Tesserato".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (Tesserato) it.uilwebapp.services.imports.ExtensionMapper.getTypeObject(nsUri,
                                type, reader);
                        }
                    }
                }

                // Note all attributes that were handled. Used to differ normal attributes
                // from anyAttributes.
                java.util.Vector handledAttributes = new java.util.Vector();

                reader.next();

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "Azienda").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Azienda").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setAzienda(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "AziendaOld").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "AziendaOld").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setAziendaOld(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "Cap").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Cap").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setCap(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "Cognome").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Cognome").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setCognome(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "Comune").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Comune").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setComune(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "IdUtente").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "IdUtente").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "IdUtente" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIdUtente(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIdUtente(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "Indice").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Indice").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "Indice" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setIndice(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setIndice(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "Nome").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Nome").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setNome(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "Provincia").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Provincia").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setProvincia(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "ProvinciaSindacale").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "ProvinciaSindacale").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setProvinciaSindacale(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "SettoreTessera").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "SettoreTessera").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setSettoreTessera(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "StampataDa").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "StampataDa").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setStampataDa(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/PRINT_CARD_CORE_VB",
                            "Via").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Via").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setVia(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                                content));
                    } else {
                        reader.getElementText(); // throw away text nodes if any.
                    }

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if (reader.isStartElement()) {
                    // 2 - A start element we are not expecting indicates a trailing invalid property
                    throw new org.apache.axis2.databinding.ADBException(
                        "Unexpected subelement " + reader.getName());
                }
            } catch (javax.xml.stream.XMLStreamException e) {
                throw new java.lang.Exception(e);
            }

            return object;
        }
    } //end of factory class
}
