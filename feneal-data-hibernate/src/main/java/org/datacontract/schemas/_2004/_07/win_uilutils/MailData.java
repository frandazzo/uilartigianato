/**
 * MailData.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis2 version: 1.7.1  Built on : Feb 20, 2016 (10:02:19 GMT)
 */
package org.datacontract.schemas._2004._07.win_uilutils;


/**
 *  MailData bean class
 */
@SuppressWarnings({"unchecked",
    "unused"
})
public class MailData implements org.apache.axis2.databinding.ADBBean {
    /* This type was generated from the piece of schema that had
       name = MailData
       Namespace URI = http://schemas.datacontract.org/2004/07/WIN.UILUTILS
       Namespace Prefix = ns3
     */

    /**
     * field for Body
     */
    protected java.lang.String localBody;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localBodyTracker = false;

    /**
     * field for EnableSSL
     */
    protected boolean localEnableSSL;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localEnableSSLTracker = false;

    /**
     * field for Port
     */
    protected int localPort;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localPortTracker = false;

    /**
     * field for Sender
     */
    protected java.lang.String localSender;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSenderTracker = false;

    /**
     * field for SmtpAccount
     */
    protected java.lang.String localSmtpAccount;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSmtpAccountTracker = false;

    /**
     * field for SmtpMailFrom
     */
    protected java.lang.String localSmtpMailFrom;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSmtpMailFromTracker = false;

    /**
     * field for SmtpPassword
     */
    protected java.lang.String localSmtpPassword;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSmtpPasswordTracker = false;

    /**
     * field for SmtpServer
     */
    protected java.lang.String localSmtpServer;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSmtpServerTracker = false;

    /**
     * field for Subject
     */
    protected java.lang.String localSubject;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localSubjectTracker = false;

    /**
     * field for Tos
     */
    protected com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring localTos;

    /*  This tracker boolean wil be used to detect whether the user called the set method
     *   for this attribute. It will be used to determine whether to include this field
     *   in the serialized XML
     */
    protected boolean localTosTracker = false;

    public boolean isBodySpecified() {
        return localBodyTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getBody() {
        return localBody;
    }

    /**
     * Auto generated setter method
     * @param param Body
     */
    public void setBody(java.lang.String param) {
        localBodyTracker = true;

        this.localBody = param;
    }

    public boolean isEnableSSLSpecified() {
        return localEnableSSLTracker;
    }

    /**
     * Auto generated getter method
     * @return boolean
     */
    public boolean getEnableSSL() {
        return localEnableSSL;
    }

    /**
     * Auto generated setter method
     * @param param EnableSSL
     */
    public void setEnableSSL(boolean param) {
        // setting primitive attribute tracker to true
        localEnableSSLTracker = true;

        this.localEnableSSL = param;
    }

    public boolean isPortSpecified() {
        return localPortTracker;
    }

    /**
     * Auto generated getter method
     * @return int
     */
    public int getPort() {
        return localPort;
    }

    /**
     * Auto generated setter method
     * @param param Port
     */
    public void setPort(int param) {
        // setting primitive attribute tracker to true
        localPortTracker = param != java.lang.Integer.MIN_VALUE;

        this.localPort = param;
    }

    public boolean isSenderSpecified() {
        return localSenderTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSender() {
        return localSender;
    }

    /**
     * Auto generated setter method
     * @param param Sender
     */
    public void setSender(java.lang.String param) {
        localSenderTracker = true;

        this.localSender = param;
    }

    public boolean isSmtpAccountSpecified() {
        return localSmtpAccountTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSmtpAccount() {
        return localSmtpAccount;
    }

    /**
     * Auto generated setter method
     * @param param SmtpAccount
     */
    public void setSmtpAccount(java.lang.String param) {
        localSmtpAccountTracker = true;

        this.localSmtpAccount = param;
    }

    public boolean isSmtpMailFromSpecified() {
        return localSmtpMailFromTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSmtpMailFrom() {
        return localSmtpMailFrom;
    }

    /**
     * Auto generated setter method
     * @param param SmtpMailFrom
     */
    public void setSmtpMailFrom(java.lang.String param) {
        localSmtpMailFromTracker = true;

        this.localSmtpMailFrom = param;
    }

    public boolean isSmtpPasswordSpecified() {
        return localSmtpPasswordTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSmtpPassword() {
        return localSmtpPassword;
    }

    /**
     * Auto generated setter method
     * @param param SmtpPassword
     */
    public void setSmtpPassword(java.lang.String param) {
        localSmtpPasswordTracker = true;

        this.localSmtpPassword = param;
    }

    public boolean isSmtpServerSpecified() {
        return localSmtpServerTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSmtpServer() {
        return localSmtpServer;
    }

    /**
     * Auto generated setter method
     * @param param SmtpServer
     */
    public void setSmtpServer(java.lang.String param) {
        localSmtpServerTracker = true;

        this.localSmtpServer = param;
    }

    public boolean isSubjectSpecified() {
        return localSubjectTracker;
    }

    /**
     * Auto generated getter method
     * @return java.lang.String
     */
    public java.lang.String getSubject() {
        return localSubject;
    }

    /**
     * Auto generated setter method
     * @param param Subject
     */
    public void setSubject(java.lang.String param) {
        localSubjectTracker = true;

        this.localSubject = param;
    }

    public boolean isTosSpecified() {
        return localTosTracker;
    }

    /**
     * Auto generated getter method
     * @return com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring
     */
    public com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring getTos() {
        return localTos;
    }

    /**
     * Auto generated setter method
     * @param param Tos
     */
    public void setTos(
        com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring param) {
        localTosTracker = true;

        this.localTos = param;
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
                    "http://schemas.datacontract.org/2004/07/WIN.UILUTILS");

            if ((namespacePrefix != null) &&
                    (namespacePrefix.trim().length() > 0)) {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    namespacePrefix + ":MailData", xmlWriter);
            } else {
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "type",
                    "MailData", xmlWriter);
            }
        }

        if (localBodyTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "Body", xmlWriter);

            if (localBody == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localBody);
            }

            xmlWriter.writeEndElement();
        }

        if (localEnableSSLTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "EnableSSL", xmlWriter);

            if (false) {
                throw new org.apache.axis2.databinding.ADBException(
                    "EnableSSL cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localEnableSSL));
            }

            xmlWriter.writeEndElement();
        }

        if (localPortTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "Port", xmlWriter);

            if (localPort == java.lang.Integer.MIN_VALUE) {
                throw new org.apache.axis2.databinding.ADBException(
                    "Port cannot be null!!");
            } else {
                xmlWriter.writeCharacters(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
                        localPort));
            }

            xmlWriter.writeEndElement();
        }

        if (localSenderTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "Sender", xmlWriter);

            if (localSender == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localSender);
            }

            xmlWriter.writeEndElement();
        }

        if (localSmtpAccountTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "SmtpAccount", xmlWriter);

            if (localSmtpAccount == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localSmtpAccount);
            }

            xmlWriter.writeEndElement();
        }

        if (localSmtpMailFromTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "SmtpMailFrom", xmlWriter);

            if (localSmtpMailFrom == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localSmtpMailFrom);
            }

            xmlWriter.writeEndElement();
        }

        if (localSmtpPasswordTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "SmtpPassword", xmlWriter);

            if (localSmtpPassword == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localSmtpPassword);
            }

            xmlWriter.writeEndElement();
        }

        if (localSmtpServerTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "SmtpServer", xmlWriter);

            if (localSmtpServer == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localSmtpServer);
            }

            xmlWriter.writeEndElement();
        }

        if (localSubjectTracker) {
            namespace = "http://schemas.datacontract.org/2004/07/WIN.UILUTILS";
            writeStartElement(null, namespace, "Subject", xmlWriter);

            if (localSubject == null) {
                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
            } else {
                xmlWriter.writeCharacters(localSubject);
            }

            xmlWriter.writeEndElement();
        }

        if (localTosTracker) {
            if (localTos == null) {
                writeStartElement(null,
                    "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                    "Tos", xmlWriter);

                // write the nil attribute
                writeAttribute("xsi",
                    "http://www.w3.org/2001/XMLSchema-instance", "nil", "1",
                    xmlWriter);
                xmlWriter.writeEndElement();
            } else {
                localTos.serialize(new javax.xml.namespace.QName(
                        "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                        "Tos"), xmlWriter);
            }
        }

        xmlWriter.writeEndElement();
    }

    private static java.lang.String generatePrefix(java.lang.String namespace) {
        if (namespace.equals(
                    "http://schemas.datacontract.org/2004/07/WIN.UILUTILS")) {
            return "ns3";
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
        public static MailData parse(javax.xml.stream.XMLStreamReader reader)
            throws java.lang.Exception {
            MailData object = new MailData();

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

                        if (!"MailData".equals(type)) {
                            //find namespace for the prefix
                            java.lang.String nsUri = reader.getNamespaceContext()
                                                           .getNamespaceURI(nsPrefix);

                            return (MailData) it.uilwebapp.services.imports.ExtensionMapper.getTypeObject(nsUri,
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
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "Body").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Body").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setBody(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "EnableSSL").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "EnableSSL").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "EnableSSL" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setEnableSSL(org.apache.axis2.databinding.utils.ConverterUtil.convertToBoolean(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "Port").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Port").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        throw new org.apache.axis2.databinding.ADBException(
                            "The element: " + "Port" + "  cannot be null");
                    }

                    java.lang.String content = reader.getElementText();

                    object.setPort(org.apache.axis2.databinding.utils.ConverterUtil.convertToInt(
                            content));

                    reader.next();
                } // End of if for expected property start element

                else {
                    object.setPort(java.lang.Integer.MIN_VALUE);
                }

                while (!reader.isStartElement() && !reader.isEndElement())
                    reader.next();

                if ((reader.isStartElement() &&
                        new javax.xml.namespace.QName(
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "Sender").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Sender").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setSender(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "SmtpAccount").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "SmtpAccount").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setSmtpAccount(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "SmtpMailFrom").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "SmtpMailFrom").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setSmtpMailFrom(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "SmtpPassword").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "SmtpPassword").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setSmtpPassword(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "SmtpServer").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "SmtpServer").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setSmtpServer(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "Subject").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Subject").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if (!"true".equals(nillableValue) &&
                            !"1".equals(nillableValue)) {
                        java.lang.String content = reader.getElementText();

                        object.setSubject(org.apache.axis2.databinding.utils.ConverterUtil.convertToString(
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
                            "http://schemas.datacontract.org/2004/07/WIN.UILUTILS",
                            "Tos").equals(reader.getName())) ||
                        new javax.xml.namespace.QName("", "Tos").equals(
                            reader.getName())) {
                    nillableValue = reader.getAttributeValue("http://www.w3.org/2001/XMLSchema-instance",
                            "nil");

                    if ("true".equals(nillableValue) ||
                            "1".equals(nillableValue)) {
                        object.setTos(null);
                        reader.next();

                        reader.next();
                    } else {
                        object.setTos(com.microsoft.schemas._2003._10.serialization.arrays.ArrayOfstring.Factory.parse(
                                reader));

                        reader.next();
                    }
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
